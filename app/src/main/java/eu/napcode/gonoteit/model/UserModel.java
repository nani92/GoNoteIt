package eu.napcode.gonoteit.model;

public class UserModel {

    private String userName;

    public UserModel(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
