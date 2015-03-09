/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-10-23
 */
package cn.com.sinosoft.core.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @param <T>
 * @date	2014-10-23
 */
@Repository("simpleBaseDao")
public class SimpleBaseDao implements BaseDao {
	
	@Resource
	HibernateTemplate template;
	
	/**
	 *
	 * 
	 * @param t
	 * @return
	 * @see cn.com.sinosoft.hdcdc.core.dao.BaseDao#update(java.lang.Object)
	 */
	@Override
	public <T> void update(T t) {
		template.saveOrUpdate(t);
	}

	/**
	 *
	 * 
	 * @param t
	 * @return
	 * @see cn.com.sinosoft.hdcdc.core.dao.BaseDao#batchUpdate(java.util.Collection)
	 */
	@Override
	public <T> void batchUpdate(Collection<T> t) {
		template.saveOrUpdateAll(t);
	}

	/**
	 * 
	 *
	 * 
	 * @param t
	 * @see cn.com.sinosoft.hdcdc.core.dao.BaseDao#del(java.lang.Object)
	 */
	@Override
	public <T> void del(T t) {
		template.delete(t);
	}

	/**
	 * 
	 *
	 * 
	 * @param ts
	 * @see cn.com.sinosoft.hdcdc.core.dao.BaseDao#batchDel(java.util.Collection)
	 */
	@Override
	public <T> void batchDel(Collection<T> ts) {
		template.deleteAll(ts);
	}

	/**
	 *
	 * 
	 * @param t
	 * @return
	 * @see cn.com.sinosoft.hdcdc.core.dao.BaseDao#save(java.lang.Object)
	 */
	@Override
	public <T> void save(T t) {
		template.save(t);
	}

	/**
	 *
	 * 
	 * @param ts
	 * @return
	 * @see cn.com.sinosoft.hdcdc.core.dao.BaseDao#batchSave(java.util.Collection)
	 */
	@Override
	public <T> void batchSave(Collection<T> ts) {
		template.saveOrUpdateAll(ts);
	}

	/**
	 *
	 * 
	 * @param sql
	 * @return
	 * @see cn.com.sinosoft.hdcdc.core.dao.BaseDao#queryCountBySql(java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public int queryCountBySql(final String sql, final Object[] values, final Type[] types) {
		List list = template.executeFind(new HibernateCallback() {

			@Override
			public List doInHibernate(Session session) throws HibernateException,
					SQLException {
				return session.createSQLQuery(sql).setParameters(values, types).list();
			}
			
		});
		if(list != null && list.size() > 0){
			return Integer.valueOf(String.valueOf(list.get(0)));
		}else{
			return 0;
		}
	}

	/**
	 * 执行查询列表sql<br/>
	 * 注意：若查询报错S0022，则在sql最外层添加select * from ( 原始sql ) 别名,<br/>
	 * 其中别名可为任意值,参考:http://www.tuicool.com/articles/eia63m
	 * 
	 * @param sql
	 * @return
	 * @see cn.com.sinosoft.hdcdc.core.dao.BaseDao#queryListBySql(java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	public List queryListBySql(final String sql, final Object[] values, final Type[] types) {
		return template.executeFind(new HibernateCallback() {

			@Override
			public List doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				if(values != null && types != null){
					query.setParameters(values, types);
				}
				return query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
			}
			
		});
	}

	/**
	 * queryById("12345", Demo.class);
	 *
	 * 
	 * @param id 主键
	 * @param clazz 实体类
	 * @return
	 * @see cn.com.sinosoft.hdcdc.core.dao.BaseDao#queryById(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T queryById(String id, Class<T> clazz) {
		return template.get(clazz, id);
	}

	public HibernateTemplate getTemplate(){
		return template;
	}

	/**
	 * 执行普通sql,用于删除或更新多条数据
	 * 
	 * @param sql
	 * @return
	 */
	public int executeDelOrUpdateSql(final String sql, final Object[] values, final Type[] types) {
		return template.execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				if(values != null && types != null){
					query.setParameters(values, types);
				}
				return query.executeUpdate();
			}
			
		});
	}

}
