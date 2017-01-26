package com.wander.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtils {
	
	public static void Serialize(String path, Object obj) throws IOException {
		File dir = new File(path).getParentFile();
		if (!dir.exists())
			dir.mkdirs();
		
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
	        fos = new FileOutputStream(path);
	        oos = new ObjectOutputStream(fos);
	       
	        oos.writeObject(obj);
	        oos.flush();
		} finally {
			if (fos != null)
				fos.close();
			if (oos != null)
				oos.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T Deserialize(String path) throws IOException, ClassNotFoundException {
		File file = new File(path);
		if (!file.exists())
			return null;

		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try {
			fis = new FileInputStream(path);
			ois = new ObjectInputStream(fis);
			return (T)ois.readObject();
		} finally {
			if (fis != null)
				fis.close();
			if (ois != null)
				ois.close();
		}

	}
}
