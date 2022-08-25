import com.blog.utils.JWTUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class BlogAppTest {
    @Test
    public static void main(String[] args) {
        String password = "123456";
        String salt = "wahoyu!@#";
        password = DigestUtils.md5Hex(password + salt);
        System.out.println(password);
    }
}
