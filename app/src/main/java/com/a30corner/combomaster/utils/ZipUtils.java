package com.a30corner.combomaster.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;

public class ZipUtils {
	
	public static boolean unpackZip(Context context, File file) {
		String path = context.getFilesDir().getAbsolutePath();
		if (!path.endsWith(File.separator)) {
			path = path + File.separator + "temp" + File.separator;
		} else {
			path = path + "temp" + File.separator;
		}
		return unpackZip(context, file, path);
	}

	public static boolean unpackZip(Context context, File file, String path) {
		File temp = new File(path);
		if ( !temp.exists() ) {
			temp.mkdirs();
		}
		if (!path.endsWith(File.separator)) {
		    path = path + File.separator;
		}
		
		try {
			FileInputStream is = new FileInputStream(file);
			return unpackZip(context, is, path);
		} catch (FileNotFoundException e) {
			return false;
		}
	}
	
	public static boolean unpackZip(Context context, InputStream is) {
		String path = context.getCacheDir().getAbsolutePath();
		if (!path.endsWith(File.separator)) {
		    path = path + File.separator;
		}
		return unpackZip(context, is, path);
	}
	
	public static boolean unpackZip(Context context, InputStream is, String path) {
		ZipInputStream zis = null;
		try {
			String filename;

			ZipEntry ze;
			byte[] buffer = new byte[1024];
			int count;
			zis = new ZipInputStream(new BufferedInputStream(is));
			while ((ze = zis.getNextEntry()) != null) {
				filename = ze.getName();

				// Need to create directories if not exists, or
				// it will generate an Exception...
				if (ze.isDirectory()) {
					File fmd = new File(path + filename);
					fmd.mkdirs();
					continue;
				}

				FileOutputStream fout = null;
				try {
					File f = new File(path + filename);
					if (f.exists()) {
						f.delete();
					} else {
					    File parent = f.getParentFile();
					    if (!parent.exists()) {
					        parent.mkdirs();
					    }
					}
					fout = new FileOutputStream(path + filename);

					while ((count = zis.read(buffer)) != -1) {
						fout.write(buffer, 0, count);
					}
				} finally {
					EasyUtil.close(fout);
					zis.closeEntry();
				}

			}
		} catch (IOException e) {
			LogUtil.e(e.getMessage());
			return false;
		} finally {
			EasyUtil.close(zis);
			EasyUtil.close(is);
		}
		return true;
	}
}
