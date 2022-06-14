package org.benvn123.animejunky;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Characters extends APIProcessing {
    public Characters() throws IOException, InterruptedException, ParseException {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("\nSearch query string (leave blank if N/A).");
        String query = scanner.nextLine();

        System.out.println("\nAll entries must start with this letter (leave blank if N/A).");
        String letter = scanner.nextLine();

        System.out.println("\nHow should the results be ordered by?");
        System.out.println("> name");
        System.out.println("> favorites");
        String ordered = scanner.nextLine();
        while (true) {
            if (ordered.equalsIgnoreCase("name")) {
                break;
            } else if (ordered.equalsIgnoreCase("favorites")) {
                break;
            }

            System.out.println("Please use a valid option.");
            ordered = scanner.nextLine();
        }

        scanner.close();

        String url = APIURL + "characters?order_by=" + ordered;

        if (!query.isBlank()) {
            url = url.concat("&q=" + query);
        }

        if (!letter.isBlank()) {
            url = url.concat("&letter=" + letter);
        }

        HttpResponse response = apiRequest(url);

        if (response.statusCode() == 200) {
            JSONObject jsonObject = parseJSON(response.body().toString());

            System.out.println("\n----------------------------");
            System.out.println("Here are the top " +
                    (
                            (JSONObject) (
                                    (JSONObject) jsonObject.get("pagination")
                            ).get("items")
                    ).get("count") + " characters that match your filters."
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
