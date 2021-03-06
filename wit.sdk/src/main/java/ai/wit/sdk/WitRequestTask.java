/*
    Copyright 2013 Wit Inc. All rights reserved.
 */
package ai.wit.sdk;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * The request class.
 * The only purpose of this class is to call Wit async and return the result without any post processing.
 * Created by Wit on 5/30/13.
 */
public class WitRequestTask extends AsyncTask<String, String, String> {

    private final String WIT_URL = "https://api.wit.ai/message?q=";
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_FORMAT = "Bearer %s";
    private String _accessToken;

    public WitRequestTask(String accessToken) {
        _accessToken = accessToken;
    }

    @Override
    protected String doInBackground(String... text) {
        String response = null;
        try {
            Log.d("Wit", "Requesting ...." + text[0]);
            final String getUrl = String.format("%s%s", WIT_URL, URLEncoder.encode(text[0], "utf-8"));
            URL url = new URL(getUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty(AUTHORIZATION_HEADER, String.format(BEARER_FORMAT, _accessToken));
            try {
                final InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                response = IOUtils.toString(in);
                in.close();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("Wit", "An error occurred during the request, did you set your token correctly?", e);
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
    }
}
