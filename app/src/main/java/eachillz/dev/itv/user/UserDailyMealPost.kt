package eachillz.dev.itv.user

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class UserDailyMealPost (
        @DocumentId
        val id: String?=null,
        var name:String="",
        var image_url :String = "",
        var date :String = "",
        @get:PropertyName("creation_time_ms")  @set:PropertyName("creation_time_ms") var creationTime : Long = 0,
        var calories : Long = 0,
        var user : User? = null
        )