package com.gesture.recog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by alickxu on 3/9/16.
 */
public class SettingsActivity extends Activity {
    Map<String, String> settingsMap;

    Button mEditButton;

    String mSettingsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsMap = new HashMap<>();
        setContentView(R.layout.activity_settings);

        mSettingsString = getIntent().getStringExtra("EDITED_SETTINGS");

        if(mSettingsString != null) {
            deserializeSettingsString();
        } else {

            //default settings
            settingsMap.put("flip", "command|space");
            settingsMap.put("right_left", "right");

        }



        mEditButton = (Button) findViewById(R.id.edit_settings);

        //set listener for edit button to open up new edit page
        if(mEditButton != null) {
            mEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettingsActivity.this, EditGestureActivity.class);

                    //send the current settings to
                    intent.putExtra("CURRENT_SETTINGS", serializeSettingsMap());
                    startActivity(intent);
                }
            });
        }


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

        while(it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();

            //make new table entry
            TableRow currRow = new TableRow(this);
            TextView gesture = new TextView(this);
            gesture.setText(pair.getKey());
            TextView command = new TextView(this);
            command.setText(pair.getValue());

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

        System.out.println("************" + sb.toString());

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
