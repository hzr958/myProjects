package com.smate.center.batch.service.pub;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.DynamicExtendDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.DynamicExtend;

/**
 * 动态扩展信息业务实现.
 * 
 * @author chenxiangrong
 * 
 */
@Service("dynamicExtendService")
@Transactional(rollbackFor = Exception.class)
public class DynamicExtendServiceImpl implements DynamicExtendService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final int TOP_N = 3;
  @Autowired
  private DynamicExtendDao dynamicExtendDao;

  /**
   * 保存动态扩展信息.
   */
  @Override
  public void saveDynamicExtends(String resDetailJson, Long dynId, int resType) throws ServiceException {
    DynamicExtend dynamicExtend = null;
    JSONArray jsonArray = JSONArray.fromObject(resDetailJson);
    JSONObject jsonObject = null;
    for (int i = 0, length = jsonArray.size(); i < length; i++) {
      dynamicExtend = new DynamicExtend();
      jsonObject = jsonArray.getJSONObject(i);
      dynamicExtend.setDynId(dynId);
      dynamicExtend.setObjectId(jsonObject.getLong("resId"));
      dynamicExtend.setObjectNode(jsonObject.getInt("resNode"));
      dynamicExtend.setObjectType(resType);
      if (i < TOP_N) {
        dynamicExtend.setFirstFlag(1);
      } else {
        dynamicExtend.setFirstFlag(0);
      }
      dynamicExtendDao.save(dynamicExtend);
    }
  }

  /**
   * 展开剩余项.
   */
  @Override
  public List<DynamicExtend> getDynamicExtends(Long dynId) throws ServiceException {
    try {
      List<DynamicExtend> list = this.dynamicExtendDao.getDynamicExtendList(dynId);
      return list;
    } catch (DaoException e) {
      logger.error("查找动态的剩余项时出错啦！{}", dynId, e);
      throw new ServiceException(e);
    }
  }
}
