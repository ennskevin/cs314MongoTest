package com.cs314;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Query {
    private MongoCollection<Document> collection;

    public Query(){
        try {
            MongoClient mongoClient = MongoClients.create(Database.connection);
            MongoDatabase database = mongoClient.getDatabase(Database.name);
            collection = database.getCollection(Database.collection);
        } 
        catch (Exception e) {
                System.err.println(e.getMessage());
        }
    }

    public void getCitiesByRegex(String field, String regex){
        Bson filter = Filters.regex(field, regex);
        // wrapper class for FindIterable<Documents> called QueryResult?
        FindIterable<Document> results = collection.find(filter);
        results.forEach(doc -> System.out.println(doc.toJson()));
    }

    private Bson between(String field, double min, double max){
        return Filters.and(Filters.gt(field, min), Filters.lt(field, max));
    }

    public void getCitiesInRange(String field, double min, double max){
        Bson filter = between(field, min, max);
        FindIterable<Document> results = collection.find(filter);
        results.forEach(doc -> System.out.println(doc.toJson()));
    }

    // etc.
}
