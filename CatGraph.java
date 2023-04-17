import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdRandom;

public class CatGraph extends EdgeWeightedGraph {
    private Bag<CatEdge>[] adj;

    public CatGraph(int V) {
        super(V);
        adj = (Bag<CatEdge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<CatEdge>();
        }
    }

    public CatGraph(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges must be non-negative");
        for (int i = 0; i < E; i++) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            double weight = 0.01 * StdRandom.uniformInt(0, 100);
            CatEdge e = new CatEdge(v, w, weight);
            addEdge(e);
        }
    }

    public void addEdge(CatEdge e) {
        super.addEdge(e);
    }

    public Iterable<CatEdge> adj(int v) {
        super.adj(v);
    }

}


