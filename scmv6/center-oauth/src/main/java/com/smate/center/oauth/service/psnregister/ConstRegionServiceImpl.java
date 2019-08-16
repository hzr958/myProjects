package com.smate.center.oauth.service.psnregister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.exception.ServiceException;
import com.smate.core.base.consts.dao.ConstRegionDao;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 国家与地区/省份的公共读取业务模块.
 * 
 * @author AJB
 * 
 */
@Service("constRegionService")
@Transactional(rollbackFor = Exception.class)
public class ConstRegionServiceImpl implements ConstRegionService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstRegionDao constRegionDao;

  @Override
  public String findRegionJsonData(Long superRegionId) throws Exception {
    try {

      List<ConstRegion> list = constRegionDao.findRegionData(superRegionId);
      List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
      for (ConstRegion cr : list) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", ObjectUtils.toString(cr.getId()));
        map.put("zh_CN_name", cr.getZhName());
        map.put("en_US_name", cr.getEnName());
        map.put("zh_CN_seq", ObjectUtils.toString(cr.getZhSeq()));
        map.put("en_US_seq", ObjectUtils.toString(cr.getEnSeq()));
        resultList.add(map);
      }
      // 返回json数据，格式
      return JacksonUtils.listToJsonStr(resultList);
    } catch (Exception e) {
      logger.error("通过discCode获取ID错误.", e);
      throw new ServiceException(e);
    }
  }

}
