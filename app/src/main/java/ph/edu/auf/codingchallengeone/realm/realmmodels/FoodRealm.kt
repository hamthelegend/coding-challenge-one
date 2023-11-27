package ph.edu.auf.codingchallengeone.realm.realmmodels

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class FoodRealm: RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var foodName: String = ""
    var shortDescription: String = ""
    var foodType: FoodTypeRealm? = null
    var isFave: Boolean = false
}