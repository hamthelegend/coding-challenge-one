package ph.edu.auf.codingchallengeone.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamthelegend.enchantmentorder.extensions.combineToStateFlow
import com.hamthelegend.enchantmentorder.extensions.mapToStateFlow
import com.hamthelegend.enchantmentorder.extensions.search
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import ph.edu.auf.codingchallengeone.realm.operations.RealmDatabase

class FoodViewModel : ViewModel() {

    private val database: RealmDatabase = RealmDatabase()

    private val _searchString = MutableStateFlow("")
    val searchString = _searchString.asStateFlow()

    val foodList = combineToStateFlow(
        database.getAllFood(),
        searchString,
        scope = viewModelScope,
        initialValue = null,
    ) { foodList, searchString ->
        foodList.search(searchString) { it.foodName }
    }

    val isLoading = foodList.mapToStateFlow(scope = viewModelScope) { it != null }

    fun addFood(name: String, shortDesc: String, type: ObjectId){
        viewModelScope.launch {
            database.addNewFood(name,shortDesc,type)
        }
    }

    fun addFoodToFave(id: ObjectId){
        viewModelScope.launch {
            database.addToFaveFood(id)
        }
    }

    fun removeFoodFromFave(id: ObjectId){
        viewModelScope.launch {
            database.removeFromFave(id)
        }
    }

    fun searchFood(searchString: String){
        _searchString.update { searchString }
    }

    fun deleteFood(id: ObjectId){
        viewModelScope.launch {
            database.removeFood(id)
        }
    }
}