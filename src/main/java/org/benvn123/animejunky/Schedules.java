package org.benvn123.animejunky;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Scanner;


class Schedules extends APIProcessing {
    public Schedules() throws IOException, InterruptedException, ParseException {
        final Scanner scanner = new Scanner(System.in);

        final String[] DAYS = {
                "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday", "any"
        };

        System.out.println("\nWeek day to filter for.");
        for (String day : DAYS) {
            System.out.println("> " + day);
        }
        String scanDay = scanner.nextLine();

        boolean invalidDay = true;
        while (true) {
            for (String day : DAYS) {
                if (scanDay.equalsIgnoreCase(day)) {
                    if (scanDay.equalsIgnoreCase("any")) {
                        scanDay = "";
                    } else {
                        scanDay = "/".concat(scanDay);
                    }

                    invalidDay = false;
                    break;
                }
            }

            if (!invalidDay) {
                break;
            }

            System.out.println("Please use a valid option.");
            scanDay = scanner.nextLine();
        }

        System.out.println("\nThe content should be kid-friendly? true/false");
        String kids = scanner.nextLine();

        while (!(kids.equalsIgnoreCase("true") || kids.equalsIgnoreCase("false"))) {
            System.out.println("Please use the values \"true\" or \"false\" only.");
            kids = scanner.nextLine();
        }


        System.out.println("\nThe content should be SFW (safe for work)? true/false");
        String sfw = scanner.nextLine();

        while (!(sfw.equalsIgnoreCase("true") || sfw.equalsIgnoreCase("false"))) {
            System.out.println("Please use the values \"true\" or \"false\" only.");
            sfw = scanner.nextLine();
        }

        scanner.close();

        String url = APIURL + "schedules" + scanDay + "?kids=" + kids + "&sfw=" + sfw;
        HttpResponse response = apiRequest(url);

        if (response.statusCode() == 200) {
            JSONObject jsonObject = parseJSON(response.body().toString());

            System.out.println("\n----------------------------");
            System.out.println("Here are the top " +
                    (
                            (JSONObject) (
                                    (JSONObject) jsonObject.get("pagination")
                            ).get("items")
                    ).get("count") + " anime that match your filters."
            );
            System.out.println("----------------------------");

            JSONArray data = (JSONArray) jsonObject.get("data");
            for (Object datum : data) {
                JSONObject item = (JSONObject) datum;
                System.out.println("\nTitle: " + item.get("title").toString() + " (" + item.get("type") + ")");
                System.out.println("Rating: " + item.get("rating").toString());
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
}
