package seu.lab.dolphin.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MoteClient {
	public static final String IP_ADDR = "192.168.1.162";// 服务器地址
	public static final int PORT = 13579;// 服务器端口号

	public void toggle() {
		new Thread(){
			@Override
			public void run() {
				try {
					Socket socket = new Socket(IP_ADDR, PORT);
					socket.close();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

}