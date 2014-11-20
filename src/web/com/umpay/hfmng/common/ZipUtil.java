/*
 * Project Name：fileService 
 * Copyright:UMPAY
 * Description：
 */
package com.umpay.hfmng.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

/**
 *
 * @author Ding Zhe
 * @since Apr 10, 2010 2:22:18 PM
 */
public class ZipUtil {
	public static Logger log = Logger.getLogger(ZipUtil.class);
	
	protected static byte[] buf = new byte[1024];

	/**
	 * 私有构造函数防止被构建
	 */
	private ZipUtil() {

	}

	/**
	 * 遍历目录并添加文件.
	 * 
	 * @param jos -JAR 输出流
	 * @param file -目录文件名
	 * @param pathName -ZIP中的目录名
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void recurseFiles(final JarOutputStream jos,
			final File file, final String pathName) throws IOException,
			FileNotFoundException {
		//文件夹则往下遍历
		if (file.isDirectory()) {
			final String sPathName = pathName + file.getName() + "/";
			jos.putNextEntry(new JarEntry(sPathName));
			final String[] fileNames = file.list();
			if (fileNames != null) {
				for (int i = 0; i < fileNames.length; i++) {
					recurseFiles(jos, new File(file, fileNames[i]), sPathName);
				}

			}
		}
		//读取文件到ZIP/JAR文件条目
		else {
			//使用指定名称创建新的 ZIP/JAR 条目
			final JarEntry jarEntry = new JarEntry(pathName + file.getName());
			final FileInputStream fin = new FileInputStream(file);
			final BufferedInputStream in = new BufferedInputStream(fin);
			//开始写入新的 ZIP 文件条目并将流定位到条目数据的开始处。
			jos.putNextEntry(jarEntry);

			int len;
			while ((len = in.read(buf)) >= 0) {
				//将字节数组写入当前 ZIP 条目数据
				jos.write(buf, 0, len);
			}

			in.close();
			// 关闭当前 ZIP 条目并定位流以写入下一个条目
			jos.closeEntry();
		}
	}

	/**
	 * 创建 ZIP/JAR 文件.
	 * 
	 * @param directory -要添加的目录
	 * @param zipFile -保存的 ZIP 文件名
	 * @param zipFolderName -ZIP 中的路径名
	 * @param level -压缩级别(0~9)
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void makeDirectoryToZip(final File directory,
			final File zipFile, final String zipFolderName, final int level)
			throws IOException, FileNotFoundException {

		FileOutputStream fos = null;
		try {
			//输出文件流
			fos = new FileOutputStream(zipFile);
		} catch (final Exception e) {
			//建立打包后的空文件
			new File(zipFile.getParent()).mkdirs();
			zipFile.createNewFile();
			fos = new FileOutputStream(zipFile);
		}

		//使用指定的 Manifest 创建新的 JarOutputStream。清单作为输出流的第一个条目被写入
//		final JarOutputStream jos = new JarOutputStream(fos, new Manifest());
		final JarOutputStream jos = new JarOutputStream(fos);
		jos.setLevel(checkZipLevel(level));
		final String[] fileNames = directory.list();
		if (fileNames != null) {
			for (int i = 0; i < fileNames.length; i++) {
				//对一级目录下的所有文件或文件夹进行处理
				recurseFiles(jos, new File(directory, fileNames[i]),
						zipFolderName == null ? "" : zipFolderName);
			}

		}

		//关闭 ZIP 输出流和正在过滤的流。
		jos.close();

	}

	/**
	 * 检查并设置有效的压缩级别,避免压缩级别设置错的异常
	 * 
	 * @param level -压缩级别
	 * @return 有效的压缩级别或者默认压缩级别
	 */
	public static int checkZipLevel(final int level) {
		if (level < 0 || level > 9) {
			return 7;
		} else {
			return level;
		}

	}

