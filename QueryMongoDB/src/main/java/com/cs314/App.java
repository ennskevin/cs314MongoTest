package com.cs314;

public class App {

    public static void main( String[] args ) {
        System.out.println();System.out.println();System.out.println();
        System.out.println("HELLO");
        System.out.println();System.out.println();System.out.println();
        
        Query q = new Query();
        q.getCitiesByRegex("city_ascii", "%denver%");
    }

}
