package cn.com.sinosoft.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 加载properties工具类
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-11-3
 */
public class PropertiesUtil {

	Properties p;

	public PropertiesUtil(String propertiesPath) throws IOException {
		if(!StrUtils.hasText(propertiesPath)){
			throw new IOException("PropertiesUtil 构造参数不能为空.");
		}
		propertiesPath = propertiesPath.endsWith(".properties") ? propertiesPath :
			propertiesPath + ".properties";
		p = new Properties();
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream(propertiesPath);
		this.p.load(inputStream);
	}

	public String getValue(String key) {
		return p.getProperty(key);
	}

	public Properties getProperties() {
		return this.p;
	}

	/**
	 * 获取一个配置文件的某个key的值
	 *
	 * 
	 * @param propertiesPath
	 * @param key
	 * @return
	 * @throws IOException
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public static Object getValue(String propertiesPath, String key) throws IOException{
		if(!StrUtils.hasText(propertiesPath)){
			throw new IOException("PropertiesUtil 构造参数不能为空.");
		}
		propertiesPath = propertiesPath.endsWith(".properties") ? propertiesPath :
			propertiesPath + ".properties";
		InputStream inputStream = PropertiesUtil.class.getClassLoader()
				.getResourceAsStream(propertiesPath);
		Properties pro = new Properties();
		pro.load(inputStream);
		return pro.get(key);
	}
	
}
