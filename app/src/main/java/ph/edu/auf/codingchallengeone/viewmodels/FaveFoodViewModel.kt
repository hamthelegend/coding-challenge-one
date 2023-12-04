package ph.edu.auf.codingchallengeone.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import ph.edu.auf.codingchallengeone.realm.operations.RealmDatabase
import ph.edu.auf.codingchallengeone.realm.realmmodels.FaveFoodRealm

class FaveFoodViewModel : ViewModel() {

    private val database: RealmDatabase = RealmDatabase()

    private val _searchString = MutableStateFlow("")
    val searchString = _searchString.asStateFlow()

    val foodList = combineToStateFlow(
        database.getAllFaveFood(),
        searchString,
        scope = viewModelScope,
        initialValue = null,
    ) { foodList, searchString ->
        foodList.search(searchString) { it.food!!.foodName }
    }

    val isLoading = foodList.mapToStateFlow(scope = viewModelScope) { it != null }

    fun searchFood(searchString: String){
        _searchString.update { searchString }
    }

    fun removeFromFave(id: ObjectId){
        viewModelScope.launch {
            database.removeFromFave(id)
        }
    }
}