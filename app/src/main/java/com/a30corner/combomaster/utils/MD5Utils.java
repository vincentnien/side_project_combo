package com.a30corner.combomaster.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	public static String getMD5(File updateFile) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LogUtil.e(e.toString());
			return null;
		}

		InputStream is = null;
		try {
			is = new FileInputStream(updateFile);
		} catch (FileNotFoundException e) {
			LogUtil.e(e.toString());
			return null;
		}

		byte[] buffer = new byte[8 * 1024];
		int read;
		try {
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			String output = bigInt.toString(16);
			output = String.format("%32s", output).replace(' ', '0');
			return output;
		} catch (IOException e) {
			LogUtil.e(e, e.toString());
		} finally {
			EasyUtil.close(is);
		}
		return null;
	}
}
