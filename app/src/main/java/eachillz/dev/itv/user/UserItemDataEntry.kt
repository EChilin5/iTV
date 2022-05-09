package eachillz.dev.itv.user

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class UserItemDataEntry (
    @DocumentId
    val id: String?=null,
    var title:String="",
    var imagedescription:String = "",
    var image_url :String = "",
    @get:PropertyName("creation_time_ms")  @set:PropertyName("creation_time_ms") var creationTime : Long = 0,
    var user: User? = null
) : Serializable