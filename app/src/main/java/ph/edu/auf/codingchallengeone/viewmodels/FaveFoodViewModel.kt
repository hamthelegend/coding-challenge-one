package ph.edu.auf.codingchallengeone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import ph.edu.auf.codingchallengeone.realm.operations.RealmDatabase
import ph.edu.auf.codingchallengeone.realm.realmmodels.FaveFoodRealm

class FaveFoodViewModel : ViewModel() {

    private var _faveFoodList = MutableLiveData<ArrayList<FaveFoodRealm>>()
    private var _isLoading = MutableLiveData(false)

    private val database: RealmDatabase = RealmDatabase()

    val foodList: LiveData<ArrayList<FaveFoodRealm>> get() = _faveFoodList
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getFaveFoodList(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = database.getAllFaveFood()
            withContext(Dispatchers.Main.immediate){
                _faveFoodList.value = ArrayList(result)
            }
        }
    }

    fun removeFromFave(id: ObjectId, foodId: ObjectId){
        viewModelScope.launch(Dispatchers.IO) {
            database.removeFromFave(id,foodId)
            getFaveFoodList()
        }
    }

}