package eachillz.dev.itv.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import eachillz.dev.itv.R
import eachillz.dev.itv.databinding.WeightItemBinding
import eachillz.dev.itv.model.WeightWatcherModal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class WeightAdapter(private var weightModals: MutableList<WeightWatcherModal>) : RecyclerView.Adapter<WeightAdapter.WeightViewHolder>() {

    private var _binding: WeightItemBinding?=null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        _binding = WeightItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeightViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        val weightItem = weightModals[position]
        var weightProgressText = "First Record"
        var isWeightDecreased = true
        if(position < weightModals.size-1){
            var original : Int = weightModals[position].weight
            var newWeight: Int = weightModals[position+1].weight
             var weightResult = original - newWeight

            if(weightResult < 0){
                weightProgressText = "Loss ${weightResult.absoluteValue} lbs"
            }else{
                weightProgressText = "Gained ${weightResult} lbs"
                isWeightDecreased = false

            }
        }
        if(position == weightModals.size -1){
            binding.itemWeightVLine1.isVisible = false
        }
        holder.bind(weightItem, binding, weightProgressText, isWeightDecreased)
    }

    override fun getItemCount(): Int {
        return weightModals.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class WeightViewHolder(itemView: WeightItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            weightItem: WeightWatcherModal,
            binding: WeightItemBinding,
            weightProgressText: String,
            isWeightDecreased: Boolean
        ) {
            var tvdate:TextView = binding.tvDateValue
            var tvWeight : TextView = binding.tvWeightl
            var tvWeightChange : TextView = binding.tvPercentageWeight
            val outputDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            binding.tvWeightItemCalories.text = "Total Calories: ${weightItem.calories}"
            tvdate.text =  outputDateFormat.format(weightItem.date)
            tvWeight.text = "${ weightItem.weight } lb"
            tvWeightChange.text = weightProgressText

            if(isWeightDecreased){
                @ColorInt val colorInt = ContextCompat.getColor(itemView.context, R.color.teal_200)
                tvWeightChange.setTextColor(colorInt)
            }else{
                @ColorInt val colorInt = ContextCompat.getColor(itemView.context, R.color.fusia)
                tvWeightChange.setTextColor(colorInt)
            }


        }

    }
}