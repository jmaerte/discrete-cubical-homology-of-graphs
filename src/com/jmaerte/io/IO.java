package com.jmaerte.io;

import com.jmaerte.graph.Graph;
import com.jmaerte.hom.Homomorphism;
import com.jmaerte.homology.Homology;
import com.jmaerte.util.Vector3D;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class IO {

    private static final String HELP =
            "+===============================+=========================================+\n" +
            "|   Command                     |               Description               |\n" +
            "+===============================+=========================================+\n" +
            "| help                          | Shows this menu                         |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| print                         | Prints the graph.                       |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| vertex [name]                 | Adds an unbound vertex to the graph     |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| edge [vertex 1] [vertex 2]    | Adds an edge between two vertices to    |\n" +
            "|                               | the graph. Note that you need to type   |\n" +
            "|                               | the vertices names.                     |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| polygon [n]                   | Sets the graph to be the n-gon.         |\n" +
            "|                               | NOTE: vertices are named numerical      |\n" +
            "|                               |       from 1 to n.                      |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| load [path]                   | Loads the graph from given path.        |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| homology [m] [M]              | Finish graph construction and initiate  |\n" +
            "|                               | homology group calculation.             |\n" +
            "|                               | m and M are the boundaries.             |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| homomorphisms [m] [M]         | Finish graph construction and initiate  |\n" +
            "|                               | homomorphism generation only.           |\n" +
            "|                               | m and M are the boundaries.             |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| exit                          | Ends this program.(Graph is not saved)  |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| clear                         | Clears the graph.                       |\n" +
            "+===============================+=========================================+\n" +
            "| NOTE: When you start a calculation the graph is saved and you can still |\n" +
            "|       modify it or do other calculations later.                         |\n" +
            "+=========================================================================+";

    public static Graph loadFromFile(String path) {
        char[] text = null;
        Graph graph = new Graph();
        try {
            text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8).toCharArray();
            boolean set = false;
            String lastVertex = "";
            String currVertex = "";
            for(char c : text) {
                if(c == '[') {
                    lastVertex = "";
                    currVertex = "";
                    set = true;
                }else if(c == ']') {
                    set = false;
                    if(!currVertex.equals("") && !lastVertex.equals("")) {
                        boolean firstEx = false;
                        int first = -1;
                        boolean secondEx = false;
                        int second = -1;
                        Set<Map.Entry<Integer, String>> entries = graph.factory.labels.entrySet();
                        for(Map.Entry<Integer, String> entry : entries) {
                            if(entry.getValue().equals(lastVertex)) {
                                first = entry.getKey();
                                firstEx = true;
                            }
                            if(entry.getValue().equals(currVertex)) {
                                second = entry.getKey();
                                secondEx = true;
                            }
                        }
                        if(!firstEx) {
                            first = graph.addVertex(lastVertex).id;
                        }
                        if(!secondEx) {
                            second = graph.addVertex(currVertex).id;
                        }
                        graph.addEdge(first, second);
                    }else throw new Exception("These are not valid vertex labels: [" + lastVertex + "," + currVertex + "]");
                }else if(set) {
                    if(c == ',') {
                        lastVertex = currVertex;
                        currVertex = "";
                    }else if(c == ' ') {
                        throw new Exception("Vertex label black listed character: '" + c + "'.");
                    }else {
                        currVertex += c;
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        return graph;
    }

    public static Graph fromConsole(Graph bluePrint) {
        if(bluePrint == null) System.out.println(HELP);
        Graph graph = bluePrint == null ? new Graph() : bluePrint;
        String command;
        Scanner sc = new Scanner(System.in);
        long ms;
        Homology homology = null;

        while((command = sc.nextLine()) != null) {
            String[] parts = command.split(" ");
            switch(parts[0]) {
                case "help":
                    System.out.println(HELP);
                    break;
                case "print":
                    System.out.println(graph);
                    break;
                case "vertex":
                    if(graph.addVertex(parts[1]) == null) {
                        System.out.println("Error: Vertex " + parts[1] + " already exists.");
                    }else System.out.println("Vertex " + parts[1] + " added!");
                    break;
                case "edge":
                    int i = -1;
                    int j = -1;
                    Set<Map.Entry<Integer, String>> entries = graph.factory.labels.entrySet();
                    for(Map.Entry<Integer, String> entry : entries) {
                        if(entry.getValue().equals(parts[1])) i = entry.getKey();
                        if(entry.getValue().equals(parts[2])) j = entry.getKey();
                    }
                    if(i < 0) {
                        System.out.println("Error: Unknown vertex " + parts[1] + "!");
                    }else if(j < 0) {
                        System.out.println("Error: Unknown vertex " + parts[2] + "!");
                    }else if(graph.addEdge(i, j)) {
                        System.out.println("Edge {" + parts[1] + ", " + parts[2] + "} added.");
                    }else {
                        if(parts[1].equals(parts[2])) System.out.println("Error: Can't connect a vertex with itself!");
                        else System.out.println("Error: Edge {" + parts[1] + ", " + parts[2] + "} already exists.");
                    }
                    break;
                case "polygon":
                    graph = Graph.getGon(Integer.valueOf(parts[1]));
                    System.out.println("Graph is now set to " + Integer.valueOf(parts[1]) + "-gon.");
                    break;
                case "load":
                    graph = loadFromFile(parts[1]);
                    System.out.println("Graph is now set to the from given files content constructed one.");
                    break;
                case "homology":
                    ms = System.currentTimeMillis();
                    homology = new Homology(graph);
                    try {
                        homology.homology(Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Calculation finished after " + (System.currentTimeMillis() - ms) + "ms");
                    break;
                case "homomorphisms":
                    ms = System.currentTimeMillis();
                    homology = new Homology(graph);
                    try {
                        homology.homomorphisms(Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Calculation finished after " + (System.currentTimeMillis() - ms) + "ms");
                    break;
                case "clear":
                    graph = new Graph();
                    System.out.println("Graph cleared.");
                    break;
                case "exit":
                    System.exit(0);
                    System.out.println("Process finished!");
                    break;
                default:
                    System.out.println("Unknown command. Use help to see an overview of possible commands.");
            }
        }
        return graph;
    }

}
