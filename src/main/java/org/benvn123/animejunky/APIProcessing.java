package org.benvn123.animejunky;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class APIProcessing {
    final static String APIURL = "https://api.jikan.moe/v4/";

    static HttpResponse apiRequest(String url) throws IOException, InterruptedException {
        // Create a client object and API request object
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Retrieve the response from the API request
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    static JSONObject parseJSON(String responseBody) throws ParseException {
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(responseBody);
        JSONObject jsonObject = (JSONObject) obj;

        return jsonObject;
    }
}
