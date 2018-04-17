package com.navigation.android.maps3;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by acer on 15-04-2018.
 */

public class MyDownloadTask extends AsyncTask<String, Void, String> {

    private GoogleMap myMap;
    private Context context;
    MyDownloadTask(GoogleMap map, Context context){
        this.myMap = map;
        this.context = context;
    }

    // downloading data from the given url in background
    @Override
    protected String doInBackground(String... urls) {
        String urlResult = "";
        if (urls != null && urls.length > 0){

            try {
                urlResult = downloadDataFromUrl(urls[0]);
                Log.e("URl downloaded data", urlResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return urlResult;
    }


    @Override
    protected void onPostExecute(String result) {
        Log.e("onPostExecute", "recieved result");
        ParseResponseData parseResponseData = new ParseResponseData(this.myMap, this.context);
        parseResponseData.execute(result);
    }

    // method to download data from the given url
    private String downloadDataFromUrl(String dataUrl) throws IOException {
        String responseData = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            Log.e("downloadDataFromUrl", "inside downloadDataFromUrl function");
            URL url = new URL(dataUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";



            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            responseData = stringBuffer.toString();
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            urlConnection.disconnect();
        }

        return responseData;

    }
}

