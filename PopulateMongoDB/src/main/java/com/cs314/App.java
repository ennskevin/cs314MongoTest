package com.cs314;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class App {

    public static void main( String[] args ) {
        int localPort = 27017;
        String connection = "mongodb://localhost:27017";
        Path csvPath = Paths.get("src/main/resources/worldcities.csv");
        
        try (MongoClient mongoClient = MongoClients.create(connection)) {
            MongoDatabase database = mongoClient.getDatabase("cs314");
            MongoCollection<Document> collection = database.getCollection("cities");
            // collection.dropIndexes();
            
            try (InputStream inputStream = App.class.getResourceAsStream("/worldcities.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line = reader.readLine();
                String[] fields = processLine(line);
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
                collection.insertMany(batch);
                // recreateIndexes(collection, fields);
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
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

    private static void recreateIndexes(MongoCollection<Document> collection, String[] fields) {
        Document indexDoc = new Document();
        for (int i = 0; i < fields.length; i++) {
            indexDoc.append(fields[i], 1);
        }
        collection.createIndex(indexDoc);
    }

}