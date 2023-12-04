package ph.edu.auf.codingchallengeone.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import ph.edu.auf.codingchallengeone.R
import ph.edu.auf.codingchallengeone.adapters.FoodAdapter
import ph.edu.auf.codingchallengeone.databinding.FragmentFoodBinding
import ph.edu.auf.codingchallengeone.dialogs.AddFoodDialog
import ph.edu.auf.codingchallengeone.realm.realmmodels.FoodRealm
import ph.edu.auf.codingchallengeone.viewmodels.FoodViewModel

class FoodFragment : Fragment(), AddFoodDialog.AddFoodData, FoodAdapter.FoodAdapterCallback {

    private val viewModel: FoodViewModel by viewModels()
    private lateinit var binding: FragmentFoodBinding
    private lateinit var adapter: FoodAdapter
    private lateinit var foodList: ArrayList<FoodRealm>
    private var recoverMode : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        foodList = arrayListOf()


        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        adapter = FoodAdapter(requireActivity(), foodList, this)

        binding.rvFood.layoutManager = layoutManager
        binding.rvFood.adapter = adapter

        binding.fab.setOnClickListener{
            val addFoodDialog = AddFoodDialog()
            addFoodDialog.addFoodDataCallback = this
            addFoodDialog.show(requireActivity().supportFragmentManager,null)
        }


        lifecycleScope.launch {
            viewModel.foodList.collect {
                foodList.clear()
                foodList.addAll(it ?: emptyList())
                adapter.notifyDataSetChanged()
            }
        }

        binding.edtSearch.addTextChangedListener {
            viewModel.searchFood(it.toString())
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
               return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedData: FoodRealm = foodList[viewHolder.adapterPosition]

                val position = viewHolder.adapterPosition

                foodList.removeAt(position)

                adapter.notifyItemRemoved(position)

                recoverMode = false

                Snackbar.make(binding.rvFood, "Removed food: ${deletedData.foodName}",Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        foodList.add(position, deletedData)
                        adapter.notifyItemInserted(position)
                        recoverMode = true
                    }
                    .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        override fun onShown(transientBottomBar: Snackbar?) {
                            super.onShown(transientBottomBar)
                        }

                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)
                            if(!recoverMode){
                                viewModel.deleteFood(deletedData.id)
                            }
                        }
                    }).show()

            }

        })



    }

    override fun addData(name: String, shortDesc: String, type: ObjectId) {
        viewModel.addFood(name,shortDesc,type)
    }

    override fun addToFave(id: ObjectId) {
        viewModel.addFoodToFave(id)
    }

    override fun removeFromFave(id: ObjectId) {
        viewModel.removeFoodFromFave(id)
    }

}