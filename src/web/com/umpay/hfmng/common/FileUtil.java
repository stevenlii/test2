/*************************************************************************
*file name    :  FileUtil.java
*owner      :  Ding Zhe
*copyright   :   umpay
*description  : 
*modified    : 1. Jun 9, 2010  Ding Zhe 创建  
*************************************************************************/
package com.umpay.hfmng.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sdicons.json.validator.impl.predicates.Content;

/*************************************************************************
 *class : FileUtil
 *@author :  Ding Zhe
 *@version :  1.0  
 *description :  
 *@see :                        
 *************************************************************************/
public class FileUtil {
	
	private static final Log logger = LogFactory.getLog(FileUtil.class);
	
	public static byte[] load(String path, String fileName) throws Exception {
		if(path==null||"".equals(path.trim())){
			throw new Exception("证书路径为空！");
		}
		if(fileName==null||"".equals(fileName.trim())){
			throw new Exception("证书名为空！");
		}
		
		File file = new File(path + fileName);
		InputStream is = new FileInputStream(file);

		// 得到文件长度   
		long length = file.length();

		// 长度限制   
		if (length > Integer.MAX_VALUE) {
			// 文件太大了的异常处理   
			return null;
		}

		// 创建对应长度的字节数组   
		// 注意，如果文件很大，这个将耗掉大量的内存   
		byte[] bytes = new byte[(int) length];

		// 读取数据   
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// 确信所有的数据已经读取完毕   
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		// 关闭输入流   
		is.close();
		return bytes;
		
	}
	
    /**    
     * 删除文件，可以是单个文件或文件夹    
     * @param   fileName    待删除的文件名    
     * @return 文件删除成功返回true,否则返回false    
     */     
    public static boolean delete(String fileName) throws Exception {      
        File file = new File(fileName);      
        if(!file.exists()){      
            logger.info("删除文件失败："+fileName+"文件不存在");
            return false;      
        }else{      
            if(file.isFile()){      
                      
                return deleteFile(fileName);      
            }else{      
                return deleteDirectory(fileName);      
            }      
        }      
    }      
          
    /**    
     * 删除单个文件    
     * @param   fileName    被删除文件的文件名    
     * @return 单个文件删除成功返回true,否则返回false    
     */     
    public static boolean deleteFile(String fileName) throws Exception {      
        File file = new File(fileName);  
        if(file.isFile() && file.exists()){      
            file.delete();
            logger.info("删除单个文件"+fileName+"成功！");
            return true;      
        }else{      
            logger.info("删除单个文件"+fileName+"失败,文件不存在!");
            return false;      
        }      
    }      
          
    /**    
     * 删除目录（文件夹）以及目录下的文件    
     * @param   dir 被删除目录的文件路径    
     * @return  目录删除成功返回true,否则返回false    
     */     
    public static boolean deleteDirectory(String dir) throws Exception {      
        //如果dir不以文件分隔符结尾，自动添加文件分隔符      
        if(!dir.endsWith(File.separator)){      
            dir = dir+File.separator;      
        }      
        File dirFile = new File(dir);      
        //如果dir对应的文件不存在，或者不是一个目录，则退出      
        if(!dirFile.exists() || !dirFile.isDirectory()){      
            logger.info("删除目录失败"+dir+"目录不存在！");
            return false;      
        }      
        boolean flag = true;      
        //删除文件夹下的所有文件(包括子目录)      
        File[] files = dirFile.listFiles();      
        for(int i=0;i<files.length;i++){      
            //删除子文件      
            if(files[i].isFile()){      
                flag = deleteFile(files[i].getAbsolutePath());      
                if(!flag){      
                    break;      
                }      
            }      
            //删除子目录      
            else{      
                flag = deleteDirectory(files[i].getAbsolutePath());      
                if(!flag){      
                    break;      
                }      
            }      
        }      
              
        if(!flag){      
            logger.info("删除目录失败");
            return false;      
        }      
              
        //删除当前目录      
        if(dirFile.delete()){      
            logger.info("删除目录"+dir+"成功！");
            return true;      
        }else{      
            logger.info("删除目录"+dir+"失败！");
            return false;      
        }      
    }  
    
