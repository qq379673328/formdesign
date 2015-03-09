/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-10-23
 */
package cn.com.sinosoft.core.service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.sinosoft.common.util.DbDialect;
import cn.com.sinosoft.common.util.PropertiesUtil;
import cn.com.sinosoft.core.dao.SimpleBaseDao;
import cn.com.sinosoft.core.service.model.ExtListData;
import cn.com.sinosoft.core.service.model.PageParam;
import cn.com.sinosoft.core.service.model.PagingResult;
import cn.com.sinosoft.core.service.model.PagingSql;
import cn.com.sinosoft.core.service.model.PagingSrcSql;

/**
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-10-23
 */
@Service
public class SimpleServiceImpl{
	
	private static final Logger log = LoggerFactory.getLogger(SimpleServiceImpl.class);
	
	@Resource
	protected SimpleBaseDao dao;
	private final static String dbConfigFilePath = "db.properties";
	private static String CUR_DIALECT = DbDialect.MYSQL;
	
	static{
		try {
			CUR_DIALECT = String.valueOf(PropertiesUtil.getValue(dbConfigFilePath, "db.dialect"));
		} catch (IOException e) {
			e.printStackTrace();
			log.error("数据库方言识别失败，请检查文件" + dbConfigFilePath);
		}
	}

	/**
	 * 保存
	 * 
	 * @param t
	 * @return
	 */
	public void saveBean(Object t) {
		dao.save(t);
	}

	/**
	 * 通过id查询
	 *
	 * 
	 * @param <T>
	 * @param id
	 * @return
	 */
	public <T> T queryById(String id, Class<T> clazz) {
		return dao.queryById(id, clazz);
	}

	/**
	 * 完全更新
	 * 
	 * @param t
	 * @return
	 */
	public <T> void update(T t) {
		dao.update(t);
	}
	
	/**
	 * 部分更新
	 * 
	 * @param t
	 * @return
	 */
	@Transactional
	public <T> void updatePart(Class<T> clazz, T t, String id, String[] ignorePro) {
		T obj = dao.getTemplate().get(clazz, id);
		BeanUtils.copyProperties(t, obj, ignorePro);
		dao.update(obj);
	}
	
	/**
	 * 加载对象
	 *
	 * 
	 * @param <T>
	 * @param t
	 * @param id
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public <T> T load(Class<T> t, String id){
		return dao.getTemplate().load(t, id);
	}
	
	/**
	 * 批量完全更新
	 *
	 * 
	 * @param <T>
	 * @param ts
	 */
	public <T> void batchUpdate(Collection<T> ts) {
		dao.batchUpdate(ts);
	}

	
	/**
	 * 删除
	 * 
	 * @param t
	 */
	public <T> void del(T t) {
		dao.del(t);
	}

	/**
	 * 批量删除
	 * 
	 * @param t
	 */
	public <T> void batchDel(Collection<T> ts) {
		dao.batchDel(ts);
	}
	
	/**
	 * 分页查询-使用此方法请实现getPagingSrcSql方法
	 * 
	 * @param params
	 * @return
	 */
	public PagingResult pagingSearch(Map<String, String> params, PageParam pageParams, PagingSrcSql srcSql) {
		PagingResult result = new PagingResult();
		PagingSql pagingSql = generatePagingSql(srcSql.getSrcSql(), pageParams);
		pagingSql.setTypes(srcSql.getTypes());
		pagingSql.setValues(srcSql.getValues());
		if(pageParams.getTotalPage() == 0){//未查询总数
			int totalPage = dao.queryCountBySql(pagingSql.getCountSql(), pagingSql.getValues(), pagingSql.getTypes());
			pageParams.setTotalPage(totalPage);
		}
		result.setTotal(pageParams.getTotalPage());
		result.setRows(dao.queryListBySql(pagingSql.getListSql(), pagingSql.getValues(), pagingSql.getTypes()));
		return result;
	}
	
	public PagingResult pagingSearch(PageParam pageParams, PagingSrcSql srcSql){
		return pagingSearch(null,pageParams,srcSql);
	}

