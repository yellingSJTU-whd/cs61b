package lab11.graphs;

import edu.princeton.cs.algs4.Queue;

/**
 * @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;


    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /**
     * Conducts a breadth first search of the maze starting at the source.
     */
    private void bfs(int v) {

        Queue<Integer> queue = new Queue<>();
        queue.enqueue(v);

        while (!queue.isEmpty()) {
            int u = queue.dequeue();

            if (u == t) {
                return;
            }
            marked[u] = true;
            announce();

            for (int w : maze.adj(u)) {
                if (!marked[w]) {
                    queue.enqueue(w);

                    edgeTo[w] = u;
                    distTo[w] = distTo[u] + 1;
                    marked[w] = true;
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
}

