package by.bsu.mmf.kazieva.graph;

import by.bsu.mmf.kazieva.way.Pair;

import java.io.*;
import java.util.*;

public class Graph {
    public int numberOfVertices;         // Number of graph nodes
    public int[][] adjacencyMatrix;      // Adjacency matrix with weights
    public boolean[][] linksMatrix;      // Adjacency matrix with links only. We'll use Warshall algorithm to it.

    public List<Edge> edges;             // Graph's edges

    public Graph() {                     // Constructor
        numberOfVertices = 0;
        adjacencyMatrix = null;
        linksMatrix = null;
        edges = null;
    }

    public Graph(Graph g) {              // Copy constructor
        numberOfVertices = g.numberOfVertices;

        createMatrix(numberOfVertices);

        for (int i = 0; i < numberOfVertices; ++i)
            for (int j = 0; j < numberOfVertices; ++j) {
                adjacencyMatrix[i][j] = g.adjacencyMatrix[i][j];
                linksMatrix[i][j] = g.linksMatrix[i][j];
            }

        if (g.edges != null) edges = new ArrayList<>(g.edges);
    }

    public boolean isNull() {
        for (int i = 0; i < numberOfVertices; ++i)
            for (int j = 0; j < numberOfVertices; ++j)
                if (linksMatrix[i][j])
                    return false;

        return true;    // no edges
    }

    public boolean isCreated() {         // is graph created
        return numberOfVertices != 0;
    }

    public void deleteGraph() {
        numberOfVertices = 0;
        adjacencyMatrix = null;
        linksMatrix = null;
        edges = null;
    }

    private void createMatrix(int n) {     // create graph
        numberOfVertices = n;
        adjacencyMatrix = new int[n][n];
        linksMatrix = new boolean[n][n];
    }

    private void outputGraphInFile(){
        try(FileWriter writer = new FileWriter("notes.txt", false))
        {
            for (int i = 0; i < numberOfVertices; ++i){
                for (int j =0; j < numberOfVertices; ++j){
                    String text =  "";
                    text+=(adjacencyMatrix[i][j]+" ");
                    writer.write(text);
                }
            writer.append('\n');
            }
            writer.flush();

        }
        catch(IOException e){

            System.out.println(e.getMessage());
        }
    }


    private void createVerticesAndEdges() {
        // create edges
        edges = new ArrayList<>();
        for (int i = 0; i < numberOfVertices; ++i)
            for (int j = i; j < numberOfVertices; ++j)
                if (adjacencyMatrix[i][j] != 0) {
                    edges.add(new Edge(adjacencyMatrix[i][j], i, j));
                }
    }

    private void makeWarshallAlgorithm() {       // Warshall algorithm
        for (int k = 0; k < numberOfVertices; ++k)
            for (int i = 0; i < numberOfVertices; ++i)
                for (int j = 0; j < numberOfVertices; ++j)
                    linksMatrix[i][j] = linksMatrix[i][j] || (linksMatrix[i][k] && linksMatrix[k][j]);

        for (int i = 0; i < numberOfVertices; ++i)
            linksMatrix[i][i] = false;
    }

    public void generateGraph(int numberOfVertices, int linksPercent,
                              int leftBound, int rightBound) { // Graph generation

        deleteGraph();

        createMatrix(numberOfVertices);

        Random rand = new Random();             // random generator

        if (linksPercent > 100) linksPercent = 100;

        List<Pair<Integer, Integer>> list = new ArrayList<>();

        int edges = numberOfVertices * (numberOfVertices - 1) * linksPercent / 200; // 200 because graph isn't oriented

        for (int i = 0; i < numberOfVertices; ++i)
            for (int j = i; j < numberOfVertices; ++j)
                if (i != j) list.add(new Pair(i, j));

        Collections.shuffle(list);              // Random_shuffle

        boolean b = rand.nextBoolean();

        if (b) {
            for (int i = 0; i < edges; ++i) {
                linksMatrix[list.get(0).first][list.get(0).second] = true;
                list.remove(0);
            }
        } else {
            for (int i = 0; i < edges; ++i) {
                linksMatrix[list.get(list.size() - 1).first][list.get(list.size() - 1).second] = true;
                list.remove(list.size() - 1);
            }
        }

        int range = rightBound - leftBound;

        for (int i = 0; i < numberOfVertices; ++i)           // Random weights
            for (int j = 0; j < numberOfVertices; ++j)
                if (linksMatrix[i][j])
                    adjacencyMatrix[i][j] = rand.nextInt(range + 1) + leftBound;

        for (int i = 0; i < numberOfVertices; ++i)           // make matrix symmetric
            for (int j = i; j < numberOfVertices; ++j) {
                linksMatrix[j][i] = linksMatrix[i][j];
                adjacencyMatrix[j][i] = adjacencyMatrix[i][j];
            }

        makeWarshallAlgorithm();                        // WarshallAlgorithm

        createVerticesAndEdges();
        outputGraphInFile();
    }

    public boolean createGraphFromFile(String fileName) {      // create graph from file
        String inputFileName = "src/resourses/" + fileName + ".txt";
        deleteGraph();

        int[] massOfDigits = new int[10000000];
        int numberOfDigits = 0;

        File file = new File(inputFileName);

        try {
            Scanner input = new Scanner(file);
            while (input.hasNextInt()) {
                massOfDigits[numberOfDigits++] = input.nextInt();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (Math.sqrt(numberOfDigits) % 1 == 0) {
            int numberOfElements = (int) Math.sqrt(numberOfDigits);
            int[][] elements = new int[numberOfElements][numberOfElements];

            if (numberOfElements < 2)                        // too few elements
                return false;

            for (int i = 0; i < numberOfElements; i++)
                for (int j = 0; j < numberOfElements; j++)
                    elements[i][j] = massOfDigits[i * numberOfElements + j];

            for (int i = 0; i < numberOfElements; ++i)
                if (elements[i][i] != 0)                     // wrong elements on diagonal
                    return false;

            for (int i = 0; i < numberOfElements; ++i)
                for (int j = 0; j < numberOfElements; ++j)
                    if (elements[i][j] != elements[j][i])    // wrong elements
                        return false;


            // real creation of graph
            createMatrix(numberOfElements);
            for (int i = 0; i < numberOfElements; i++)
                for (int j = 0; j < numberOfElements; j++) {
                    adjacencyMatrix[i][j] = massOfDigits[i * numberOfElements + j];
                    linksMatrix[i][j] = adjacencyMatrix[i][j] != 0;
                }

            makeWarshallAlgorithm();

            createVerticesAndEdges();

            return true; // graph created
        }

        return false;  // graph is not created
    }

    private boolean isNullRow(int[][] mm, int index) {
        for (int i = 0; i < 10; ++i)
            if (mm[index][i] != 0)
                return false;
        return true;
    }

    public void removePheromone() {
        for (Edge i : edges)
            i.pheromone = 0;
    }

    public void printGraph(){

        System.out.println("adjacencyMatrix:" + "\n");
        for (int i = 0; i < numberOfVertices; ++i){
            for (int j =0; j < numberOfVertices; ++j)
                System.out.print(adjacencyMatrix[i][j]+" ");
            System.out.println();
        }

    }
}
