package com.gesture.recog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alickxu on 3/15/16.
 */
public class SettingsActivity extends AppCompatActivity {

    public static final String SERVER_IP = "SERVER_IP";

    private static final int PORT = 9001;

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    String mServerAddress;
    String serializedSettings;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mListView = (ListView) findViewById(R.id.settings_list_view);

        mServerAddress = getIntent().getStringExtra(SERVER_IP);

        mExecutorService.submit(new Receiver(serializedSettings));

        //deserialize settings data here

    }

    private class Receiver implements Runnable {

        private Socket socket;

        public String settingsData;

        public Receiver(String settingsData) { this.settingsData = settingsData; }

        @Override
        public void run() {
            try {
                socket = new Socket(mServerAddress, PORT);
                String data = Utils.readFully(socket.getInputStream());
                settingsData = data;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Utils.closeQuietly(socket);
            }
        }

    }
}
