package com.gesture.recog;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyboardActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SERVER_IP = "SERVER_IP";

    private static final int PORT = 8800;

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private String mServerAddress;

    private EditText mKeyboardInput;

    private Button mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        mServerAddress = getIntent().getStringExtra(SERVER_IP);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setSubtitle(getString(R.string.connected_to, mServerAddress));

        mKeyboardInput = (EditText) findViewById(R.id.et_input);
        mKeyboardInput.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            sendKeys();
                            return true;
                        }
                        return false;
                    }
                });

        mSendButton = (Button) findViewById(R.id.btn_send);
        mSendButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                sendKeys();
                break;
        }
    }

    private void sendKeys() {
        mExecutorService.submit(new Sender(mKeyboardInput.getText().toString()));
        mKeyboardInput.setText("");
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