    /**
     * 判断文件是否存在操作
     * @param path
     * @param filename
     * @return
     */
    public static boolean isFileExit(String path, String filename) {
		File f = new File(path, filename);
		if (f.exists()) {
			return true;
		}
		return false;
	} 
    /**
     * ********************************************
     * method name   : getDownloadFile 
     * description   : 根据路径获取下载文件。若不存在，则根据上周日的文件解压-生成excel-压缩为zip-下载
     * @return       : File
     * @param        : @param bankidtype
     * @param        : @return
     * @param        : @throws Exception
     * modified      : xuhuafeng ,  2013-5-29  下午03:30:30
     * *******************************************
     */
    public File getDownloadFile(String bankidtype) throws Exception{
		String path = "";
		if("MW%".equals(bankidtype)){
			path = "D:/usr/mpsp/goods/";
		}
		else if("XE%".equals(bankidtype)){
			path = "D:/usr/mpsp/goods/";
		}
		
		// 如果路径不存在 或者路径不存在
		if("".equals(path)){
			throw new Exception("下载路径为空。");
		}
		
		File dir = null;
		try {
			dir = new File(path); // 初始化文件
		} catch (Exception e) {
			logger.error("初始化下载路径[ " + path + " ] 失败。", e);
			throw new Exception("初始化下载路径[ " + path + " ] 失败。");
		}
		File resultFile = null;
		Runtime rt=Runtime.getRuntime(); //获得系统的Runtime对象rt
		logger.info("生成excel前， Free Memory = "+rt.freeMemory());
		if(dir != null){
			if(!dir.exists()){
				throw new Exception(path + "不存在。");
			}
			if(!dir.isDirectory()){
				throw new Exception(path + "不是目录。");
			}
			
			/**
			 * 因为使用了ZipUtil，这个功能会在解压缩完成之后修改文件名后缀
			 * 	例如  XE_20120108_week.txt.gz --> XE_20120108_week.txt.zip
			 */
			// 先判断是否已经存在下载文件了
			// 得到当天的上一个周日
			String preSunday = TimeUtil.getPreSunday();
			String fileName = bankidtype.substring(0, 2) + "_" + preSunday + "_week";
			File downloadFile = new File(path + File.separator + fileName + ".zip");
			if(downloadFile.exists()){
				logger.info("下载文件已存在:" + downloadFile.getPath());
				return downloadFile;
			}
			
			/**
			 * 1、列出所有文件
			 * 2、判断个数  如果只有1个文件 直接返回
			 * 3、多个文件取最后生成的 根据文件名格式XE_20120108_week.txt.gz | MW_20120108_week.txt.gz
			 */
			
			// 2、获得上周生成的文件
			resultFile = new File(path + File.separator + fileName + ".txt.gz");
			// 解压
			ZipUtil.decompress(resultFile.getPath(), path);
			logger.info("文件" + resultFile.getPath() + "解压完成，路径为" + path);
			
			// 生成excel
			WritableWorkbook wwb = null;
			String zipName = "";
			String toFileName = "";
			ByteArrayOutputStream targetFile = null;
			String sheetNameString = "列表";
			try {
				// 开始读取resultFile的每一行的数据   文件名为XE_20120108_week.txt | MW_20120108_week.txt
				// 根据gz格式的文件名得到txt的文件名
				int index = resultFile.getName().lastIndexOf(".");
				String txtName = resultFile.getName().substring(0, index);
				logger.info("需要将文件" + txtName + "转化成excel。");
				zipName = txtName.replaceAll(".txt", ".zip");
				toFileName = txtName.replaceAll(".txt", ".xls");
				
				File txtFile = new File(path + File.separator + txtName);
				targetFile = new ByteArrayOutputStream();
				wwb = Workbook.createWorkbook(targetFile);

				// 添加第一个工作表并设置第一个Sheet的名字
				WritableSheet sheet = wwb.createSheet(sheetNameString, 0);
				BufferedReader in = new BufferedReader(new FileReader(txtFile));
				int row = 0;
				int count = 0;
				int sheetNum = 0;
				logger.info("循环前， Free Memory = "+rt.freeMemory());
				while(true){
					String line = in.readLine();
					if(line == null){
						logger.info("共写入数据 " + count + " 行。");
						break;
					}
					//每65000行换一页
					if(row > 65000){
						row = 0;
						sheetNum++;
						sheet = wwb.createSheet(sheetNameString+sheetNum, sheetNum);
					}
					// 数据的数据
					String[] dataArray = line.split("\\|");
					for(int i = 0; i <dataArray.length; i++){
						sheet.addCell(new Label(i, row, dataArray[i]));
					}
					row++;
					count++;
				}
				logger.info("循环后in.close()前， Free Memory = "+rt.freeMemory());
				in.close();
			} catch (RowsExceededException e1) {
				logger.error("写入行异常", e1);
			} catch (WriteException e1) {
				logger.error("写文件异常", e1);
			}
			logger.info("循环后write前， Free Memory = "+rt.freeMemory());
			try {
				wwb.write();
				logger.info("循环后write后， Free Memory = "+rt.freeMemory());
				wwb.close();
				File resultExcelFile = new File(path + File.separator + toFileName);
				FileOutputStream fos = new FileOutputStream(resultExcelFile);
				targetFile.writeTo(fos);
				targetFile.flush();
				targetFile.close();
				fos.close();
				logger.info("压缩前， Free Memory = "+rt.freeMemory());
				//将excel文件压缩
				List<String>files = new ArrayList<String>();
				files.add(resultExcelFile.getPath());
				try {
					//下面步骤会将.xls文件压缩为.zip文件
					ZipUtil.makeZipFile(files, path, zipName.substring(0, zipName.indexOf(".")));
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("生成压缩文件失败："+e);
				}
				logger.info("生成压缩文件成功！");
				logger.info("压缩后， Free Memory = "+rt.freeMemory());
				return new File(path + File.separator + zipName);
			} catch (IOException e) {
				logger.error("写入Excel文件IOException异常："+e);
			} catch (Exception e) {
				logger.error("未知异常", e);
			}
			
		}
		return resultFile;
	}
	
