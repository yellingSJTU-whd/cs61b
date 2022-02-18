package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.LinkedHashSet;
import java.util.Set;

public class Solver {
    private int totalMoves;
    private Set<WorldState> solution;

    /**
     * Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     *
     * @param initial initial world state
     */
    public Solver(WorldState initial) {
        totalMoves = 0;
        solution = new LinkedHashSet<>();
        solution.add(initial);

        if (initial.isGoal()) {
            return;
        }

        SearchNode initialNode = new SearchNode(initial, 0, null);
        MinPQ<SearchNode> minHeap = new MinPQ<>(Solver::compare);
        minHeap.insert(initialNode);
        SearchNode currNode = initialNode;
        while (!currNode.state.isGoal()) {


            for (WorldState worldState : currNode.state.neighbors()) {
                if (currNode.pre == null || !worldState.equals(currNode.pre.state)) {
                    minHeap.insert(new SearchNode(worldState, totalMoves + 1, currNode));
                }
            }
            currNode = minHeap.delMin();
            totalMoves++;
            System.out.println(currNode.state.toString());
        }
    }

    private static int compare(SearchNode x, SearchNode y) {
        return Integer.compare(x.from + x.state.estimatedDistanceToGoal(),
                y.from + x.state.estimatedDistanceToGoal());
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState
     *
     * @return the minimum number of moves to solve the puzzle starting
     * at the initial WorldState
     */
    public int moves() {
        return totalMoves;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution
     *
     * @return a sequence of WorldStates from the initial WorldState
     * to the solution
     */
    public Iterable<WorldState> solution() {
        return solution;
    }

    private class SearchNode {
        private WorldState state;
        private int from;
        private SearchNode pre;

        SearchNode(WorldState state, int from, SearchNode pre) {
            this.state = state;
            this.from = from;
            this.pre = pre;
        }
    }
}
