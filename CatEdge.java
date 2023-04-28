import edu.princeton.cs.algs4.Edge;

public class CatEdge extends Edge {

    private double weight;

    public CatEdge(int v, int w) {
        super(v, w, 1);
        weight = 1;
    }
    
    public void changeWeight(double newWeight) {
        weight = newWeight;
    }

    public double weight() {
        return weight;
    }
}
