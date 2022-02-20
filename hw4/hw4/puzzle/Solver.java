package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Solver {
    private final List<WorldState> solution;
    private final Map<WorldState, Integer> distanceMap;

    /**
     * Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     *
     * @param initial initial world state
     */
    public Solver(WorldState initial) {
        int count = 0;

        distanceMap = new HashMap<>();
        int totalMoves = 0;
        solution = new ArrayList<>();

        MinPQ<SearchNode> minHeap = new MinPQ<>(SearchNode::compare);
        SearchNode initialNode = new SearchNode(initial, 0, null);
        minHeap.insert(initialNode);
        while (!minHeap.isEmpty()) {
            SearchNode curr = minHeap.delMin();
            if (curr.state.isGoal()) {
                System.out.println("The number of total things ever enqueued: " + count);
                return;
            }
            solution.add(curr.state);
            for (WorldState worldState : curr.state.neighbors()) {
                if (curr.pre != null && worldState.equals(curr.pre.state)) {
                    continue;
                }
                minHeap.insert(new SearchNode(worldState, totalMoves + 1, curr));
                count++;
            }
        }
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState
     *
     * @return the minimum number of moves to solve the puzzle starting
     * at the initial WorldState
     */
    public int moves() {
        return solution.size() - 1;
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
        private final WorldState state;
        private final int from;
        private final SearchNode pre;

        SearchNode(WorldState state, int from, SearchNode pre) {
            this.state = state;
            this.from = from;
            this.pre = pre;
        }

        public int compare(SearchNode another) {
            if (!distanceMap.containsKey(state)) {
                distanceMap.put(state, state.estimatedDistanceToGoal());
            }
            if (!distanceMap.containsKey(another.state)) {
                distanceMap.put(another.state, another.state.estimatedDistanceToGoal());
            }
            return Integer.compare(from + distanceMap.get(state),
                    another.from + distanceMap.get(another.state));
        }
    }
}
