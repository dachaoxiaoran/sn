package sn.sn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * @author 王超
 */
public class Util {
	
	/**
	 * write text into a file
	 * @param path
	 * @param text
	 */
	public void writeFile(String path, String text) {
		FileOutputStream fileOutputStream = null;
		try {
			File file = new File(path);
			if (!file.exists()) file.createNewFile();
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(text.getBytes());
			fileOutputStream.flush();
		} catch(Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileOutputStream != null) fileOutputStream.close();
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * get text from url
	 * @param url
	 * @return text
	 */
	public String httpConn(String url) {
		StringBuilder buffer = new StringBuilder();
		try {
			URL u = new URL(url);
			URLConnection con = u.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
