package agh.project.iot.iot_device;

import android.app.Activity;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created 15.04.17.
 */
public class phantActivity {

    private String val;
    private httpRequest requestType;
    private AsyncHttpClient client = new AsyncHttpClient();
    private static final String DELETE_KEY = "P7zEDQbZnQHZdqrBrDP0uQL2OJO8";
    private static final String HEADER_TYPE = "Phant-Private-Key";
    private static final String PRIVATE_KEY = "QDOYQqMeNqFE0eqyqmP8cOv82Z2P"; // online "RbgR2BBVeKilDrMg95yP";
    private static final String PUBLIC_KEY =  "0wLdKVzjaVFyjb0Z0v83SPbzQNQ1"; // online "YGznqddEbRHZr1DXbQ6o";
    private static final String postUrl = "http://192.168.0.197:8080/input/"+PUBLIC_KEY; // online "http://data.sparkfun.com/input/"+PUBLIC_KEY;
    private static final String clearUrl = "http://192.168.0.197:8080/input/"+PUBLIC_KEY+"/clear?private_key="+PRIVATE_KEY; //online "http://data.sparkfun.com/input/"+PUBLIC_KEY+"/clear?private_key="+PRIVATE_KEY;
    private static final String[] fields = {"red", "green", "blue", "flag"};
    private Activity context;

    public phantActivity(Activity context, httpRequest requestType, String val) {
        this.context = context;
        this.requestType = requestType;
        this.val = val;
        client.removeAllHeaders();
        client.addHeader(HEADER_TYPE, PRIVATE_KEY);
        executeRequest();
    }

    public void executeRequest() {
        final TextView log = (TextView) context.findViewById(R.id.logText);
        switch (requestType) {
            case POST_FLAG:
                client.post(postUrl, createParams(httpRequest.POST_FLAG.getKeys(), val), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //log.setText("SENDING \"" + "MOV_STATUS" + "\" TO FLAG FIELD");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //log.setText("FAILED SENDING \"" + "MOV_STATUS" + "\" TO FLAG FIELD");
                    }
                });
                break;
            case POST_RED:
                client.post(postUrl, createParams(httpRequest.POST_RED.getKeys(), val), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        log.setText("SEND \"" + val + "\" TO RED FIELD");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        log.setText("FAILED SENDING VALUE TO RED FIELD");
                    }
                });
                break;
            case POST_BLUE:
                client.post(postUrl, createParams(httpRequest.POST_BLUE.getKeys(), val), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        log.setText("SEND \"" + val + "\" TO BLUE FIELD");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        log.setText("FAILED SENDING VALUE TO BLUE FIELD");
                    }
                });
                break;
            case POST_GREEN:
                client.post(postUrl, createParams(httpRequest.POST_GREEN.getKeys(), val), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        log.setText("SEND \"" + val + "\" TO GREEN FIELD");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        log.setText("FAILED SENDING VALUE TO GREEN FIELD");
                    }
                });
                break;
            case POST_ALL:
                client.post(postUrl, createParams(httpRequest.POST_ALL.getKeys(), val), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        log.setText("SEND \"" + val + "\" TO ALL FIELDS");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        log.setText("FAILED SENDING VALUE TO ALL FIELDS");
                    }
                });
                break;
            case CLEAR:
                client.get(clearUrl, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        log.setText("STREAM CLEARED SUCCESSFULLY");
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        log.setText("FAILED TO CLEAR STREAM");
                    }
                });
                break;
            case GET:
                break;
            case DELETE:
                break;
        }
    }

    private RequestParams createParams(String[] keys, String val){
        RequestParams params = new RequestParams();
        for(String key : keys) {
            params.add(key, val);
        }
        return  fillEmptyParams(keys, params);
    }

    private RequestParams fillEmptyParams(String[] keys, RequestParams params) {
        List<String> keysList = new ArrayList<>(Arrays.asList(keys));
        List<String> fieldsList = new ArrayList<>(Arrays.asList(fields));
        if (fieldsList.removeAll(keysList)) {
            for (String key : fieldsList) {
                params.add(key, "0");
            }
        }
        return params;
    }


    public enum httpRequest {
        POST_FLAG ("flag"),
        POST_RED ("red"),
        POST_BLUE ("blue"),
        POST_GREEN ("green"),
        POST_ALL ("red", "blue", "green"),
        CLEAR,
        GET,
        DELETE;

        String[] keys;

        httpRequest(String... keys ) {
            this.keys = keys;
        }

        public String[] getKeys(){
            return keys;
        }
    }
}


