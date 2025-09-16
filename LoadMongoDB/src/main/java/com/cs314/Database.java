package com.cs314;

public final class Database {
    public static final int port = 27017;
    public static final String connection = String.format("mongodb://localhost:%d", localPort);
    public static final String user = "readWriter";
    public static final String password = "password";
    public static final String name = "cs314";
    public static final String collection = "cities";
}