	public static void copyFile(File in, File out) throws Exception {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(in);
			fos = new FileOutputStream(out);
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			fis.close();
			fos.close();
		}
	}

	public static void delFile(String filepath) throws IOException {
		try {
			File f = new File(filepath);// 定义文件路径
			if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
				if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
					f.delete();
				} else {// 若有则把文件放进数组，并判断是否有下级目录
					File[] files = f.listFiles();
					int i = f.listFiles().length;
					for (int j = 0; j < i; j++) {
						if (files[j].isDirectory()) {
							delFile(files[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
						}
						files[j].delete();// 删除文件
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} 
		
	}
	/**
	 * 由多个文件生成压缩包
	 * @param files 需要压缩的文件
	 * @param tagetDir 压缩文件存放的目录
	 * @param tagetFileName 压缩文件名，不带后缀
	 * @throws Exception
	 */
	public static void makeZipFile( List<String> files,String tagetDir,String tagetFileName) throws Exception {
		try {
			File tagetFile = new File(tagetDir);
			if (!tagetFile.isDirectory()) {
				String msg = tagetFile + "，该目录不存在！";
				log.error("msg");
				throw new Exception(msg);
			}
			String tempPath = tagetDir + File.separatorChar + tagetFileName;
			File tempFile = new File(tempPath);
			if (!tempFile.isDirectory()) {
				tempFile.mkdir();
			}
			int noExitNum = 0;
			for (int i = 0; i < files.size(); i++) {
				String filePath = files.get(i);
				int last = filePath.lastIndexOf(File.separatorChar);
				String fileName = filePath.substring(last);
				
				File in = new File(filePath);
				if (in.exists()) {
					File out = new File(tempPath + File.separatorChar + fileName);
					copyFile(in, out);
				} else {
					noExitNum++;
					log.info("生成压缩文件，源文件不存在：" + filePath);
				}
			}
			if (noExitNum >= files.size()) {
				log.info("生成压缩文件错误：源文件都不存在");
				throw new Exception("生成压缩文件错误：源文件都不存在");
			} else {
				final File pagesDirectory = new File(tempPath);
				String zipFileName = tagetFileName + ".zip";
				final File zipFile = new File(tagetDir, File.separatorChar+ zipFileName);
				if (zipFile.isFile() && zipFile.exists()) {
					zipFile.delete();
				}
				ZipUtil.makeDirectoryToZip(pagesDirectory, zipFile, "", 9);
				FileUtil.deleteDirectory(tempPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} 
		
	}
	
	/**
	 * 解压缩指定文件到目标目录
	 * @param srcFile	 需要解压缩的文件地址
	 * @param tarFile	文件解压缩到的目的地址
	 * @throws Exception
	 */
	public static void decompress(String srcFile,String tarFile) throws Exception
	{
		FileInputStream f = new FileInputStream(srcFile);
		CheckedInputStream scumi = new CheckedInputStream(f,new Adler32());
		ZipInputStream zis = new ZipInputStream(scumi);
		BufferedInputStream bis = new BufferedInputStream(zis);
		
		/**
		 * 解压缩到以zip文件名为文件夹的目录下
		 */
		String path=tarFile;
		System.out.println("Decompress_Path:"+path);
		File dir = new File(path);
		if(!dir.exists())
		{
			dir.mkdirs();
		}
		
		ZipEntry ze;
		while( (ze=zis.getNextEntry()) != null )
		{
			System.out.println("Write_File:"+ze);
			File new_File = new File(dir+"/"+ze);
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new_File));
			int x;
			while( (x = bis.read()) != -1 )
			{
				out.write(x);
			}
			out.flush();
			out.close();
			System.out.println("写入成功");
		}
		bis.close();
		System.out.println("操作结束");
	}

	public static void main(final String args[]) throws Exception {
		// makeDirectoryToZip();
		// final String homeDir = System.getProperty("user.dir");
//		final String homeDir = "F:" + File.separatorChar + "temp"
//				+ File.separatorChar  + "file" + File.separatorChar + "transAcc";
//		String startDateStr = "20100401";
//		String endDateStr = "20100410";
//		String zipFileName = startDateStr + "-" + endDateStr + ".zip";
//		final File zipFile = new File(homeDir, File.separatorChar + zipFileName);
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
//		Date curDate = formatter.parse(startDateStr);
//		Date endDate = formatter.parse(endDateStr);
//		Calendar cal1 = Calendar.getInstance();
//		cal1.setTime(curDate);
//		File tempDir = new File(homeDir + File.separatorChar + "temp");
//		if (!tempDir.exists()) {
//			tempDir.mkdir();
//		}
//
//		while (curDate.getTime() <= endDate.getTime()) {
//			String fileName = "9996." + formatter.format(curDate) + ".txt";
//			File in = new File(homeDir + File.separatorChar + fileName);
//			if (in.exists()) {
//				File out = new File(homeDir + File.separatorChar + "temp"
//						+ File.separatorChar + fileName);
//				
//				copyFile(in, out);
//			}
//			cal1.add(Calendar.DATE, 1);
//			curDate = cal1.getTime();
//		}
//		final File pagesDirectory = new File(homeDir, "temp");
//
//		System.out.println("Making zip file from folder /src to " + zipFile);
//
//		ZipUtil.makeDirectoryToZip(pagesDirectory, zipFile, "", 9);
//
//		System.out.println("Zip file " + zipFile + " has been made.");
		String homeDir = "D:/usr/mpsp/goods/MW_20130519_week.txt.gz";
		String zipDir = "D:/usr/mpsp/goods";
//		String platFileStr = "D:/usr/mpsp/goods/MW_20130519_week.txt.gz";
//		List<String> files = new ArrayList<String>();
//		files.add(homeDir + File.separator + "35463465_C555CAA9F82F484B.key.p8");
//		files.add(homeDir + File.separator + "35463465_C555CAA9F82F484B.cert.crt");
//		files.add(platFileStr);
//		makeZipFile(files,zipDir,"35463465_1");
		decompress(homeDir, zipDir);
	}

}
