import webServer.*;
import webServer.util.log;

import java.io.*;
import java.net.*;

class ThreadIt implements Runnable {
    private BufferedReader request;
    private PrintWriter response;
    private String serverRoot = ""; 
    private Socket sockIt;                 
    private String clientRequest = ""; 
    private String fileName = ""; 
    private String hostName = ""; 
    private int splitOne = 0;
    private int splitTwo = 0;

    public ThreadIt(
            Socket s, 
            String threadedRoot, 
            String threadedHome,
            String threadedLog) throws UnknownHostException, IOException {
        try {
            serverRoot = threadedRoot;
            sockIt = s;

            // Listen to the client
            request = new BufferedReader(new InputStreamReader(sockIt.getInputStream()));
            
            // Tell the client what's up
            response = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockIt.getOutputStream())), true);

            sockIt.getInetAddress();
            hostName = InetAddress.getLocalHost().toString();

            run(); // fire it up

        } catch (IndexOutOfBoundsException ie) {
            //handling Error :Bad Request
            log lg = new log();
            lg.LogEvent(threadedLog, "BAD REQUEST", 2);
        }
    }
    
    public synchronized void run() {
        try {
            request = new BufferedReader(new InputStreamReader(sockIt.getInputStream()));
            response = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockIt.getOutputStream())), true);
        } catch (IOException ioe) {
            // @TODO: something here.
        }
        try {
            //Getting The System File Separator ex : on windows \ ,on linux /
            String FILE_SEPARATOR = System.getProperty("file.separator");

            clientRequest = request.readLine();

            splitOne = clientRequest.indexOf("/") + 1;
            splitTwo = clientRequest.indexOf("HTTP") - 1;

            fileName = serverRoot + FILE_SEPARATOR + clientRequest.substring(splitOne, splitTwo).replace('/', '\\');

            System.out.println("[CLIENT REQUEST]: " + clientRequest);
            System.out.println("[REQUESTED FILE]: " + fileName);
            System.out.println("*******************************************************************************\n");
     
            webServer.FileProcessor f = new webServer.FileProcessor();
            
            f.ReadFile(fileName,hostName,sockIt.getOutputStream(),clientRequest.substring(splitOne,splitTwo));

        } catch (IOException ioe) {
            // @TODO - log this
        } finally {
            try {
                sockIt.close(); 
            } catch (IOException ioe) {}
        }
    }
} 

public class PracticalWebServer {

    private static int      SERVER_PORT;
    private static String   SERVER_ROOT;
    private static String   SERVER_HOMEPAGE;
    private static String   SERVER_LOG;

    public void getServerDirectives() throws IOException {

        SERVER_PORT     = Config.defaultPort;
        SERVER_ROOT     = Config.homeDirectory;
        SERVER_HOMEPAGE = Config.defaultHomepage;
        SERVER_LOG      = Config.logFile;
    }

    public void StartMessage() {
        
        System.out.println("[SERVER STATE]: STARTED...\n");
        
    }
    
    public static void main(String[] args) throws IOException {
        
        PracticalWebServer Practically = new PracticalWebServer();
        Practically.getServerDirectives(); 
        Practically.StartMessage(); 
        
        try { 
            ServerSocket S = new ServerSocket(SERVER_PORT);
            try {
                while (true) {
                    Socket sock = S.accept();
                    try {
                        System.out.println("[REMOTE HOST]: " + sock.getInetAddress().toString());
                        System.out.println("[ACTIVE LOCAL PORT]: " + sock.getPort());

                        new ThreadIt(sock, SERVER_ROOT, SERVER_HOMEPAGE, SERVER_LOG);

                    } catch (IOException e) {
                        sock.close(); // close the socket.
                    }
                }
            } finally {
                S.close(); // seriously, close it.
            }
        } catch (BindException B) {
            System.out.println("SERVER Already Running");
            System.exit(0);
        }
    }
} 