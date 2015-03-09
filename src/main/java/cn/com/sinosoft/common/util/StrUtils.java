package cn.com.sinosoft.common.util;

import java.util.List;
import java.util.UUID;

import org.springframework.util.StringUtils;

/**
 * @author cx
 * @param <T>
 * @date 2014年6月18日
 * @comment 开发过程中的判断请用StrUtils类
 *          StrUtils.hasText(str);如果str为null或者为空返回false
 *          StrUtils.trimAllWhitespace(str);去除空格
 * @see    特殊判断请在此类基础上自己扩展！！
 */
public class StrUtils<T> extends  StringUtils {
	
	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 判断一个字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		if (null != str && !str.trim().equals("")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 重载方法判断一个对象是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		if (null != obj && !obj.equals(null)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断一个list对象是否为空
	 * 
	 * @param list
	 * @return
	 */
	public static <T> boolean isNull(List<T> list) {
		if (null != list && list.size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断两个字符串相等
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean isEqual(String a, String b) {
		if (a == b || a.equals(b)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 重载判断两个对象相等
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean isEqual(Object a, Object b) {
		if (a == b || a.equals(b)) {
			return true;
		} else {
			return false;
		}
	}
	
}

