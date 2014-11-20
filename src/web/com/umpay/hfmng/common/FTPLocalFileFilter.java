package com.umpay.hfmng.common;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * FTP本地文件过滤器
 * <p>创建日期：2013-12-26 </p>
 * @version V1.0  
 * @author jxd
 * @see
 */
public class FTPLocalFileFilter implements FileFilter {
    private Pattern p;

    public FTPLocalFileFilter(String filename) {
        String reg = filename.replaceAll("\\.", "\\\\.").replaceAll("\\?", "\\.").replaceAll("\\*", "\\.\\*");
        p = Pattern.compile(reg);
    }
    
    public boolean accept(File pathname) {
        String filename = pathname.getName();
        Matcher m = p.matcher(filename);
        return m.matches();
    }
    
}
