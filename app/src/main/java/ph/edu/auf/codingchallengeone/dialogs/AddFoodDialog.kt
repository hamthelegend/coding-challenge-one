package ph.edu.auf.codingchallengeone.dialogs

import android.R
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import ph.edu.auf.codingchallengeone.databinding.DialogAddFoodBinding
import ph.edu.auf.codingchallengeone.realm.operations.RealmDatabase
import ph.edu.auf.codingchallengeone.realm.realmmodels.FoodTypeRealm


//SHOULD BE MATCH_PARENT AND WRAP_CONTENT DIALOG
class AddFoodDialog : DialogFragment(), OnItemSelectedListener {

    private lateinit var binding: DialogAddFoodBinding
    lateinit var addFoodDataCallback: AddFoodData
    private var foodTypeList = arrayListOf<FoodTypeRealm>()
    private var foodTypeListSpinner = arrayListOf<String>()
    private lateinit var selectedType: String
    private lateinit var database: RealmDatabase

    interface AddFoodData{
        fun addData(name: String, shortDesc: String, type: ObjectId)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogAddFoodBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val coroutineContext = Job() + Dispatchers.IO
        val scope = CoroutineScope(coroutineContext + CoroutineName("GetFoodTypes"))
        scope.launch(Dispatchers.IO) {
            val results = database.getAllFoodType()
            foodTypeList = arrayListOf()
            foodTypeList.addAll(
                results.map {
                    it
                }
            )
            foodTypeListSpinner.addAll(
                foodTypeList.map {
                    if(!::selectedType.isInitialized){
                        selectedType = it.id.toHexString()
                    }
                    it.typeName
                }
            )

            withContext(Dispatchers.Main.immediate){
                val spnArrayAdapter = ArrayAdapter(requireActivity().applicationContext, R.layout.simple_spinner_item, foodTypeListSpinner)
                spnArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                with(binding.spnFoodType){
                    adapter = spnArrayAdapter
                    setSelection(0,false)
                    onItemSelectedListener = this@AddFoodDialog
                    prompt = "Select Pet type"
                    gravity = Gravity.CENTER
                }
            }
        }

        with(binding){
            btnAdd.setOnClickListener{
                if(edtFoodName.text.isNullOrEmpty()){
                    edtFoodName.error = "Food type required"
                    return@setOnClickListener
                }
                if(edtShortDesc.text.isNullOrEmpty()){
                    edtShortDesc.error = "Short description required"
                    return@setOnClickListener
                }

                dialog?.dismiss()
            }
        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}