    public static void createFile(String fileName)throws Exception{
    	File file = new File(fileName);
    }
    
    public static void writeFile(String file, String... content) {     
        BufferedWriter out = null;
        if(content == null){
        	return;
        }
        try {     
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            for(String item : content){
	            out.write(item);     
	            out.newLine();
            }
        } catch (Exception e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(out != null){  
                    out.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }     
    }
    
    public static void writeFile(BufferedWriter out, String... content) {     
        if(content == null){
        	return;
        }
        try {     
            for(String item : content){
            	out.write(item);
            	if(isWindows){
            		out.newLine(); //linux 下不工作
            	}else{
            		out.write("\r\n");
            	}
            }
        } catch (Exception e) {     
            e.printStackTrace();     
        }
    } 
    
    public static String getChannel(String path, int line) throws Exception{
    	File file=new File(path);
    	if(!file.exists()||file.isDirectory()){
    		throw new FileNotFoundException();
    	}
    	BufferedReader br=new BufferedReader(new FileReader(file));
    	String sb = "";
    	try{
	    	String temp=null;
	    	
	    	temp=br.readLine();
	    	int idx = 1;
	    	while(temp != null){
	    		if(line == idx){
	    			sb = temp;
	    			break;
	    		}
	    		temp=br.readLine();
	    		idx++;
	    	}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}finally{
    		if(br != null){
    			br.close();
    		}
    	}
    	if(!"".equals(sb)){
    		String[] arr = sb.split("：");
    		return arr[1];
    	}
    	return sb;
    } 
    private static boolean isWindows = true;
    static{
    	Properties props=System.getProperties(); //获得系统属性集    
   	 	String osName = props.getProperty("os.name"); //操作系统名称
   	 	if(!osName.contains("Windows")){
   	 		isWindows = false;
   	 	}
    }
    
    public static void main(String[] args) throws Exception{
		String file = "E:/resin-3.1.9/webapps/hfMngBusi/WEB-INF/download/综合业务管理平台__蝶彩电子_20131022.txt";
		System.out.println(getChannel(file, 2));
	}
}
