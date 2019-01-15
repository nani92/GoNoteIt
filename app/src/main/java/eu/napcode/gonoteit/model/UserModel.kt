package eu.napcode.gonoteit.model

import eu.napcode.gonoteit.dao.user.UserEntity
import eu.napcode.gonoteit.data.user.favoritesMapToList

class UserModel(var userName: String) {
    var favorites: List<Long> = mutableListOf()

    constructor(userEntity: UserEntity) : this(userEntity.name) {
        favorites = favoritesMapToList(userEntity.favorites)
    }
}
