package com.gesture.recog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by harshitha on 3/1/16.
 */
public class typeChar extends Activity{

    Button myButton;
    private TextView myText;
    private EditText myInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myButton=(Button)findViewById(R.id.button1);
        myText=(TextView)findViewById(R.id.textView1);
        myInput= (EditText) findViewById(R.id.editText1);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                myText.setText(myInput.getText());
            }
        });
    }
}
