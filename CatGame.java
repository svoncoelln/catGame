import edu.princeton.cs.algs4.DijkstraUndirectedSP;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Edge;

public class CatGame {
    private boolean escaped, trapped;
    public int numCols, catPosition;
    private CatGraph board;
    public boolean[] marked;

    public CatGame(int n) {
        numCols = n;
        escaped = false;
        trapped = false;
        catPosition = posToInd(numCols/2, numCols/2);
        board = new CatGraph(n*n + 1);
        marked = new boolean[n*n + 1];

        for (int r = 0; r < n; r ++) {
            //non bottom rows
            if (r < n-1) {
                for (int c = 0; c < n; c++){
                    int index = posToInd(r, c); 
                    if (r == 0 || c == 0 || c == n-1) {
                        board.addEdge(new CatEdge(index, n*n)); //freedom hexagon
                    }
                    if (c < n-1) {
                        board.addEdge(new CatEdge(index, index+1)); //to the right
                    }
                    if (r%n == 0 && c > 0) {
                        board.addEdge(new CatEdge(index, index+n-1)); //down and to the left 
                    }
                    if (r%n == 1 && c < n-1) {
                        board.addEdge(new CatEdge(index, index+n+1)); //down and to the right 
                    }
                    board.addEdge(new CatEdge(index, index+n)); //down
                }
            }
            //bottom row
            else if (r == n-1) {
                for (int c = 0; c < n; c++){
                    int index = posToInd(r, c);
                    board.addEdge(new CatEdge(index, n*n)); //freedom hexagon
                    if (c < n-1) {
                        board.addEdge(new CatEdge(index, index+1)); //to the right
                    }
                }
            }
        }

        int numBlocked = (int)(Math.random() * (n-1)/2) + (n-1)/2; 
        
        for (int i = 0; i < numBlocked; i++) {
            int index = (int)(Math.random()*(n*n)-1);
            marked[index] = true;
            for (Edge j : board.adj(index)) {
                CatEdge e = (CatEdge) j;
                e.changeWeight(Double.POSITIVE_INFINITY);
            }
        }
    }

    public void markTile(int row, int col) { //do i need to indicate escaped/trapped somehow?
        int index = posToInd(row, col);
        marked[index] = true;

        for (Edge i : board.adj(index)) {
            CatEdge e = (CatEdge) i;
            e.changeWeight(Double.POSITIVE_INFINITY);
        }
        DijkstraUndirectedSP sp = new DijkstraUndirectedSP(board, index);
        if (!sp.hasPathTo(numCols*numCols)) { // might need to do distto == infinity
            System.out.println("dspt trapped");
            trapped = true;
        }
        else {
            Stack<Edge> path = (Stack<Edge>) sp.pathTo(numCols*numCols);
            catPosition = path.pop().other(catPosition);
        }
        if (catPosition == numCols*numCols) {
            escaped = true;
        }
    }

    public boolean marked(int row, int col) {
        return marked[posToInd(row, col)]; //check distTo
    }

    public boolean catIsTrapped() {
        return trapped;
    }

    public boolean catHasEscaped() {
        return escaped;
    }

    public int[] getCatTile() {
        return indToPos(catPosition);
    }

    private int posToInd(int row, int col) {
        return row*numCols + col;
    }

    private int[] indToPos(int index) {
        int r = index / numCols;
        int c = index % numCols;
        int[] result = {r, c};
        return result;
    }

    public static void main(String[] args) {
        CatGame g = new CatGame(3);
        for (int i = 0; i < g.marked.length; i++) {
            System.out.println("i: " + i + ", " + g.marked[i]);
        }
        System.out.println();
        for (Edge i : g.board.edges()) {
            int v = i.either();
            int w = i.other(v);
            System.out.println(v + ", " +  w + ", " + i.weight());
        }
        // System.out.println("trapped? " + g.catIsTrapped());
        // System.out.println("pos: " + g.catPosition);
        // g.markTile(0, 0);
        // System.out.println("pos: " + g.catPosition);
        // g.markTile(1, 0);
        // System.out.println("pos: " + g.catPosition);
        // System.out.println(g.catHasEscaped());
    }
}