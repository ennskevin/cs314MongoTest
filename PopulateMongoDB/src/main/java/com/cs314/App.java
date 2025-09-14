package com.cs314;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.bson.Document;

public class App {

    public static void main( String[] args ) {
        System.out.println();System.out.println();System.out.println();
        System.out.println("HELLO");
        System.out.println();System.out.println();System.out.println();
        int localPort = 27017;
        String connection = "mongodb://localhost:27017";
        String csvPath = args[0];
        String dataConfigPath = args[1];

        String[] fieldsFromConfig;
        String[] typesFromConfig;
        // open data config csv
        try (BufferedReader reader = Files.newBufferedReader(dataConfigPath)) {
            fieldsFromConfig = processLine(reader.readLine());
            typesFromConfig = processLine(reader.readLine());
        }
        
        // Open csv
        Path path = Paths.get(csvPath);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine();
            String[] fields = processLine(line);
            if (!validateFields(fields, fieldsFromConfig)) return;
            
            // Connect to db
            try (MongoClient mongoClient = MongoClients.create(connection)) {
                MongoDatabase database = mongoClient.getDatabase("cs314");
                MongoCollection<Document> collection = database.getCollection("cities");
                
                // Batch and write
                List<Document> batch = new ArrayList<>();
                int batchCap = 1000;
                while((line = reader.readLine()) != null) {
                    String[] values = processLine(line);
                    batch.add(buildDoc(fields, values));
                    if (batch.size() == batchCap) {
                        collection.insertMany(batch);
                        batch.clear();
                    }                   
                }
                if (batch.size() > 0) {
                    collection.insertMany(batch);
                }
                createIndexes(collection, fields);
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private static String[] processLine(String line) {
        String[] values = line.split(",");
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].replace("\"", "");
        }
        return values;
    }
    
    private static Document buildDoc(String[] fields, String[] values) {
        Document doc = new Document();
        for (int i = 0; i < fields.length; i++) {
            doc.append(fields[i], values[i]);
        }
        return doc;
    }

    private static void createIndexes(MongoCollection<Document> collection, String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            String key = fields[i];
            collection.createIndex(Indexes.ascending(key));
        }
    }

    private static Boolean validateFields() {
        return false;
    }


    private static Object parseValue(String value, String type) {
        if (value == null || value.isEmpty()) return null;
        switch (type.toLowerCase()) {
            case "int":
                return Integer.parseInt(value);
            case "long":
                return Long.parseLong(value);
            case "double":
                return Double.parseDouble(value);
            case "boolean":
                return Boolean.parseBoolean(value);
            case "string":
                return value;
            default:
                return value;
        }
    }
}
