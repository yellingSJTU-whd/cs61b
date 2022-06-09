package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.HugLifeUtils;
import huglife.Occupant;

import java.awt.Color;
import java.util.List;
import java.util.Map;

public class Clorus extends Creature {
    /**
     * Red, green and blue color.
     */
    private int r, g, b;

    /**
     * Cloruses should lose 0.03 units of energy on a MOVE action
     */
    private static final double MOVE_ENERGY_LOSE = 0.03;

    /**
     * Cloruses should lose 0.01 units of energy on a STAY action
     */
    private static final double STAY_ENERGY_LOSE = 0.01;

    /**
     * When a Clorus replicates, it keeps 50% of its energy.
     */
    private static final double REP_ENERGY_RETAINED_RATIO = 0.5;

    /**
     * The other 50% goes to its offspring.
     */
    private static final double REP_ENERGY_GIVEN_RATIO = 0.5;

    /**
     * Creates a clorus with energy equal to e.
     */
    public Clorus(double e) {
        super("clorus");
        r = g = b = 0;
        energy = e;
    }

    /**
     * Cloruses should lose 0.03 units of energy on a MOVE action.
     */
    @Override
    public void move() {
        energy -= MOVE_ENERGY_LOSE;
    }

    /**
     * Clorus gains energy from the creature being attacked.
     */
    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    /**
     * When a Clorus replicates, it keeps 50% of its energy.
     * The other 50% goes to its offspring.
     * No energy is lost in the replication process.
     */
    @Override
    public Clorus replicate() {
        energy *= REP_ENERGY_RETAINED_RATIO;
        return new Clorus(energy * REP_ENERGY_GIVEN_RATIO);
    }

    /**
     * Cloruses should lose 0.01 units of energy on a STAY action.
     */
    @Override
    public void stay() {
        energy -= STAY_ENERGY_LOSE;
    }

    /**
     * Cloruses should obey exactly the following behavioral rules:
     * 1. If there are no empty squares, the Clorus will STAY.
     * 2. Otherwise, if any Plips are seen, the Clorus will ATTACK one of them randomly.
     * 3. Otherwise, if the Clorus has energy greater than or equal to one,
     * it will REPLICATE to a random empty square.
     * 4. Otherwise, the Clorus will MOVE to a random empty square.
     */
    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        }

        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (plips.size() > 0) {
            return new Action(Action.ActionType.ATTACK, HugLifeUtils.randomEntry(plips));
        }

        if (energy >= 1) {
            return new Action(Action.ActionType.REPLICATE, HugLifeUtils.randomEntry(empties));
        }

        return new Action(Action.ActionType.MOVE, HugLifeUtils.randomEntry(empties));
    }

    /**
     * Return the color red = 34, green = 0, blue = 231.
     */
    @Override
    public Color color() {
        r = 34;
        g = 0;
        b = 231;
        return color(r, g, b);
    }
}
