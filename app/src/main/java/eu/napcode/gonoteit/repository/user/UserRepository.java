package eu.napcode.gonoteit.repository.user;

public interface UserRepository {

    void authenticateUser(String login, String password);
}
