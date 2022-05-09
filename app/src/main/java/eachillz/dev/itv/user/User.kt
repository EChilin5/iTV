package eachillz.dev.itv.user

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class User(
    @get:Exclude var id: String = "",
    var email : String ="",
    var username: String =""
) : Serializable