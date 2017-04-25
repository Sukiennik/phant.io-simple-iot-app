package agh.project.iot.iot_device;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created 24.04.17.
 */
public class ActivityRecognizedService extends IntentService {

    private Map<Integer, Integer> activities = new HashMap<>();
    private List<Integer> monitored = Arrays.asList(8, 7, 5, 3);

    public ActivityRecognizedService() {
        super("ActivityService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            try {
                handleDetectedActivities( result.getProbableActivities() );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) throws InterruptedException {
        System.out.println("handler");
        for( DetectedActivity activity : probableActivities ) {
            if(monitored.contains(activity.getType()))
                activities.put(activity.getType(), activity.getConfidence());
        }

        for(Map.Entry<Integer, Integer> entry : activities.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

        Map.Entry<Integer, Integer> maxEntry = null;
        for (Map.Entry<Integer, Integer> entry : activities.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        int type = maxEntry.getKey();
        int confidence = maxEntry.getValue();

        switch( type ) {
            case DetectedActivity.RUNNING: {
                Log.e("ActivityRecogition", "Running: " + confidence);
                new phantActivity(postActivity.getAppContext(), phantActivity.httpRequest.POST_FLAG, "3");
                Thread.sleep(1000);
                break;
            }
            case DetectedActivity.STILL: {
                Log.e("ActivityRecogition", "Still: " + confidence);
                new phantActivity(postActivity.getAppContext(), phantActivity.httpRequest.POST_FLAG, "1");
                Thread.sleep(1000);
                break;
            }
            case DetectedActivity.TILTING: {
                Log.e("ActivityRecogition", "Tilting: " + confidence);
                new phantActivity(postActivity.getAppContext(), phantActivity.httpRequest.POST_FLAG, "4");
                Thread.sleep(1000);
                break;
            }
            case DetectedActivity.WALKING: {
                Log.e("ActivityRecogition", "Walking: " + confidence);
                new phantActivity(postActivity.getAppContext(), phantActivity.httpRequest.POST_FLAG, "2");
                Thread.sleep(1000);
                break;
            }

        }
    }
}

