package eachillz.dev.itv.model

import com.google.firebase.firestore.DocumentId
import eachillz.dev.itv.user.User
import java.io.Serializable
import java.util.*

data class WeightWatcherModal (
    @DocumentId
    val id: String? = null,
    var weight:Int = 0,
    var date: Date = Date(),
    var user: User? = null
        ):Serializable