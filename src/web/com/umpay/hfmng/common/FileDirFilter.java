package com.umpay.hfmng.common;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * @ClassName: FileDirFilter
 * @Description: TODO
 * @version: 1.0
 * @author: panyouliang
 * @Create: 2013-10-18
 */
public class FileDirFilter implements FilenameFilter {
	  private Pattern pattern;

	  public FileDirFilter(String regex) {
	    pattern = Pattern.compile(regex);
	  }
	  public boolean accept(File dir, String name) {
	    return pattern.matcher(new File(name).getName()).matches();
	  }
	} 
