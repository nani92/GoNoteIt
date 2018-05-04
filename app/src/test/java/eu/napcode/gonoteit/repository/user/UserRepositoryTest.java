package eu.napcode.gonoteit.repository.user;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

    private UserRepository userRepository;

    @Before
    public void initial() {
        userRepository = new UserRepositoryImpl();
    }
}