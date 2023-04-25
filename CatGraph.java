import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Bag;

public class CatGraph extends EdgeWeightedGraph {
    private Bag<CatEdge>[] adj;

    public CatGraph(int V) {
        super(V);
        adj = (Bag<CatEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<CatEdge>();
        }
    }
}


