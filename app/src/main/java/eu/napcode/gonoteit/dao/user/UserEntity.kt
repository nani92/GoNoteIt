package eu.napcode.gonoteit.dao.user

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import eu.napcode.gonoteit.dao.user.UserEntity.Companion.TABLE_NAME
import eu.napcode.gonoteit.data.user.favoritesMapToString
import eu.napcode.gonoteit.model.UserModel

@Entity(tableName = TABLE_NAME)
data class UserEntity(

        @PrimaryKey
        @ColumnInfo(name = COLUMN_NAME)
        var name: String?,

        @ColumnInfo(name = COLUMN_FAVORITES)
        var favorites: String?
) {

    constructor(userModel: UserModel) :
            this(
                    name = userModel.userName,
                    favorites = favoritesMapToString(userModel.favorites)
            )


    companion object {

        const val TABLE_NAME = "user"

        const val COLUMN_NAME = "name"
        const val COLUMN_FAVORITES = "favorites"
    }
}
