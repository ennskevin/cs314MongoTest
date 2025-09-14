package com.cs314;

public class Database {
    public static final int localPort = 27017;
    public static final String connection = String.format("mongodb://localhost:%d", localPort);
}
