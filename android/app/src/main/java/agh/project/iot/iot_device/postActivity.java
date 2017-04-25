package agh.project.iot.iot_device;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;

import java.util.Timer;
import java.util.TimerTask;

public class postActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient gApiClient;
    private static Activity appContext;

    public static Activity getAppContext() {
        return postActivity.appContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postActivity.appContext = this;

        gApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        EditText input = (EditText) findViewById(R.id.inputText);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView log = (TextView) findViewById(R.id.logText);
                log.setText(null);
            }
        });

        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        ToggleButton movToggle = (ToggleButton) findViewById(R.id.movementButton);
        movToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    gApiClient.connect();
                } else {
                    gApiClient.disconnect();
                }
            }
        });
    }


    public void onGreenButton(View view) {
        EditText input = (EditText) findViewById(R.id.inputText);
        String value = input.getText().toString();
        new phantActivity(this, phantActivity.httpRequest.POST_GREEN, value);

        findViewById(R.id.greenButton).setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        findViewById(R.id.greenButton).setEnabled(true);
                    }
                });
            }
        }, 500);
    }

    public void onRedButton(View view) {
        EditText input = (EditText) findViewById(R.id.inputText);
        String value = input.getText().toString();
        new phantActivity(this, phantActivity.httpRequest.POST_RED, value);

        findViewById(R.id.redButton).setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        findViewById(R.id.redButton).setEnabled(true);
                    }
                });
            }
        }, 500);
    }

    public void onBlueButton(View view) {
        EditText input = (EditText) findViewById(R.id.inputText);
        String value = input.getText().toString();
        new phantActivity(this, phantActivity.httpRequest.POST_BLUE, value);

        findViewById(R.id.blueButton).setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        findViewById(R.id.blueButton).setEnabled(true);
                    }
                });
            }
        }, 500);
    }

    public void onALLButton(View view) {
        EditText input = (EditText) findViewById(R.id.inputText);
        String value = input.getText().toString();
        new phantActivity(this, phantActivity.httpRequest.POST_ALL, value);

        findViewById(R.id.allButton).setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        findViewById(R.id.allButton).setEnabled(true);
                    }
                });
            }
        }, 500);
    }

    public void onClearButton(View view) {
        new phantActivity(this, phantActivity.httpRequest.CLEAR, null);

        findViewById(R.id.clearButton).setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        findViewById(R.id.clearButton).setEnabled(true);
                    }
                });
            }
        }, 500);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("on connect");
        Intent intent = new Intent( this, ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( gApiClient, 3000, pendingIntent );
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("google api connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("google api connection fail");
    }


}
