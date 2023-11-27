package ph.edu.auf.codingchallengeone.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.mongodb.kbson.ObjectId
import ph.edu.auf.codingchallengeone.R
import ph.edu.auf.codingchallengeone.databinding.ContentRvFoodBinding
import ph.edu.auf.codingchallengeone.realm.realmmodels.FaveFoodRealm
import ph.edu.auf.codingchallengeone.realm.realmmodels.FoodRealm


private const val BREAKFAST = 1
private const val DRINKS = 2
private const val LUNCH = 3
private const val DINNER = 4

class FaveFoodAdapter(private var context: Context, private var faveFoodList: ArrayList<FoodRealm>, private var faveFoodAdapterCallback: FaveFoodAdapterCallback) : RecyclerView.Adapter<FaveFoodAdapter.FaveFoodHolder>() {

    interface FaveFoodAdapterCallback{
        fun removeFromFave(id: ObjectId, foodId : ObjectId)
    }

    inner class FaveFoodHolder(private val binding: ContentRvFoodBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(itemData: FaveFoodRealm){
            with(binding){
                txtFoodDesc.text = itemData.food?.foodName
                txtFoodName.text = itemData.food?.foodName
                imgFoodType.setImageDrawable(getImageDrawable(itemData.food?.foodType!!.type))
                cbFave.isChecked = true
                cbFave.setOnCheckedChangeListener { _, b ->
                    if(!b){
                    }
                }
            }
        }
    }

    private fun getImageDrawable(type: Int): Drawable {
        return when (type){
            BREAKFAST -> {
                ContextCompat.getDrawable(context, R.drawable.ic_breakfast)!!
            }
            DRINKS ->{
                ContextCompat.getDrawable(context, R.drawable.ic_drinks)!!
            }
            LUNCH ->{
                ContextCompat.getDrawable(context, R.drawable.ic_lunch)!!
            }
            DINNER ->{
                ContextCompat.getDrawable(context, R.drawable.ic_dinner)!!
            }else ->{
                ContextCompat.getDrawable(context, R.drawable.ic_breakfast)!!
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaveFoodHolder {
        val binding = ContentRvFoodBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FaveFoodHolder(binding)
    }

    override fun getItemCount(): Int {
        return faveFoodList.size - 5
    }

    override fun onBindViewHolder(holder: FaveFoodHolder, position: Int) {
        val faveFoodData = faveFoodList[position]
        holder.bind(faveFoodData)
    }

}