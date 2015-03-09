/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-10-24
 */
package cn.com.sinosoft.core.service.model;

import org.hibernate.type.Type;

/**
 *
 *	分页查询用sql
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-10-24
 */
public class PagingSql {
	
	/**
	 * 原始sql
	 */
	String srcSql;
	/**
	 * 分页sql-总数
	 */
	String countSql;
	/**
	 * 分页sql-数据集
	 */
	String listSql;
	/**
	 * 绑定参数
	 */
	Object[] values;
	/**
	 * 参数类型
	 */
	Type[] types;
	
	public String getCountSql() {
		return countSql;
	}
	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}
	public String getListSql() {
		return listSql;
	}
	public void setListSql(String listSql) {
		this.listSql = listSql;
	}
	public Object[] getValues() {
		return values;
	}
	public void setValues(Object[] values) {
		this.values = values;
	}
	public Type[] getTypes() {
		return types;
	}
	public void setTypes(Type[] types) {
		this.types = types;
	}
	public String getSrcSql() {
		return srcSql;
	}
	public void setSrcSql(String srcSql) {
		this.srcSql = srcSql;
	}

}
