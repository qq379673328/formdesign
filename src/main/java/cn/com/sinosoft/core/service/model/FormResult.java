/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-10-29
 */
package cn.com.sinosoft.core.service.model;

/**
 * 表单提交返回结果
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-10-29
 */
public class FormResult {
	
	/**
	 * 成功
	 */
	public static final int SUCCESS = 1;
	/**
	 * 失败
	 */
	public static final int ERROR = 0;

	/**
	 * 是否成功:1-成功,0-失败
	 */
	int success;
	/**
	 * 返回信息
	 */
	String message;
	/**
	 * 返回的数据
	 */
	Object data;
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
