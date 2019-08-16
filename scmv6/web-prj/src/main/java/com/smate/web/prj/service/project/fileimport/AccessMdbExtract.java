package com.smate.web.prj.service.project.fileimport;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Table;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class AccessMdbExtract {
	private static Logger logger = LoggerFactory.getLogger(AccessMdbExtract.class);
	private static final String DEFAULTCHARSET = "GBK";

	@SuppressWarnings({ "unchecked" })
	public static List<Map<String, Object>> getData(String filepath, String tableName) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Table table = null;
			try {
				table = Database.open(new File(filepath), false, true, Charset.forName(DEFAULTCHARSET), null)
						.getTable(tableName);
			} catch (Exception e) {
				logger.error(e.getMessage());
				table = Database.open(new File(filepath)).getTable(tableName);
			}
			for (Map<String, Object> row : table) {
				Map map = convertObjType(row);
				list.add(map);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("读取数据文件出错");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("读取数据文件出错");
		}
		return list;
	}

	@SuppressWarnings({ "unchecked" })
	public static List<Map<String, Object>> getData(File dataFile, String tableName) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Table table = null;
			try {
				table = Database.open(dataFile, true, false, Charset.forName(DEFAULTCHARSET), null).getTable(tableName);
			} catch (Exception e) {
				logger.error(e.getMessage());
				table = Database.open(dataFile).getTable(tableName);
			}
			for (Map<String, Object> row : table) {
				Map map = convertObjType(row);
				list.add(map);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new Exception("读取数据文件出错");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("读取数据文件出错");
		}
		return list;
	}

	public static List<Map<String, Object>> getData(File dataFile) throws Exception {
		List<Map<String, Object>> list = null;
		String tableName = getTableNames(dataFile);
		if (StringUtils.isNotBlank(tableName)) {
			list = getData(dataFile, tableName);
		}
		return list;
	}

	public static String getTableNames(File dataFile) throws Exception {
		try {
			Set<String> tabNames = Database.open(dataFile, true, false, Charset.forName(DEFAULTCHARSET), null)
					.getTableNames();
			return tabNames.iterator().next();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	/**
	 * sql.append(
	 * "select id,xmbh,zwmc,ywmc,xmlb,gjszth,xkdm1,xkdm2,kxbm,yjxz,sqje, format(qsny,'yyyy-mm-dd') as sqqsny,");
	 * sql.append("format(zzny,'yyyy-mm-dd') as sqzzny,sqrxs,"); sql.append(
	 * "syslb,sysdm,xm,py,xb,dzyj, format(csny,'yyyy-mm-dd') as sqcsny,sfzh,mzdm,zcdm,xw,gjdqdm,dwmc,dwdm,dwxz,");
	 * sql.append("lsgxdm,yzbm,sqrdh,szss,szcs,xxdz,zrs,gjrs,zjrs,cjrs,bshrs,bssrs,sssrs,"); sql.append(
	 * "cjdws,zwztc,ywztc,zy,jb,xs,xm1,sfzh1,xb1, format(csny1,'yyyy-mm-dd')  as sqcsny1,dwdm1,dwmc1,role1,zcdm1,xm2,sfzh2,"
	 * ); sql.append("xb2, format(csny2,'yyyy-mm-dd')  as sqcsny2,dwdm2,dwmc2,role2,zcdm2,xm3,sfzh3,xb3,format(csny3,'yyyy-mm-dd') as sqcsny3,"
	 * ); sql.append(
	 * "dwdm3,dwmc3,role3,zcdm3,xm4,sfzh4,xb4,format(csny4,'yyyy-mm-dd')  as sqcsny4,dwdm4,dwmc4,role4,zcdm4,xm5,sfzh5,xb5,"
	 * ); sql.append("format(csny5,'yyyy-mm-dd')  as sqcsny5,dwdm5,dwmc5,role5,zcdm5,xm6,sfzh6,xb6,format(csny6,'yyyy-mm-dd')  as sqcsny6,dwdm6,"
	 * ); sql.append("dwmc6,role6,zcdm6,xm7,sfzh7,xb7,format(csny7,'yyyy-mm-dd') as sqcsny7,dwdm7,dwmc7,role7,zcdm7,");
	 * sql.append("xm8,sfzh8,xb8,format(csny8,'yyyy-mm-dd') as sqcsny8,dwdm8,dwmc8,role8,"); sql.append("zcdm8,xm9,sfzh9,xb9,format(csny9,'yyyy-mm-dd') as sqcsny9,dwdm9,dwmc9,role9,zcdm9,grant_type_id,sub_grant_type_id,grant_description, "
	 * ); sql.append(
	 * "dzyj1,dzyj2,dzyj3,dzyj4,dzyj5,dzyj6,dzyj7,dzyj8,dzyj9,cyrdh1,cyrdh2,cyrdh3,cyrdh4,cyrdh5,cyrdh6,cyrdh7,cyrdh8,cyrdh9"
	 * ); sql.append(" from Export order by id");.
	 * 
	 * @param row
	 */
	@SuppressWarnings({ "unchecked" })
	private static Map convertObjType(Map<String, Object> row) {
		Map map = new HashMap();
		for (Iterator iterator = row.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			Object obj = row.get(key);
			if ("qsny".equalsIgnoreCase(key)) {
				key = "SQQSNY";
			}
			if ("zzny".equalsIgnoreCase(key)) {
				key = "SQZZNY";
			}
			if ("CSNY".equalsIgnoreCase(key)) {
				key = "SQCSNY";
			} else if (!"CSNY".equalsIgnoreCase(key) && (key.indexOf("CSNY") > -1 || key.indexOf("csny") > -1)) {
				key = "SQ" + key.toUpperCase();
			}
			if (obj != null && obj instanceof Date) {
				Date date = (Date) obj;
				String pattern = "yyyy-MM-dd";
				SimpleDateFormat formatter = new SimpleDateFormat(pattern);
				String strDate = formatter.format(date);
				map.put(key, strDate);
			} else {
				map.put(key, obj);
			}
		}
		return map;
	}
}
