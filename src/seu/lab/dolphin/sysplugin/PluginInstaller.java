package seu.lab.dolphin.sysplugin;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import seu.lab.dolphin.sysplugin.ShellUtils.CommandResult;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class PluginInstaller {
	
	static String CPU_ABI;
	static final String TAG = "PluginInstaller";
	static{
		if(android.os.Build.CPU_ABI.startsWith("arm"))
			CPU_ABI = "armeabi";
		else CPU_ABI = android.os.Build.CPU_ABI;
	}
	
	public static void deleteFile(File file){
		if(file.isFile())file.delete();
		else if(file.isDirectory()){
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		file.delete();
	} 

	public static void copy(InputStream is, File root, String path) throws IOException {
		Log.i(TAG, is.available()+" copy to "+root.toString()+"/"+path);

		File target = new File(root,path);
		if(!target.exists())target.createNewFile();
		OutputStream os = new FileOutputStream(target);
        byte data[] = new byte[1024];
        int temp = 0;
        while ((temp = is.read(data)) != -1) {
    		Log.i(TAG, "temp "+temp);
        	os.write(data,0,temp);
        }
        os.flush();
		os.close();
		is.close();
	}
	
	public static boolean install(Context ctx){
		File root = new File("/storage/sdcard0/project_dolphin");
		if(root.exists()){
			deleteFile(root);
		}
		if(!root.mkdir()){
			Log.i(TAG, "mkdir()");
		}
		
		AssetManager am = ctx.getAssets();
		try {
			String[] strings = am.list("");
			for (int i = 0; i < strings.length; i++) {
				Log.i(TAG, strings[i]);
			}
			Log.i(TAG, "install "+CPU_ABI);
			copy(am.open("install.sh"),root,"install.sh");
			copy(am.open("uninstall.sh"),root,"uninstall.sh");
			copy(am.open("dolphincall_libs/"+CPU_ABI+"/dolphincall"),root,"dolphincall");
			copy(am.open("dolphinget_libs/"+CPU_ABI+"/dolphinget"),root,"dolphinget");
			copy(am.open("dolphinsu_libs/"+CPU_ABI+"/dolphinsu"),root,"dolphinsu");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ShellUtils shell = new ShellUtils();
		
		CommandResult result = shell.execCommand(
				"sh /storage/sdcard0/project_dolphin/install.sh", 
				ShellUtils.COMMAND_SU);
		for (int i = 0; i < result.successMsg.size(); i++) {
			Log.i(TAG, "successMsg:"+result.successMsg.get(i));
		}		
		for (int i = 0; i < result.errorMsg.size(); i++) {
			Log.i(TAG, "errorMsg:"+result.errorMsg.get(i));
		}		
		Log.i(TAG, "result: "+result.result);
		return true;
	}
	
	public static boolean uninstall(){
		ShellUtils shell = new ShellUtils();
		CommandResult result = shell.execCommand(
				"sh /storage/sdcard0/project_dolphin/uninstall.sh", 
				ShellUtils.COMMAND_SU);
		for (int i = 0; i < result.successMsg.size(); i++) {
			Log.i(TAG, "successMsg:"+result.successMsg.get(i));
		}		
		for (int i = 0; i < result.errorMsg.size(); i++) {
			Log.i(TAG, "errorMsg:"+result.errorMsg.get(i));
		}
		Log.i(TAG, "result: "+result.result);
		return true;
	}
}
