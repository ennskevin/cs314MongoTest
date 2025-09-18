package com.cs314;

import java.util.ArrayList;
import java.util.HashMap;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class mongoGuide {
    public static final String DATABASE = "cs314";
    public static final String COLLECTION = "cities";
    public static final String NAME_FIELD = "city";
    public static final String LOCATION_FIELD = "location";

    static class Place extends HashMap<String, String> {

    }

    static class Places extends ArrayList<Place> {

    }

    static class Database {
        static Integer found(Bson filter) throws Exception {
            try (MongoClient mongoClient = MongoClients.create(Credential.URL)) {
                MongoDatabase database = mongoClient.getDatabase(DATABASE);
                MongoCollection<Document> collection = database.getCollection(COLLECTION);
                long results = collection.countDocuments(filter);
                return (int) results;
            } catch (Exception e) {
                throw e;
            }
        }
    }

    static class Select {
        static Bson near(Place place, Double distance){
            Double lon = Double.valueOf(place.get("longitude"));
            Double lat = Double.valueOf(place.get("latitude"));
    
            // fieldName, lon, lat, maxDistance, minDistance
            return Filters.near(LOCATION_FIELD, lon, lat, distance, 0.0);
        }

        static Bson match(String matchRegex){
            return Filters.regex(NAME_FIELD, matchRegex);
        }
    }

    static class Credential {
        static final int PORT = 27017;
        static final String USER = "reader";
        static final String PASSWORD = "password";

        static final String URL = String.format("mongodb://%s:%s@localhost:%d/?authSource=admin", USER, PASSWORD, PORT);
    }

    static class Report {
        static void printPlaces(Places places) {
            for (Place place : places) {
                System.out.println(place);
            }
        }
    }
}
