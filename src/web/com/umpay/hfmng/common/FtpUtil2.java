package com.umpay.hfmng.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;


public class FtpUtil2 extends FTPClient {
	private static final Logger _log = Logger.getLogger(FtpUtil2.class);
    
    private int timeout = 0;
    
    /**
     * 默认构造函数
     *
     */
    public FtpUtil2() {
        super();
    }
    
    /**
     * 构造函数 指定服务器
     * @param server ftp服务器
     * @throws SocketException
     * @throws IOException
     */
    public FtpUtil2(String server) throws SocketException, IOException {
        super();
        connect(server);
    }
    
    /**
     * 构造函数 指定服务器和端口
     * @param server ftp服务器
     * @param port ftp端口
     * @throws SocketException
     * @throws IOException
     */
    public FtpUtil2(String server, int port) throws SocketException, IOException {
        super();
        connect(server, port);
    }
    
    /**
     * 构造函数 指定服务器,登陆用户名和口令
     * @param server 服务器
     * @param username 用户名
     * @param password 口令
     * @throws SocketException
     * @throws IOException
     */
    public FtpUtil2(String server, String username, String password) throws SocketException, IOException {
        super();
        connect(server);
        login(username, password);
    }
    
    /**
     * 构造函数 指定服务器和端口,登陆用户名和口令
     * @param server 服务器
     * @param port 端口
     * @param username 用户名
     * @param password 口令
     * @throws SocketException
     * @throws IOException
     */
    public FtpUtil2(String server, int port, String username, String password) throws SocketException, IOException {
        super();
        connect(server, port);
        login(username, password);
    }
    
    /**
     * 设置超时时间(秒)
     * @param millisec
     */
    public void setTimeout(int sec) {
        timeout = sec * 1000;
        super.setDefaultTimeout(timeout);
        super.setDataTimeout(timeout);
        _log.info("setTimeout("+sec+") ok.");
    }
    
    /**
     * 连接服务器
     * @param server 服务器地址
     * @throws SocketException
     * @throws IOException
     */
    public void connect(String server) throws SocketException, IOException {
        super.connect(server);
    }
    
    /**
     * 连接服务器
     * @param server 服务器地址
     * @param port 服务器端口
     * @throws SocketException
     * @throws IOException
     */
    public void connect(String server, int port) throws SocketException, IOException {
        super.connect(server, port);
        _log.info("connect("+server+", "+port+") ok.");
        setSoTimeout(timeout);
    }
    
    /**
     * 匿名登陆
     * @throws IOException
     */
    public void login() throws IOException {
        boolean ret = super.login("anonymous", "anonymous@anonymous.com");
        if (ret) {
            _log.info("login(anonymous) ok.");
        } else {
            throw new IOException("login(anonymous) fail.");
        }
    }

    /**
     * 登陆服务器
     * @param username 用户名
     * @param password 口令
     * @return
     * @throws IOException
     */
    public boolean login(String username, String password) throws IOException {
        boolean ret = super.login(username, password);
        if (ret) {
            _log.info("login("+username+", "+password+") ok.");
        } else {
            throw new IOException("login(" + username + ", " + password + ") fail.");
        }
        return ret;
    }

    /**
     * 切换工作目录
     * @param path
     * @throws IOException
     */
    public void cd(String path) throws IOException {
        if (path != null && path.length() > 0) {
            boolean ret = changeWorkingDirectory(path);
            if (ret) {
                _log.info("cd("+path+") ok.");
            } else {
                throw new IOException("cd(" + path + ") fail.");
            }
        }
    }
    
    /**
     * 设置以BINARY方式传送文件
     * @throws IOException
     */
    public void bin() throws IOException {
        boolean ret = setFileType(BINARY_FILE_TYPE);
        if (ret) {
            _log.info("bin() ok.");
        } else {
            throw new IOException("bin() fail.");
        }
    }
    
    /**
     * 设置以ASCII方式传送文件
     * @throws IOException
     */
    public void asc() throws IOException {
        boolean ret = setFileType(ASCII_FILE_TYPE);
        if (ret) {
            _log.info("asc() ok.");
        } else {
            throw new IOException("asc() fail.");
        }
    }
    
    /**
     * 从服务器上下载指定文件
     * @param remote 远程文件名
     * @throws IOException
     */
    public void get(String remote) throws IOException {
        get(remote, remote);
    }
    
    /**
     * 从服务器上下载指定文件到指定的本地目录，或指定的本地文件名
     * @param remote 远程文件名
     * @param local 本地目录或文件名
     * @throws IOException
     */
    public void get(String remote, String local) throws IOException {
        boolean ret = false;
        File file = new File(local);
        if (file.isDirectory()) {
            file = new File(local + File.separatorChar + remote);
        }
        FileOutputStream fos = new FileOutputStream(file);
        ret = retrieveFile(remote, fos);
        fos.close();
        if (ret) {
            _log.info("get("+ remote+ ", "+ local + File.separatorChar + remote + ") ok.");
        } else {
            throw new IOException("get(" + local + File.separatorChar + remote + ") fail.");
        }
    }
    
