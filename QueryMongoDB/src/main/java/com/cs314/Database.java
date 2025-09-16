package com.cs314;

public final class Database {
    public static final int port = 27017;
    public static final String user = "reader";
    public static final String password = "password";
    public static final String name = "cs314";
    public static final String collection = "cities";

    public static final String connection = String.format("mongodb://%s:%s@localhost:%d/?authSource=admin", user,password,port);
}
