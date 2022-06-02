package eachillz.dev.itv.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import eachillz.dev.itv.user.User
import java.util.*

data class UserDailyMealPost (
        @DocumentId
        val id: String?=null,
        var name:String="",
        var image_url :String = "",
        var date : Date = Date(),
        @get:PropertyName("creation_time_ms")  @set:PropertyName("creation_time_ms") var creationTime : Long = 0,
        var calories : Long = 0,
        var user : User? = null
        )