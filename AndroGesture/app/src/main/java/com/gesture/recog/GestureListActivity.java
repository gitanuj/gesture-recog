package com.gesture.recog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GestureListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String SERVER_IP = "SERVER_IP";

    private static final int GET_GESTURES_PORT = 8801;

    private static final int SAVE_GESTURES_PORT = 8802;

    private static final int CREATE_GESTURE_REQUEST_CODE = 1;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private ListView mListView;

    private View mEmptyView;

    private FloatingActionButton mFAB;

    private GesturesAdapter mAdapter;

    private String mServerAddress;

    private List<Gesture> mGestures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_list);

        mServerAddress = getIntent().getStringExtra(SERVER_IP);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setSubtitle(getString(R.string.connected_to, mServerAddress));

        mListView = (ListView) findViewById(R.id.list);
        mEmptyView = findViewById(R.id.empty_view);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(this);

        mAdapter = new GesturesAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        notifyAdapter();

        mExecutorService.submit(new FetchGesturesRunnable());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mExecutorService.submit(new SaveGesturesRunnable(new ArrayList<>(mGestures)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                trainGestures();
                break;
        }
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

    private void trainGestures() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_gesture_info, null);
        final EditText gestureName = (EditText) dialogView.findViewById(R.id.et_gesture_name);
        final EditText gestureCmd = (EditText) dialogView.findViewById(R.id.et_gesture_command);
        new AlertDialog.Builder(this).setTitle("Create gesture").setView(dialogView).setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = gestureName.getText().toString();
                String cmd = gestureCmd.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString(SensorActivity.SERVER_IP, mServerAddress);
                bundle.putString(CreateGestureActivity.GESTURE_NAME, name);
                bundle.putString(CreateGestureActivity.GESTURE_COMMAND, cmd);
                Intent intent = Utils.buildLaunchIntent(GestureListActivity.this, CreateGestureActivity.class, bundle);
                GestureListActivity.this.startActivityForResult(intent, CREATE_GESTURE_REQUEST_CODE);
            }
        }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Gesture gesture = (Gesture) data.getSerializableExtra("gesture");
            mGestures.add(gesture);
            notifyAdapter();
            Snackbar.make(mFAB, "Added new gesture", Snackbar.LENGTH_LONG).show();
        }
    }

    private void notifyAdapter() {
        if (mAdapter.getCount() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
        } else {
            mEmptyView.setVisibility(View.INVISIBLE);
            mListView.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Gesture gesture = mGestures.get(position);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_gesture_info, null);
        final EditText gestureName = (EditText) dialogView.findViewById(R.id.et_gesture_name);
        final EditText gestureCmd = (EditText) dialogView.findViewById(R.id.et_gesture_command);
        gestureName.setText(gesture.name);
        gestureName.setEnabled(false);

        gestureCmd.setText(gesture.command);

        new AlertDialog.Builder(this).setTitle("Edit gesture").setView(dialogView).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Gesture g = new Gesture(gestureName.getText().toString(), gestureCmd.getText().toString());
                mGestures.remove(position);
                mGestures.add(position, g);
                notifyAdapter();
            }
        }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGestures.remove(position);
                notifyAdapter();
            }
        }).create().show();
    }

    private class GesturesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mGestures != null) {
                return mGestures.size();
            }
            return 0;
        }

        @Override
        public Gesture getItem(int position) {
            if (mGestures != null) {
                return mGestures.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(GestureListActivity.this).inflate(R.layout.list_item_gesture, parent, false);
                holder = new ViewHolder();
                holder.init(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.bindViews(getItem(position));
            return convertView;
        }
    }

    private static class ViewHolder {
        private TextView name;
        private TextView command;

        public void init(View view) {
            name = (TextView) view.findViewById(R.id.tv_name);
            command = (TextView) view.findViewById(R.id.tv_command);
        }

        public void bindViews(Gesture gesture) {
            this.name.setText(gesture.name);
            this.command.setText(gesture.command);
        }
    }

    private class FetchGesturesRunnable implements Runnable {

        @Override
        public void run() {
            Socket socket = null;
            try {
                socket = new Socket(mServerAddress, GET_GESTURES_PORT);
                String data = Utils.readFully(socket.getInputStream());
                JSONObject jsonObject = new JSONObject(data);
                Iterator<String> iterator = jsonObject.keys();

                final List<Gesture> gestures = new ArrayList<>();
                while (iterator.hasNext()) {
                    String name = iterator.next();
                    String command = jsonObject.getString(name);
                    gestures.add(new Gesture(name, command));
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mGestures = gestures;
                        notifyAdapter();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Utils.closeQuietly(socket);
            }
        }
    }

    private class SaveGesturesRunnable implements Runnable {

        private List<Gesture> gestures;

        public SaveGesturesRunnable(List<Gesture> gestures) {
            this.gestures = gestures;
        }

        @Override
        public void run() {
            Socket socket = null;
            try {
                socket = new Socket(mServerAddress, SAVE_GESTURES_PORT);
                PrintWriter writer = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
                JSONObject jsonObject = new JSONObject();
                for (Gesture gesture : gestures) {
                    jsonObject.put(gesture.name, gesture.command);
                }
                writer.print(jsonObject.toString());
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Utils.closeQuietly(socket);
            }
        }
    }
}
