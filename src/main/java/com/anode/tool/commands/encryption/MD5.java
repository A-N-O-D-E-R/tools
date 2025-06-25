package com.anode.tool.commands.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {

    private MD5() {}
  
    /**
     * Generates an MD5 hash from the given input string.
     *
     * @param  input  the string to be hashed
     * @return        the MD5 hash as a string
     */
    public static String getMd5(String input) {
      try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) hashtext = "0" + hashtext;
        return hashtext;
      } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }
    }
  }
  
