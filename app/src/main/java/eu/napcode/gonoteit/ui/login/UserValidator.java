package eu.napcode.gonoteit.ui.login;

import eu.napcode.gonoteit.app.utils.InputValidator;

public class UserValidator extends InputValidator {

    public boolean isUserValid(String login, String password) {
        return isLoginValid(login) && isPasswordValid(password);
    }

    public boolean isLoginValid(String login) {
        return super.isInputNotEmpty(login);
    }

    public boolean isPasswordValid(String password) {
        return super.isInputNotEmpty(password);
    }
}
