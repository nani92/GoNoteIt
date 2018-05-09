package eu.napcode.gonoteit.app.utils;

public class InputValidator {

    public boolean isInputNotEmpty(String input) {
        return input != null && input.length() > 0;
    }
}
