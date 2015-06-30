package com.ycnet.autowifi.model;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.ycnet.autowifi.util.WifiAdmin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sandy.xie on 2015/6/29.
 */
public class AutoConnect {
    /**
     * wifi password rule:8 to 63 ASCII codes or 8-64 hexadecimal codes
     * usually,we use numeric and characters
     * 48~57 is numerice
     * 65~90 is upper case
     * 97~122 is floor case
     */
    public String autoConnect(String SSID, WifiAdmin.WifiCipherType type, WifiAdmin wifiAdmin) {
        ArrayList validChar = new ArrayList();
        ArrayList validNum = new ArrayList();
        char tmp;
        for (int i = 48; i <= 57; i++) {
            tmp = (char) i;
            validChar.add(tmp);
            validNum.add(tmp);
        }

        for (int i = 65; i <= 90; i++) {
            tmp = (char) i;
            validChar.add(tmp);
        }

        for (int i = 97; i <= 122; i++) {
            tmp = (char) i;
            validChar.add(tmp);
        }
        int validSize = validChar.size();
        int validNumSize = validNum.size();
        String result = "";

        //process telephone or mobile
        for (int wifiLength = 8; wifiLength < 64; wifiLength++) {
            if (wifiLength == 8 || wifiLength == 11) {
                result = connectDetail(validNum, validNumSize, wifiLength, SSID, type, wifiAdmin);
                if (result != "")  return result;
            }
        }

        //process all
        for (int wifiLength = 8; wifiLength < 64; wifiLength++) {
            result = connectDetail(validChar, validSize, wifiLength, SSID, type, wifiAdmin);
            if (result != "")  return result;
        }
        return result;
    }

    private int connectWifi(String SSID, String wifiPsw, WifiAdmin.WifiCipherType type, WifiAdmin wifiAdmin) {
        WifiConfiguration wcg = wifiAdmin.createWifiInfo(SSID, wifiPsw, type);
        wifiAdmin.addNetwork(wcg);
        return wifiAdmin.getmWifiManager().getConnectionInfo().getIpAddress();
    }

    private String connectDetail(ArrayList validChar, int validSize, int wifiLength, String SSID, WifiAdmin.WifiCipherType type, WifiAdmin wifiAdmin) {
        double totalTryTimes = Math.pow(validSize, wifiLength);
        for (int tryTimes = 0; tryTimes < totalTryTimes; tryTimes++) {
            String wifiPsw = "";
            for (int curLength = 0; curLength < wifiLength; curLength++) {
                Random rand = new Random();
                int tmpRand = Math.abs(rand.nextInt(validSize));
                wifiPsw += validChar.get(tmpRand);
            }
            int ip = connectWifi(SSID, wifiPsw, type, wifiAdmin);
            if (ip > 0) {
                return wifiPsw + ":" + ip + ":" + tryTimes;
            }
        }
        return "";
    }
}
