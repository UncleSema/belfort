import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ct.belfort.util.Utilities;

public class UtilitiesTest {

    @Test
    void makeDoubleTest() {
        Assertions.assertEquals(Utilities.makeDouble(114, 250000000), 114.25);
        Assertions.assertEquals(Utilities.makeDouble(-200, -200000000), -200.20);
        Assertions.assertEquals(Utilities.makeDouble(-0 , -10000000), -0.01);
        Assertions.assertEquals(Utilities.makeDouble(123 , 123456789), 123.123456789);
        Assertions.assertEquals(Utilities.makeDouble(1312323 , 12345678), 1312323.012345678);
    }
}
