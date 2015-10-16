package webServer;

public class MimeTypeProcessor {

	private String FILE_EXTENSION;  

	public String getMimes(String FILE_MIME) {

		FILE_EXTENSION = FILE_MIME.substring(FILE_MIME.lastIndexOf(".") + 1, FILE_MIME.length());

		if (FILE_EXTENSION.equals("htm") || FILE_EXTENSION.equals("html")) return "text/html";
    		else if (FILE_EXTENSION.equals("gif")) return "image/gif";
    		else if (FILE_EXTENSION.equals("jpeg") || FILE_EXTENSION.equals("jpg") || FILE_EXTENSION.equals("jpe")) return "image/jpeg";
    		else if (FILE_EXTENSION.equals("png")) return "image/png";
    		else if (FILE_EXTENSION.equals("js")) return "application/x-javascript";
    		else if (FILE_EXTENSION.equals("bmp")) return "image/bmp";
    		else if (FILE_EXTENSION.equals("png")) return "image/png";
    		else if (FILE_EXTENSION.equals("tiff") || FILE_EXTENSION.equals("tif")) return "image/tiff";
		else return "text/plain";
	}
} 