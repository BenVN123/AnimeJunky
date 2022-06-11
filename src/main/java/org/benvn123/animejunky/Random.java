package org.benvn123.animejunky;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Scanner;

class Random extends APIProcessing {
    public Random() throws IOException, InterruptedException, ParseException {
        final Scanner scanner = new Scanner(System.in);

        final String[] ENTITIES = {"anime", "manga", "character"};

        System.out.println("\nRandom generator for...");
        for (String entity : ENTITIES) {
            System.out.println("> " + entity);
        }
        String scanEntity = scanner.nextLine();

        boolean invalidEntity = true;
        while (true) {
            for (String entity : ENTITIES) {
                if (scanEntity.equalsIgnoreCase(entity)) {
                    if (scanEntity.equalsIgnoreCase("character")) {
                        scanEntity = scanEntity.concat("s");
                    }

                    invalidEntity = false;
                    break;
                }
            }

            if (!invalidEntity) {
                break;
            }

            System.out.println("Please use a valid option.");
            scanEntity = scanner.nextLine();
        }

        scanner.close();

        String url = APIURL + "random/" + scanEntity;
        HttpResponse response = apiRequest(url);

        if (response.statusCode() == 200) {
            switch (scanEntity) {
                case "anime" -> randomAnime(response);
                case "manga" -> randomManga(response);
                case "characters" -> randomCharacter(response);
            }
        } else {
            System.out.println("\nUh oh! Something went wrong.");
            System.out.println("Response code: " + response.statusCode());
        }
    }

    private static void randomAnime(HttpResponse response) throws ParseException {
        JSONObject data = (JSONObject) parseJSON(response.body().toString()).get("data");

        System.out.println("\nTitle: " + data.get("title").toString() + " (" + data.get("type") + ")");
        System.out.println("Rating: " + data.get("rating").toString());
        System.out.println("Status: " + data.get("status").toString());
        System.out.println("Link: " + data.get("url").toString());

        if (data.get("synopsis") != null) {
            System.out.println("Synopsis: " + data.get("synopsis").toString());
        } else {
            System.out.println("Synopsis: N/A");
        }
    }

    private static void randomManga(HttpResponse response) throws ParseException {
        JSONObject data = (JSONObject) parseJSON(response.body().toString()).get("data");

        System.out.println("\nTitle: " + data.get("title").toString() + " (" + data.get("type") + ")");
        System.out.println("Status: " + data.get("status").toString());
        System.out.println("Link: " + data.get("url").toString());

        if (data.get("synopsis") != null) {
            System.out.println("Synopsis: " + data.get("synopsis").toString());
        } else {
            System.out.println("Synopsis: N/A");
        }
    }

    private static void randomCharacter(HttpResponse response) throws ParseException {
        JSONObject data = (JSONObject) parseJSON(response.body().toString()).get("data");

        System.out.println("\nName: " + data.get("name").toString());
        System.out.println("Favorites: " + data.get("favorites").toString());
        System.out.println("Link: " + data.get("url").toString());
    }
}
