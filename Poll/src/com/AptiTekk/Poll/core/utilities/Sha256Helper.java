package com.AptiTekk.Poll.core.utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256Helper {

	private static final String SALT = "POLLSYS";

	public static byte[] rawToSha(String raw) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update((raw + SALT).getBytes("UTF-8"));
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
