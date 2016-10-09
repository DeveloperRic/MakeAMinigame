package com.rictacius.makeAMinigame.util.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.rictacius.makeAMinigame.util.Log;

public class ConfigUtil {

	public ConfigUtil() {
	}

	public static File outputClassFile(Class<?> root, String source, File folder, String path) {
		String send = folder.getPath() + File.separator + path;
		return outputClassFile(root, source, send);
	}

	public static File outputClassFile(Class<?> root, String source, String path) {
		InputStream stream = root.getResourceAsStream(source);
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(path));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = stream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			System.out.println("Done!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		File send = new File(path);
		if (!send.exists()) {
			Log.log(ConfigUtil.class,
					"Could not output classfile {" + source + "} in {" + root.getName() + "} to {" + path + "}",
					Log.Level.FATAL);
			return null;
		}
		return send;
	}

}
