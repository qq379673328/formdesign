/**
 * 
 *
 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date 2014-11-12
 */
package cn.com.sinosoft.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Service;

/**
 *
 * @author	<a href="mailto:nytclizy@gmail.com">李志勇</a>
 * @date	2014-11-12
 */
@Service
public class CommonService extends SimpleServiceImpl {

	/**
	 * 获取某类型码表
	 * 
	 * @param type
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public Object getTypeCode(String type) {
		return dao.queryListBySql("select * from t_com_biz_codes where codetype = ? and isuse = 1 ",
				new String[]{type},
				new Type[]{StringType.INSTANCE} );
	}

	/**
	 * 获取机构列表
	 * 
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public Object getOrgList() {
		return dao.queryListBySql("select * from t_com_org_local WHERE isuse = 1  ",
				null,
				null);
	}
	
	/**
	 * 获取街道列表
	 * 
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public Object getStreetList() {
		return dao.queryListBySql("SELECT t.zonecode, t.zonename FROM t_com_zonecode_local t WHERE t.zonelevel = '4' and isuse = 1 ",
				null,
				null);
	}

	/**
	 * 获取村列表
	 * 
	 * @param streetCode
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	public Object getVillageList(String streetCode){
		if(streetCode == null || streetCode.trim().equals("")){
			return new ArrayList<Object>();
		}
		String sCode = null;
		if(streetCode.length() == 12){//本地区划码
			sCode = streetCode.substring(0, 9);
		}else if(streetCode.length() == 8){//全国标准区划
			//补0
			sCode = streetCode.substring(0, 6) + "0" + streetCode.substring(6, 2);
		}else{
			return new ArrayList<Object>();
		}
		sCode += "%";
		return dao.queryListBySql("SELECT t.zonecode, t.zonename FROM t_com_zonecode_local t WHERE t.zonelevel = '5' and isuse = 1" +
				" and t.zonecode like ? ",
				new Object[]{sCode},
				new Type[]{StringType.INSTANCE});
	}

	/**
	 *  获取码值获取码名-本地区划
	 * 
	 * @param code
	 * @return
	 * @author <a href="mailto:nytclizy@gmail.com">李志勇</a>
	 */
	@SuppressWarnings("unchecked")
	public Object getLocalZoneNameByCode(String code) {
		Collection<Object> items = dao.queryListBySql("SELECT t.zonename FROM t_com_zonecode_local t WHERE t.zonecode = ? and isuse = 1",
				new Object[]{code},
				new Type[]{StringType.INSTANCE});
		if(items.size() > 0){
			return ((Map<String, Object>)(items.toArray()[0])).get("zonename");
		}else{
			return "";
		}
	}
	
}
