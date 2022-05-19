package eachillz.dev.itv.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FoodSearchResult(
    var _links: Links,
    var hints: List<Hint>,
    var parsed: List<Parsed>,
    var text: String
)

data class Links(
    var next: Next
)

data class Hint(
    var food: Food,
    var measures: List<Measure>
)

data class Parsed(
    var food: FoodX
)

data class Next(
    var href: String,
    var title: String
)

data class Food(
    var brand: String,
    var category: String,
    var categoryLabel: String,
    var foodContentsLabel: String,
    var foodId: String,
    var image: String,
    var label: String,
    var nutrients: Nutrients,
    var servingSizes: List<ServingSize>
)

data class Measure(
    var label: String,
    var qualified: List<Qualified>,
    var uri: String,
    var weight: Double
)

data class Nutrients(
    var CHOCDF: Double,
    var ENERC_KCAL: Double,
    var FAT: Double,
    var FIBTG: Double,
    var PROCNT: Double
)

data class ServingSize(
    var label: String,
    var quantity: Double,
    var uri: String
)

data class Qualified(
    var qualifiers: List<Qualifier>,
    var weight: Double
)

data class Qualifier(
    var label: String,
    var uri: String
)

data class FoodX(
    var category: String,
    var categoryLabel: String,
    var foodId: String,
    var image: String,
    var label: String,
    var nutrients: NutrientsX
)

data class NutrientsX(
    var CHOCDF: Double,
    var ENERC_KCAL: Double,
    var FAT: Double,
    var FIBTG: Double,
    var PROCNT: Double
)