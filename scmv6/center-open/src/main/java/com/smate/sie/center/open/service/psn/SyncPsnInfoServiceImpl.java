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
import com.smate.center.open.dao.profile.PersonEmailDao;
import com.smate.center.open.dao.psn.PdwhAddrPsnUpdateRecordDao;
import com.smate.center.open.dao.register.PersonRegisterDao;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.model.profile.PersonEmail;
import com.smate.center.open.model.psn.PdwhAddrPsnUpdateRecord;
import com.smate.center.open.model.register.PersonRegister;
import com.smate.center.open.service.data.ThirdDataTypeBase;

@Transactional(rollbackFor = Exception.class)
public class SyncPsnInfoServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonRegisterDao personDao;
  @Autowired
  private PersonEmailDao personEmailDao;
  @Autowired
  private PdwhAddrPsnUpdateRecordDao pdwhAddrPsnUpdateRecordDao;

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
    Object email = serviceData.get("email");
    if (email == null) {
      logger.error("服务参数 邮件不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_285, paramet, "服务参数 邮件 email不能为空");
      return temp;
    }
    paramet.put("email", email);
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

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> infoMap = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    try {

      Long psnId = Long.parseLong(paramet.get(OpenConsts.MAP_PSNID).toString());
      String email = paramet.get("email").toString();
      PersonRegister psn = personDao.get(psnId);
      if (psn != null) {
        psn.setEmail(email);
        personDao.save(psn);
        // 更新psn_email
        PersonEmail psnEmail = null;
        psnEmail = personEmailDao.getByPsnId(psnId, email);
        if (psnEmail == null) {
          psnEmail = new PersonEmail();
          psnEmail.setEmail(email);
          String[] emailPart = email.split("@");
          psnEmail.setLeftPart(emailPart[0]);
          psnEmail.setRightPart(emailPart[1]);
          psnEmail.setPerson(psn);
        }
        // 是否验证过的邮件
        psnEmail.setVerify(true);
        psnEmail.setFirstMail(true);
        // 由于注册时调用了center-open系统里面的逻辑，在open系统里会保存sys_user记录，设置注册邮箱为登录邮箱
        psnEmail.setLoginMail(true);
        personEmailDao.save(psnEmail);
        List<PersonEmail> list = personEmailDao.getByPsnIdAndLoginEmailAndFirstEmail(psnId, email);
        if (list != null && list.size() > 0) {
          for (PersonEmail pm : list) {
            pm.setFirstMail(false);
            pm.setLoginMail(false);
            personEmailDao.save(pm);
          }
        }
        pdwhAddrPsnUpdateRecordDao.deleteRecordByPsnId(psnId);
        PdwhAddrPsnUpdateRecord record = new PdwhAddrPsnUpdateRecord(psnId, 2, 0);
        pdwhAddrPsnUpdateRecordDao.save(record);
      }
      dataList.add(infoMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;

    } catch (Exception e) {
      logger.error("用户的email 异常  map=" + paramet.toString());
      throw new OpenSerGetPsnInfoException(e);
    }

  }

}
