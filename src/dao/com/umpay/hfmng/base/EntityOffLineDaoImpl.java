package com.umpay.hfmng.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.dao.DataAccessException;


/**
 * 负责为单个Entity 提供CRUD操作的IBatis DAO基类.<br/> 子类只要在类定义时指定所管理Entity的Class,
 * 即拥有对单个Entity对象的CRUD操作.
 * 
 * <pre>
 * public class UserDaoImpl extends EntityDaoImpl&lt;User&gt; {
 * }
 * </pre>
 * 
 * @author Jinlu
 * @version 2008.08.13
 * @see BaseDao, EntityDao
 */
@SuppressWarnings("unchecked")
public class EntityOffLineDaoImpl<T> extends CommonOfflineDaoImpl implements EntityDao<T> {

	protected static final String POSTFIX_FIND = ".Find";

	protected static final String POSTFIX_SELECT_PRIAMARYKEY = ".Get";

	protected static final String POSTFIX_SELECT_PRIAMARYKEYMAP = ".GetMap";

	protected static final String POSTFIX_INSERT = ".Insert";

	protected static final String POSTFIX_UPDATE = ".Update";
	
	protected static final String POSTFIX_DELETE = ".Delete";

	/**
	 * DAO所管理的Entity类型.
	 */
	protected Class<T> entityClass;

	public EntityOffLineDaoImpl() {
		entityClass = getSuperClassGenricType(getClass(), 0);
		//log.debug("Entity类型=" + entityClass.getName());
	}

	/*
	 * 根据条件查询对象（无分页）
	 * 
	 */
	public List<T> findBy(T t) throws DataAccessException {
		return find(entityClass.getSimpleName() + POSTFIX_FIND, t);
	}
	
	/*
	 * 根据主键查询对象
	 * 
	 */
	public T get(Serializable key) throws DataAccessException {
		return (T)get(entityClass.getSimpleName() + POSTFIX_SELECT_PRIAMARYKEY, key);
	}

	/*
	 * 根据条件查询对象（有分页）
	 * 
	 */
	public void pagedFindBy(PageBean pageBean, T t) throws DataAccessException {
		find(entityClass.getSimpleName() + POSTFIX_FIND, pageBean, t);
	}

	/*
	 * 保存对象
	 * 
	 */
	public void insert(T t) throws DataAccessException{
		save(entityClass.getSimpleName() + POSTFIX_INSERT, t);
	}
	/*
	 * 删除对象
	 */
	public void delete(T t)throws DataAccessException {
		 delete(entityClass.getSimpleName() + POSTFIX_DELETE, t);
	}
	/*
	 * 更新对象
	 * 
	 */
	public int update(T t) throws DataAccessException {
		return update(entityClass.getSimpleName() + POSTFIX_UPDATE, t);
	}

	/*
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型.
	 * 
	 * @param clazz clazz The class to introspect @param index the Index of the
	 * generic ddeclaration,start from 0. @return the index generic declaration,
	 * or <code>Object.class</code> if cannot be determined
	 */
	private static Class getSuperClassGenricType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class) params[index];
	}

}
