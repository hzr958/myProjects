package com.smate.center.open.service.data.checkopenid;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.dao.psn.SysMergeUserHistoryDao;
import com.smate.center.open.dao.union.his.OpenUserUnionHisDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.model.psn.SysMergeUserHis;
import com.smate.center.open.model.union.his.OpenUserUnionHis;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.login.ThirdLoginService;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

/**
 * 验证 openid是否在科研之友 被合并， 如果有被合并了 返回对应的新的openid
 * 
 * 如果对应的openid与 对应的token没有关联记录 就创建一个关联记录
 * 
 * @author ajb
 *
 */

@Transactional(rollbackFor = Exception.class)
public class CheckOpenidServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private OpenUserUnionHisDao openUserUnionHisDao;
  @Autowired
  private SysMergeUserHistoryDao sysMergeUserHistoryDao;

  @Autowired
  private ThirdLoginService thirdLoginService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 没有 参数校验 直接返回成功
    Map<String, Object> temp = new HashMap<String, Object>();

    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object openid = serviceData.get("openid");
    if (openid == null || StringUtils.isBlank(openid.toString())) {
      logger.error("服务参数   openid不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_296, paramet, "scm-296 具体服务类型参数  openid 不能为空");
      return temp;
    }
    paramet.put("ckeckOpenid", openid);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 
   * 
   * @param psnId
   * @throws OpenException
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      Map<String, Object> infoMap = new HashMap<String, Object>();
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

      Long checkOpenid = Long.parseLong(paramet.get("ckeckOpenid").toString());
      String token = paramet.get("token").toString();
      OpenUserUnionHis openUserUnionHis = openUserUnionHisDao.getByOpenidAndToken(checkOpenid, token);
      // 未处理
      if (openUserUnionHis != null && openUserUnionHis.getStatus() == 0) {
        SysMergeUserHis sysMergeUserHis = sysMergeUserHistoryDao.getMergeByDelPsnId(openUserUnionHis.getPsnId());
        if (sysMergeUserHis != null) {
          // 保存人的psnId
          Long psnId = sysMergeUserHis.getPsnId();
          Long openId = openUserUnionDao.getOpenIdByPsnId(psnId);
          if (openId != null) {
            // 如果不等于空 判断有没有关联记录 没有就创建关联记录
            createOpenUnion(token, psnId, openId);
          } else {
            // 没有openid产生openid
            openId = thirdLoginService.getOpenId(token, psnId, OpenConsts.OPENID_CREATE_TYPE_6);
          }
          openUserUnionHis.setDealDate(new Date());
          openUserUnionHis.setStatus(1);
          openUserUnionHisDao.save(openUserUnionHis);
          infoMap.put("openid", openId.toString());
        } else {
          // 没有
          infoMap.put("openid", "");
        }
      } else {
        // 没有
        infoMap.put("openid", "");
      }

      dataList.add(infoMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("检查openid是否合并异常 map=" + paramet.toString());
      throw new OpenSerGetPsnInfoException(e);
    }
  }

  private void createOpenUnion(String tempToken, Long psnId, Long openId) {
    OpenUserUnion temp = openUserUnionDao.getOpenUserUnion(openId, tempToken);
    if (temp == null) {
      temp = new OpenUserUnion();
      temp.setCreateDate(new Date());
      temp.setOpenId(openId);
      temp.setCreateType(OpenConsts.OPENID_CREATE_TYPE_1);
      temp.setToken(tempToken);
      temp.setPsnId(psnId);
      openUserUnionDao.save(temp);
    }
  }

}
