/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-10-23
 */
package cn.com.sinosoft.core.service.model;

/**
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-10-23
 */
public class PageParam {

	/**
	 * 所需条数
	 */
	int rows;
	/**
	 * 当前页数
	 */
	int page;
	/**
	 * 总数
	 */
	int total;
	/**
	 * 起始条数
	 */
	int start;
	/**
	 * 总页数
	 */
	int totalPage;
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}
