package com.jmaerte.io;

import com.jmaerte.graph.Graph;
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
            "| vertex [name]                 | Adds an unbound vertex to the graph     |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| edge [vertex 1] [vertex 2]    | Adds an edge between two vertices to    |\n" +
            "|                               | the graph. Note that you need to type   |\n" +
            "|                               | the vertices names.                     |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| polygon [n]                   | Sets the graph to be the n-gon.         |\n" +
            "|                               | NOTE: vertices are named numerical.     |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| bound [m] [M]                 | Sets the index interval of homology     |\n" +
            "|                               | groups to be calculated.                |\n" +
            "|                               | NOTE: Per default m = 1, M = 2.         |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| load [path]                   | Loads the graph from given path.        |\n" +
            "+-------------------------------+-----------------------------------------+\n" +
            "| finish                        | Finish graph construction.              |\n" +
            "+===============================+=========================================+";

    public static Graph loadFromFile(String path) {
        String text = null;
        try {
            text = new String(Files.readAllBytes(Paths.get("file")), StandardCharsets.UTF_8);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Vector3D<Graph, Integer, Integer> fromConsole() {
        Graph graph = new Graph();
        String command;
        Scanner sc = new Scanner(System.in);
        int min = 1;
        int max = 2;
        System.out.println(HELP);

        while((command = sc.nextLine()) != null) {
            String[] parts = command.split(" ");
            switch(parts[0]) {
                case "help":
                    System.out.println(HELP);
                    break;
                case "vertex":
                    graph.addVertex(parts[1]);
                    break;
                case "edge":
                    int i = 0;
                    int j = 0;
                    Set<Map.Entry<Integer, String>> entries = graph.factory.labels.entrySet();
                    for(Map.Entry<Integer, String> entry : entries) {
                        if(entry.getValue().equals(parts[1])) i = entry.getKey();
                        if(entry.getValue().equals(parts[2])) j = entry.getKey();
                    }
                    graph.addEdge(i, j);
                    break;
                case "polygon":
                    graph = Graph.getGon(Integer.valueOf(parts[1]));
                    break;
                case "load":
                    graph = loadFromFile(parts[1]);
                    break;
                case "bound":
                    min = Integer.valueOf(parts[1]);
                    max = Integer.valueOf(parts[2]);
                    break;
                case "finish":
                    return new Vector3D<>(graph, min, max);
                default:
                    System.out.println("Unknown command. Use help to see an overview of possible commands.");
            }
        }
        return new Vector3D<>(graph, min, max);
    }

}
