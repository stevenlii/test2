package com.umpay.hfmng.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;


/**
 * 针对单个Entity对象的操作定义，使用范型的实现
 * 
 * @author Jinlu
 * @version 2008.08.13
 */
public interface EntityDao<T> {

	/**
	 * 查询指定的实体
	 * @param o 
	 * @return
	 * @throws DataAccessException 
	 */
	T get(Serializable o) throws DataAccessException;
//	/**
//	 * 根据联合主键查询
//	 * 
//	 * @param pkMap
//	 * @return
//	 */
//	public T get(Map<String,Object> pkMap)throws DataAccessException;
	/**
	 * 根据指定的条件查询记录
	 * @param t
	 * @return
	 * @throws DataAccessException 
	 */
	List<T> findBy(T t) throws DataAccessException;
	
	/**
	 * 根据指定的条件分页查询记录
	 * @param pageBean
	 * @param t
	 * @throws DataAccessException 
	 */
	void pagedFindBy(PageBean pageBean, T t) throws DataAccessException;

	/**
	 * 保存实体
	 * @param t
	 * @throws DataAccessException 
	 * @throws ViolationException 
	 */
	void insert(T t) throws DataAccessException;
	
	/**
	 * 更新实体
	 * @param t
	 * @throws DataAccessException 
	 */
	int update(T t) throws DataAccessException;
	/**
	 * 删除实体
	 * @param t
	 * @throws DataAccessException 
	 */
    void delete(T t) throws DataAccessException;
}
