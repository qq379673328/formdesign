/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-11-12
 */
package cn.com.sinosoft.core.action;

import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.com.sinosoft.common.util.Excel.Table2Excel;
import cn.com.sinosoft.core.service.CommonService;

/**
 * 公共action
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-11-12
 */
@Controller
public class CommonAction extends BaseController {

	@Resource
	CommonService commonService;
	
	/**
	 * 导出简单excel
	 *
	 * 
	 * @param response
	 * @param tableJson
	 * @param title
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("exportSimpleTable")
	public void exportExcel(HttpServletResponse response, String tableJson, String title){
		try {
			if(title == null || title.trim().equals("")){
				title = "export";
			}
			response.setContentType("application/vnd.ms-excel");
			title = URLEncoder.encode(title,"GB2312"); 
			title = URLDecoder.decode(title, "ISO8859_1"); 
			response.setHeader("Content-disposition", "attachment;filename=" + title + ".xls"); 
			OutputStream ouputStream = response.getOutputStream();
			new Table2Excel().transJson2Excel(tableJson, ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 未授权跳转
	 *
	 * 
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("unauthorized")
	public Object unauthorized(
			HttpServletRequest req,
			HttpServletResponse res){
		String xmlHttpRequest = req.getHeader("X-Requested-With");
		ModelAndView mav = new ModelAndView("unauthorized");
		if (xmlHttpRequest != null){
			if (xmlHttpRequest.equalsIgnoreCase("XMLHttpRequest")) {
				res.setStatus(403);
			}
		}
		return mav;
	}
	
}
