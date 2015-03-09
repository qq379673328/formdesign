/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2015-3-4
 */
package cn.com.sinosoft.formdesign.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.com.sinosoft.core.action.BaseController;
import cn.com.sinosoft.core.service.model.FormResult;
import cn.com.sinosoft.core.service.model.PageParam;
import cn.com.sinosoft.core.service.model.PagingResult;
import cn.com.sinosoft.formdesign.model.TFormdesignForm;
import cn.com.sinosoft.formdesign.model.TFormdesignFormdata;
import cn.com.sinosoft.formdesign.service.FormDesignService;

/**
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2015-3-4
 */
@Controller
@RequestMapping("formdesign")
public class FormDesignController extends BaseController {
	
	@Resource
	FormDesignService formDesignService;
	
	/**
	 * 表单添加
	 *
	 * 
	 * @param form
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("add")
	public FormResult add(String title, String content, String contentSrc, String id){
		return formDesignService.add(title, content, contentSrc, id);
	}
	
	/**
	 * 表单删除
	 *
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("del")
	public FormResult del(String id){
		return formDesignService.del(id);
	}

	/**
	 * 表单发布
	 *
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("pub")
	public FormResult pub(String id){
		return formDesignService.pub(id);
	}
	
	/**
	 * 取消发布
	 *
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("canclepub")
	public FormResult canclePub(String id){
		return formDesignService.cancelPub(id);
	}
	
	/**
	 * 通过id获取表单
	 *
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("getformbyid")
	public FormResult getFormById(String id){
		TFormdesignForm form = formDesignService.getFormById(id);
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("title", form.getTitle());
		item.put("id", form.getId());
		item.put("dtCreate", form.getDtUpdate());
		item.put("isPub", form.getIsPub());
		try {
			item.put("content", new String(form.getContent(), "UTF-8"));
			item.put("contentSrc", new String(form.getContentSrc(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("获取表单内容失败-不支持的编码");
		}
		FormResult ret = new FormResult();
		ret.setSuccess(FormResult.SUCCESS);
		ret.setData(item);
		return ret;
		
	}
	
	/**
	 * 分页查询表单
	 *
	 * 
	 * @param params
	 * @param pageParams
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("getforms")
	public PagingResult getForms(
			@RequestParam Map<String ,String> params,
			PageParam pageParams){
		return formDesignService.getForms(params, pageParams);
	}
	
	/**
	 * 分页查询表单数据
	 *
	 * 
	 * @param params
	 * @param pageParams
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("getformdatas")
	public PagingResult getFormDatas(
			@RequestParam Map<String ,String> params,
			PageParam pageParams){
		return formDesignService.getFormDatas(params, pageParams);
	}
	
	/**
	 * 表单数据录入页面
	 *
	 * 
	 * @param params
	 * @param pageParams
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("formdatainput/{formid}")
	public ModelAndView formdatainput(@PathVariable String formid){
		ModelAndView mav = new ModelAndView("views/forminput/forminput");
		TFormdesignForm form = formDesignService.getFormById(formid);
		mav.addObject("title", form.getTitle());
		mav.addObject("id", form.getId());
		mav.addObject("dtCreate", form.getDtUpdate());
		mav.addObject("isPub", form.getIsPub());
		try {
			mav.addObject("content", new String(form.getContent(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("获取表单内容失败-不支持的编码");
		}
		return mav;
	}
	
	/**
	 * 表单数据查看页面
	 *
	 * 
	 * @param params
	 * @param pageParams
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("/formdataview/{formdataid}")
	public ModelAndView formdataview(@PathVariable String formdataid){
		ModelAndView mav = new ModelAndView("views/forminput/forminputview");
		TFormdesignFormdata formdata = formDesignService.getFormDataById(formdataid);
		TFormdesignForm form = formDesignService.getFormById(formdata.getFormId());
		mav.addObject("title", form.getTitle());
		mav.addObject("dtFormCreate", form.getDtCreate());
		mav.addObject("dtFormDataCreate", formdata.getDtCreate());
		try {
			mav.addObject("formdata", new String(formdata.getFormData(), "UTF-8"));
			mav.addObject("content", new String(form.getContent(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("获取表单内容失败-不支持的编码");
		}
		return mav;
	}
	
	/**
	 * 表单数据录入-保存
	 *
	 * 
	 * @param params
	 * @param pageParams
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("formdatasave")
	public ModelAndView formdatasave(@RequestParam Map<String, Object> data, String formid){
		String dataid = formDesignService.formdatasave(data, formid);
		if(dataid == null){//保存失败
			ModelAndView mav = new ModelAndView("views/forminput/forminputerror");
			mav.addObject("info", "操作失败,服务器异常");
			return mav;
		}else{
			return new ModelAndView("redirect:/formdesign/formdataview/" + dataid);
		}
	}
	
	/**
	 * 表单数据删除
	 *
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@RequestMapping("formdatadel")
	public FormResult formDataDel(String id){
		return formDesignService.formDataDel(id);
	}
	
}
