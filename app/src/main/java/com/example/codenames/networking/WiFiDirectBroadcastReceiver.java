package com.example.codenames.networking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.ArrayList;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    private WiFiDirectHandler wifiDirectHandler;

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, WiFiDirectHandler wifiDirectHandler) {
        this.wifiDirectHandler = wifiDirectHandler;

        this.manager = manager;
        this.channel = channel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wi-Fi P2P is enabled
                Log.d("WiFiDirectReceiver", "Wi-Fi P2P is enabled");
            } else {
                // Wi-Fi P2P is not enabled
                Log.d("WiFiDirectReceiver", "Wi-Fi P2P is not enabled");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            if (manager != null) {
                manager.requestPeers(channel, peerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (manager == null) {
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                // We are connected with the other device, request connection info to find group owner IP
                manager.requestConnectionInfo(channel, connectionInfoListener);
            } else {
                // It's a disconnect
                Log.d("WiFiDirectReceiver", "Disconnected");
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }

    // aktualisiert die peers im WiFiDirectHandler
    private final WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            wifiDirectHandler.updatePeers(new ArrayList<>(peerList.getDeviceList()));
        }
    };

    // ruft handleConnectionInfo mit aktueller WifiP2pInfo im WiFIDirectHandler auf
    private final WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            wifiDirectHandler.handleConnectionInfo(info);
        }
    };
}