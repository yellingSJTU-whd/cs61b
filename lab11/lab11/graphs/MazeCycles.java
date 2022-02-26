package lab11.graphs;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stack;

/**
 * @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private final int s;
    private final int[] pre;

    public MazeCycles(Maze m) {
        super(m);
        s = StdRandom.uniform(maze.V());
        pre = new int[maze.V()];
        distTo[s] = 0;
    }

    @Override
    public void solve() {
        dfs(s);
    }

    // Helper methods go here
    private void dfs(int v) {
        Stack<Integer> stack = new Stack<>();
        stack.push(v);

        while (!stack.isEmpty()) {
            int u = stack.pop();
            marked[u] = true;
            announce();

            for (int w : maze.adj(u)) {
                if (!marked[w]) {
                    stack.push(w);
                    distTo[w] = distTo[u] + 1;
                    pre[w] = u;
                } else if (pre[u] != w) {
                    renderCycle(w, u);
                    return;
                }
            }
        }
    }

    private void renderCycle(int w, int u) {
        for (int i = 0; i < maze.V(); i++) {
            edgeTo[i] = i;
        }
        edgeTo[w] = u;
        while (u != w) {
            edgeTo[u] = pre[u];
            u = pre[u];
        }
        announce();
    }
}