    /**
     * 从服务器上下载多个文件
     * @param remote 远程通配文件名(* - 任意个任意字符；? - 任意一个字符)
     * @throws IOException
     */
    public void mget(String remote) throws IOException {
        mget(remote, ".");
    }
    
    /**
     * 从服务器上下载多个文件到指定的本地目录 
     * @param remote 远程通配文件名(* - 任意个任意字符；? - 任意一个字符)
     * @param local_path 本地目录 
     * @throws IOException
     */
    public void mget(String remote, String local_path) throws IOException {
        FTPFile[] files = listFiles(remote);
        for (int i = 0; i < files.length; i++) {
            FileOutputStream fos = new FileOutputStream(local_path + File.separatorChar + files[i].getName());
            boolean ret = retrieveFile(files[i].getName(), fos);
            fos.close();
            if (ret) {
                _log.info("mget("+ remote+ ", "+ local_path+ ") ok.");
            } else {
                throw new IOException("mget(" + remote + ", " + local_path + ") fail.");
            }
        }
        _log.info("mget("+ remote+ ", "+ local_path+ ") not found file.");
    }
    
    /**
     * 上传本地文件到服务器
     * @param local 本地文件名
     * @throws IOException
     */
    public void put(String local) throws IOException {
        put(local, local);
    }
    
    /**
     * 上传本地文件到服务器，并更名
     * @param local 本地文件名
     * @param remote 远程文件名
     * @throws IOException
     */
    public void put(String local, String remote) throws IOException {
        boolean ret = false;
        FileInputStream fis = new FileInputStream(local);
        ret = storeFile(remote, fis);
        fis.close();
        if (ret) {
            _log.info("put("+ local+ ", "+ remote+ ") ok.");
        } else {
            throw new IOException("put(" + local + ", " + remote + ") fail.");
        }
    }
    
    /**
     * 上传指定的若干本地文件到服务器
     * @param local 本地通配文件名(* - 任意个任意字符；? - 任意一个字符)或目录
     * @throws IOException
     */
    public void mput(String local) throws IOException {
        File dir = new File(local);
        File files[];
        if (dir.isDirectory()) {
            files = dir.listFiles();
        } else {
            if (!dir.isAbsolute()) {
                dir = dir.getAbsoluteFile();
            }
            String filename = dir.getName();
            dir = dir.getParentFile();
            files = dir.listFiles(new FTPLocalFileFilter(filename));
        }
        for (int i = 0; i < files.length; i++) {
            FileInputStream fis = new FileInputStream(files[i]);
            boolean ret = storeFile(files[i].getName(), fis);
            fis.close();
            if (ret) {
                _log.info("mput("+ local+ ") ok.");
            } else {
                throw new IOException("mput(" + local + ") fail.");
            }
        }
        _log.info("mput("+ local+ ") not found file.");
    }
    
    /**
     * 结束ftp会话
     * @return
     */
    public boolean bye() {
        if (!isConnected()) {
            return true;
        }
        boolean ret = false;
        try {
            ret = logout();
            disconnect();
        } catch (IOException e) { }
        return ret;
    }
    
    public static void main(String[] args) throws IOException {
//    	FtpUtil2 ftp = new FtpUtil2();
//        ftp.connect("221.179.56.6",21);
//        ftp.login("remotepay", "20091231%$#");
//        ftp.cd("/");
//		ftp.put("C:/TRAN_FEE_888001900010004_01210100_20091207.dat","TRAN_FEE_888001900010004_01210100_20091207.dat");
//        ftp.bye();
    	FtpUtil2 ftp = new FtpUtil2();
    	ftp.setTimeout(600);
		ftp.connect("192.168.1.252", 21);
		ftp.login("mpsp", "mpsp4321");
		ftp.enterLocalPassiveMode();
		// 进入存放文件的ftp路径
		ftp.cd("/usr/mpsp/hfStlSvr/hfdata/payback");
		ftp.bin();
		String localpath = "D:/log/hfMngBusi/";
		// 若指定路径不存在，则创建
		File localpathdir = new File(localpath);
		if (!localpathdir.exists()) {
			boolean flag = localpathdir.mkdirs();
			if (flag) {
				System.out.println("退费明细文件存放指定路径不存在, 创建成功!");
			} else {
				System.out.println("退费明细文件存放指定路径不存在, 创建失败!");
				return;
			}
		}
		ftp.get("payback201311.xls", localpath);
		ftp.bye();
    	
    }
    
    
}
