package com.ycnet.autowifi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ycnet.autowifi.util.WifiAdmin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.IBinder;
import android.os.SystemClock;

public class ConnectWifiService  extends Service{
	@Override
	public IBinder onBind(Intent intent){
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent,int flags,int startId){
		new Thread(new Runnable(){
			@Override
			public void run(){
				processConnect();
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * wifi密码：8-63个ASCII码字符或8-64个十六进制字符
	 * 商家常用爲數字與字符
	 * 48~57爲数字
	 * 65~90为大写字母
	 * 97~122为小写字母
	 */
	private void processConnect(){
		String wifiPsw = "";
		ArrayList validChar = new ArrayList(); 
		char tmp;
		
		for(int i=48; i<=57; i++){
			tmp = (char)i;
			validChar.add(tmp);
		}
		
		for(int i=65; i<=90; i++){
			tmp = (char)i;
			validChar.add(tmp);
		}
		
		for(int i=97; i<=122; i++){
			tmp = (char)i;
			validChar.add(tmp);
		}
		int validSize = validChar.size();
		int tmpRand;
		for(int wifiLength=8; wifiLength<64; wifiLength++){
			int totalTryTimes = (int) Math.pow(validSize, wifiLength);
			for(int tryTimes = 0; tryTimes < totalTryTimes; tryTimes++){
				wifiPsw = "";
				for(int curLength=0; curLength<wifiLength; curLength++){
					Random rand= new Random();
					tmpRand = Math.abs(rand.nextInt()) ;
					wifiPsw += validChar.get(tmpRand % ( tmpRand - 1) );
				}
				
				CreateWifiInfo("newhome", wifiPsw, WifiCipherType.WIFICIPHER_WPA); 
			}
		}
	}
	
	private List<ScanResult> getWifList(){
		WifiAdmin wifiAdmin = new WifiAdmin();
		wifiAdmin->getWifiList();
	}
}
