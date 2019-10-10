package NewTime.net.http.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileManager {

	public static byte[] getFileContent(File file) {
		if(!file.exists() || !file.isFile()) {
			return null;
		}
		try {
			byte[] buffer = new byte[(int) file.length()];		
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			in.read(buffer);
			in.close();
			return buffer;
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] getFileContent(String path) {
		File file = new File(path);
		return getFileContent(file);
	}
	
	public static byte[] getErrorFile(int error) {
		byte[] data = getFileContent("default/" + error + ".html");
		if(data != null) {
			return data;
		}
		return ("Error: " + error).getBytes();
	}
	
}
