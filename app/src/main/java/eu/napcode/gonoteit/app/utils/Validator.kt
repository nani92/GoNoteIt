package eu.napcode.gonoteit.app.utils

fun isInputNotEmpty(input: String?): Boolean {
    return input != null && input.isNotEmpty()
}

fun isUserValid(login: String?, password: String?): Boolean {
    return isLoginValid(login) && isPasswordValid(password)
}

fun isLoginValid(login: String?): Boolean {
    return isInputNotEmpty(login)
}

fun isPasswordValid(password: String?): Boolean {
    return isInputNotEmpty(password)
}


fun isTokenValid(token: String?): Boolean {
    return isInputNotEmpty(token)
}

fun isHostValid(host: String?): Boolean {
    return isInputNotEmpty(host)
}

