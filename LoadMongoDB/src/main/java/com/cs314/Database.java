package com.cs314;

public final class Database {
    public final int port;
    public final String user;
    public final String password;
    public final String name;
    public final String collection;

    public final String connection;
    public Database(int port, String user, String password, String name, String collection) {
        this.port = port;
        this.user = user;
        this.password = password;
        this.name = name;
        this.collection = collection;
        this.connection = String.format("mongodb://%s:%s@localhost:%d/?authSource=admin", user,password,port);
    }
}
