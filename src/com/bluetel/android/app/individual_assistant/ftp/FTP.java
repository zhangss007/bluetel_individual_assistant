package com.bluetel.android.app.individual_assistant.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * ftp下载类
 * @author zss
 *
 * 2013-8-29
 * bluetel-android-app-broadcast
 */
public class FTP extends Thread{
	
	
	private Handler handler ;
	private String hostName ;
	private static final String userName = "monitor" ;
	private static final String passwd = "123456" ;
	private List<FTPFile> remoteFiles ;
	private List<String> remoteFileNames ;
	public static final String REMOTE_PATH = "broadsound" ;
	private FTPClient ftpClient ;
	private String currentPath = "" ;
	
	/**
	 * ftp登录成功
	 */
	public static final String FTP_LOGIN_SUCCESS = "FTP_LOGIN_SUCCESS" ;
	/**
	 * ftp登录失败
	 */
	public static final String FTP_LOGIN_ERROR = "FTP_LOGIN_ERROR" ;
	/**
	 * ftp文件下载成功
	 */
	public static final String FTP_RETRIVE_SUCESS = "FTP_RETRIVE_SUCESS" ;
	/**
	 * ftp文件上传成功
	 */
	public static final String FTP_STORE_SUCCESS = "FTP_STORE_SUCCESS" ;
	
	/**
	 *创建ftp上传、下载任务
	 *STORE 上传
	 *RETRIEVE下载 
	 */
	public static final String FTP_STORE = "STORE" ;
	public static final String FTP_RETRIEVE = "RETRIEVE" ;
	
	private String ftpTaskType ;
	
	/**
	 * 流量统计
	 */
	private int response ;
	
    private File file ;
    private String filePath ; 
    
	/**
	 * 创建构造函数
	 * @param hostName
	 */
	public FTP(Handler handler,String hostName) {
	
		super();
		this.handler = handler ;
		this.hostName = hostName;
		this.remoteFiles = new ArrayList<FTPFile>() ;
		this.remoteFileNames = new ArrayList<String>() ;
	}
	
	
	
