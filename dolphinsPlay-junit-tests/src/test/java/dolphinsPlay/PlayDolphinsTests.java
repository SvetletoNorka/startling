package dolphinsPlay;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PlayDolphinsTests {

    private Dolphin dolphin1;
    private Dolphin dolphin2;

    private DolphinsPlay play;

    @BeforeEach
    public void setup() {

        this.dolphin1 = new Dolphin("Type1", "Name1", 400);
        this.dolphin2 = new Dolphin("Type2", "Name2", 200);
        this.play = new DolphinsPlay("Pool", 2);
    }

    @Test
    public void testAddDolphin() {
        play.addDolphin(dolphin1);
        play.addDolphin(dolphin2);

        Assertions.assertEquals(2, play.getCount());
    }

    @Test
    public void testAddNullDelphin() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            play.addDolphin(null);
        });
    }

    @Test
    public void testCapacity() {
        play.addDolphin(dolphin1);
        play.addDolphin(dolphin2);
        Dolphin dolphin3 = new Dolphin("Type3", "Name3", 100);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            play.addDolphin(dolphin3);
        });
    }

    @Test
    public void testDolphinExist() {
        play.addDolphin(dolphin1);
        play.addDolphin(dolphin2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            play.addDolphin(dolphin2);
        });
    }

    @Test
    public void testRemoveDolphin() {
        play.addDolphin(dolphin1);
        Assertions.assertTrue(play.removeDolphin("Name1"));
    }

    @Test
    public void testGetTheDolphinWithTheMaxEnergy() {
        play.addDolphin(dolphin1);
        play.addDolphin(dolphin2);
        Assertions.assertEquals(play.getTheDolphinWithTheMaxEnergy(), "Name1");
    }

    @Test
    public void testFindAllDolphinsByType() {
        DolphinsPlay dolphinsPlay = new DolphinsPlay("Swimming", 5);
        dolphinsPlay.addDolphin(dolphin1);
        dolphinsPlay.addDolphin(dolphin2);
        Dolphin dolphin3 = new Dolphin("Type1", "Name3", 20);
        dolphinsPlay.addDolphin(dolphin3);
        List<Dolphin> dolphins1 = dolphinsPlay.findAllDolphinsByType("Type1");

        Assertions.assertEquals(2, dolphins1.size());
        Assertions.assertEquals(dolphin1, dolphins1.get(0));
        Assertions.assertEquals(dolphin3, dolphins1.get(1));
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals("Pool", play.getName());
    }

    @Test
    public void testGetCount() {
        play.addDolphin(dolphin1);
        play.addDolphin(dolphin2);
        Assertions.assertEquals(2, play.getCount());
    }

    @Test
    public void testGetCapacity() {
        Assertions.assertEquals(2, play.getCapacity());
    }

    @Test
    public void testSetNegativeCapacity() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DolphinsPlay testPlay = new DolphinsPlay("TestPlay", -3);
        });
    }

    @Test
    public void testGetDolphins() {
        play.addDolphin(dolphin1);
        play.addDolphin(dolphin2);

        List<Dolphin> dolphins = play.getDolphins();

        Assertions.assertEquals(2, dolphins.size());
        Assertions.assertTrue(dolphins.contains(dolphin1));
        Assertions.assertTrue(dolphins.contains(dolphin2));
    }

}