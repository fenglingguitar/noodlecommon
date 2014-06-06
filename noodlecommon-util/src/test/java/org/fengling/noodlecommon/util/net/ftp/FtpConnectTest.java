package org.fengling.noodlecommon.util.net.ftp;

import org.fengling.noodlecommon.util.net.ftp.FtpConnect;
import org.fengling.noodlecommon.util.net.ftp.FtpConnect.FtpException;
import org.fengling.noodlecommon.util.net.ftp.FtpConnect.FtpInfo;
import org.junit.Test;

public class FtpConnectTest {

	@Test
	public void testFtpDownload() throws FtpException {
		
		FtpInfo ftpInfoDownText = new FtpInfo();
		ftpInfoDownText.setLocalPath("D:");
		ftpInfoDownText.setLocalFileName("test.txt");
		ftpInfoDownText.setFtpFileName("test.txt");
		FtpConnect.ftpDownload(ftpInfoDownText);
		System.out.println("Download test.txt success");
		
		FtpInfo ftpInfoDownImage = new FtpInfo();
		ftpInfoDownImage.setLocalPath("D:");
		ftpInfoDownImage.setLocalFileName("Chrysanthemum.jpg");
		ftpInfoDownImage.setFtpFileName("Chrysanthemum.jpg");
		FtpConnect.ftpDownload(ftpInfoDownImage);
		System.out.println("Download Chrysanthemum.jpg success");
	}

	@Test
	public void testFtpUpload() throws FtpException {
		
		FtpInfo ftpInfoUpText = new FtpInfo();
		ftpInfoUpText.setLocalPath("D:");
		ftpInfoUpText.setLocalFileName("test.txt");
		ftpInfoUpText.setFtpFileName("test1.txt");
		FtpConnect.ftpUpload(ftpInfoUpText);
		System.out.println("Upload test1.txt success");
		
		FtpInfo ftpInfoUpImage = new FtpInfo();
		ftpInfoUpImage.setLocalPath("D:");
		ftpInfoUpImage.setLocalFileName("Chrysanthemum.jpg");
		ftpInfoUpImage.setFtpPath("/test2/test2/");
		ftpInfoUpImage.setFtpFileName("Chrysanthemum1.jpg");
		FtpConnect.ftpUpload(ftpInfoUpImage);
		System.out.println("Upload Chrysanthemum1.jpg success");
	}

}
