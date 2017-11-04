package ng.codeinn.cryptoconvert.NetworkUtils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ng.codeinn.cryptoconvert.MyCurrency;
import ng.codeinn.cryptoconvert.R;

/**
 * Created by Jer on 11/3/2017.
 */

public final class QueryUtils {


    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Created a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    //Created a net work request method called makeHttpRequest with a Url arg that throws IOException
    private static String makeHttpRequest(URL url) throws IOException {
        //Set jsonResponse String to an empty string
        String jsonResponse = "";

        //if the url arg is null return the empty jsonResponse String
        if (url == null){
            return jsonResponse;
        }

        //Create a HttpsURLConnection variable called urlConnection and set it to null
        HttpsURLConnection urlConnection = null;
        //Create a InputStream variable called inputStream and set it to null
        InputStream inputStream = null;
        try {

            //call the openConnection method on the Url variable, casting it to HttpsURLConnection and storing it in the urlConnection
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            // setting the request method of the urlConnection to be "GET"
            urlConnection.setRequestMethod("GET");
            // to connect the urlConnection
            urlConnection.connect();

            // if the urlConnection is successful getInputStream of the urlConnection
            //then read the inputStream into the jsonResponse
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem receiving the earthquake JSON result.", e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        //Return the read input stream stored in the jsonResponse
        return jsonResponse;
    }

    // created a method to read from the inputStream
    private static String readFromStream(InputStream inputStream) {
        // Created a new StringBuilder object called output
        StringBuilder output = new StringBuilder();

        //if the input stream is not null; createa a new InputStreamReader object with the inputStream and read it in the UTF-8 Charset
        // create a new BufferedReader object with the InputStreamReader object
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {

                // read the first line BufferedReader object store it in a string variable "line" then go to the next line
                String line = reader.readLine();

                // while line is not null append line to the StringBuilder object called output
                // then read the current line, store it in line then go to the next line
                while (line != null){
                    output.append(line);
                    line = reader.readLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        String outputString = output.toString();

        Log.i("output", outputString);

        // Return the String builder object as a string by caling the toString method
        return output.toString();
    }
    public static List<MyCurrency> extractCurrencyFromJSON(String currencyJSON, Context context) {

        if (TextUtils.isEmpty(currencyJSON)) {
            return null;
        }
        List<MyCurrency> currencyArrayList = new ArrayList<>();

        try {
            JSONObject baseJSONResponse = new JSONObject(currencyJSON);

            JSONObject BTCObject = baseJSONResponse.getJSONObject("BTC");
            JSONObject ETHObject = baseJSONResponse.getJSONObject("ETH");
            Resources resources = context.getResources();
            String[] currencyArray = resources.getStringArray(R.array.currency_code);
            String[] currencyNAmeArray = resources.getStringArray(R.array.currency_name);
            for (int i=0; i<currencyArray.length; i++){
                String currencyID = currencyArray[i];
                String rateBTC = ""+BTCObject.getDouble(currencyID);
                String rateETH = ""+ETHObject.getDouble(currencyID);
                String currencyName = currencyNAmeArray[i];

                MyCurrency myCurrency = new MyCurrency(currencyName, currencyID,rateBTC, rateETH );

                currencyArrayList.add(myCurrency);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the sources JSON results", e);
        }
        return currencyArrayList;
    }

    public static List<MyCurrency> fetchCurrencyData(String requestUrl, Context context){
        // creates a URL out of the String 'requestedUrl' with the createUrl() method
        URL url = createUrl(requestUrl);

        // sets a String 'jsonResponse' as null
        String jsonResponse = null;


        try{

            // makes HTTP request on the created url and stores it in the jsonResponse string
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem making HTTP request");
        }

        List<MyCurrency> currencies  = extractCurrencyFromJSON(jsonResponse, context);
        return currencies;
    }

}
