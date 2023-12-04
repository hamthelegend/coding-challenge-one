package ph.edu.auf.codingchallengeone.realm.operations

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import ph.edu.auf.codingchallengeone.realm.realmmodels.FaveFoodRealm
import ph.edu.auf.codingchallengeone.realm.realmmodels.FoodRealm
import ph.edu.auf.codingchallengeone.realm.realmmodels.FoodTypeRealm
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RealmDatabase {

    private val realm: Realm by lazy {
        val config = RealmConfiguration.Builder(
            schema = setOf(FaveFoodRealm::class, FoodRealm::class, FoodTypeRealm::class)
        ).schemaVersion(1)
            .initialData {
                copyToRealm(
                    FoodTypeRealm().apply {
                        typeName = "Breakfast"
                        type = 1
                    }
                )
                copyToRealm(
                    FoodTypeRealm().apply {
                        typeName = "Drinks"
                        type = 2
                    }
                )
                copyToRealm(
                    FoodTypeRealm().apply {
                        typeName = "Lunch"
                        type = 3
                    }
                )
                copyToRealm(
                    FoodTypeRealm().apply {
                        typeName = "Lunch"
                        type = 4
                    }
                )
            }
            .build()
        Realm.open(config)
    }

    //SHOULD NOT INCLUDE FAVES
    fun getAllFood(): Flow<List<FoodRealm>> {
        return realm.query<FoodRealm>().asFlow().map { it.list }
    }

    fun getAllFaveFood(): Flow<List<FaveFoodRealm>> {
        return realm.query<FaveFoodRealm>().asFlow().map { it.list }
    }

    fun getAllFoodType(): List<FoodTypeRealm> {
        return realm.query<FoodTypeRealm>().find()
    }

    fun getAllFoodByType(type: Int): Flow<List<FoodRealm>> {
        return realm.query<FoodRealm>("item.id = $0", type).asFlow().map { it.list }
    }

    suspend fun addNewFood(name: String, shortDesc: String, type: ObjectId) {
        withContext(Dispatchers.IO) {
            realm.write {
                val foodType: FoodTypeRealm? = realm.query<FoodTypeRealm>("id = $0", type)
                    .first()
                    .find()

                val food = FoodRealm().apply {
                    this.foodType = findLatest(foodType!!)
                    this.foodName = name
                    this.isFave = false
                    this.shortDescription = shortDesc
                }
                copyToRealm(food)
            }
        }
    }

    suspend fun addToFaveFood(objectId: ObjectId) {
        withContext(Dispatchers.IO) {
            realm.write {
                val realmFoodObject = realm.query<FoodRealm>("id = $0", objectId).find().first()

                val currentDate = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val faveFoodObject = FaveFoodRealm().apply {
                    this.food = findLatest(realmFoodObject)
                    this.dateAdded = currentDate.format(formatter)
                }
                copyToRealm(faveFoodObject)

                findLatest(realmFoodObject)?.isFave = true
            }
        }
    }

    suspend fun editFoodDetails(name: String, shortDesc: String, type: ObjectId, foodId: ObjectId) {
        withContext(Dispatchers.IO) {
            realm.write {

                val foodType: FoodTypeRealm = realm.query<FoodTypeRealm>("id = $0", type)
                    .find()
                    .first()

                val realmFoodObject = realm.query<FoodRealm>("id = $0", type).find().first()
                realmFoodObject.foodName = name
                realmFoodObject.foodType = foodType
                realmFoodObject.shortDescription = shortDesc

            }
        }
    }

    suspend fun removeFromFave(id: ObjectId) {
        withContext(Dispatchers.IO) {
            realm.write {
                val food = realm.query<FoodRealm>("id = $0", id).find().firstOrNull()

                val faveFood = query<FaveFoodRealm>("food = $0", food).first().find()

                faveFood
                    ?.let { delete(it) }
                    ?: throw IllegalStateException("Food not found")

                food?.let(::findLatest)?.isFave = false
            }
        }
    }

    suspend fun removeFood(id: ObjectId) {
        withContext(Dispatchers.IO) {
            realm.write {
                query<FoodRealm>("id = $0", id)
                    .first()
                    .find()
                    ?.let { delete(it) }
                    ?: throw IllegalStateException("Food not found")
            }
        }
    }
}