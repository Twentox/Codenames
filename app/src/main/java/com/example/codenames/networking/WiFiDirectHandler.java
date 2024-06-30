package com.example.codenames.networking;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class WiFiDirectHandler {
    public AppCompatActivity activity;

    private final WifiP2pManager manager;
    private final WifiP2pManager.Channel channel;
    private final BroadcastReceiver receiver;
    private final IntentFilter intentFilter;

    public List<WifiP2pDevice> peers;
    public List<String> peerNames = new ArrayList<>();

    private WifiP2pInfo groupInfo;

    public WiFiDirectHandler(AppCompatActivity activity) {
        this.activity = activity;

        manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(activity, activity.getMainLooper(), null);
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        requestPermissions();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public void registerReceiver() {
        activity.registerReceiver(receiver, intentFilter);
    }

    public void unregisterReceiver() {
        activity.unregisterReceiver(receiver);
    }

    public void discoverPeers() {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "Discovery Initiated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(activity, "Discovery Failed: " + reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // wird von WiFiDirectBroadcaster aufgerufen
    public void updatePeers(List<WifiP2pDevice> peers) {
        this.peers = peers;

        updatePeerNames();
    }

    private void updatePeerNames() {
        peerNames.clear();

        for (WifiP2pDevice device: peers) {
            peerNames.add(device.deviceName);
        }

        synchronized (peerNames) {
            peerNames.notify();
        }
    }

    public void connectToPeer(WifiP2pDevice device, boolean shouldBeGO) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;

        if (shouldBeGO) {
            config.groupOwnerIntent = 15;
        } else {
            config.groupOwnerIntent = 0;
        }

        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(activity, "Connection Initiated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(activity, "Connection Failed: " + reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleConnectionInfo(WifiP2pInfo info) {
        if (info.groupFormed && info.isGroupOwner) {
            // This device is the group owner
            Toast.makeText(activity, "Group Owner", Toast.LENGTH_SHORT).show();
        } else if (info.groupFormed) {
            // This device is a client
            Toast.makeText(activity, "Client", Toast.LENGTH_SHORT).show();
        }

        groupInfo = info;
    }
}
