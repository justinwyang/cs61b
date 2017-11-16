import java.util.Observable;
/**
 *  @author Josh Hug
 */

public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int end = -1;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        for (int i = 0; i < distTo.length; i++) {
            if (!marked[i]) {
                if (dfs(i, -1)) {
                    return;
                }
            }
        }

    }

    private boolean dfs(int v, int parent) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                if (dfs(w, v) && end != -1) {
                    edgeTo[w] = v;
                    announce();
                    if (v == end) {
                        end = -1;
                    }
                    return true;
                }
            } else if (w != parent) {
                end = w;
                edgeTo[w] = v;
                return true;
            }
        }
        return false;
    }
}

