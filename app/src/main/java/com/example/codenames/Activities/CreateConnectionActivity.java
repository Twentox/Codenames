package com.example.codenames.Activities;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.codenames.R;
import com.example.codenames.networking.*;

public class CreateConnectionActivity extends AppCompatActivity {
    private WiFiDirectHandler wiFiDirectHandler;
    private final ConnectionHandler connectionHandler = ConnectionHandler.getConnectionHandler();

    private ArrayAdapter<String> adapter;
    private ListView listView;

    private Thread displayPeersThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_connection_activity);

        WiFiDirectHandler.initWiFiDirectHandler(this);
        wiFiDirectHandler = WiFiDirectHandler.getWiFiDirectHandler();
        wiFiDirectHandler.discoverPeers();

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WifiP2pDevice device = wiFiDirectHandler.peers.get(position);
                wiFiDirectHandler.connectToPeer(device);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        wiFiDirectHandler.registerReceiver();
        startDisplayPeers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wiFiDirectHandler.unregisterReceiver();
        stopDisplayPeers();
    }

    public void startDisplayPeers() {
        displayPeersThread = new Thread(() -> {
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
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });

        displayPeersThread.start();
    }

    public void stopDisplayPeers() {
        if (displayPeersThread != null) {
            displayPeersThread.interrupt();

            try {
                displayPeersThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void sendContinue(View view) {
        connectionHandler.stopAcceptClients();
        Thread t = new Thread(() -> {
            connectionHandler.sendContinue();

            runOnUiThread(() -> {
                Intent intent = new Intent(CreateConnectionActivity.this, ChooseTeamActivity.class);
                startActivity(intent);
            });
        });

        t.start();
    }
}