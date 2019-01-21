package eu.napcode.gonoteit.data.user

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import eu.napcode.gonoteit.model.UserModel

fun mapUserDataStringToUserData(userModel: UserModel, data: Any?) {
    if (data !is String) {
        return
    }

    val userData = Gson().fromJson(data, UserData::class.java)

    userModel.favorites = userData.favorites
}

class UserData {
    @SerializedName("favorite")
    var favorites : List<Long> = listOf()
}