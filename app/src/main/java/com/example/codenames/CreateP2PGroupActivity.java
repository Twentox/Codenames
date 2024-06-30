package com.example.codenames;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codenames.networking.WiFiDirectHandler;

public class CreateP2PGroupActivity extends AppCompatActivity {
    private WiFiDirectHandler wiFiDirectHandler;

    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_p2p_group);

        wiFiDirectHandler = new WiFiDirectHandler(this);
        wiFiDirectHandler.discoverPeers();

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WifiP2pDevice device = wiFiDirectHandler.peers.get(position);
                wiFiDirectHandler.connectToPeer(device, true);
            }
        });

        displayPeers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wiFiDirectHandler.registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wiFiDirectHandler.unregisterReceiver();
    }

    public void displayPeers() {
        new Thread(() -> {
            while (true) {
                synchronized (wiFiDirectHandler.peerNames) {
                    try {
                        wiFiDirectHandler.peerNames.wait();
                        runOnUiThread(() -> {
                            adapter.clear();
                            adapter.addAll(wiFiDirectHandler.peerNames);
                            adapter.notifyDataSetChanged();
                        });
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }).start();
    }
}