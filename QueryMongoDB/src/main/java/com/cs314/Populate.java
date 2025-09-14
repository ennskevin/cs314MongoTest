package com.cs314;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

public class Populate {
    public Populate(String csvPath){
        // Connect to db
        try (MongoClient mongoClient = MongoClients.create(Database.connection)) {
            MongoDatabase database = mongoClient.getDatabase("cs314");
            MongoCollection<Document> collection = database.getCollection("cities");
            
            // Open csv
            Path path = Paths.get(csvPath);
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line = reader.readLine();
                String[] fields = processLine(line);
                System.out.println();System.out.println();System.out.println();
                System.out.println(Arrays.toString(fields));
                System.out.println();System.out.println();System.out.println();
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
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private String[] processLine(String line) {
        String[] values = line.split(",");
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].replace("\"", "");
        }
        return values;
    }
    
    private Document buildDoc(String[] fields, String[] values) {
        Document doc = new Document();
        for (int i = 0; i < fields.length; i++) {
            doc.append(fields[i], values[i]);
        }
        return doc;
    }

    private void createIndexes(MongoCollection<Document> collection, String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            String key = fields[i];
            collection.createIndex(Indexes.ascending(key));
        }
    }
}
