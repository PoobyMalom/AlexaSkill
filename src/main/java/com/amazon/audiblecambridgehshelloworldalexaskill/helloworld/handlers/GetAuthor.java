package com.amazon.audiblecambridgehshelloworldalexaskill.helloworld.handlers;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class GetAuthor {

    public String searchBook(String searchKey) {
        HttpURLConnection httpURLConnection = buildHttpGetRequest(searchKey);
        InputStream responseStream = executeHttpRequest(httpURLConnection);
        return parseHttpResponse(responseStream);
    }

    private HttpURLConnection buildHttpGetRequest(String searchKey) {
        try {
            String encodedSearchKey = URLEncoder.encode(searchKey, "UTF-8");
            String urlString = "http://openlibrary.org/search.json?title=" + encodedSearchKey;
            URL url = new URL(urlString);
            System.out.println(url);
            System.out.println("url query: " + url.getQuery());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            return conn;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // This method executes HTTP request (conn), and returns HTTP response
    private InputStream executeHttpRequest(HttpURLConnection conn) {
        try {
            // If the response code doesn't indicate success, throw an exception
            // TODO: fill in the success response code below
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code :  " + conn.getResponseCode());
            }
            return conn.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // This method parse the HTTP response (inputStream), and returns a objectModel which carries data
    // You need to add json library using Maven in order to use JSONObject and XML.toJSONObject (remove "//" when pasting to pom.xml)
    // <dependency>
    //      <groupId>org.json</groupId>
    //      <artifactId>json</artifactId>
    //      <version>20180813</version>
    //  </dependency>
    // TODO: replace return type with your model which carries data that you need
    private String parseHttpResponse(final InputStream inputStream) {
        try {
            // convert input stream object to JSON object
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String responseStr = bufferedReader.lines().collect(Collectors.joining("\n"));
            JSONObject responseJson = new JSONObject(responseStr);
            bufferedReader.close();

            // Or, use IOUtils to convert input stream object to string.
            // You need to add Apache commons library in order to use IOUtils, or you can use other ways to convert InputStream object to String
            // <dependency>
            //      <groupId>commons-io</groupId>
            //      <artifactId>commons-io</artifactId>
            //      <version>2.5</version>
            // </dependency>

            return responseJson.getJSONArray("docs").getJSONObject(0).getJSONArray("author_name").optString(0).toLowerCase();
        } catch (Exception ignored) {}
        return null;
    }
}
