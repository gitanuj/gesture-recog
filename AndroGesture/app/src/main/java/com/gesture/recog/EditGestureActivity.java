package com.gesture.recog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by alickxu on 3/10/16.
 */
public class EditGestureActivity extends Activity {

    Map<String, String> settingsMap, initialSettingsMap;

    String mSettingsString;

    Button mSaveButton;
    Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_settings);

        initialSettingsMap = new HashMap<>();
        settingsMap = new HashMap<>();

        mSettingsString = getIntent().getStringExtra(SettingsActivity.CURRENT_SETTINGS);
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
                intent.putExtra(SettingsActivity.EDITED_SETTINGS, serializeSettingsMap());
                startActivity(intent);
            }
        });

        //cancel button just goes back
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditGestureActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                //send back the initial settingsMap
                settingsMap = new HashMap<>();

                intent.putExtra(SettingsActivity.EDITED_SETTINGS, serializeInitialSettingsMap());
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
            TextView gesture = new TextView(this);
            gesture.setText(pair.getKey());
            gesture.setTag("g" + i);
            EditText command = new EditText(this);
            command.setText(pair.getValue());
            command.setTag("c" + i);

            //make delete button
            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v)
                {
                    // row is your row, the parent of the clicked button
                    View row = (View) v.getParent();
                    // container contains all the rows, you could keep a variable somewhere else to the container which you can refer to here
                    ViewGroup container = ((ViewGroup)row.getParent());
                    // delete the row and invalidate your view so it gets redrawn
                    container.removeView(row);
                    container.invalidate();

                    Toast toast = Toast.makeText(getApplicationContext(), "Gesture Deleted!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            currRow.addView(gesture);
            currRow.addView(command);
            currRow.addView(deleteButton);

            //add table entry to table
            settingsTable.addView(currRow);

            i++;
        }
    }

    //go through the table, get all of the gesture mappings, and store them as a String
    //string is formatted as follows: "gesture1,keymap1!gesture2,keymap2!gesture3,keymap3!..."
    private String serializeSettingsMap() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < settingsMap.size(); i++) {
            TextView g = (TextView)findViewById(R.id.edit_settings_layout).findViewWithTag("g" + i);
            EditText c = (EditText)findViewById(R.id.edit_settings_layout).findViewWithTag("c" + i);

            if(g != null && c != null) {
                sb.append(g.getText().toString() + "," + c.getText().toString() + "!");
            }
        }

        return sb.toString();
    }

    //when user presses cancel button, call this method to return what the settingsmap was before
    private String serializeInitialSettingsMap() {
        StringBuilder sb = new StringBuilder();

        Iterator it = initialSettingsMap.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
            String g = pair.getKey();
            String c = pair.getValue();

            sb.append(g+","+c+"!");
        }

        return sb.toString();
    }

    private void deserializeSettingsString() {
        if(mSettingsString == null)
            return;

        String[] tokens = mSettingsString.split("!");
        for(int i = 0; i < tokens.length; i++) {

            String[] thisGesture = tokens[i].split(",");

            settingsMap.put(thisGesture[0], thisGesture[1]);
            initialSettingsMap.put(thisGesture[0], thisGesture[1]);
        }
    }
}
