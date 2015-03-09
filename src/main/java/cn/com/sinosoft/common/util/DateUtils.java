/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-10-29
 */
package cn.com.sinosoft.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 *	处理日期
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-10-29
 */
public class DateUtils {

	/**
	 * 格式化时间戳
	 *
	 * 
	 * @param timestamp
	 * @param pattern
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public static String formatTimestamp(Timestamp timestamp, String pattern){
		return new SimpleDateFormat(pattern).format(new Date(timestamp.getTime()));
	}
	
}
