package org.benvn123.animejunky;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Top extends APIProcessing {
    private static final Scanner scanner = new Scanner(System.in);

    public Top() throws IOException, ParseException, InterruptedException {
        final String[] ENTITIES = {"anime", "manga", "characters"};

        System.out.println("\nEntity to filter for.");
        for (String entity : ENTITIES) {
            System.out.println("> " + entity);
        }
        String scanEntity = scanner.nextLine();

        boolean invalidEntity = true;
        while (true) {
            for (String entity : ENTITIES) {
                if (scanEntity.equalsIgnoreCase(entity)) {
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

        switch (scanEntity) {
            case "anime" -> topAnimeManga("anime");
            case "manga" -> topAnimeManga("manga");
            case "characters" -> topCharacters();
        }

        scanner.close();
    }

    private static void topAnimeManga(String media) throws IOException, InterruptedException, ParseException {
        String[] TYPES;
        String[] FILTERS;

        if (media.equals("anime")) {
            TYPES = new String[] {"tv", "movie", "ova", "special", "ona", "music", "any"};
            FILTERS = new String[] {"airing", "upcoming", "bypopularity", "favorite", "any"};
        } else {
            TYPES = new String[] {"manga", "novel", "lightnovel", "oneshot", "doujin", "manhwa", "manhua"};
            FILTERS = new String[] {"publishing", "upcoming", "bypopularity", "favorite", "any"};
        }

        System.out.println("\nWhat type of " + media + " would you like?");
        for (String type : TYPES) {
            System.out.println("> " + type);
        }
        String scanType = scanner.nextLine();

        boolean invalidType = true;
        while (true) {
            for (String type : TYPES) {
                if (scanType.equalsIgnoreCase(type)) {
                    if (scanType.equalsIgnoreCase("any")) {
                        scanType = "";
                    } else {
                        scanType = "type=".concat(scanType);
                    }

                    invalidType = false;
                    break;
                }
            }

            if (!invalidType) {
                break;
            }

            System.out.println("Please use a valid option.");
            scanType = scanner.nextLine();
        }

        System.out.println("\nWhat filter would you like to use?");
        for (String filter : FILTERS) {
            System.out.println("> " + filter);
        }
        String scanFilter = scanner.nextLine();

        boolean invalidFilter = true;
        while (true) {
            for (String filter : FILTERS) {
                if (scanFilter.equalsIgnoreCase(filter)) {
                    if (scanFilter.equalsIgnoreCase("any")) {
                        scanFilter = "";
                    } else {
                        scanFilter = "filter=".concat("scanFilter");
                    }

                    invalidFilter = false;
                    break;
                }
            }

            if (!invalidFilter) {
                break;
            }

            System.out.println("Please use a valid option.");
            scanFilter = scanner.nextLine();
        }

        String url = APIURL + "top/" + media + "?" + scanType + "&" + scanFilter;

        HttpResponse response = apiRequest(url);

        if (response.statusCode() == 200) {
            JSONObject jsonObject = parseJSON(response.body().toString());

            System.out.println("\n----------------------------");
            System.out.println("Here are the top " +
                    (
                            (JSONObject) (
                                    (JSONObject) jsonObject.get("pagination")
                            ).get("items")
                    ).get("count") + " " + media + " that match your filters."
            );
            System.out.println("----------------------------");

            JSONArray data = (JSONArray) jsonObject.get("data");
            for (Object datum : data) {
                JSONObject item = (JSONObject) datum;
                System.out.println("\nTitle: " + item.get("title").toString() + " (" + item.get("type") + ")");

                if (media.equals("anime")) {
                    System.out.println("Rating: " + item.get("rating").toString());
                }

                System.out.println("Status: " + item.get("status").toString());
                System.out.println("Link: " + item.get("url").toString());

                if (item.get("synopsis") != null) {
                    System.out.println("Synopsis: " + item.get("synopsis").toString());
                } else {
                    System.out.println("Synopsis: N/A");
                }
            }
        } else {
            System.out.println("\nUh oh! Something went wrong.");
            System.out.println("Response code: " + response.statusCode());
        }
    }

    private static void topCharacters() throws IOException, InterruptedException, ParseException {
        HttpResponse response = apiRequest(APIURL + "top/characters");

        if (response.statusCode() == 200) {
            JSONObject jsonObject = parseJSON(response.body().toString());

            System.out.println("\n----------------------------");
            System.out.println("Here are the top " +
                    (
                            (JSONObject) (
                                    (JSONObject) jsonObject.get("pagination")
                            ).get("items")
                    ).get("count") + " characters."
            );
            System.out.println("----------------------------");

            JSONArray data = (JSONArray) jsonObject.get("data");
            for (Object datum : data) {
                JSONObject item = (JSONObject) datum;
                System.out.println("\nName: " + item.get("name").toString());
                System.out.println("Favorites: " + item.get("favorites").toString());
                System.out.println("Link: " + item.get("url").toString());
            }
        } else {
            System.out.println("\nUh oh! Something went wrong.");
            System.out.println("Response code: " + response.statusCode());
        }
    }
}
