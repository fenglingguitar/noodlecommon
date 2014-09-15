package org.fengling.noodlecommon.util.net.ftp;

import org.fengling.noodlecommon.util.net.ftp.FtpConnect;
import org.fengling.noodlecommon.util.net.ftp.FtpConnect.FtpException;
import org.fengling.noodlecommon.util.net.ftp.FtpConnect.FtpInfo;
import org.junit.Test;

public class FtpConnectTest {

	@Test
	public void testFtpDownload() throws FtpException {
		
		FtpInfo ftpInfoDownText = new FtpInfo();
		ftpInfoDownText.setFtpIp("127.0.0.1");
		ftpInfoDownText.setFtpPort(21);
		ftpInfoDownText.setFtpUser("ftpuser");
		ftpInfoDownText.setFtpPasswd("ftpuser");
		ftpInfoDownText.setLocalPath("D:\\Program Files\\QuickEasyFTPServer\\ftpuser-local");
		ftpInfoDownText.setLocalFileName("test.txt");
		ftpInfoDownText.setFtpPath("/");
		ftpInfoDownText.setFtpFileName("test.txt");
		FtpConnect.ftpDownload(ftpInfoDownText);
		System.out.println("Download test.txt success");
		
		FtpInfo ftpInfoDownImage = new FtpInfo();
		ftpInfoDownImage.setFtpIp("127.0.0.1");
		ftpInfoDownImage.setFtpPort(21);
		ftpInfoDownImage.setFtpUser("ftpuser");
		ftpInfoDownImage.setFtpPasswd("ftpuser");
		ftpInfoDownImage.setLocalPath("D:\\Program Files\\QuickEasyFTPServer\\ftpuser-local");
		ftpInfoDownImage.setLocalFileName("test1.jpg");
		ftpInfoDownImage.setFtpPath("/");
		ftpInfoDownImage.setFtpFileName("test1.jpg");
		FtpConnect.ftpDownload(ftpInfoDownImage);
		System.out.println("Download test1.jpg success");
	}

	@Test
	public void testFtpUpload() throws FtpException {
		
		FtpInfo ftpInfoUpText = new FtpInfo();
		ftpInfoUpText.setFtpIp("127.0.0.1");
		ftpInfoUpText.setFtpPort(21);
		ftpInfoUpText.setFtpUser("ftpuser");
		ftpInfoUpText.setFtpPasswd("ftpuser");
		ftpInfoUpText.setLocalPath("D:\\Program Files\\QuickEasyFTPServer\\ftpuser-local");
		ftpInfoUpText.setLocalFileName("test.txt");
		ftpInfoUpText.setFtpPath("/");
		ftpInfoUpText.setFtpFileName("test.txt");
		FtpConnect.ftpUpload(ftpInfoUpText);
		System.out.println("Upload test.txt success");
		
		FtpInfo ftpInfoUpImage = new FtpInfo();
		ftpInfoUpImage.setFtpIp("127.0.0.1");
		ftpInfoUpImage.setFtpPort(21);
		ftpInfoUpImage.setFtpUser("ftpuser");
		ftpInfoUpImage.setFtpPasswd("ftpuser");
		ftpInfoUpImage.setLocalPath("D:\\Program Files\\QuickEasyFTPServer\\ftpuser-local");
		ftpInfoUpImage.setLocalFileName("test2.jpg");
		ftpInfoUpImage.setFtpPath("/");
		ftpInfoUpImage.setFtpFileName("test2.jpg");
		FtpConnect.ftpUpload(ftpInfoUpImage);
		System.out.println("Upload test2.jpg success");
	}

}
