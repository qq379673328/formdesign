/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-11-3
 */
package cn.com.sinosoft.common.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理sql工具类，可扩展
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-11-3
 */
public class SqlUtil {

	private static final Logger log = LoggerFactory.getLogger(SqlUtil.class);
	
	//数据库配置文件
	private final static String dbConfigFilePath = "db.properties";
	//当前数据库方言,默认为mysql
	private static String CUR_DIALECT = DbDialect.MYSQL;
	//方言未识别信息
	//private static final String INFO_DIALECT_INVALID = "方言未识别,可用方言为mysql、oracle";
	
	/**
	 * 初始化参数
	 */
	static{
		try {
			CUR_DIALECT = String.valueOf(PropertiesUtil.getValue(dbConfigFilePath, "db.dialect"));
		} catch (IOException e) {
			e.printStackTrace();
			log.error("数据库方言识别失败，请检查文件" + dbConfigFilePath);
		}
	}
	
	/**
	 * 字符串转日期
	 *
	 * 
	 * @param fieldName 字段名
	 * @param fieldType 字段类型 0|1 0:代表字段,1:代表字符串
	 * @param dateType 0|1<br/>
	 * 	0：年-月-日,<br/>
	 * 	1:年-月-日 时：分：秒<br/>
	 * 如：传入 name,0,0。返回 to_date(name, 'yyyy-MM-dd') ") <br/>
	 * 传入name,1,1。返回 to_date('name', 'yyyy-MM-dd HH:mm:ss')
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public static String toDate(String fieldName, int fieldType, int dateType){
		String ret = null;
		if(CUR_DIALECT.equals(DbDialect.MYSQL)){
			if(fieldType == 0){
				ret = " str_to_date('" + fieldName + "','" + getSqlDateFormatStr(dateType) + "') ";
			}else if(fieldType == 1){
				ret = " str_to_date('" + fieldName + "','" + getSqlDateFormatStr(dateType) + "') ";
			}
		}else if(CUR_DIALECT.equals(DbDialect.ORACLE)){
			if(fieldType == 0){
				ret = " to_date('" + fieldName + "','" + getSqlDateFormatStr(dateType) + "') ";
			}else if(fieldType == 1){
				ret = " to_date('" + fieldName + "','" + getSqlDateFormatStr(dateType) + "') ";
			}
		}
		return ret;
	}
	
	/**
	 * 字符串转日期
	 *
	 * 
	 * @param fieldName 字段名
	 * @param fieldType 字段类型 0|1 0:代表字段,1:代表字符串
	 * @param dateType 0|1<br/>
	 * 	0：年-月-日,<br/>
	 * 	1:年-月-日 时：分：秒<br/>
	 * 如：传入 name,0,0。返回 to_char(name, 'yyyy-MM-dd') ") <br/>
	 * 传入name,1,1。返回 to_char('name', 'yyyy-MM-dd HH:mm:ss')
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public static String toChar(String fieldName, int fieldType, int dateType){
		String ret = null;
		if(CUR_DIALECT.equals(DbDialect.MYSQL)){
			if(fieldType == 0){
				ret = " date_format(" + fieldName + ",'" + getSqlDateFormatStr(dateType) + "') ";
			}else if(fieldType == 1){
				ret = " date_format('" + fieldName + "','" + getSqlDateFormatStr(dateType) + "') ";
			}
		}else if(CUR_DIALECT.equals(DbDialect.ORACLE)){
			if(fieldType == 0){
				ret = " to_char(" + fieldName + ",'" + getSqlDateFormatStr(dateType) + "') ";
			}else if(fieldType == 1){
				ret = " to_char('" + fieldName + "','" + getSqlDateFormatStr(dateType) + "') ";
			}
		}
		return ret;
		
	}
	
	/**
	 * 获取格式化日期方式
	 *
	 * 
	 * @param type
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	private static String getSqlDateFormatStr(int type){
		String ret = null;
		switch(type){
			case 0://年月日
				if(CUR_DIALECT.equals(DbDialect.MYSQL)){
					ret = "%Y-%m-%d";
				}else if(CUR_DIALECT.equals(DbDialect.ORACLE)){
					ret = "yyyy-MM-dd";
				}
				break;
			case 1://年月日-时分秒
				if(CUR_DIALECT.equals(DbDialect.MYSQL)){
					ret = "%Y-%m-%d %H:%i:%S";
				}else if(CUR_DIALECT.equals(DbDialect.ORACLE)){
					ret = "yyyy-MM-dd hh24:mm:ss";
				}
				break;
			default:
				if(CUR_DIALECT.equals(DbDialect.MYSQL)){
					ret = "%Y-%m-%d";
				}else if(CUR_DIALECT.equals(DbDialect.ORACLE)){
					ret = "yyyy-MM-dd";
				}
		}
		return ret;
	}
	
}
