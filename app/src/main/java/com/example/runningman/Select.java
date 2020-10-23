package com.example.runningman;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.pulltorefresh.PullToRefresh;


public class Select extends Activity implements PullToRefresh.OnRefreshListener {
    private Bluetooth bt;
    private ListView listView;
    private Button not_found;
    private List<BluetoothDevice> paired;
    private PullToRefresh pull_to_refresh;
    private boolean registered=false;
private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered=true;

        bt = new Bluetooth(this);
        bt.enableBluetooth();

        pull_to_refresh = (PullToRefresh)findViewById(R.id.pull_to_refresh);
        listView =  (ListView)findViewById(R.id.list);
        not_found =  (Button) findViewById(R.id.not_in_list);
not_found.setEnabled(true);
        pull_to_refresh.setListView(listView);
        pull_to_refresh.setOnRefreshListener(this);
        pull_to_refresh.setSlide(500);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Select.this, Chat.class);
                i.putExtra("pos", position);
                if(registered) {
                    unregisterReceiver(mReceiver);
                    registered=false;
                }
                startActivity(i);
                finish();
            }
        });

        not_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView.getAdapter() != null) {
                    if (!listView.getAdapter().isEmpty()) {
                        Intent i = new Intent(Select.this, Chat.class);
                        i.putExtra("pos", 0);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });

        addDevicesToList();
    }

    @Override
    public void onRefresh() {
        List<String> names = new ArrayList<String>();
        for (BluetoothDevice d : bt.getPairedDevices()){
            names.add(d.getName());
        }

        String[] array = names.toArray(new String[names.size()]);
        final MyListActivity adapter = new MyListActivity(this, array);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
           /*     listView.removeViews(0, listView.getCount()-1);*/
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
                paired = bt.getPairedDevices();
            }
        });
        pull_to_refresh.refreshComplete();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }
    }

    private void addDevicesToList(){
        paired = bt.getPairedDevices();

        List<String> names = new ArrayList<>();
        for (BluetoothDevice d : paired){
            names.add(d.getName());
        }

        String[] array = names.toArray(new String[names.size()]);

       MyListActivity adapter = new MyListActivity(this, array);

        listView.setAdapter(adapter);

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setEnabled(false);
                            }
                        });
                        Toast.makeText(Select.this, "Turn on bluetooth", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addDevicesToList();
                                listView.setEnabled(true);
                            }
                        });
                        break;
                }
            }
        }
    };
}

