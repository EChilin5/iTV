package eachillz.dev.itv.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// curl -X GET --header "Accept: application/json" "
// https://api.edamam.com/api/food-database/v2/parser?
// app_id=
// &app_key=
// &ingr=pizza
// &nutrition-type=cooking"

 interface FoodService {
    @GET("parser?")
    fun foodInfo(
        @Query("app_id") app_id:String,
        @Query("app_key") app_key :String,
        @Query("ingr") ingr :String,
        @Query("nutrition-type") nutrition_type :String,

    ): Call<FoodSearchResult>
}