/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-10-29
 */
package cn.com.sinosoft.common.util;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

/**
 * 处理数据集
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-10-29
 */
public class DataSetUtils {

	/**
	 * 格式化结果集中的时间戳字段
	 *
	 * 
	 * @param items
	 * @param fieldName
	 * @param pattern
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@SuppressWarnings("unchecked")
	public static void formatTimestampField(Collection<Object> items, String fieldName, String pattern){
		for(Object item : items){
			Map<String, Object> i = (Map<String, Object>)item;
			if(i.get(fieldName) != null){
				Timestamp t = (Timestamp)i.get(fieldName);
				i.put(fieldName, DateUtils.formatTimestamp(t, pattern));
			}
		}
		
	}
	
}
