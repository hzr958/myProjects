package com.smate.web.dyn.service.dynamic;

import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.dyn.dao.DynamicMsgDao;
import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPubDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.utils.exception.DynException;

/**
 * 
 * @author zjh 成果动态实现类
 *
 */
@Service("DynamicMsgService")
@Transactional(rollbackFor = Exception.class)
public class DynamicMsgServiceImpl implements DynamicMsgService {

  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private PsnConfigPubDao psnConfigPubDao;

  /**
   * 获取A人员当天XX模板动态数量
   */
  @Override
  public int getPubDynamicCount(Long psnId, Integer resType, String dynType) throws DynException {
    return dynamicMsgDao.getPubDynamicToday(psnId, resType, dynType);
  }

  /**
   * 检查成果是否允许发动态--公开的才可以
   * 
   */
  @Override
  public Boolean getPubPermit(Map<String, Object> mapData) throws DynException {
    // 成果的权限。只有7==公开 4==仅本人
    // 只检查dynType=B3TEMP
    String dynType = mapData.get("dynType").toString();
    Long pubId = NumberUtils.toLong(mapData.get("pubId").toString());
    if ("B3TEMP".equals(dynType)) {
      PsnConfigPub psnConfigPub = null;
      psnConfigPub = psnConfigPubDao.getPsnConfigPubByPubId(pubId);
      if (psnConfigPub != null && psnConfigPub.getAnyUser() == 7) {
        return true;
      }
      return false;
    }
    return true;
  }

  @Override
  public DynamicMsg getById(Long dynId) throws Exception {
    // TODO Auto-generated method stub
    return dynamicMsgDao.get(dynId);
  }

}
