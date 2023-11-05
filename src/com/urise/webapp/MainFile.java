package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFile {
    public static void main(String[] args) {
        String filePath = ".\\.gitignore";

        File file = new File(filePath);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }

        File dir = new File(".\\src\\com\\urise\\webapp");
        System.out.println(dir.isDirectory());
        String[] list = dir.list();
        if (list != null) {
            for(String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        printFile(dir);
    }

    private static void printFile(File path) {
        if(path.isDirectory()) {
            System.out.println("Directory: " + path.getName());
            File[] list = path.listFiles();
            if (list != null) {
                for(File name : list) {
                    printFile(name);
                }
                System.out.println();
            }
        } else {
            System.out.println("File: " + path.getName());
        }
    }
}
