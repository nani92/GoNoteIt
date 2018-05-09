package eu.napcode.gonoteit.repository.user;

import com.apollographql.apollo.api.Response;

import eu.napcode.gonoteit.AuthenticateMutation;
import io.reactivex.Observable;

public interface UserRepository {

    Observable<Response<AuthenticateMutation.Data>> authenticateUser(String login, String password);

    public void saveUserAuthData(String login, String token);
}
