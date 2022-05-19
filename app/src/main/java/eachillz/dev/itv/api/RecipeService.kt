package eachillz.dev.itv.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.edamam.com/api/recipes/v2?
// type=public
// &q=chicken
// &app_id=
// &app_key=


interface RecipeService{
    @GET("v2?")
    fun recipeInfo(
        @Query("type") type:String,
        @Query("q") foodEntry :String,
        @Query("app_id") app_id :String,
        @Query("app_key") app_key :String,
    ): Call<RecipeResult>
}