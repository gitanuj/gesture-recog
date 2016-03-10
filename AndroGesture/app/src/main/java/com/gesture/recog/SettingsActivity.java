package com.gesture.recog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsMap = new HashMap<String, String>();

        //default settings
        settingsMap.put("flip", "command|space");
        settingsMap.put("right_left", "right");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

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
    public String serializeTable(TableLayout tableLayout) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < tableLayout.getChildCount(); i++) {
            View view = tableLayout.getChildAt(i);
            if(view instanceof TableRow) {
                //we have the setting and table now
                TableRow row = (TableRow) view;
                TextView firstTextView = (TextView) row.getChildAt(0);
                TextView secondTextView = (TextView) row.getChildAt(1);
                String gesture = firstTextView.getText().toString();
                String keyboard = secondTextView.getText().toString();

                //add it to the stringbuilder
                sb.append(gesture + "," + keyboard);
                sb.append("!");
            }
        }

        return sb.toString();
    }
}
