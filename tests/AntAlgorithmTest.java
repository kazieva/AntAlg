import by.bsu.mmf.kazieva.algoritm.AntAlgorithm;
import by.bsu.mmf.kazieva.graph.Graph;
import by.bsu.mmf.kazieva.way.Pair;


public class AntAlgorithmTest {
    @org.junit.Test
    public void autoAlgorithmFromFile() throws Exception {
        Graph graph = new Graph();
        graph.createGraphFromFile("graph");
        graph.printGraph();
        Pair<Integer, Integer> path = new Pair<>(3,5);
        AntAlgorithm antAlgorithm = new AntAlgorithm(graph , 0.5,0.5, 50, 10000, path );
        antAlgorithm.autoAlgorithm();

    }

    @org.junit.Test
    public void autoAlgorithmFromFileAfterGeneration() throws Exception {
        Graph graph = new Graph();
        graph.createGraphFromFile("notes");
        graph.printGraph();
        Pair<Integer, Integer> path = new Pair<>(3,500);
        AntAlgorithm antAlgorithm = new AntAlgorithm(graph , 0.5,0.5, 50, 10000, path );
        antAlgorithm.autoAlgorithm();

    }

    @org.junit.Test
    public void autoAlgorithmFromGeneration() throws Exception {
        Graph graph = new Graph();
        graph.generateGraph(500, 10, 10, 100);
        graph.printGraph();
        Pair<Integer, Integer> path = new Pair<>(33,250);
        AntAlgorithm antAlgorithm = new AntAlgorithm(graph , 0.5,0.5, 30, 1000, path );
        antAlgorithm.autoAlgorithm();

    }
}