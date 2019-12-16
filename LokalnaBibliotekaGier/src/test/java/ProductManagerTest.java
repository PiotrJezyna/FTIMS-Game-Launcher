import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductManagerTest {
    @Test
    public void testPlay(){

        try {
            LocalLibrary localLibrary = new LocalLibrary(1);
            localLibrary.installGame(1);
            localLibrary.play(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}