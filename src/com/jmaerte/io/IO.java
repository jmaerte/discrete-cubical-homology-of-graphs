package com.jmaerte.io;

import com.jmaerte.graph.Graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IO {

    public static Graph loadFromFile(String path) {
        String text = null;
        try {
            text = new String(Files.readAllBytes(Paths.get("file")), StandardCharsets.UTF_8);
        }catch(Exception e) {
            e.printStackTrace();
        }
        boolean tuple = false;
        boolean vertices = false;
        boolean edges = false;

        for(int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
        }
        return null;
    }

}
