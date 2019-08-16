package com.smate.sie.center.open.service.psn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.consts.SieConstPositionDao;
import com.smate.core.base.utils.model.consts.SieConstPosition;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.sie.center.open.dao.dept.Sie6InsPsnDao;
import com.smate.sie.center.open.model.dept.SiePsnIns;

/**
 * @author Yexingyuan
 */
@Transactional(rollbackFor = Exception.class)
public class SyncSNSPsnInfoServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private Sie6InsPsnDao personDao;

  @Autowired
  private SieConstPositionDao constpositionDao;

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
    // 获取参数并执行校验
    // 校验PSN_ID的值
    Object psnId = serviceData.get(OpenConsts.MAP_PSNID);

    if (psnId == null || !NumberUtils.isDigits(psnId.toString())) {
      logger.error("获取通过psnId不能为空，必须为数字，psnId=" + paramet.get(OpenConsts.MAP_PSNID));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2002, paramet, "scm-2002 具体服务类型参数  psnId 不能为空，必须为数字");
      return temp;
    }
    paramet.put("psnId", psnId);

    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> temp1 = new HashMap<String, Object>();
    Map<String, Object> infoMap = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    try {
      Long psnId = Long.parseLong(paramet.get(OpenConsts.MAP_PSNID).toString());
      List<SiePsnIns> psnlist = personDao.getSiePsnInsByPsnIns(psnId);
      Map<String, Object> datamap = super.checkDataMapParamet(paramet, temp1);
      if (psnlist.size() > 0) {
        for (SiePsnIns siePsnIns : psnlist) {
          SiePsnIns ispsnins = dealWithSiePsnIns(siePsnIns, datamap);
          personDao.save(ispsnins);
        }
        temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      } else {
        temp.put(OpenConsts.RESULT_MSG, "该用户未加入任何单位，不执行更新");
      }
      dataList.add(infoMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("处理SNS同步至ROL数据时出错：" + paramet.toString());
      throw new OpenSerGetPsnInfoException(e);
    }
  }

  private SiePsnIns dealWithSiePsnIns(SiePsnIns psn, Map<String, Object> paramet) {
    SiePsnIns psntemp = psn;
    if (psntemp != null) {
      if (null != paramet.get("email") && StringUtils.isNotBlank(paramet.get("email").toString())) {
        psntemp.setEmail(paramet.get("email").toString());
      }
      if (null != paramet.get("zhName") && StringUtils.isNotBlank(paramet.get("zhName").toString())) {
        psntemp.setZhName(paramet.get("zhName").toString());
      }
      if (null != paramet.get("enName") && StringUtils.isNotBlank(paramet.get("enName").toString())) {
        psntemp.setEnName(paramet.get("enName").toString());
      }
      if (null != paramet.get("firstName") && StringUtils.isNotBlank(paramet.get("firstName").toString())) {
        psntemp.setFirstName(paramet.get("firstName").toString());
      }
      if (null != paramet.get("lastName") && StringUtils.isNotBlank(paramet.get("lastName").toString())) {
        psntemp.setLastName(paramet.get("lastName").toString());
      }
      if (null != paramet.get("sex") && StringUtils.isNotBlank(paramet.get("sex").toString())) {
        psntemp.setSex(Integer.valueOf(paramet.get("sex").toString()));
      }
      // 设置职称和职称ID
      if (null != paramet.get("position")) {
        String positionStr = paramet.get("position").toString();
        if (StringUtils.isNotBlank(positionStr)) {
          psntemp.setPosition(positionStr);
          if (constpositionDao.getPosByName(positionStr) != null) {
            SieConstPosition sieConstPosition = constpositionDao.getPosByName(positionStr);
            psntemp.setPosId(sieConstPosition.getId());
            psntemp.setPosGrades(
                sieConstPosition.getPosGrades() == null ? null : Integer.valueOf(sieConstPosition.getPosGrades()));
          } else {
            psntemp.setPosId(null);
            psntemp.setPosGrades(null);
          }
        }
      }
      // 电话
      if (null != paramet.get("tel") && StringUtils.isNotBlank(paramet.get("tel").toString())) {
        // psntemp.setTel(paramet.get("tel").toString());
        psntemp.setMobile(paramet.get("tel").toString());
      }
      if (null != paramet.get("avatars") && StringUtils.isNotBlank(paramet.get("avatars").toString())) {
        psntemp.setAvatars(paramet.get("avatars").toString());
      }
      if (null != paramet.get("regionId") && StringUtils.isNotBlank(paramet.get("regionId").toString())) {
        psntemp.setRegion_id(Long.valueOf(paramet.get("regionId").toString()));
      }
    }
    return psntemp;
  }

}
