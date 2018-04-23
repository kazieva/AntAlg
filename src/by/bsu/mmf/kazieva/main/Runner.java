package by.bsu.mmf.kazieva.main;

import by.bsu.mmf.kazieva.algoritm.AntAlgorithm;
import by.bsu.mmf.kazieva.graph.Graph;
import by.bsu.mmf.kazieva.way.Pair;

public class Runner {

    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.generateGraph(20, 50, 10, 100);
        graph.createGraphFromFile("graph");
        graph.printGraph();
        Pair<Integer, Integer> path = new Pair<>(3,5);
        AntAlgorithm antAlgorithm = new AntAlgorithm(graph , 0.1,0.5, 50, 10000, path );
        antAlgorithm.autoAlgorithm();
    }
}
