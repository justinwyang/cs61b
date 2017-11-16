import java.util.ArrayDeque;

/**
 *  @author Josh Hug
 */

public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        ArrayDeque<Integer> q = new ArrayDeque<>();
        announce();
        q.add(s);

        while (!q.isEmpty()) {
            int v = q.pop();
            marked[v] = true;
            announce();

            if (v == t) {
                targetFound = true;
            }

            if (targetFound) {
                return;
            }

            for (int w: maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    announce();
                    distTo[w] = distTo[v] + 1;
                    q.add(w);
                    if (targetFound) {
                        return;
                    }
                }

            }
        }
    }

    @Override
    public void solve() {
        bfs();
    }
}