	@Override
	public void run() {

		super.run();
		try {
			
			ftpClient = new FTPClient() ;			
			openConnect() ;
			
			if (ftpTaskType.equals(FTP_RETRIEVE)){
				
				//进行ftp下载操作
				FTPFile[] files = ftpClient.listFiles(REMOTE_PATH);
				
				for(FTPFile  file:files){
					
					remoteFiles.add(file) ;
					remoteFileNames.add(file.getName()) ;
				}
				handlerMessage(FTP_RETRIVE_SUCESS) ;
			}else if (ftpTaskType.equals(FTP_STORE)){
				
				//进行ftp上传操作
				Result result = null;
		        try {
		            // 上传
		            result = uploading(file,filePath);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        if (result.isSucceed()) {
		            Log.i("TAG", "uploading ok...time:" + result.getTime() + " and size:" + result.getReponse());
		            //Toast.makeText(FTPActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
		            handlerMessage(FTP_STORE_SUCCESS) ;
		            
		        } else {
		            Log.e("TAG", "uploading fail");
		            //Toast.makeText(FTPActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
		        }
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	private void openConnect() throws IOException{
		
			//utf-8
		 	ftpClient.setControlEncoding("gb2312");
	        int reply; // 服务器响应值
	        // 连接至服务器
	        ftpClient.connect(hostName);
	        // 获取响应值
	        reply = ftpClient.getReplyCode();
	        if (!FTPReply.isPositiveCompletion(reply)) {
	            // 断开连接
	            ftpClient.disconnect();
	            throw new IOException("connect fail: " + reply);
	        }
	        // 登录到服务器
	        ftpClient.login(userName, passwd);
	        // 获取响应值
	        reply = ftpClient.getReplyCode();
	        if (!FTPReply.isPositiveCompletion(reply)) {
	            // 断开连接
	            ftpClient.disconnect();
	            throw new IOException("connect fail: " + reply);
	        } else {
	            // 获取登录信息
	            FTPClientConfig config = new FTPClientConfig(ftpClient.getSystemType().split(" ")[0]);
	            //zh
	            config.setServerLanguageCode("gb2312");
	            ftpClient.configure(config);
	            // 使用被动模式设为默认
	            ftpClient.enterLocalPassiveMode();
	            // 二进制文件支持
	            ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
	            System.out.println("login");
	            handlerMessage(FTP_LOGIN_SUCCESS) ;
	        }
	}
	
	private void handlerMessage(String loginStatus){
		
		Message message = new Message() ;
		message.what = 0x222 ;
		message.obj = loginStatus ;
		handler.sendMessage(message) ;
	}

	
	/**
	 * 
	 *关闭ftp服务
	 * @throws IOException 
	 */
	public void closeConnect() throws IOException{
	
		if(ftpClient != null){
			
			//登出ftp 
			ftpClient.logout() ;
			//关闭ftp连接
			ftpClient.disconnect() ;
		}
	}
	


	/**
	 * 设置ftp执行任务类型
	 * @param ftpTaskType
	 */
	public void setFtpTaskType(String ftpTaskType) {
		
		this.ftpTaskType = ftpTaskType;
	}



	public List<FTPFile> getRemoteFiles() {
		
		return remoteFiles;
	}
	
	
	
	public List<String> getRemoteFileNames() {
		
		return remoteFileNames;
	}



	/**
	 * 设置上传文件的信息
	 * @param file
	 * @param filePath
	 */
	public void setStoreFileInfo(File file, String filePath){
		
		this.file  = file ;
		this.filePath = filePath ;
	}
	
	/**
     * 上传.
     * @param localFile 本地文件
     * @param remotePath FTP目录
     * @return Result
     * @throws IOException 
     */
    public Result uploading(File localFile, String remotePath) throws IOException {
        boolean flag = true;
        Result result = null;
        // 初始化FTP当前目录
        currentPath = remotePath;
        // 初始化当前流量
        response = 0;
        // 二进制文件支持
        ftpClient.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        // 使用被动模式设为默认
        ftpClient.enterLocalPassiveMode();
        // 设置模式
        ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
        // 改变FTP目录
        ftpClient.changeWorkingDirectory(REMOTE_PATH);
        // 获取上传前时间
        Date startTime = new Date();
        if (localFile.isDirectory()) {
            // 上传多个文件
            flag = uploadingMany(localFile);
        } else {
            // 上传单个文件
            flag = uploadingSingle(localFile);
        }
        // 获取上传后时间
        Date endTime = new Date();
        // 返回值
        result = new Result(flag, Util.getFormatTime(endTime.getTime() - startTime.getTime()), Util.getFormatSize(response));
        return result;
    }

    /**
     * 上传单个文件.
     * @param localFile 本地文件
     * @return true上传成功, false上传失败
     * @throws IOException 
     */
    private boolean uploadingSingle(File localFile) throws IOException {
        boolean flag = true;
        // 创建输入流
        InputStream inputStream = new FileInputStream(localFile);
        // 统计流量
        response += (double) inputStream.available() / 1;
        // 上传单个文件
        flag = ftpClient.storeFile(localFile.getName(), inputStream);
        // 关闭文件流
        inputStream.close();
        return flag;
    }

    /**
     * 上传多个文件.
     * @param localFile 本地文件夹
     * @return true上传成功, false上传失败
     * @throws IOException 
     */
    private boolean uploadingMany(File localFile) throws IOException {
        boolean flag = true;
        // FTP当前目录
        if (!currentPath.equals(REMOTE_PATH)) {
            currentPath = currentPath + REMOTE_PATH + localFile.getName();
        } else {
            currentPath = currentPath + localFile.getName();
        }
        // FTP下创建文件夹
        ftpClient.makeDirectory(currentPath);
        // 更改FTP目录
        ftpClient.changeWorkingDirectory(currentPath);
        // 得到当前目录下所有文件
        File[] files = localFile.listFiles();
        // 遍历得到每个文件并上传
        for (File file : files) {
            if (file.isHidden()) {
                continue;
            }
            if (file.isDirectory()) {
                // 上传多个文件
                flag = uploadingMany(file);
            } else {
                // 上传单个文件
                flag = uploadingSingle(file);
            }
        }
        return flag;
    }
}
