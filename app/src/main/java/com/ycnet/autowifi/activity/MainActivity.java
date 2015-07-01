package com.ycnet.autowifi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ycnet.autowifi.R;
import com.ycnet.autowifi.model.AutoConnect;
import com.ycnet.autowifi.util.WifiAdmin;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        WifiAdmin wifiAdmin = new WifiAdmin(this.getApplicationContext());
        wifiAdmin.startScan();
        List<ScanResult> scanResults = wifiAdmin.getWifiList();

        for (ScanResult scanResult : scanResults) {
            dataList.add(scanResult.SSID);
        }
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int index,
                                    long arg3) {
                WifiAdmin wifiAdmin = new WifiAdmin(view.getContext());
                wifiAdmin.openWifi();
                String targetSSID = dataList.get(index);
                AutoConnect autoConnect = new AutoConnect();
                String realPsw = autoConnect.autoConnect(targetSSID, WifiAdmin.WifiCipherType.WIFICIPHER_WPA, wifiAdmin);
                realPsw = realPsw == "" ? "未找到合适的密码" : realPsw;
                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).setMessage(realPsw).create();
                alertDialog.show();
            }
        });
    }
}
