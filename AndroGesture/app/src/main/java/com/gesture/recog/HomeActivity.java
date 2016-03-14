package com.gesture.recog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HomeActivity extends Activity implements View.OnClickListener {

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
        Utils.launchActivity(this, OptionActivity.class, bundle);
    }
}