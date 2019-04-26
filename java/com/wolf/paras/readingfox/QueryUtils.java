package com.wolf.paras.readingfox;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public static String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
        //Default Constructer
    }

    public static List<Book> extractJsonFeatures(String shoopingJson) {
        if (TextUtils.isEmpty(shoopingJson)) {
            return null;
        }

        List<Book> bookList = new ArrayList<Book>();

        try {
            JSONObject root = new JSONObject(shoopingJson);
            JSONArray items = root.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject index = items.getJSONObject(i);
                JSONObject volumeInfo = index.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                JSONArray authors = volumeInfo.getJSONArray("authors");

                String[] author_name = new String[authors.length()];
                for (int j = 0; j < authors.length(); j++) {
                    author_name[j] = authors.getString(j);

                }

                // String publishedDate = index.getString("publishedDate");
                String description = volumeInfo.getString("description");
               // Log.e(LOG_TAG, description);
                String pageCount = volumeInfo.getString("pageCount");
               // Log.e(LOG_TAG, pageCount);
                JSONArray category = volumeInfo.getJSONArray("categories");
                String[] category_list = new String[category.length()];
                for (int j = 0; j < category.length(); j++) {
                    category_list[j] = category.getString(j);
                   // Log.e(LOG_TAG, category_list[j]);
                }

                String averageRating;
                if (volumeInfo.has("averageRating"))
                    averageRating = volumeInfo.getString("averageRating");
                else
                    averageRating = "Rating not available";
               // Log.e(LOG_TAG, averageRating);

                String ratingCount;
                if(volumeInfo.has("ratingsCount"))
                ratingCount = volumeInfo.getString("ratingsCount");
                else{
                    ratingCount = "0";
                }
               // Log.e(LOG_TAG, ratingCount);
                String maturityRating = volumeInfo.getString("maturityRating");
              //  Log.e(LOG_TAG, maturityRating);
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String smallThumbnail = imageLinks.getString("smallThumbnail");
                Bitmap bmp = null;
                try {
                    URL url = new URL(smallThumbnail);
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String language = volumeInfo.getString("language");
                String previewLink;
                if(volumeInfo.has("previewLink"))
                 previewLink = volumeInfo.getString("previewLink");
                else
                    previewLink = null;

                JSONObject saleInfo = index.getJSONObject("saleInfo");

                String saleability = saleInfo.getString("saleability");
               // Log.e(LOG_TAG, saleability);
                String price;
                if (saleability.equals("NOT_FOR_SALE")) {
                    price = "Not For Sale";
                } else {
                    JSONObject retailPrice = saleInfo.getJSONObject("retailPrice");
                    String amount = retailPrice.getString("amount");
                    String currencyCode = retailPrice.getString("currencyCode");
                    price = "Price: " + amount +" " + currencyCode;
                }

                String buyLink;
                if(index.has("buyLink"))
                buyLink = index.getString("buyLink");
                else{
                    buyLink = null;
                }

                bookList.add(new Book(title, author_name, description, pageCount, category_list, averageRating, ratingCount, maturityRating, language, previewLink, saleability, price, buyLink,bmp));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public static List<Book> fetchBookDetails(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        URL url = createUrl(requestUrl);

        String jsonRespone = null;

        try {
            jsonRespone = makeHttpRequest(url);
        } catch (IOException e) {
           // Log.e(LOG_TAG, "Problem making Http Request", e);
        }

        List<Book> bookDetails = extractJsonFeatures(jsonRespone);

        return bookDetails;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
           // Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book List JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


}
