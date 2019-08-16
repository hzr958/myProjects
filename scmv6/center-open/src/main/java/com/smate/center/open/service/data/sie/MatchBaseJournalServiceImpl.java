package com.smate.center.open.service.data.sie;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.data.isis.BaseJournalDao;
import com.smate.center.open.dao.pdwh.jnl.BaseJournalTitleDao;
import com.smate.center.open.model.pdwh.jnl.BaseJournalTitleTo;
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
 * 匹配基础期刊.
 * 
 * @author tsz
 *
 */

@Transactional(rollbackFor = Exception.class)
public class MatchBaseJournalServiceImpl extends ThirdDataTypeBase {



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
    Object issnObj = paramet.get(OpenConsts.MAP_ISSN);
    if (jnameObj == null || StringUtils.isBlank(jnameObj.toString())) {
      logger.error("期刊信息jname不能为空，jname=" + paramet.get(OpenConsts.MAP_JNAME));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2001, paramet, "scm-2001 具体服务类型参数  jname 不能为空");
      return temp;
    }
    if (issnObj == null || StringUtils.isBlank(issnObj.toString())) {
      logger.error("期刊信息缺少issn，issn=" + paramet.get(OpenConsts.MAP_ISSN));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2003, paramet, "缺少issn参数");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> data = new HashMap<String, Object>();
    String jname = paramet.get(OpenConsts.MAP_JNAME).toString();
    String issn = paramet.get(OpenConsts.MAP_ISSN).toString();
    // 匹配
    Long baseJnlId = null;
    if (StringUtils.isNotBlank(jname) && StringUtils.isNotBlank(issn)) {
      jname = jname.trim();
      issn = issn.trim();
      baseJnlId = baseJournalDao.snsJnlMatchBaseJnlId(jname, issn);
      if (baseJnlId == null) {
        List<BaseJournalTitleTo> bjtList = baseJournalTitleDao.snsJnlMatchBaseJnlId(jname, issn);
        baseJnlId = CollectionUtils.isNotEmpty(bjtList) ? bjtList.get(0).getJnlId() : null;
      }
    }
    data.put("baseJnlId", baseJnlId);
    dataList.add(data);
    return successMap("获取通过期刊信息成功", dataList);
  }



}
