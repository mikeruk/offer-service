package insuranceservices.offerservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


// The @ActiveProfiles("test")uses the application-test.properties which applies H2 DB, because
// it's needed to run tests in the GitHub Actions Flow.
@ActiveProfiles("test")
@SpringBootTest
class OfferServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
