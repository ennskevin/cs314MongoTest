package com.cs314;

public class App {

    public static void main( String[] args ) {
        System.out.println();System.out.println();System.out.println();
        System.out.println("HELLO");
        System.out.println();System.out.println();System.out.println();
        
        String csvPath = args[0];
        Populate c = new Populate(csvPath);
    }

}
