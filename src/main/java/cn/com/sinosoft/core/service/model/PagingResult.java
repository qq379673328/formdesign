/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-10-23
 */
package cn.com.sinosoft.core.service.model;

import java.util.Collection;

/**
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-10-23
 */
public class PagingResult {
	
	/**
	 * 总数目
	 */
	int total;
	/**
	 * 列表内容
	 */
	Collection<Object> rows;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Collection<Object> getRows() {
		return rows;
	}
	public void setRows(Collection<Object> rows) {
		this.rows = rows;
	}

}
