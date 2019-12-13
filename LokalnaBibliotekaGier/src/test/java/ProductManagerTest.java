import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductManagerTest {
    @Test
    public void testPlay(){
        LocalLibrary localLibrary = new LocalLibrary();
        localLibrary.play();
    }
}