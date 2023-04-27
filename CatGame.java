import edu.princeton.cs.algs4.DijkstraUndirectedSP;
import edu.princeton.cs.algs4.Stack;

public class CatGame {
    private boolean escaped, trapped;
    private int numCols, catPosition;
    private CatGraph board;
    private boolean[] marked;

    public CatGame(int n) {
        numCols = n;
        escaped = false;
        trapped = false;
        catPosition = posToInd(numCols/2, numCols/2);
        board = new CatGraph(n*n + 1);
        marked = new boolean[n*n + 1];

        for (int i = 0; i < n*n; i++) {
            int r = indToPos(i)[0];
            int c = indToPos(i)[1];

            if (r == 0 || c == 0 || r == (n-1) || c == (n-1)) {
                board.addEdge(new CatEdge(i, n*n, 1));
            }
            if (c > 0 && r < n) {
                board.addEdge(new CatEdge(i, posToInd(r, c-1), 1));
            }
            if (r > 0 && r < n) {
                board.addEdge(new CatEdge(i, posToInd(r, c-1), 1));
            }
        }
        int numBlocked = (int)(Math.random() * n/2) + n/2; 
        
        for (int i = 0; i < numBlocked; i++) {
            marked[(int)(Math.random()*(n*n))] = false;
        }
    }

    public void markTile(int row, int col) {
        int index = posToInd(row, col);
        marked[index] = true;
        for (CatEdge i : board.adj(index)) {
            i.changeWeight(Double.POSITIVE_INFINITY);
        }
        DijkstraUndirectedSP sp = new DijkstraUndirectedSP(board, index);
        Stack<CatEdge> path = sp.pathTo(numCols*numCols);
        catPosition = path.pop().other(catPosition);
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
        CatGame g = new CatGame(5);
    }
}