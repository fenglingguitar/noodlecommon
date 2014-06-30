package org.fengling.noodlecommon.util.net.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpConnect {

	public static void ftpDownload(final FtpInfo ftpInfo) throws FtpException {
    	
		doFtpSession(ftpInfo, new FtpTemplate() {

			@Override
			public void action(FTPClient ftp) throws FtpException {
				
				File localFile = new File(ftpInfo.getLocalPath() + ftpInfo.getLocalFileName());  
	            OutputStream outputStream = null;  
	            try {
	            	outputStream = new FileOutputStream(localFile); 
	            	ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
					if(!ftp.retrieveFile(ftpInfo.getFtpFileName(), outputStream)) {
	            		localFile.delete();
	            		throw new FtpException("Read ftp file false, FtpInfo: " + ftpInfo);
	            	}
	            } catch (FileNotFoundException e) {
	            	throw new FtpException("Ftp file not found, FtpInfo: " + ftpInfo + ", Exception: " + e.getMessage());
				} catch (IOException e) {
					throw new FtpException("Read ftp file false, FtpInfo: " + ftpInfo + ", Exception: " + e.getMessage());
				} finally {
	            	try {
	            		if (outputStream != null) {	            			
		            		outputStream.close();
	            		}
	            	} catch (IOException e) {
	            	}
	            }
			}
		});
    }
	
	public static void ftpUpload(final FtpInfo ftpInfo) throws FtpException {

		doFtpSession(ftpInfo, new FtpTemplate() {

			@Override
			public void action(FTPClient ftp) throws FtpException {
				
				File localFile = new File(ftpInfo.getLocalPath() + ftpInfo.getLocalFileName());  
	            FileInputStream inputStream = null; 
	            try {
	            	inputStream = new FileInputStream(localFile); 
	            	ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
	            	if(!ftp.storeFile(ftpInfo.getFtpFileName(), inputStream)) {
	            		throw new FtpException("Write ftp file false, FtpInfo: " + ftpInfo);
	            	}
	            } catch (FileNotFoundException e) {
	            	throw new FtpException("Ftp file not found, FtpInfo: " + ftpInfo + ", Exception: " + e.getMessage());
				} catch (IOException e) {
					throw new FtpException("Read ftp file false, FtpInfo: " + ftpInfo + ", Exception: " + e.getMessage());
				} finally {
	            	try {
	            		if (inputStream != null) {	            			
	            			inputStream.close();
	            		}
	            	} catch (IOException e) {
	            	}
	            }
			}
		});
    }
	
	public static void doFtpSession(FtpInfo ftpInfo, FtpTemplate ftpTemplate) throws FtpException {
		
    	FTPClient ftp = new FTPClient();
    	ftp.setConnectTimeout(ftpInfo.getFtpTimeout()); 
    	ftp.setDataTimeout(ftpInfo.getFtpTimeout());
    	ftp.setControlEncoding(ftpInfo.getEncoding());
    	
		try {
			ftp.connect(ftpInfo.getFtpUrl(), ftpInfo.getFtpPort());
			try {
				ftp.login(ftpInfo.getFtpUser(), ftpInfo.getFtpPasswd());
				
				if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
					throw new FtpException("Test isPositiveCompletion false, FtpInfo: " + ftpInfo);
				}
				
				try {
					if(!ftp.changeWorkingDirectory(ftpInfo.getFtpPath())) {
						if (!ftpInfo.isCreateDir()) {
							throw new FtpException("Change working directory false, FtpInfo: " + ftpInfo);
						} 
						String paths[] = ftpInfo.getFtpPath().split("/");
						for (String path : paths) {
							if (!path.equals(".") && !path.equals("")) {								
								/*if (!ftp.makeDirectory(path)) {
									throw new FtpException("Make directory false, FtpInfo: " + ftpInfo);
								}*/
								ftp.mkd(path);
								if (!ftp.changeWorkingDirectory(path)) {
									throw new FtpException("Change working directory false, FtpInfo: " + ftpInfo);
								}								
							}
						}
					}
					
					ftpTemplate.action(ftp);
					
				} catch (IOException e) {
					throw new FtpException("Deal working directory false, FtpInfo: " + ftpInfo + ", Exception: " + e);
				}
			} catch (IOException e) {
				throw new FtpException("Login ftp false, FtpInfo: " + ftpInfo + ", Exception: " + e);
			} finally {
				try {
					ftp.logout();
				} catch (IOException e) {
				}
			}
		} catch (IOException e) {
			throw new FtpException("Connect ftp false, FtpInfo: " + ftpInfo + ", Exception: " + e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static class FtpInfo {
		
		private String ftpUrl = "127.0.0.1";
	    private int ftpPort = 21;
	    private String ftpUser = "test";
	    private String ftpPasswd = "test";
	    private String ftpPath = "/";
	    private String ftpFileName = "test";
	    private String localPath = File.separator;
	    private String localFileName = "test";
	    private int ftpTimeout = 10000;
	    private String encoding = "UTF-8";
	    private boolean isCreateDir = true;
	    
		public String getFtpUrl() {
			return ftpUrl;
		}
		public void setFtpUrl(String ftpUrl) {
			this.ftpUrl = ftpUrl;
		}
		
		public int getFtpPort() {
			return ftpPort;
		}
		public void setFtpPort(int ftpPort) {
			this.ftpPort = ftpPort;
		}
		
		public String getFtpUser() {
			return ftpUser;
		}
		public void setFtpUser(String ftpUser) {
			this.ftpUser = ftpUser;
		}
		
		public String getFtpPasswd() {
			return ftpPasswd;
		}
		public void setFtpPasswd(String ftpPasswd) {
			this.ftpPasswd = ftpPasswd;
		}
		
		public String getFtpPath() {
			return ftpPath;
		}
		public void setFtpPath(String ftpPath) {
			if (!ftpPath.endsWith("/")) {
				ftpPath += "/";
			}
			this.ftpPath = ftpPath;
		}
		
		public String getFtpFileName() {
			return ftpFileName;
		}
		public void setFtpFileName(String ftpFileName) {
			this.ftpFileName = ftpFileName;
		}
		
		public String getLocalPath() {
			return localPath;
		}
		public void setLocalPath(String localPath) {
			if (!localPath.endsWith(File.separator)) {
				localPath += File.separator;
			}
			this.localPath = localPath;
		}
		
		public String getLocalFileName() {
			return localFileName;
		}
		public void setLocalFileName(String localFileName) {
			this.localFileName = localFileName;
		}
		
		public int getFtpTimeout() {
			return ftpTimeout;
		}
		public void setFtpTimeout(int ftpTimeout) {
			this.ftpTimeout = ftpTimeout;
		}
		
	    public String getEncoding() {
			return encoding;
		}
		public void setEncoding(String encoding) {
			this.encoding = encoding;
		}
		
		public boolean isCreateDir() {
			return isCreateDir;
		}
		public void setCreateDir(boolean isCreateDir) {
			this.isCreateDir = isCreateDir;
		}
		
		public String toString() {
			return (new StringBuilder())
					.append("[ftpUrl:")
					.append(ftpUrl)
					.append(", ftpPort:")
					.append(ftpPort)
					.append(", ftpUser:")
					.append(ftpUser)
					.append(", ftpPasswd:")
					.append(ftpPasswd)
					.append(", ftpPath:")
					.append(ftpPath)
					.append(", ftpFileName:")
					.append(ftpFileName)
					.append(", localPath:")
					.append(localPath)
					.append(", localFileName:")
					.append(localFileName)
					.append(", ftpTimeout:")
					.append(ftpTimeout)
					.append(", encoding:")
					.append(encoding)
					.append(", isCreateDir:")
					.append(isCreateDir)
					.append("]")
					.toString();
		}
	}
	
	public interface FtpTemplate {
		public void action(FTPClient ftp) throws FtpException;
	}
	
	public static class FtpException extends Exception {
		
		private static final long serialVersionUID = -4589562374004394963L;
		
		public FtpException(String message) {
			super(message);
		}
	}
}
