package eachillz.dev.itv.util

data class RecipteModal(
    var recipes: List<Recipe>
)

data class Recipe(
    var aggregateLikes: Int,
    var analyzedInstructions: List<AnalyzedInstruction>,
    var cheap: Boolean,
    var creditsText: String,
    var cuisines: List<Any>,
    var dairyFree: Boolean,
    var diets: List<String>,
    var dishTypes: List<String>,
    var extendedIngredients: List<ExtendedIngredient>,
    var gaps: String,
    var glutenFree: Boolean,
    var healthScore: Double,
    var id: Int,
    var image: String,
    var imageType: String,
    var instructions: String,
    var license: String,
    var lowFodmap: Boolean,
    var occasions: List<Any>,
    var originalId: Any,
    var pricePerServing: Double,
    var readyInMinutes: Int,
    var servings: Int,
    var sourceName: String,
    var sourceUrl: String,
    var spoonacularScore: Double,
    var spoonacularSourceUrl: String,
    var summary: String,
    var sustainable: Boolean,
    var title: String,
    var vegan: Boolean,
    var vegetarian: Boolean,
    var veryHealthy: Boolean,
    var veryPopular: Boolean,
    var weightWatcherSmartPoints: Int
)

data class AnalyzedInstruction(
    var name: String,
    var steps: List<Step>
)

data class ExtendedIngredient(
    var aisle: String,
    var amount: Double,
    var consistency: String,
    var id: Int,
    var image: String,
    var measures: Measures,
    var meta: List<String>,
    var name: String,
    var nameClean: String,
    var original: String,
    var originalName: String,
    var unit: String
)

data class Step(
    var equipment: List<Equipment>,
    var ingredients: List<Ingredient>,
    var length: Length,
    var number: Int,
    var step: String
)

data class Equipment(
    var id: Int,
    var image: String,
    var localizedName: String,
    var name: String,
    var temperature: Temperature
)

data class Ingredient(
    var id: Int,
    var image: String,
    var localizedName: String,
    var name: String
)

data class Length(
    var number: Int,
    var unit: String
)

data class Temperature(
    var number: Double,
    var unit: String
)

data class Measures(
    var metric: Metric,
    var us: Us
)

data class Metric(
    var amount: Double,
    var unitLong: String,
    var unitShort: String
)

data class Us(
    var amount: Double,
    var unitLong: String,
    var unitShort: String
)