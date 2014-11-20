/** *****************  JAVA头文件说明  ****************
 * file name  :  CategoryDaoImpl.java
 * owner      :  xu
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-10-24
 * *************************************************/ 

package com.umpay.hfmng.dao.impl;

import org.springframework.stereotype.Repository;

import com.umpay.hfmng.base.EntityOffLineDaoImpl;
import com.umpay.hfmng.dao.CategoryDao;
import com.umpay.hfmng.model.Category;


/** ******************  类说明  *********************
 * class       :  CategoryDaoImpl
 * @author     :  xuhuafeng
 * @version    :  1.0  
 * description :  
 * ************************************************/
@Repository
public class CategoryDaoImpl extends EntityOffLineDaoImpl<Category> implements
		CategoryDao {

}
