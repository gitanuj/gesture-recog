package com.gesture.recog;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyboardActivity extends Activity {

    public static final String SERVER_IP = "SERVER_IP";

    private static final int PORT = 8800;

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private String mServerAddress;

    private EditText mKeyboardInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        mServerAddress = getIntent().getStringExtra(SERVER_IP);

        mKeyboardInput = (EditText) findViewById(R.id.et_input);
        mKeyboardInput.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            mExecutorService.submit(new Sender(v.getText().toString()));
                            mKeyboardInput.setText("");
                            return true;
                        }
                        return false;
                    }
                });
    }

    private class Sender implements Runnable {

        private final String data;

        private Socket socket;

        public Sender(String data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(mServerAddress, PORT);
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                out.print(data);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Utils.closeQuietly(socket);
            }
        }
    }
}

