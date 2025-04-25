package net.dvdplay.aemcomm;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPTransferType;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.dvdplay.aem.Aem;
import net.dvdplay.aemcontroller.AEMGui;
import net.dvdplay.communication.DataPacketComposer;
import net.dvdplay.communication.NvPairSet;
import net.dvdplay.communication.RCSet;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.DvdplayLevel;
import net.dvdplay.request.PingRequest;
import net.dvdplay.task.TimeOutException;
import net.dvdplay.task.TimedExecute;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.PostMethod;

public class Comm {
   private static final String HTTP = "http://";
   private static final String HTTPS = "https://";
   private static final String ENCODING_UTF8 = "UTF-8";
   private static final String ENCODING_UTF16 = "UTF-16";
   private static final int BUFFER_SIZE = 4096;

   public static String parseRequestResponse(String aResponse) {
      String start = "RESULT=";
      String end = "</DVDPLAY-NVPAIR>";
      int index = aResponse.indexOf(start);
      if (index < 0) {
         throw new CommException("Could not parse RequestResponse: " + aResponse);
      } else {
         String sub = aResponse.substring(index);
         index = sub.indexOf(end);
         if (index < 0) {
            throw new CommException("Could not parse RequestResponse: " + aResponse);
         } else {
            String ret = sub.substring(start.length(), index + end.length());
            Aem.logDetailMessage(DvdplayLevel.FINER, ret);
            return ret;
         }
      }
   }

   public static String sendRequest(String aRequest, int aTimeOut) {
      return sendRequest(aRequest, Aem.getServerAddress(), aTimeOut);
   }

   public static String sendRequest(String aRequest) throws CommException {
      return sendRequest(aRequest, Aem.getServerAddress(), 60000);
   }

   public static String sendRequestNoTimeOut(String aRequest, String aServerAddress) throws CommException {
      return !AEMGui.mHttps ? sendHttpRequest(aRequest, aServerAddress) : sendHttpsRequest(aRequest, aServerAddress);
   }

   public static String sendRequest(String aRequest, String aServerAddress) {
      return sendRequest(aRequest, aServerAddress, 60000);
   }

