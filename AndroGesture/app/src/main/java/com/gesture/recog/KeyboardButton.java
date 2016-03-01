
/**
package com.gesture.recog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by harshitha on 2/28/16.
 */
/**
public class KeyboardButton extends Activity implements View.OnClickListener {

    private Button keybutton;
    //Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        keybutton = (Button) findViewById(R.id.keyboard_control);
        keybutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, keyboardActivity.class);
        startActivity(intent);
    }
}

    /**
    @Override
    public void onClick(View v) {

        InputMethodManager imm = (InputMethodManager)myContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        /**
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            imm.showSoftInput(edt1, InputMethodManager.SHOW_IMPLICIT);


    }
    */






