/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-10-29
 */
package cn.com.sinosoft.common.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * 与spring相关的工具
 * 
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-10-29
 */
public class SpringUtil implements ApplicationContextAware {
	
	private static  ApplicationContext applicationContext;

	private static final String INFO_SEP = "|";
	
	/**
	 * 格式化spring的验证信息
	 *
	 * 
	 * @param result
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public static String formatBindingResult(BindingResult result){
		StringBuffer sb = new StringBuffer();
		if(result.hasErrors()){
			for(ObjectError error : result.getAllErrors()){
				sb.append(error.getDefaultMessage() + INFO_SEP);
			}
		}
		return sb.toString();
	}
	

	/**
	 * 获取实例
	 * 
	 * @param name
	 *            Bean名称
	 * @return 实例
	 */
	public static  Object getBean(String name) {
		Assert.hasText(name);
		return applicationContext.getBean(name);
	}

	/**
	 * 获取实例
	 * 
	 * @param name
	 *            Bean名称
	 * @param type
	 *            Bean类型
	 * @return 实例
	 */
	public  <T> T getBean(String name, Class<T> type) {
		Assert.hasText(name);
		Assert.notNull(type);
		return applicationContext.getBean(name, type);
	}


	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		// TODO Auto-generated method stub
		applicationContext=arg0;
	}


}
