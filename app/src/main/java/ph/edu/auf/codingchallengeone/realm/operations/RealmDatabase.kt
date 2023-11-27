package ph.edu.auf.codingchallengeone.realm.operations

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import org.mongodb.kbson.ObjectId
import ph.edu.auf.codingchallengeone.realm.realmmodels.FaveFoodRealm
import ph.edu.auf.codingchallengeone.realm.realmmodels.FoodRealm
import ph.edu.auf.codingchallengeone.realm.realmmodels.FoodTypeRealm
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**INITIAL DATA
 *            "Breakfast"; type = 1
 *             "Drinks"; type = 2
 *             "Lunch"; type = 3
 *             "Dinner"; type = 4
**/
class RealmDatabase {

    private val realm: Realm by lazy {
        val config = RealmConfiguration.Builder(
            schema = setOf(FaveFoodRealm::class,FoodRealm::class)
        ).schemaVersion(1)
        .build()
        Realm.open(config)
    }

    //SHOULD NOT INCLUDE FAVES
    fun getAllFood(): List<FoodRealm>{
        return realm.query<FoodRealm>().find()
    }

    fun getAllFaveFood(): List<FaveFoodRealm>{
        return realm.query<FaveFoodRealm>().find()
    }

    fun getAllFoodType(): List<FoodTypeRealm>{
        return realm.query<FoodTypeRealm>().find()
    }

    fun getAllFoodByType(type: Int): List<FoodRealm>{
        return realm.query<FoodRealm>("item.id = $0",type).find()
    }

    suspend fun addNewFood(name: String, shortDesc: String, type: ObjectId){
        realm.write {
            val foodType : FoodTypeRealm? = realm.query<FoodTypeRealm>("id = $0",type)
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

    fun searchFood(searchString: String): List<FoodRealm>{
        return realm.query<FoodRealm>("isFave = $0 AND foodName CONTAINS $1",false,searchString).find()
    }

    suspend fun addToFaveFood(objectId: ObjectId){
        realm.write {
            val realmFoodObject = realm.query<FoodRealm>("id = $0",objectId).find().first()

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

    suspend fun editFoodDetails(name: String, shortDesc: String, type: ObjectId, foodId : ObjectId){
        realm.write {

            val foodType : FoodTypeRealm = realm.query<FoodTypeRealm>("id = $0",type)
                .find()
                .first()

            val realmFoodObject = realm.query<FoodRealm>("id = $0",type).find().first()
            realmFoodObject.foodName = name
            realmFoodObject.foodType = foodType
            realmFoodObject.shortDescription = shortDesc

        }
    }

    suspend fun removeFromFave(id: ObjectId, foodObjectID: ObjectId){

        realm.write {
            query<FaveFoodRealm>("id = $0",id)
                .first()
                .find()
                ?.let { delete(it) }
                ?: throw IllegalStateException("Food not found")

            val foodRealm : FoodRealm = realm.query<FoodRealm>("id = $0",foodObjectID).find().first()
            findLatest(foodRealm)?.isFave = false

        }
    }

    suspend fun removeFood(id: ObjectId){
        realm.write {
            query<FoodRealm>("id = $0",id)
                .first()
                .find()
                ?.let { delete(it) }
                ?: throw IllegalStateException("Food not found")
        }
    }

}