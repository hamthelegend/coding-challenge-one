package ph.edu.auf.codingchallengeone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import ph.edu.auf.codingchallengeone.realm.operations.RealmDatabase
import ph.edu.auf.codingchallengeone.realm.realmmodels.FoodRealm

class FoodViewModel : ViewModel() {

    private var _foodList = MutableLiveData<ArrayList<FoodRealm>>()
    private var _isLoading = MutableLiveData(false)

    private val database: RealmDatabase = RealmDatabase()

    val foodList : LiveData<ArrayList<FoodRealm>> get() = _foodList
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        getFoodList()
    }

    fun getFoodList(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = database.getAllFood()
            withContext(Dispatchers.Main.immediate){
                _foodList.value = ArrayList(result)
            }
        }
    }

    fun addFood(name: String, shortDesc: String, type: ObjectId){
        viewModelScope.launch(Dispatchers.IO) {
            database.addNewFood(name,shortDesc,type)
            getFoodList()
        }
    }

    fun addFoodToFave(id: ObjectId){
        viewModelScope.launch(Dispatchers.IO) {
            database.addToFaveFood(id)
            getFoodList()
        }
    }

    fun searchFood(searchString: String){
        viewModelScope.launch(Dispatchers.IO) {
            val result = database.searchFood(searchString)
            withContext(Dispatchers.Main.immediate){
            }
        }
    }

    fun deleteFood(id: ObjectId){
        viewModelScope.launch(Dispatchers.IO) {
            database.removeFood(id)
            getFoodList()
        }
    }


}