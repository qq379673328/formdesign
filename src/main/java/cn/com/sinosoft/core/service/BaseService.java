/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-10-23
 */
package cn.com.sinosoft.core.service;

import java.util.Collection;
import java.util.Map;

import cn.com.sinosoft.core.service.model.PageParam;
import cn.com.sinosoft.core.service.model.PagingSrcSql;

/**
 * 业务层接口
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-10-23
 */
public interface BaseService {
	
	/**
	 * 插入对象
	 *
	 * 
	 * @param t
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public void saveBean(Object t);

	/**
	 * queryById("123123", Demo.class);
	 *
	 * 
	 * @param <T> hibernate bean
	 * @param id 主键
	 * @param clazz 实体类
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> T queryById(String id, Class<T> clazz);
	
	/**
	 * 更新对象
	 *
	 * 
	 * @param t
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> void update(T t);
	
	/**
	 * 批量更新
	 *
	 * 
	 * @param <T>
	 * @param ts
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> void batchUpdate(Collection<T> ts); 
	
	/**
	 * 删除对象
	 *
	 * 
	 * @param t
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> void del(T t);
	
	/**
	 * 批量删除对象
	 *
	 * 
	 * @param t
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> void batchDel(Collection<T> t);
	
	/**
	 * 分页查询
	 *
	 * 
	 * @param params 查询参数
	 * @param pageParams 分页参数
	 * @param srcSql 原始sql
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public Object pagingSearch(Map<String, String> params, PageParam pageParams, PagingSrcSql srcSql);
	
}
