package com.cs314;

public final class Database {
    public static final int localPort = 27017;
    public static final String connection = String.format("mongodb://localhost:%d", localPort);
    public static final String user = "readWriter";
    public static final String password = "password";
}