   public static String sendRequest(String aRequest, String aServerAddress, int aTimeOut) throws CommException {
      try {
         Class[] lClass = new Class[]{
            String.class, String.class
         };
         Object[] lObj = new Object[]{aRequest, aServerAddress};
         Object lRet = TimedExecute.execute(
              Comm.class,
              "sendRequestNoTimeOut",
              lClass,
              lObj,
              aTimeOut
         );

         return (String)lRet;
      } catch (TimeOutException e) {
         Aem.setRequestError();
         Aem.logDetailMessage(DvdplayLevel.ERROR, e.getMessage());
         throw new TimeOutException("sendRequest failed");
      } catch (Exception e) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, e.getMessage());
         throw new CommException("sendRequest failed");
      }
   }

   private static synchronized HttpClient makeHttpClient(String targetUri) {
      HttpClient hc = null;

      try {
         String url = "";
         hc = new HttpClient();
         hc.setStrictMode(false);
         hc.setConnectionTimeout(15000);
         HostConfiguration hconf = new HostConfiguration();
         hconf.setHost(new URI(targetUri));
         hc.setHostConfiguration(hconf);
         return hc;
      } catch (URIException e) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "URIException caught " + e.getMessage());
         throw new CommException("makeHttpClientFailed.");
      }
   }

   private static String sendHttpsRequest(String aRequest, String aServerAddress) throws CommException {
      String response = "";
      HttpConnection connection = null;
      PostMethod pm = null;
      ObjectOutputStream oos = null;

      try {
         String URLString = "https://" + aServerAddress + "/AEMServer/servlet/AEMServer";
         Aem.logDetailMessage(DvdplayLevel.FINER, "URLString is " + URLString);
         pm = new PostMethod(URLString);
         HttpState state = new HttpState();
         HttpClient hc = makeHttpClient(URLString);
         connection = hc.getHttpConnectionManager().getConnection(hc.getHostConfiguration());
         connection.open();
         pm.addRequestHeader("Content-Type", "text/html");
         pm.addRequestHeader("Content-Encoding", "gzip");
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         GZIPOutputStream gzos = new GZIPOutputStream(baos, 4096);
         oos = new ObjectOutputStream(gzos);
         String msg = "COMMAND=" + aRequest;
         oos.writeObject(msg);
         oos.flush();
         oos.close();
         pm.setRequestBody(new ByteArrayInputStream(baos.toByteArray()));
         pm.execute(state, connection);
         String lEncoding = "";
         boolean lCompressMode = false;

         try {
            Header h1 = pm.getResponseHeader("Content-Encoding");
            lEncoding = h1.getValue();
         } catch (Exception e) {
            lCompressMode = false;
         }

         if (lEncoding != null) {
            Aem.logDetailMessage(DvdplayLevel.FINE, "Encoding is " + lEncoding);
            if (lEncoding.toUpperCase().indexOf("GZIP") != -1) {
               lCompressMode = true;
            }
         } else {
            Aem.logDetailMessage(DvdplayLevel.FINE, "Encoding is null");
         }

         InputStream responseStream = null;
         InputStream var26 = new GZIPInputStream(pm.getResponseBodyAsStream(), 4096);
         ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);

         int c;
         while ((c = var26.read()) != -1) {
            bos.write(c);
         }

         String lResponse = new String(bos.toByteArray(), "UTF-16");
         Aem.logDetailMessage(DvdplayLevel.FINER, lResponse);
         if (pm != null) {
            pm.releaseConnection();
         }

         if (connection != null && connection.isOpen()) {
            connection.close();
         }

         return lResponse;
      } catch (CommException e) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "CommException caught " + e.getMessage());
      } catch (MalformedURLException e) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "MalformedURLException caught " + e.getMessage());
      } catch (IOException e) {
         Aem.setServerConnectionStatus(false);
         Aem.logDetailMessage(DvdplayLevel.ERROR, "IOException caught " + e.getMessage());
      }

      if (pm != null) {
         pm.releaseConnection();
      }

      if (connection != null && connection.isOpen()) {
         connection.close();
      }

      throw new CommException("sendHttpsRequest failed");
   }

   private static String sendHttpRequest(String aRequest, String aServerAddress) throws CommException {
      try {
         String URLString = "http://" + aServerAddress + "/AEMServer/servlet/AEMServer";
         Aem.logDetailMessage(DvdplayLevel.FINER, "URLString is " + URLString);
         URL lURL = new URL(URLString);
         HttpURLConnection lHttpConn = (HttpURLConnection)lURL.openConnection();
         lHttpConn.setDoOutput(true);
         lHttpConn.setRequestProperty("Content-Type", "text/html");
         ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(lHttpConn.getOutputStream(), 4096));
         String msg = "COMMAND=" + aRequest;
         oos.writeObject(msg);
         oos.flush();
         oos.close();
         String lEncoding = lHttpConn.getContentEncoding();
         boolean lCompressMode = false;
         if (lEncoding != null) {
            Aem.logDetailMessage(DvdplayLevel.FINE, "Encoding is " + lEncoding);
            if (lEncoding.toUpperCase().indexOf("GZIP") != -1) {
               lCompressMode = true;
            }
         } else {
            Aem.logDetailMessage(DvdplayLevel.FINE, "Encoding is null");
         }

         InputStream lResponseStream = null;
         InputStream var16 = new GZIPInputStream(lHttpConn.getInputStream(), 4096);
         ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);

         int c;
         while ((c = var16.read()) != -1) {
            bos.write(c);
         }

         String lResponse = new String(bos.toByteArray(), "UTF-16");
         Aem.logDetailMessage(DvdplayLevel.FINER, lResponse);
         return lResponse;
      } catch (MalformedURLException e) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "MalformedURLException caught " + e.getMessage());
      } catch (IOException e) {
         Aem.setServerConnectionStatus(false);
         Aem.logDetailMessage(DvdplayLevel.ERROR, "IOException caught " + e.getMessage());
      }

      throw new CommException("sendHttpRequest failed");
   }

   public static void ping() throws CommException {
      ping(Aem.getServerAddress(), 0);
   }

   public static void ping(int aTimeOut) throws CommException {
      ping(Aem.getServerAddress(), aTimeOut);
   }

   public static void ping(String aAddress) throws CommException {
      ping(aAddress, 0);
   }

   public static void ping(String aAddress, int aTimeOut) throws CommException {
      if (!Aem.isStandAlone()) {
         try {
            String requeststring = new PingRequest(Aem.getAemId(), "2.0", "", Integer.toString(Aem.getAemStatus()), Aem.getAemAckRCSet()).getAsXmlString();
            long lTime = System.currentTimeMillis();
            lTime = lTime / 8L * 8L;
            String lStatusHex = Integer.toHexString(Aem.getAemStatus());
            String lHexLength = String.valueOf(lStatusHex.length());
            String lHex = Integer.toHexString(BigInteger.valueOf(lTime).intValue());
            if (lStatusHex.equalsIgnoreCase("0")) {
               lStatusHex = "";
               lHexLength = "0";
            }

            String lStatus = lHexLength + lStatusHex + lHex.substring(lStatusHex.length(), lHex.length());
            Aem.logSummaryMessage("Ping\t" + lStatus);
            Aem.logDetailMessage(DvdplayLevel.FINER, requeststring);
            String requestRet;
            if (aTimeOut > 0) {
               requestRet = sendRequest(requeststring, aAddress, aTimeOut);
            } else {
               requestRet = sendRequestNoTimeOut(requeststring, aAddress);
            }

            DataPacketComposer lDPC = new DataPacketComposer();
            NvPairSet lNvPairSet = lDPC.nvDeMarshal(requestRet);
            String lResult = lNvPairSet.getNvPair("Result").getValueAsString();
            if (Integer.parseInt(lResult) != 0) {
               throw new CommException(lNvPairSet.getNvPair("Message").getValueAsString());
            } else {
               boolean lSendAck = false;

               try {
                  for (int i = 0; i < lNvPairSet.size(); i++) {
                     String lServerCmd = "ServerCmd" + (i + 1);
                     int lIndex = lNvPairSet.findName(lServerCmd);
                     if (lIndex < 0) {
                        break;
                     }

                     RCSet lCmdRCSet = lNvPairSet.getNvPair(lIndex).getValueAsRCSet();
                     if (Aem.setServerCmdList(lCmdRCSet)) {
                        lSendAck = true;
                     }
                  }
               } catch (Exception var18) {
                  Aem.logDetailMessage(DvdplayLevel.WARNING, "Error reading server cmds: " + var18.getMessage(), var18);
               }

               if (lSendAck) {
                  ping();
               }

               Aem.clearAckRCSet();
            }
         } catch (DvdplayException var19) {
            Aem.logDetailMessage(DvdplayLevel.ERROR, var19.getMessage());
            throw new CommException("Ping Failed");
         }
      }
   }

   public static void downloadFile(String aRemoteFile, String aLocalFile) {
      try {
         Aem.logSummaryMessage("Downloading " + aRemoteFile);
         URL lURL = new URL("http://" + Aem.getServerAddress() + aRemoteFile.replaceAll(" ", "%20"));
         DataInputStream in = new DataInputStream(lURL.openStream());
         DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(aLocalFile)));
         byte[] bytes = new byte[1024];

         int len;
         while ((len = in.read(bytes)) > 0) {
            out.write(bytes, 0, len);
         }

         in.close();
         out.close();
         Aem.logSummaryMessage("Downloaded " + aLocalFile);
         return;
      } catch (MalformedURLException var7) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "MalformedURLException caught " + var7.getMessage());
      } catch (IOException var8) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "IOException caught " + var8.getMessage());
      }

      File lFile = new File(aLocalFile);
      if (lFile.exists() && !lFile.delete()) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, "Failed to delete " + aLocalFile);
      }

      throw new CommException("Failed to download " + aLocalFile);
   }

   public static void sendFile(String aLocalFile, String aRemoteDir) {
      int MAX_RETRIES = 3;
      int i = 0;

      for (i = 0; i < 3; i++) {
         try {
            Class[] lClass = new Class[]{
               String.class, String.class
            };
            Object[] lObj = new Object[]{aLocalFile, aRemoteDir};
            Object lRet = TimedExecute.execute(
               Comm.class,
               "sendFileHTTPAttempt",
               lClass,
               lObj,
               600000
            );
            break;
         } catch (TimeOutException var7) {
            Aem.setRequestError();
            Aem.logDetailMessage(DvdplayLevel.WARNING, "sendFile attempt " + (i + 1) + ": " + var7.getMessage());
         } catch (Exception var8) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "sendFile attempt " + (i + 1) + ": " + var8.getMessage());
         }
      }

      if (i >= 3) {
         throw new CommException("sendFile failed");
      }
   }

   public static void sendFileHTTPAttempt(String aLocalFile, String aRemoteDir) {
      String filename = aLocalFile;
      String boundary = "ThisBoundaryStringCanBeAnythingWhichDoesNotAppearInTheMessage";
      String twoHyphens = "--";
      String lineEnd = "\r\n";
      HttpURLConnection lHttpURLConn = null;
      int maxBufferSize = 65536;

      try {
         FileInputStream fileInputStream = new FileInputStream(new File(filename));
         URL lURL = new URL("http://" + Aem.getFTPAddress() + "/fileupload/FileUploadServlet");
         lHttpURLConn = (HttpURLConnection)lURL.openConnection();
         lHttpURLConn.setRequestMethod("POST");
         lHttpURLConn.setRequestProperty("Connection", "Keep-Alive");
         lHttpURLConn.setDoOutput(true);
         lHttpURLConn.setUseCaches(false);
         lHttpURLConn.setRequestProperty("Accept-Charset", "iso-8859-1,*,utf-8");
         lHttpURLConn.setRequestProperty("Accept-Language", "en");
         lHttpURLConn.setRequestProperty("Content-type", "multipart/form-data; boundary=" + boundary);
         DataOutputStream lDataOutStream = new DataOutputStream(lHttpURLConn.getOutputStream());
         lDataOutStream.writeBytes(twoHyphens + boundary + lineEnd);
         lDataOutStream.writeBytes("Content-Disposition: form-data;name=\"" + aRemoteDir + "\"; filename=\"" + filename + "\"" + lineEnd);
         lDataOutStream.writeBytes(lineEnd);
         int bytesAvailable = fileInputStream.available();
         int bufferSize = Math.min(bytesAvailable, maxBufferSize);
         byte[] buffer = new byte[bufferSize];

         for (int bytesRead = fileInputStream.read(buffer, 0, bufferSize); bytesRead > 0; bytesRead = fileInputStream.read(buffer, 0, bufferSize)) {
            lDataOutStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
         }

         lDataOutStream.writeBytes(lineEnd);
         lDataOutStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
         fileInputStream.close();
         lDataOutStream.flush();
         lDataOutStream.close();
      } catch (Exception var22) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var22.getMessage(), var22);
         throw new CommException("sendFileHTTPAttempt failed");
      }

      try {
         BufferedReader lBufferedRreader = new BufferedReader(new InputStreamReader(lHttpURLConn.getInputStream()));
         String input = "";
         String inputLine = "";

         while ((inputLine = lBufferedRreader.readLine()) != null) {
            input = input + inputLine;
         }

         lBufferedRreader.close();
         StringTokenizer lStrTok = new StringTokenizer(input, "&");
         String lResultString = lStrTok.nextToken();
         String lMessageString = lStrTok.nextToken();
         lStrTok = new StringTokenizer(lResultString, "=");
         lStrTok.nextToken();
         int lResult = Integer.parseInt(lStrTok.nextToken());
         if (lResult != 0) {
            lStrTok = new StringTokenizer(lMessageString, "=");
            lStrTok.nextToken();
            String lMessage = lStrTok.nextToken();
            throw new CommException("Request result is failure (result=" + lResult + ", message=" + lMessage + ").");
         }
      } catch (Exception var21) {
         Aem.logDetailMessage(DvdplayLevel.WARNING, var21.getMessage(), var21);
         throw new CommException("sendFileHTTPAttempt failed");
      }
   }

   public static void sendFileFTP(String aLocalFile, String aRemoteDir, String aRemoteFile) throws CommException {
      int MAX_RETRIES = 3;
      int i = 0;

      for (i = 0; i < 3; i++) {
         try {
            sendFileFTPAttempt(aLocalFile, aRemoteDir, aRemoteFile);
            break;
         } catch (CommException var6) {
            Aem.logDetailMessage(DvdplayLevel.WARNING, "sendFileFTP attempt " + (i + 1) + ": " + var6.getMessage());
         }
      }

      if (i >= 3) {
         throw new CommException("sendFileFTP failed");
      }
   }

   private static void sendFileFTPAttempt(String aLocalFile, String aRemoteDir, String aRemoteFile) throws CommException {
      try {
         String lServer = Aem.getFTPAddress();
         String lUser = "admin";
         String lPassword = "kiosk";
         FTPClient lClient = new FTPClient(lServer);
         long lFileSize = new File(aLocalFile).length();
         long lTimeout;
         if (lFileSize > 300000L) {
            lTimeout = 300000L;
         } else {
            lTimeout = lFileSize;
         }

         Aem.logDetailMessage(DvdplayLevel.FINE, "sendFileAttempt: timeout is " + lTimeout);
         lClient.setTimeout((int)lTimeout);
         lClient.login(lUser, lPassword);
         lClient.setType(FTPTransferType.BINARY);
         lClient.chdir(aRemoteDir);

         try {
            lClient.mkdir(Integer.toString(Aem.getAemId()));
         } catch (Exception var12) {
         }

         lClient.chdir(Integer.toString(Aem.getAemId()));
         lClient.put(aLocalFile, aRemoteFile);
      } catch (Exception var13) {
         Aem.logDetailMessage(Level.WARNING, var13.getMessage());
         throw new CommException("sendFileAttempt failed");
      }
   }

   public static void testFTP() {
      testFTP(Aem.getFTPAddress());
   }

   public static void testFTP(String aFTPAddress) {
      testFileUploadHTTP(aFTPAddress);
   }

   public static void testFileUploadHTTP(String aAddress) {
      try {
         String URLString = "http://" + aAddress + "/fileupload/FileUploadServlet";
         URL lURL = new URL(URLString);
         HttpURLConnection lHttpConn = (HttpURLConnection)lURL.openConnection();
         lHttpConn.setDoOutput(true);
         lHttpConn.setRequestProperty("Content-Type", "text/html");
         BufferedReader in = new BufferedReader(new InputStreamReader(lHttpConn.getInputStream()));
         String inputLine = "";
         String input = "";

         while ((inputLine = in.readLine()) != null) {
            input = input + inputLine;
         }

         in.close();
         return;
      } catch (MalformedURLException var7) {
         Aem.logDetailMessage(DvdplayLevel.ERROR, "MalformedURLException caught " + var7.getMessage());
      } catch (IOException var8) {
         Aem.setServerConnectionStatus(false);
         Aem.logDetailMessage(DvdplayLevel.ERROR, "IOException caught " + var8.getMessage());
      }

      throw new CommException("testFileUploadHTTP failed");
   }
}
