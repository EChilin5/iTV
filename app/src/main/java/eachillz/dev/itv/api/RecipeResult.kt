package eachillz.dev.itv.api

import com.google.gson.annotations.SerializedName

data class RecipeResult(
    var hits: List<Hit>
)

data class Hit(
    var recipe: Recipe
)

data class Recipe(
    var calories: Double,
    var cautions: List<String>,
    var cuisineType: List<String>,
    var dietLabels: List<String>,
    var digest: List<Digest>,
    var dishType: List<String>,
    var healthLabels: List<String>,
    var image: String,
    var images: Images,
    var ingredientLines: List<String>,
    var ingredients: List<Ingredient>,
    var label: String,
    var mealType: List<String>,
    var shareAs: String,
    var source: String,
    var totalDaily: TotalDaily,
    var totalNutrients: TotalNutrients,
    var totalTime: Double,
    var totalWeight: Double,
    var uri: String,
    var url: String,
    var yield: Double
)



data class Digest(
    var daily: Double,
    var hasRDI: Boolean,
    var label: String,
    var schemaOrgTag: String,
    var sub: List<Sub>,
    var tag: String,
    var total: Double,
    var unit: String
)

data class Images(
    var LARGE: LARGE,
    var REGULAR: REGULAR,
    var SMALL: SMALL,
    var THUMBNAIL: THUMBNAIL
)

data class Ingredient(
    var food: String,
    var foodCategory: String,
    var foodId: String,
    var image: String,
    var measure: String,
    var quantity: Double,
    var text: String,
    var weight: Double
)

data class TotalDaily(
    var CA: CA,
    var CHOCDF: CHOCDF,
    var CHOLE: CHOLE,
    var ENERC_KCAL: ENERCKCAL,
    var FASAT: FASAT,
    var FAT: FAT,
    var FE: FE,
    var FIBTG: FIBTG,
    var FOLDFE: FOLDFE,
    var K: K,
    var MG: MG,
    var NA: NA,
    var NIA: NIA,
    var P: P,
    var PROCNT: PROCNT,
    var RIBF: RIBF,
    var THIA: THIA,
    var TOCPHA: TOCPHA,
    var VITA_RAE: VITARAE,
    var VITB12: VITB12,
    var VITB6A: VITB6A,
    var VITC: VITC,
    var VITD: VITD,
    var VITK1: VITK1,
    var ZN: ZN
)

data class TotalNutrients(
    var CA: CAX,
    @SerializedName("CHOCDF")
    var cho: CHOCDFX,
    @SerializedName("CHOCDF.net")
    var CHOCDF: CHOCDFNet,
    var CHOLE: CHOLEX,
    var ENERC_KCAL: ENERCKCALX,
    var FAMS: FAMS,
    var FAPU: FAPU,
    var FASAT: FASATX,
    var FAT: FATX,
    var FATRN: FATRN,
    var FE: FEX,
    var FIBTG: FIBTGX,
    var FOLAC: FOLAC,
    var FOLDFE: FOLDFEX,
    var FOLFD: FOLFD,
    var K: KX,
    var MG: MGX,
    var NA: NAX,
    var NIA: NIAX,
    var P: PX,
    var PROCNT: PROCNTX,
    @SerializedName("SUGAR")
    var sugar: SUGAR,
    @SerializedName("SUGAR.added")
    var addedSugar: SUGARAdded,
    @SerializedName("Sugar.alcohol")
    var alcoholSugar : SugarAlcohol,
    var THIA: THIAX,
    var TOCPHA: TOCPHAX,
    var VITA_RAE: VITARAEX,
    var VITB12: VITB12X,
    var VITB6A: VITB6AX,
    var VITC: VITCX,
    var VITD: VITDX,
    var VITK1: VITK1X,
    var WATER: WATER,
    var ZN: ZNX
)

data class Sub(
    var daily: Double,
    var hasRDI: Boolean,
    var label: String,
    var schemaOrgTag: String,
    var tag: String,
    var total: Double,
    var unit: String
)

data class LARGE(
    var height: Int,
    var url: String,
    var width: Int
)

data class REGULAR(
    var height: Int,
    var url: String,
    var width: Int
)

data class SMALL(
    var height: Int,
    var url: String,
    var width: Int
)

data class THUMBNAIL(
    var height: Int,
    var url: String,
    var width: Int
)

data class CA(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class CHOCDF(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class CHOLE(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class ENERCKCAL(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FASAT(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FAT(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FE(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FIBTG(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FOLDFE(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class K(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class MG(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class NA(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class NIA(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class P(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class PROCNT(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class RIBF(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class THIA(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class TOCPHA(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITARAE(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITB12(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITB6A(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITC(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITD(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITK1(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class ZN(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class CAX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class CHOCDFX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class CHOCDFNet(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class CHOLEX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class ENERCKCALX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FAMS(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FAPU(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FASATX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FATX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FATRN(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FEX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FIBTGX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FOLAC(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FOLDFEX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class FOLFD(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class KX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class MGX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class NAX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class NIAX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class PX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class PROCNTX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class RIBFX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class SUGAR(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class SUGARAdded(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class SugarAlcohol(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class THIAX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class TOCPHAX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITARAEX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITB12X(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITB6AX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITCX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITDX(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class VITK1X(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class WATER(
    var label: String,
    var quantity: Double,
    var unit: String
)

data class ZNX(
    var label: String,
    var quantity: Double,
    var unit: String
)