package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

public class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {

    private static final String LOG_TAG = EndpointsAsyncTask.class.getSimpleName();

    // IP addresses for different machines.
    // From: https://futurestud.io/tutorials/how-to-run-an-android-app-against-a-localhost-api
    private static final String EMULATOR_IP_ADDRESS = "10.0.2.2";
    private static final String DEVICE_IP_ADDRESS = BuildConfig.DEVICE_IP;

    // Boolean to know the device type
    private boolean isEmulator;

    // Interface to retrieve the result when the task finishes
    // From: https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
    public interface AsyncResponse {
        void processFinish(String joke);
    }

    private static MyApi myApiService = null;
    private AsyncResponse mDelegate;
    private Context mContext;
    private boolean success;

    // Constructor
    public EndpointsAsyncTask(Context context, AsyncResponse delegate) {
        mContext = context;
        mDelegate = delegate;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (myApiService == null) {  // Only do this once
            checkDeviceType();
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl(getRootUrl())
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            myApiService = builder.build();
        }

        try {
            success = true;
            return myApiService.sayJoke().execute().getData();
        } catch (IOException e) {
            success = false;
            Log.e(LOG_TAG, "Failed connection: " + e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (mDelegate != null) {
            if (success) {
                mDelegate.processFinish(result);
            } else {
                Toast.makeText(mContext,
                        mContext.getString(com.example.android.jokedisplaylibrary.R.string.error),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Helper method for checking the device type
     * From: https://stackoverflow.com/questions/2799097/how-can-i-detect-when-an-android-application-is-running-in-the-emulator
     */
    private void checkDeviceType() {
        if (Build.FINGERPRINT.contains("generic")) {
            isEmulator = true;
            Log.i(LOG_TAG, "This is an emulator");
        } else {
            isEmulator = false;
            Log.i(LOG_TAG, "This is a physical device or an emulator using a Bridget Network Adapter");
        }
    }

    /**
     * Helper method to get the root url depending which type of machine is running the app
     */
    private String getRootUrl() {
        String ipAddress;
        if (isEmulator) {
            ipAddress = EMULATOR_IP_ADDRESS;
        } else {
            ipAddress = DEVICE_IP_ADDRESS;
        }

        return "http://" + ipAddress + ":8080/_ah/api/";
    }
}
