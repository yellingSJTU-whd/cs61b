package lab11.graphs;

import edu.princeton.cs.algs4.MinPQ;
import java.util.Comparator;

/**
 * @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private Maze maze;
    private boolean visited[];

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /**
     * Estimate of the distance from v to the target.
     */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /**
     * Performs an A star search from vertex s.
     */
    private void astar() {
        MinPQ<Integer> minHeap = new MinPQ<>(Comparator.comparingInt(this::h));
        minHeap.insert(s);
        visited = new boolean[maze.V()];
        visited[s] = marked[s] = true;

        while (!minHeap.isEmpty()) {
            Integer curr = minHeap.delMin();
            marked[curr] = true;
            System.out.println(curr + " " + h(curr));
            announce();

            if (curr == t) {
                return;
            }

            for (int u : maze.adj(curr)) {
                if (!visited[u]) {
                    visited[u] = true;
                    edgeTo[u] = curr;
                    distTo[u] = distTo[curr] + 1;
                    minHeap.insert(u);
                }
            }
        }
    }

    @Override
    public void solve() {
        astar();
    }

}

