package ph.edu.auf.codingchallengeone.realm.realmmodels

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class FaveFoodRealm : RealmObject{
    @PrimaryKey
    var id : ObjectId = ObjectId()
    var food: FoodRealm? = null
    var dateAdded: String = ""
}