package eachillz.dev.itv.api

import com.google.gson.annotations.SerializedName
import java.util.*

data class CovidData (
    @SerializedName("dateChecked") val dataChecked: Date,
    @SerializedName("positiveIncrease") val positiveIncrease:Int,
    @SerializedName("negativeIncrease") val negativeIncrease:Int,
    @SerializedName("deathIncrease") val deathIncrease:Int,
    @SerializedName("state") val state:String,
    )