	/**
	 * 生成分页sql
	 *
	 * 
	 * @param sql
	 * @param pageParams
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public PagingSql generatePagingSql(String sql, PageParam pageParams){
		PagingSql ps = new PagingSql();
		String countSql = "SELECT COUNT(1) FROM (" + sql + ") page_a";
		String listSql = renderPagingToSql(sql, pageParams);
		ps.setCountSql(countSql);
		ps.setListSql(listSql);
		return ps;
	}

	/**
	 * 根据方言处理分页sql
	 *
	 * 
	 * @param sql
	 * @param pageParams
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	private String renderPagingToSql(String sql, PageParam pageParams){
		if(pageParams.getPage() < 1){
			pageParams.setPage(1);
		}
		if(pageParams.getRows() < 0){
			pageParams.setRows(0);
		}
		StringBuffer sb = new StringBuffer();
		if(CUR_DIALECT.equals(DbDialect.ORACLE)){
			sb.append("SELECT * from (SELECT p_m.*, ROWNUM nm FROM (");
			sb.append(sql);
			sb.append(" )p_m )p_n WHERE p_n.nm > " + (pageParams.getPage() - 1) * pageParams.getRows());
			sb.append(" AND p_n.nm <=" + pageParams.getPage() * pageParams.getRows());
		}else if(CUR_DIALECT.equals(DbDialect.MYSQL)){
			sb.append(sql + " LIMIT " + (pageParams.getPage() - 1) * pageParams.getRows() + "," + pageParams.getRows());
		}else{
			throw new RuntimeException(CUR_DIALECT + " 方言未识别,可用方言为 oracle,mysql");
		}
		return sb.toString();
	}
	
	/**
	 * 通过反射为对象的属性赋值,需要存在set方法
	 *
	 * 
	 * @param obj 对象
	 * @param proName 属性名
	 * @param value 值
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public void setObjProValue(Object obj, String proName, Object value) throws Exception{
		Field field = obj.getClass().getDeclaredField(proName);
		field.setAccessible(true);
		field.set(obj, value);
	}
	/**
	 * 通过反射获取对象的属性值,需要存在get方法
	 *
	 * 
	 * @param obj 对象
	 * @param proName 属性名
	 * @param value 值
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public Object getObjProValue(Object obj, String proName) throws Exception{
		Field field = obj.getClass().getDeclaredField(proName);
		field.setAccessible(true);
		return field.get(obj);
	}
	
	/**
	 * 将map绑定到bean上
	 *
	 * 
	 * @param data
	 * @param cls
	 * @return
	 * @throws Exception
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public Object bindData2Class(Map<String, Object> data, Class<?> cls, String parentFieldName) throws Exception{
		Field[] fields = cls.getDeclaredFields();
		int len = fields.length;
		Object obj = cls.getConstructor().newInstance();
		for(int i = 0; i < len; i++){
			Field field = fields[i];
			field.setAccessible(true);
			String fieldTypeName = field.getType().getName();
			String fieldName = field.getName();
			Object fieldVal;
			if(parentFieldName == null){
				fieldVal = data.get(fieldName);
			}else{
				fieldVal = data.get(parentFieldName + "." + fieldName);
			}
			//以下类型确保覆盖所有基本类型
			if(fieldTypeName.equals("java.lang.String")){//字符串
				if(!StringUtils.isEmpty((String)fieldVal))
					field.set(obj, (String)fieldVal);
			}else if(fieldTypeName.equals("java.lang.Integer")){//整型
				if(!StringUtils.isEmpty((String)fieldVal))
					field.set(obj, Integer.parseInt((String)fieldVal));
			}else if(fieldTypeName.equals("java.lang.Float")){//浮点型
				if(!StringUtils.isEmpty((String)fieldVal))
					field.set(obj, Float.parseFloat((String)fieldVal));
			}else if(fieldTypeName.equals("java.lang.Double")){//双精度
				if(!StringUtils.isEmpty((String)fieldVal))
					field.set(obj, Double.parseDouble((String)fieldVal));
			}else if(fieldTypeName.equals("java.util.Date")){//日期类型-年月日
				if(!StringUtils.isEmpty((String)fieldVal))
					field.set(obj, new SimpleDateFormat("yyyy-MM-dd").parse((String)fieldVal));
			}else if(fieldTypeName.equals("java.sql.Timestamp")){//日期类型-年月日时分秒
				if(!StringUtils.isEmpty((String)fieldVal))
					field.set(obj, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String)fieldVal));
			}else if(fieldTypeName.equals("java.lang.Boolean")){//布尔型
				if(!StringUtils.isEmpty((String)fieldVal))
					field.set(obj, Boolean.parseBoolean((String)fieldVal));
			}else if(fieldTypeName.equals("java.lang.Long")){//long
				if(!StringUtils.isEmpty((String)fieldVal))
					field.set(obj, Long.parseLong((String)fieldVal));
			}else if(fieldTypeName.equals("java.lang.Short")){//short
				if(!StringUtils.isEmpty((String)fieldVal))
					field.set(obj, Short.parseShort((String)fieldVal));
			}else if(fieldTypeName.equals("java.util.Set")){//特别绑定Set集合
					field.set(obj, new HashSet(0));
			}else if(fieldTypeName.equals("java.sql.Blob")
					|| fieldTypeName.equals("java.sql.Clob")
					|| fieldTypeName.equals("java.lang.Byte")
					|| fieldTypeName.equals("java.lang.BigInteger")
					|| fieldTypeName.equals("java.lang.BigDecmail")
					|| fieldTypeName.equals("java.lang.Character")
					){//未绑定类型
				//do nothing
			}else{//假定为id类
				if(parentFieldName == null){
					field.set(obj, bindData2Class(data, Class.forName(fieldTypeName), fieldName));
				}else{
					field.set(obj, bindData2Class(data, Class.forName(fieldTypeName), parentFieldName + "." + fieldName));
				}
			}
		}
		return obj;
	}
	
	/**
	 * 转换扩展list  json数据为对象
	 *
	 * 
	 * @param jsonData
	 * @return
	 * @throws Exception
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public ExtListData transExtListJsonData(String jsonData) throws Exception{
		return new ObjectMapper().getJsonFactory()
			.createJsonParser(jsonData)
			.readValueAs(ExtListData.class);
	}
	
}
