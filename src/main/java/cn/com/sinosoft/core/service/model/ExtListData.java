/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-11-25
 */
package cn.com.sinosoft.core.service.model;

import java.util.List;
import java.util.Map;

/**
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-11-25
 */
public class ExtListData {

	Map<String, List<Map<String, Object>>> extListData;

	public Map<String, List<Map<String, Object>>> getExtListData() {
		return extListData;
	}

	public void setExtListData(
			Map<String, List<Map<String, Object>>> extListData) {
		this.extListData = extListData;
	}
	
}
