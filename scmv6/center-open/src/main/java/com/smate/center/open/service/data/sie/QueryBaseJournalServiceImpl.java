package com.smate.center.open.service.data.sie;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.data.isis.BaseJournalDao;
import com.smate.center.open.dao.pdwh.jnl.BaseJournalTitleDao;
import com.smate.center.open.model.pdwh.jnl.BaseJournal2;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.json.JacksonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询基础期刊.
 * 
 * @author tsz
 *
 */

@Transactional(rollbackFor = Exception.class)
public class QueryBaseJournalServiceImpl extends ThirdDataTypeBase {

  public static Integer MAX_SIZE = 10 ;



  @Autowired
  private BaseJournalTitleDao baseJournalTitleDao;
  @Autowired
  private BaseJournalDao baseJournalDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {

    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        paramet.putAll(dataMap);
      }
    }
    Object jnameObj = paramet.get(OpenConsts.MAP_JNAME);
    if (jnameObj == null || StringUtils.isBlank(jnameObj.toString())) {
      logger.error("期刊信息jname不能为空，jname=" + paramet.get(OpenConsts.MAP_JNAME));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2001, paramet, "scm-2001 具体服务类型参数  jname 不能为空");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> data = new HashMap<String, Object>();
    String jname = paramet.get(OpenConsts.MAP_JNAME).toString().toLowerCase();
    boolean isEnglish = org.apache.commons.lang.StringUtils.isAsciiPrintable(jname);
    List<BaseJournal2> list = baseJournalDao.findBaseJournal(jname, MAX_SIZE);
    if(CollectionUtils.isNotEmpty(list)){
      for(BaseJournal2 bj : list){
        Map map = new HashMap();
        dataList.add(map);
        map.put("jid",bj.getJouId());
        if(isEnglish){
          map.put("jname",StringUtils.isNotBlank(bj.getTitleEn()) ? bj.getTitleEn() : bj.getTitleXx());
        }else{
          map.put("jname",StringUtils.isNotBlank(bj.getTitleXx()) ? bj.getTitleXx() : bj.getTitleEn());

        }
        map.put("issn",StringUtils.isNotBlank(bj.getPissn()) ? bj.getPissn() : bj.getEissn());
      }
    }
    return successMap("获取通过期刊信息成功", dataList);
  }



}
