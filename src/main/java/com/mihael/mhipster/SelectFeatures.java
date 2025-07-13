package com.mihael.mhipster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SelectFeatures {

    public static void main(String[] args) throws IOException {
        String templatePath = args[0];
        String fileContent = Files.readString(Paths.get("original.txt"));
    }
}
