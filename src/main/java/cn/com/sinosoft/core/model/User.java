/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-12-1
 */
package cn.com.sinosoft.core.model;


/**
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-12-1
 */
public class User {

	/**
	 * 用户id
	 */
	private String id;
	/**
	 * 用户名称
	 */
	private String name;
	/**
	 * 所属机构
	 */
	private String orgCode;
	/**
	 * 所属机构-描述
	 */
	private String orgDesc;
	/**
	 * 所属区域
	 */
	private String zoneCode;
	/**
	 * 所属区域-描述
	 */
	private String zoneDesc;
	/**
	 * 登陆名
	 */
	private String loginName;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getZoneCode() {
		return zoneCode;
	}
	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrgDesc() {
		return orgDesc;
	}
	public void setOrgDesc(String orgDesc) {
		this.orgDesc = orgDesc;
	}
	public String getZoneDesc() {
		return zoneDesc;
	}
	public void setZoneDesc(String zoneDesc) {
		this.zoneDesc = zoneDesc;
	}
	
}
