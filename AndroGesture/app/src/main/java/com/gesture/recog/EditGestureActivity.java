package com.gesture.recog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Map;

/**
 * Created by alickxu on 3/10/16.
 */
public class EditGestureActivity extends Activity {
    Map<String, String> settingsMap;

    String mSettingsString;

    Button mSaveButton;
    Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingsString = getIntent().getStringExtra("CURRENT_SETTINGS");
        mSaveButton = (Button) findViewById(R.id.save_edits);
        mCancelButton = (Button) findViewById(R.id.cancel_edits);

        //cancel button just goes back
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditGestureActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void deserializeSettingsString() {

    }
}
