package com.gesture.recog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditText;

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mEditText = (EditText) findViewById(R.id.et_server_address);
        mButton = (Button) findViewById(R.id.btn_connect);

        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String serverAddress = mEditText.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(SensorActivity.SERVER_IP, serverAddress);
        Intent intent = Utils.buildLaunchIntent(this, GestureActivity.class, bundle);
        startActivity(intent);
    }
}