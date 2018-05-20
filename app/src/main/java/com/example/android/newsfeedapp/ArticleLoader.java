package com.example.android.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class ArticleLoader extends AsyncTaskLoader<ArrayList<Article>> {

    private URL mUrl = null;
    private static final int HTTP_CONNECTION_OKAY = 200;
    private static final int HTTP_READ_TIMEOUT = 10000;
    private static final int HTTP_CONNECT_TIMEOUT = 15000;
    ArticleLoader(Context context, URL url) {
        super(context);
        mUrl = url;
    }

    private static ArrayList<Article> extractArticles(URL url) throws IOException {

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        String jSonResponse = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(HTTP_READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HTTP_CONNECTION_OKAY) {
                inputStream = httpURLConnection.getInputStream();
                jSonResponse = readStream(inputStream);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return parseJSON(jSonResponse);
    }

    private static String readStream(InputStream inputStream) throws IOException {
        StringBuilder jSonResponse = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                jSonResponse.append(line);
                line = reader.readLine();
            }
        }
        return jSonResponse.toString();
    }

    private static ArrayList<Article> parseJSON(String jSonResponse) {

        ArrayList<Article> articles = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jSonResponse);
            JSONObject response = root.optJSONObject("response");
            JSONArray results = response.optJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject element = results.optJSONObject(i);
                String title = element.optString("webTitle");
                String section = element.optString("sectionName");
                String url = element.optString("webUrl");
                String date = element.optString("webPublicationDate");

                JSONArray tags = element.optJSONArray("tags");
                StringBuilder author = new StringBuilder();

                for (int y = 0; y < tags.length(); y++) {
                    JSONObject tagsElement = tags.optJSONObject(y);
                    String type = tagsElement.optString("type");
                    if (type.hashCode() == "contributor".hashCode()) {
                        author.append(tagsElement.optString("webTitle"));
                        author.append(", ");
                    }
                }
                if (author.length() > 0) {
                    author.insert(0, "By: ");
                    author.setLength(author.length() - 2);
                }
                articles.add(new Article(title, section, url, author.toString(), date));
            }
        } catch (JSONException e) {
            Log.e("ArticleLoader", "Problem parsing JSON results", e);
        }

        return articles;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        try {
            return extractArticles(mUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
