package com.gesture.recog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

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

        setContentView(R.layout.activity_edit_settings);
        settingsMap = new HashMap<>();

        mSettingsString = getIntent().getStringExtra("CURRENT_SETTINGS");
        mSaveButton = (Button) findViewById(R.id.save_edits);
        mCancelButton = (Button) findViewById(R.id.cancel_edits);

        //load the current settings
        deserializeSettingsString();

        //save button sends back an intent with updated strings
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditGestureActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EDITED_SETTINGS", serializeSettingsMap());
                startActivity(intent);
            }
        });

        //cancel button just goes back
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditGestureActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        init();
    }

    //dynamically create settings table
    public void init() {
        TableLayout settingsTable = (TableLayout) findViewById(R.id.displaySettings);
        Iterator it = settingsMap.entrySet().iterator();

        //set heading of table
        TableRow tableHead = new TableRow(this);

        TextView gestureHead = new TextView(this);
        gestureHead.setText("Gesture");
        tableHead.addView(gestureHead);

        TextView commandHead = new TextView(this);
        commandHead.setText("Keyboard Command");
        tableHead.addView(commandHead);

        settingsTable.addView(tableHead);

        int i = 0;
        while(it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();

            //make new table entry
            TableRow currRow = new TableRow(this);
            EditText gesture = new EditText(this);
            gesture.setText(pair.getKey());
            gesture.setId(i);
            i++;
            EditText command = new EditText(this);
            command.setText(pair.getValue());
            command.setId(i);
            i++;

            currRow.addView(gesture);
            currRow.addView(command);


            //add table entry to table
            settingsTable.addView(currRow);

        }
    }

    //go through the table, get all of the gesture mappings, and store them as a String
    //string is formatted as follows: "gesture1,keymap1!gesture2,keymap2!gesture3,keymap3!..."
    private String serializeSettingsMap() {
        StringBuilder sb = new StringBuilder();

        Iterator it = settingsMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
            String g = pair.getKey();
            String c = pair.getValue();

            sb.append(g+","+c+"!");
        }

        System.out.println("ASDFASDFASDFASDFASDFASDF" + sb.toString());

        return sb.toString();
    }

    private void deserializeSettingsString() {
        if(mSettingsString == null)
            return;

        String[] tokens = mSettingsString.split("!");
        for(int i = 0; i < tokens.length; i++) {
            System.out.println("!!!!!!   " + tokens[i]);

            String[] thisGesture = tokens[i].split(",");

            settingsMap.put(thisGesture[0], thisGesture[1]);


        }
    }
}
