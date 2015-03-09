/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-10-23
 */
package cn.com.sinosoft.core.dao;

import java.util.Collection;

import org.hibernate.type.Type;

/**
 * 数据库基本操作接口
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-10-23
 */
public interface BaseDao {

	/**
	 * 更新
	 *
	 * 
	 * @param t 对象
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> void update(T t);
	
	/**
	 * 批量更新
	 *
	 * 
	 * @param t
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> void batchUpdate(Collection<T> t);
	
	/**
	 * 删除
	 *
	 * 
	 * @param t
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> void del(T t);
	
	/**
	 * 批量删除
	 *
	 * 
	 * @param ts
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> void batchDel(Collection<T> ts);
	
	/**
	 * 保存
	 *
	 * 
	 * @param t
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> void save(T t);
	
	/**
	 * 批量保存
	 *
	 * 
	 * @param ts
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> void batchSave(Collection<T> ts);
	
	/**
	 * 执行sql获取条数-此处sql为统计sql-select count...
	 *
	 * 
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public int queryCountBySql(String sql, Object[] values, Type[] types);
	
	/**
	 * 执行sql获取列表
	 *
	 * 
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public Collection<Object> queryListBySql(String sql, Object[] values, Type[] types);
	
	/**
	 * 查询bean
	 *
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> T queryById(String id, Class<T> clazz);
	
}
