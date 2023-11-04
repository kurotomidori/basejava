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

        printFile(".\\");
    }

    private static void printFile(String path) {
        File tempDir = new File(path);
        if(tempDir.isDirectory()) {
            String[] list = tempDir.list();
            if (list != null) {
                for(String name : list) {
                    if (path.equals(".\\")) {
                        printFile(path + name);
                    } else {
                        printFile(path + "\\" + name);
                    }
                }
            }
        } else {
            System.out.println(tempDir.getName());
        }
    }
}
