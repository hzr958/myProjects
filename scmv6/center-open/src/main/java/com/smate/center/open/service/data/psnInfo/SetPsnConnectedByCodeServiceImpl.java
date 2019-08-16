package com.smate.center.open.service.data.psnInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.snscode.IrisSnsCodeService;
import com.smate.core.base.utils.dao.security.SysRolUserDao;
import com.smate.core.base.utils.model.cas.security.SysRolUser;

/**
 * 根据验证码设置帐号关联。首先根据:psnGuidID,psnID,验证码进行验证,如果验证码不存在或验证码的生成时间是24小时之前则验证失败,验证成功后进行帐号关联:
 * 
 * @author tsz
 * 
 *         guid 是通过不了验证的 所以不能使用 公用的身份验证
 * 
 *
 */
@Transactional(rollbackFor = Exception.class)
@Deprecated // 废弃的 新系统只支持 openid的绑定
public class SetPsnConnectedByCodeServiceImpl extends ThirdDataTypeBase {


  @Autowired
  private IrisSnsCodeService irisSnsCodeService;
  @Autowired
  private SysRolUserDao sysRolUserDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {

    return null;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {

    int flag = 0;
    Long psnId = NumberUtils.toLong(paramet.get("psnId").toString());
    String psnGuidID = paramet.get("psnGuidID").toString();
    Long insId = NumberUtils.toLong(paramet.get("insId").toString());
    SysRolUser sysRolUser = new SysRolUser();
    sysRolUser.setInsId(insId);
    sysRolUser.setGuid(psnGuidID);
    sysRolUser.setPsnId(psnId);
    sysRolUser.setFlag(sysRolUser.FLAG_SNS_CODE);// 验证码关联
    sysRolUser.setLastDate(new Date());
    sysRolUserDao.save(sysRolUser);
    flag = 1;

    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_FLAG, flag);
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    dataList.add(temp);
    return super.successMap("保存数据成功", null);
  }

}
