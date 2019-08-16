package com.smate.center.open.service.data.wechat;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.model.wechat.WeChatRelation;
import com.smate.core.base.utils.number.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信关联检查
 */
@Transactional(rollbackFor = Exception.class)
public class WeChatRelationCheckImpl extends ThirdDataTypeBase {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private WeChatRelationDao  weChatRelationDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      List dataList = new ArrayList();
      Long openId = NumberUtils.parseLong(paramet.get(OpenConsts.MAP_OPENID).toString());
      WeChatRelation weChatRelation = weChatRelationDao.getBySmateOpenId(openId);
      Map resMap = new HashMap();
      if(weChatRelation != null){
        resMap.put("bind",true);
      }else{
        resMap.put("bind",false);
      }
      dataList.add(resMap);
      result.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      result.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      result.put(OpenConsts.MAP_DATA,dataList);
    } catch (Exception e) {
      logger.error("检查微信关联失败",e);
    }
    return result;
  }



}
