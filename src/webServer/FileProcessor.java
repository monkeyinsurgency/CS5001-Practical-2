package webServer;

import java.io.*;
import java.util.Date;
import webServer.MimeTypeProcessor;

public class FileProcessor {

    private int readAByte; 
    private byte[] buffer; 
    private String directoryName = ""; 
    private String MIME_TYPE = "";  

    public FileProcessor() {
        // Turn everything into bytes so images display correctly
        readAByte = 0; 
        buffer = new byte[1024]; 
    }

    public void setdirectoryName(String Dir) {
        directoryName = Dir;
    }

    public String getdirectoryName() {
        return directoryName;
    }

    /**
     * 404'd
     **/

    void FileNotFound(OutputStream OUTPUT_STREAM, String hostName) throws IOException {

        OUTPUT_STREAM.write(new String("<html>\r\n").getBytes());
        OUTPUT_STREAM.flush();
        OUTPUT_STREAM.write(new String("<Title>404 - File Not Found</Title>\r\n").getBytes());
        OUTPUT_STREAM.flush();
        OUTPUT_STREAM.write(new String("<body style='background: red;'>\r\n").getBytes());
        OUTPUT_STREAM.flush();
        OUTPUT_STREAM.write(new String("<h1 style='margin-top: 200px; color: white; text-align: center'>404 – Why did you do that, Karen?</h1>\r\n").getBytes());
        OUTPUT_STREAM.flush();
        OUTPUT_STREAM.write(new String("</html>" + "\r\n").getBytes());
        OUTPUT_STREAM.flush();
    }

    /**
     * Send the OK
     **/

    public void HttpResponseOK(OutputStream OUTPUT_STREAM, File FILE_LENGTH, String FILE_MIME) throws IOException {

        /** 
         * Go get the mime, Zed. 
         **/

        MimeTypeProcessor mim = new MimeTypeProcessor();
        MIME_TYPE = mim.getMimes(FILE_MIME);

        /** 
         * Send OK
         **/
        OUTPUT_STREAM.write(new String("HTTP/1.0 200 OK\r\n").getBytes());
        OUTPUT_STREAM.flush();
        OUTPUT_STREAM.write(new String("Date: " + new Date().toString() + "\r\n").getBytes());
        OUTPUT_STREAM.flush();
        OUTPUT_STREAM.write(new String("Accept-Ranges: bytes\r\n").getBytes());
        OUTPUT_STREAM.flush();
        OUTPUT_STREAM.write(new String("content-length: " + String.valueOf(FILE_LENGTH.length()) + "\r\n").getBytes());
        OUTPUT_STREAM.flush();
        OUTPUT_STREAM.write(new String("Content-Type: " + MIME_TYPE + "\r\n").getBytes());
        OUTPUT_STREAM.flush();
        OUTPUT_STREAM.write(new String("\r\n").getBytes());
        OUTPUT_STREAM.flush();
    }

    public void ReadFile(String fileName, String hostName, OutputStream OUTPUT_STREAM, String mimeType) throws IOException {

        File f = new File(fileName);

        if (!f.exists()) {
            this.FileNotFound(OUTPUT_STREAM, hostName);
        } else {
            if (f.canRead()) {
                FileInputStream FIS = new FileInputStream(fileName);
                this.HttpResponseOK(OUTPUT_STREAM, f, mimeType);
                while ((readAByte = FIS.read(buffer, 0, buffer.length)) != -1) {
                    OUTPUT_STREAM.write(buffer, 0, readAByte);
                    OUTPUT_STREAM.flush();
                }
                FIS.close();
            }
        }
    }
}