/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2015-3-4
 */
package cn.com.sinosoft.formdesign.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.sinosoft.common.util.SqlUtil;
import cn.com.sinosoft.common.util.StrUtils;
import cn.com.sinosoft.core.dao.SimpleBaseDao;
import cn.com.sinosoft.core.service.SimpleServiceImpl;
import cn.com.sinosoft.core.service.model.FormResult;
import cn.com.sinosoft.core.service.model.PageParam;
import cn.com.sinosoft.core.service.model.PagingResult;
import cn.com.sinosoft.core.service.model.PagingSrcSql;
import cn.com.sinosoft.formdesign.model.TFormdesignForm;
import cn.com.sinosoft.formdesign.model.TFormdesignFormdata;

/**
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2015-3-4
 */
@Service
public class FormDesignService extends SimpleServiceImpl{
	
	@Resource
	SimpleBaseDao baseDao;

	/**
	 * 添加表单
	 * 
	 * @param form
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@Transactional
	public FormResult add(String title, String content, String contentSrc, String id) {
		if(!StrUtils.isNull(id)){
			//删除原始表单以及相关数据
			String delFormSql = "delete from t_formdesign_form where id = ? ";
			String delDataSql = "delete from t_formdesign_formdata where form_id = ? ";
			baseDao.executeDelOrUpdateSql(delFormSql, new String[]{id}, new Type[]{StringType.INSTANCE});
			baseDao.executeDelOrUpdateSql(delDataSql, new String[]{id}, new Type[]{StringType.INSTANCE});
		}
		FormResult result = new FormResult();
		Date date = new Date();
		TFormdesignForm form = new TFormdesignForm();
		form.setDtCreate(date);
		form.setDtUpdate(date);
		form.setId(UUID.randomUUID().toString());
		form.setIsPub("0");
		try{
			form.setContent(content.getBytes("utf-8"));
			form.setContentSrc(contentSrc.getBytes("utf-8"));
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result.setSuccess(FormResult.ERROR);
			result.setMessage("保存失败");
			return result;
		}
		form.setTitle(title);
		baseDao.save(form);
		result.setSuccess(FormResult.SUCCESS);
		return result;
	}
	
	/**
	 * 删除表单
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@Transactional
	public FormResult del(String id) {
		String delFormSql = "delete from t_formdesign_form where id = ? ";
		String delDataSql = "delete from t_formdesign_formdata where form_id = ? ";
		baseDao.executeDelOrUpdateSql(delFormSql, new String[]{id}, new Type[]{StringType.INSTANCE});
		baseDao.executeDelOrUpdateSql(delDataSql, new String[]{id}, new Type[]{StringType.INSTANCE});
		
		FormResult result = new FormResult();
		result.setSuccess(FormResult.SUCCESS);
		return result;
	}

	/**
	 * 发布表单
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@Transactional
	public FormResult pub(String id) {
		String sql = " update t_formdesign_form set is_pub = 1 where id = ? ";
		baseDao.executeDelOrUpdateSql(sql, new Object[]{id}, new Type[]{StringType.INSTANCE});
		FormResult result = new FormResult();
		result.setSuccess(FormResult.SUCCESS);
		return result;
	}

	/**
	 * 取消发布表单
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@Transactional
	public FormResult cancelPub(String id) {
		String sql = " update t_formdesign_form set is_pub = 0 where id = ? ";
		baseDao.executeDelOrUpdateSql(sql, new Object[]{id}, new Type[]{StringType.INSTANCE});
		FormResult result = new FormResult();
		result.setSuccess(FormResult.SUCCESS);
		return result;
	}

	/**
	 * 获取表单列表
	 * 
	 * @param params
	 * @param pageParams
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public PagingResult getForms(Map<String, String> params,
			PageParam pageParams) {
		PagingSrcSql srcSql = getFormsListSql(params);
		return pagingSearch(params, pageParams, srcSql);
	}
	
	/**
	 * 获取表单列表原始sql
	 *
	 * 
	 * @param params
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	private PagingSrcSql getFormsListSql(Map<String, String> params) {
		PagingSrcSql srcSql = new PagingSrcSql();
		List<Object> values = new ArrayList<Object>();
		List<Type> types = new ArrayList<Type>();
		StringBuffer sb = new StringBuffer(" SELECT t.id,t.title,t.dt_create,t.is_pub,IFNULL(m.cou, 0) cou " +
				"FROM t_formdesign_form t " +
				"LEFT JOIN ( SELECT form_id, COUNT(1) cou " +
				"FROM t_formdesign_formdata GROUP BY form_id) " +
				"m ON t.id = m.form_id ");
		if(!StrUtils.isNull(params.get("title"))){//标题
			sb.append(" AND t.title like ? ");
			values.add("%" + params.get("apanagecode") + "%");
			types.add(StringType.INSTANCE);
		}
		if(!StrUtils.isNull(params.get("ispub"))){//发布状态
			sb.append(" AND t.is_pub = ? ");
			values.add(params.get("ispub"));
			types.add(StringType.INSTANCE);
		}
		if(!StrUtils.isNull(params.get("dtcreate_begin"))){//创建日期-开始
			sb.append(" AND " + SqlUtil.toDate(params.get("dtcreate_begin"), 1, 0) + " <= t.dt_create ");
		}
		if(!StrUtils.isNull(params.get("dtcreate_end"))){//创建日期-结束
			sb.append(" AND " + SqlUtil.toDate(params.get("dtcreate_end"), 1, 0) + " >= t.dt_create ");
		}
		sb.append(" order by t.dt_create desc ");
		srcSql.setSrcSql(sb.toString());
		srcSql.setTypes(types.toArray(new Type[0]));
		srcSql.setValues(values.toArray());
		return srcSql;
	}

	/**
	 * 获取表单数据列表
	 * 
	 * @param params
	 * @param pageParams
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public PagingResult getFormDatas(Map<String, String> params,
			PageParam pageParams) {
		PagingSrcSql srcSql = getFormsDataListSql(params);
		return pagingSearch(params, pageParams, srcSql);
	}
	
	/**
	 * 获取表单数据列表原始sql
	 *
	 * 
	 * @param params
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	private PagingSrcSql getFormsDataListSql(Map<String, String> params) {
		PagingSrcSql srcSql = new PagingSrcSql();
		List<Object> values = new ArrayList<Object>();
		List<Type> types = new ArrayList<Type>();
		
		StringBuffer sb = new StringBuffer(" select id,dt_create from t_formdesign_formdata " +
				" where 1=1 ");
		if(!StrUtils.isNull(params.get("formid"))){//标题
			sb.append(" AND form_id = ? ");
			values.add(params.get("formid"));
			types.add(StringType.INSTANCE);
		}
		if(!StrUtils.isNull(params.get("dtcreate_begin"))){//上报日期-开始
			sb.append(" AND " + SqlUtil.toDate(params.get("dtcreate_begin"), 1, 0) + " <= dt_create ");
		}
		if(!StrUtils.isNull(params.get("dtcreate_end"))){//上报日期-结束
			sb.append(" AND " + SqlUtil.toDate(params.get("dtcreate_end"), 1, 0) + " >= dt_create ");
		}
		sb.append(" order by dt_create desc ");
		srcSql.setSrcSql(sb.toString());
		srcSql.setTypes(types.toArray(new Type[0]));
		srcSql.setValues(values.toArray());
		return srcSql;
	}
	
	/**
	 * 获取表单数据信息
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public TFormdesignFormdata getFormDataById(String id) {
		return baseDao.queryById(id, TFormdesignFormdata.class);
	}
	
	/**
	 * 获取表单信息
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public TFormdesignForm getFormById(String id) {
		return baseDao.queryById(id, TFormdesignForm.class);
	}

	/**
	 * 表单数据保存
	 * 
	 * @param data 提交的数据
	 * @param formid 表单id
	 * @return 数据id
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@Transactional
	public String formdatasave(Map<String, Object> data, String formid) {
		//解析data中数据为json格式
		TFormdesignFormdata formData = new TFormdesignFormdata();
		String formDataId = UUID.randomUUID().toString();
		Date date = new Date();
		formData.setDtCreate(date);
		formData.setDtUpdate(date);
		formData.setId(formDataId);
		formData.setFormId(formid);
		try {
			formData.setFormData(transData2Json(data));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		baseDao.save(formData);
		return formDataId;
	}
	private byte[] transData2Json(Map<String, Object> data) throws Exception{
		ObjectMapper om =  new ObjectMapper();
		return om.writeValueAsBytes(data);
	}
	
	/**
	 * 删除表单数据
	 * 
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@Transactional
	public FormResult formDataDel(String id) {
		String delDataSql = "delete from t_formdesign_formdata where id = ? ";
		baseDao.executeDelOrUpdateSql(delDataSql, new String[]{id}, new Type[]{StringType.INSTANCE});
		
		FormResult result = new FormResult();
		result.setSuccess(FormResult.SUCCESS);
		return result;
	}
	
}
