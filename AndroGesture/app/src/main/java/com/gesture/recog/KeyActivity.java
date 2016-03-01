package com.gesture.recog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by harshitha on 3/1/16.
 */
public class KeyActivity extends Activity{

    public static final String SERVER_IP = "SERVER_IP";

    private static final int PORT = 8800;

    private ExecutorService keyExecutorService = Executors.newSingleThreadExecutor();

    private char key_data;

    private String keyServerAddress;

    private Sender keySender;

    private Button keyButton;
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        keyButton = (Button) findViewById(R.id.keyboard_control);

        keyServerAddress= getIntent().getStringExtra(SERVER_IP);

        keyButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                keySender=new Sender();
                keyExecutorService.submit(keySender);

            }


        });
    }



    private class Sender implements Runnable {

        private Socket socket;

        private boolean cancel;

        public void cancel() {
            cancel = true;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(keyServerAddress, PORT);
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                while (!cancel) {
                    //out.printf("%c", );
                    out.flush();
                    Thread.sleep(2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Utils.closeQuietly(socket);
            }
        }
    }
}

