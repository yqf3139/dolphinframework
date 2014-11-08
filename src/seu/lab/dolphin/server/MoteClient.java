package seu.lab.dolphin.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class MoteClient {
	public static final int PORT = 13579;// 服务器端口号
	public static final String TAG = "MoteClient";

	public void toggle() {
		new Thread(){
			@Override
			public void run() {
				try {
					Socket socket = new Socket(UserPreferences.lightServer, PORT);
					socket.close();
				} catch (UnknownHostException e) {
					Log.e(TAG, e.toString());
					e.printStackTrace();
				} catch (IOException e) {
					Log.e(TAG, e.toString());
					e.printStackTrace();
				}
			}
		}.start();
	}

}