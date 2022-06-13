package eachillz.dev.itv.firestore

import android.support.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import eachillz.dev.itv.user.User
import java.io.Serializable
import java.util.*
@Keep
data class DailyMealPost(
    @DocumentId
    val id: String?=null,
    var name:String="",
    var protein: Long= 0,
    var calories: Long= 0,
    var fat: Long = 0,
    var carbs: Long = 0,
    var image: String = "",
    var serving: Int = 0,
    @get:PropertyName("creation_time_ms") @set:PropertyName("creation_time_ms") var creationTimeMs: Long = 0,
    var date : Date = Date(),
    var user: User?= null
        ):Serializable

