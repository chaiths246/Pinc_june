
package com.example.chaithra.pinc;

import android.util.Log;
import com.example.chaithra.pinc.model.Question;
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
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by chaithra on 3/26/18.
 */

public class Utils {
    public static final String LOG_TAG = Utils.class.getSimpleName();

    public static int PAGE_URL_NUM;
    public static ArrayList<Question> fetchQuestionData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        ArrayList<Question> newslist = null;
        try {
            newslist = jsonconverter(jsonResponse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newslist;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
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
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the News JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                Log.d("Network", "" + line);
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Question> jsonconverter(String newsJSON) throws ParseException {
        ArrayList<Question> newsarraylist = new ArrayList<Question>();
        ArrayList<String> commenterarr = new ArrayList<String>();
        String question = "";
        String authorName = "";
        String avatharurl = "";
        boolean isBookmarked = false;
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(newsJSON);

            if (jsonObject.has("meta")) {
                JSONObject metaObj = jsonObject.getJSONObject("meta");
                PAGE_URL_NUM = metaObj.getInt("total_pages");
            }
            // make an jsonObject in order to parse the response
            if (jsonObject.has("data")) {

                JSONArray jsonArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {

                    if (jsonArray.getJSONObject(i).has("attributes")) {
                        JSONObject attObj = jsonArray.getJSONObject(i).getJSONObject("attributes");
                        question = attObj.getString("text");
                        isBookmarked = attObj.getBoolean("is_bookmarked_by_current");

                        JSONObject authorInfoObj = attObj.getJSONObject("author_info");
                        avatharurl = authorInfoObj.getString("avatar_thumb");
                        authorName = authorInfoObj.getString("name");

                        if (authorInfoObj.has("latest_answerers_info")) {
                            JSONArray commentedinfoarr = authorInfoObj.getJSONArray("latest_answerers_info");

                            for (int commenter = 0; commenter < commentedinfoarr.length(); commenter++) {
                                if (commenter == 3) {
                                    commenterarr.add((commentedinfoarr.length() - 3) + "");
                                    break;
                                } else {
                                    commenterarr.add(commentedinfoarr.getJSONObject(i).getString("avatar_thumb"));//adding to array
                                }
                            }
                        } else {
                            newsarraylist.add(new Question(question, avatharurl, avatharurl, avatharurl, avatharurl, "", "", isBookmarked, authorName));
                        }
                        if (commenterarr.size() == 1) {
                            newsarraylist.add(new Question(question, commenterarr.get(0), null, null, null, "", "", isBookmarked, authorName));
                        } else if (commenterarr.size() == 2) {
                            newsarraylist.add(new Question(question, commenterarr.get(0), commenterarr.get(1), null, null, "", "", isBookmarked, authorName));
                        } else if (commenterarr.size() == 3) {
                            newsarraylist.add(new Question(question, commenterarr.get(0), commenterarr.get(1), commenterarr.get(2), null, "", "", isBookmarked, authorName));
                        } else if (commenterarr.size() == 4) {
                            newsarraylist.add(new Question(question, commenterarr.get(0), commenterarr.get(1), commenterarr.get(2), commenterarr.get(3), "", "", isBookmarked, authorName));
                        }


                    }

                }
            } else {
                newsarraylist = null;
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.toString());
        }
        return newsarraylist;

    }

}
