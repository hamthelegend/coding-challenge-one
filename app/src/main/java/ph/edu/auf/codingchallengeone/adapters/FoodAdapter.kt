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
import ph.edu.auf.codingchallengeone.realm.realmmodels.FoodRealm


private const val BREAKFAST = 1
private const val DRINKS = 2
private const val LUNCH = 3
private const val DINNER = 4

class FoodAdapter(
    private var context: Context,
    private var foodList: ArrayList<FoodRealm>,
    private var foodAdapterCallback: FoodAdapterCallback,
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    interface FoodAdapterCallback {
        fun addToFave(id: ObjectId)
        fun removeFromFave(id: ObjectId)
    }

    inner class FoodViewHolder(private val binding: ContentRvFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemData: FoodRealm) {
            with(binding) {
                txtFoodDesc.text = itemData.shortDescription
                txtFoodName.text = itemData.foodName
                cbFave.isChecked = itemData.isFave
                imgFoodType.setImageDrawable(getImageDrawable(itemData.foodType!!.type))
                cbFave.setOnCheckedChangeListener { _, checked ->
                    if (checked) {
                        foodAdapterCallback.addToFave(itemData.id)
                    } else {
                        foodAdapterCallback.removeFromFave(itemData.id)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding =
            ContentRvFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodData = foodList[position]
        holder.bind(foodData)
    }


    private fun getImageDrawable(type: Int): Drawable {
        return when (type) {
            BREAKFAST -> {
                ContextCompat.getDrawable(context, R.drawable.ic_breakfast)!!
            }

            DRINKS -> {
                ContextCompat.getDrawable(context, R.drawable.ic_breakfast)!!
            }

            LUNCH -> {
                ContextCompat.getDrawable(context, R.drawable.ic_breakfast)!!
            }

            DINNER -> {
                ContextCompat.getDrawable(context, R.drawable.ic_breakfast)!!
            }

            else -> {
                ContextCompat.getDrawable(context, R.drawable.ic_breakfast)!!
            }

        }
    }

}