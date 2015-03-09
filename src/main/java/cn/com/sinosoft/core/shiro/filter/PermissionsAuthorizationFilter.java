/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2015-2-3
 */
package cn.com.sinosoft.core.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 授权过滤器,通过地址跟功能点id匹配,如：
 * 	http://localhost:8082/hdcdc/epimonitor/infectlist
 * /epimonitor/infectlist 与 功能点id /epimonitor/infectlist 匹配
 * 
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2015-2-3
 */
public class PermissionsAuthorizationFilter extends AuthorizationFilter {

	/**
	 * 
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 * @throws Exception
	 * @see org.apache.shiro.web.filter.AccessControlFilter#isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object)
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		HttpServletRequest req = (HttpServletRequest)request;
		String uri = req.getRequestURI();
		String context = req.getContextPath();
		uri = uri.replaceFirst(context, "");
		req.getRequestURL();
		Subject currentUser = SecurityUtils.getSubject();
		boolean isPermitted = false;
		if(currentUser != null
			&& currentUser.isAuthenticated()
			&& currentUser.isPermitted(uri)){
			isPermitted = true;
		}
		return isPermitted;
	}

}
