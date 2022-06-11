package org.benvn123.animejunky;

import org.json.simple.parser.ParseException;

import java.io.IOException;


class Main {
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        if (args.length != 1) {
            System.out.println("Please specify one request endpoint.");
        }

        switch (args[0]) {
            case "schedules" -> new Schedules();
            case "top" -> new Top();
            case "random" -> new Random();
            default -> System.out.println("Really bruh??");
        }
    }
}
