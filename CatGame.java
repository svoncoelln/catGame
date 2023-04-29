import edu.princeton.cs.algs4.DijkstraUndirectedSP;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Edge;

/**
 * Recreate the "Trap the Cat" game using Dijktra's shortest paths algorithm. 
 * The game consists of a cat attempting to escape an n-by-n array of hexagons,
 * as the player tries to trap it by blocking hexagons so it cannot pass through
 * them. The code finds the shortest path out after a hexagon is marked,
 * and directs the cat to move one step along that path.  
 * 
 * @author Sophie von Coelln, with liberal use of Sedgewick's code
 */
public class CatGame {
    private boolean escaped, trapped; //loss and win, respectively 
    private int numCols, catPosition;
    private CatGraph board;
    private boolean[] marked;
    DijkstraUndirectedSP sp; //shortest paths tree from the cat's current position 

    /**
     * Connect all hexagons to those tiles adjacent beneath them or to their 
     * right (when applicable). All edges are given a weight of 1 by default.
     * Block a random number (between the total columns or 1.5 times the total
     * columns) of tiles by making the weight of all its incident edges infinite. 
     * @param n the number of columns in the board (total number of tiles is n^2).
     */
    public CatGame(int n) {
        numCols = n;
        escaped = false;
        trapped = false;
        catPosition = posToInd(numCols/2, numCols/2);
        board = new CatGraph(n*n + 1); //n^2 + 1 used here and in the following line because of the ghost vertex representing freedom.
        marked = new boolean[n*n + 1];

        //loop through all rows
        for (int r = 0; r < n; r++) {
            //non bottom rows
            if (r < n-1) {
                //loop through all columns, making any necessary connections 
                for (int c = 0; c < n; c++){
                    int index = posToInd(r, c); 
                    if (r == 0 || c == 0 || c == n-1) {
                        board.addEdge(new CatEdge(index, n*n)); //freedom hexagon
                    }
                    if (c < n-1) {
                        board.addEdge(new CatEdge(index, index+1)); //to the right
                    }
                    if (r%2 == 0 && c > 0) {
                        board.addEdge(new CatEdge(index, index+n-1)); //down and to the left 
                    }
                    if (r%2 == 1 && c < n-1) {
                        board.addEdge(new CatEdge(index, index+n+1)); //down and to the right 
                    }
                    board.addEdge(new CatEdge(index, index+n)); //down
                }
            }
            //bottom row
            else if (r == n-1) {
                //loop through all columns, making any necessary connections 
                for (int c = 0; c < n; c++){
                    int index = posToInd(r, c);
                    board.addEdge(new CatEdge(index, n*n)); //freedom hexagon
                    if (c < n-1) {
                        board.addEdge(new CatEdge(index, index+1)); //to the right
                    }
                }
            }
        }

        // Block a random number of tiles (between the total columns or 1.5 times the total* columns)
        int numBlocked = (int)(Math.random() * n/2) + n; 
        for (int i = 0; i < numBlocked; i++) {
            int index = (int)(Math.random()*(n*n)-1);
            if (index != catPosition) { // do not block the starting tile
                updateTileWeights(index);
            }
        }
    }

    /**
     * Block the passed tile by increasing the weight of its incident edges.
     * Find the shortest path from the cat's current position to the outside of
     * the board, and take the first step on that path. 
     * @param row the row of the blocked tile
     * @param col the column of the blocked tile 
     */
    public void markTile(int row, int col) { 
        int index = posToInd(row, col);

        if (index != catPosition) { // cannot trap the cat by clicking on it
            updateTileWeights(index);
            
            sp = new DijkstraUndirectedSP(board, catPosition); //build shortest paths tree from the cat's position 
            if (!sp.hasPathTo(numCols*numCols)) { 
                trapped = true;
            }
            else {
                Stack<Edge> path = (Stack<Edge>) sp.pathTo(numCols*numCols);
                catPosition = path.pop().other(catPosition); //move the cat one step along the identified shortest path out
            }
            if (catPosition == numCols*numCols) {
                escaped = true;
            }
        }
    }

    /**
     * Is the given tile marked/blocked?
     * @param row the row of the tile in question
     * @param col the column of the tile in question 
     * @return {@code true} if the tile is marked/blocked, {@code false} otherwise 
     */
    public boolean marked(int row, int col) {
        return marked[posToInd(row, col)]; 
    }

    /**
     * Is the cat trapped? 
     * @return {@code true} if cat is trapped, {@code false} otherwise 
     */
    public boolean catIsTrapped() {
        return trapped;
    }

    /**
     * Has the cat escaped?
     * @return {@code true} if the cat has escaped, {@code false} otherwise 
     */
    public boolean catHasEscaped() {
        return escaped;
    }

    /**
     * Return the cat's current position.
     * @return an array containing the cat's current row and column ({@code [row, col}].
     */
    public int[] getCatTile() {
        return indToPos(catPosition);
    }

    //Mark the passed tile by setting the weight of its incident edges to infinity. 
    private void updateTileWeights(int index) {
        marked[index] = true;
        for (Edge i : board.adj(index)) {
            CatEdge e = (CatEdge) i;
            e.changeWeight(Double.POSITIVE_INFINITY);
        }
    }

    //Convert from (row, col) position to index.
    private int posToInd(int row, int col) {
        return row*numCols + col;
    }

    //Convert from index to (row, col) position. 
    private int[] indToPos(int index) {
        int r = index / numCols;
        int c = index % numCols;
        int[] result = {r, c};
        return result;
    }
}
