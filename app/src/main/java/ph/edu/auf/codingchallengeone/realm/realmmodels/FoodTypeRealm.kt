package ph.edu.auf.codingchallengeone.realm.realmmodels

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class FoodTypeRealm: RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var typeName: String = ""
    var type: Int = 0
}