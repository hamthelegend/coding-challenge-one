package ph.edu.auf.codingchallengeone.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.mongodb.kbson.ObjectId
import ph.edu.auf.codingchallengeone.R
import ph.edu.auf.codingchallengeone.adapters.FaveFoodAdapter
import ph.edu.auf.codingchallengeone.databinding.FragmentFaveBinding
import ph.edu.auf.codingchallengeone.realm.realmmodels.FaveFoodRealm
import ph.edu.auf.codingchallengeone.viewmodels.FaveFoodViewModel

class FaveFragment : Fragment() {

    private lateinit var binding: FragmentFaveBinding
    private lateinit var adapter: FaveFoodAdapter
    private lateinit var viewModel: FaveFoodViewModel
    private lateinit var faveFoodList: ArrayList<FaveFoodRealm>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        faveFoodList = arrayListOf()
        viewModel = ViewModelProvider(this)[FaveFoodViewModel::class.java]

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        adapter = FaveFoodAdapter(requireActivity(),faveFoodList,this)

        binding.rvFood.layoutManager = layoutManager
        binding.rvFood.adapter = adapter

        viewModel.foodList.observe(viewLifecycleOwner){
            faveFoodList.clear()
            faveFoodList.addAll(it)
            adapter.notifyDataSetChanged()
        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun removeFromFave(id: ObjectId, foodId: ObjectId) {
        viewModel.removeFromFave(id,foodId)
    }

}