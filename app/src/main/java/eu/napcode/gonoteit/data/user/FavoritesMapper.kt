package eu.napcode.gonoteit.data.user

fun favoritesMapToString(favorites : List<Long>) : String {
    val favoritesStringBuilder = StringBuilder("{\"favorite\": [")
    for (i in favorites.indices) {
        favoritesStringBuilder.append(favorites[i])

        if (i < favorites.size - 1) {
            favoritesStringBuilder.append(',')
        }
    }
    favoritesStringBuilder.append("]}")

    return favoritesStringBuilder.toString()
}

fun favoritesMapToList(favorites : String?) : List<Long> {

    if (favorites == null) {
        return mutableListOf()
    }

    var favValues = favorites.split('[')[1]
    favValues = favValues.split(']')[0]
    var favoritesStringList = favValues.split(',')

    val favoritesList = mutableListOf<Long>()
    favoritesStringList.forEach { favoritesList.add(it.toLong())}

    return favoritesList
}