package creatures;

import huglife.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class TestClorus {
    private Clorus clorus;

    @Before
    public void setUp() {
        clorus = new Clorus(2);
    }

    @Test
    public void testReplicate() {
        Clorus offspring = clorus.replicate();

        double expectedEnergy = 1;
        double delta = 1e3;

        assertEquals(expectedEnergy, offspring.energy(), delta);
        assertNotSame(clorus, offspring);
    }

    @Test
    public void testAttack() {
        Plip plip = new Plip(0.5);
        clorus.attack(plip);

        double expectedEnergy = 2.5;
        double delta = 1e3;

        assertEquals(expectedEnergy, clorus.energy(), delta);
    }

    @Test
    public void given_no_empty_squares_when_choosing_action_then_return_stay() {
        HashMap<Direction, Occupant> surrounded = new HashMap<>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = clorus.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);
    }

    @Test
    public void given_nearby_empty_square_and_plip_when_choosing_Action_then_return_attack() {
        HashMap<Direction, Occupant> surr = new HashMap<>();
        surr.put(Direction.TOP, new Empty());
        surr.put(Direction.BOTTOM, new Empty());
        surr.put(Direction.LEFT, new Plip());
        surr.put(Direction.RIGHT, new Empty());

        Action actual = clorus.chooseAction(surr);
        Action expected = new Action(Action.ActionType.ATTACK, Direction.LEFT);

        assertEquals(expected, actual);
    }
}
