package net.dvdplay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptString {
   MessageDigest md;
   String mPassPhrase = "DVDPlay2Go";

   public EncryptString() {
      try {
         this.md = MessageDigest.getInstance("SHA-1");
      } catch (NoSuchAlgorithmException var2) {
         System.err.println("Error using MD5 algorithm");
      }
   }

   public String encryptIt(String data) {
      byte[] passByte = this.mPassPhrase.getBytes();
      byte[] dataByte = data.getBytes();
      this.md.update(passByte);
      this.md.update(dataByte);
      byte[] digested = this.md.digest();
      return new String(digested);
   }

   public String encodeBase64(String data) {
      byte[] dataByte = data.getBytes();
      return Base64.getEncoder().encodeToString(dataByte);
   }

   public String getEncryptedString(String data) {
      String hash = this.encryptIt(data);
      return this.encodeBase64(hash);
   }

   public String decodeBase64(String data) {
      try {
         byte[] decodedBytes = Base64.getDecoder().decode(data);
         return new String(decodedBytes, StandardCharsets.UTF_8);
      } catch (IllegalArgumentException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static void main(String[] args) {
      EncryptString gp = new EncryptString();
      String[] lPlainString = null;

      try {
         if (args.length <= 0) {
            BufferedReader lIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please enter the password here : ");
            lPlainString = new String[]{lIn.readLine()};
         } else if (args.length >= 1) {
            lPlainString = new String[args.length];
            lPlainString = args;
         } else {
            System.err.println();
            System.err.println("Usage : java EncryptString [plain-text-password]");
            System.err.println();
            System.exit(0);
         }

         for (int i = 0; i < lPlainString.length; i++) {
            System.out.println("EncryptedString for [" + lPlainString[i] + "] is [" + gp.getEncryptedString(lPlainString[i].trim()) + "]");
         }
      } catch (IOException var4) {
         System.out.println("Error getting input");
      }
   }
}
