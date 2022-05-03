
import java.io.File;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Assertions;

class UserLoginTest {

    private static final File userNameFile = new File("username.txt");
    private static final File passwordFile = new File("password.txt");

    @Test
    void shouldLoginIfCredentialsAreCorrect() throws FileNotFoundException {
        // given
        String username = "pitzzahh";
        String password = "Password123";
        // when
        boolean login = UserLogin.checkCredentials(userNameFile, passwordFile, username, password);
        // then
        Assertions.assertTrue(login);
    }
    @Test
    void shouldNotLoginIfCredentialsAreInCorrect() throws FileNotFoundException {
        // given
        String username = "pitzzahh";
        String password = "!Password123";
        // when
        boolean login = UserLogin.checkCredentials(userNameFile, passwordFile, username, password);
        // then
        Assertions.assertFalse(login);
    }
}