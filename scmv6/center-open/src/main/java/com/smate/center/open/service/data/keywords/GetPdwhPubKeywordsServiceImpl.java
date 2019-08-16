package com.smate.center.open.service.data.keywords;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.keyword.PdwhPubkeywordsService;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 获取基准库的关键词
 * 
 * @author aijiangbin
 * @date 2018年4月25日
 */
@Transactional(rollbackFor = Exception.class)
public class GetPdwhPubKeywordsServiceImpl extends ThirdDataTypeBase {


  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubkeywordsService pdwhPubkeywordsService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> parameter) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = parameter.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        parameter.putAll(dataMap);
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> parameter) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Object pubIdObj = parameter.get("pubId");
    Object pageNoObj = parameter.get("pageNo");
    /*
     * if(pubIdObj !=null && NumberUtils.isNumber(pubIdObj.toString())) { dataList =
     * pdwhPubkeywordsService.getKeywordByPubId(Long.parseLong(pubIdObj.toString())); }else if(pageNoObj
     * !=null && NumberUtils.isNumber(pageNoObj.toString())){ dataList =
     * pdwhPubkeywordsService.getKeywordByPageNo(Integer.parseInt(pageNoObj.toString())); }else{
     * dataList = pdwhPubkeywordsService.getNeedTranslateKeyword(); }
     */
    getCon();
    return successMap("成功获取需要翻译的关键词", dataList);

  }

  /**
   * 通过JNDI获取数据源在获取连接对象
   * 
   * @return Connection con
   */
  public static Connection getCon() {
    Connection con = null;
    try {
      Context ic = new InitialContext();
      ComboPooledDataSource source = (ComboPooledDataSource) ic.lookup("java:comp/env/jdbc/sns");
      source.getPassword();
      System.out.println(source.getPassword());
      System.out.println(source.getUser());
      System.out.println(source.getMaxPoolSize());
      System.out.println(source.getJdbcUrl());

    } catch (Exception e) {
      System.out.println("数据源没找到！");
      e.printStackTrace();
    }
    return con;
  }



}
