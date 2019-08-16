package com.smate.sie.core.base.utils.service.constant;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.consts.SieConstRegionDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.consts.SieConstRegion;

/**
 * 
 * @author hd
 *
 */
@Service("sieConstCnRegionService")
@Transactional(rollbackFor = Exception.class)
public class SieConstCnRegionServiceImpl implements SieConstCnRegionService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SieConstRegionDao sieConstRegionDao;

  @Override
  public SieConstRegion getCityByName(String name) throws SysServiceException {
    try {
      if (name == null) {
        return null;
      }
      if (!XmlUtil.containZhChar(name)) {// 英文单位处理
        List<SieConstRegion> list = sieConstRegionDao.getCityByEnName(name);
        if (list.size() == 1) {
          return list.get(0);
        } else {
          return null;
        }
      } else {
        List<String> queryParams = this.createQueryParams(name);
        List<SieConstRegion> list = sieConstRegionDao.getCityByZhNames(queryParams);
        if (list.size() == 1) {
          return list.get(0);
        } else {
          return null;
        }
      }
    } catch (Exception e) {
      logger.error("根据城市名称获取城市错误：", e);
      throw new SysServiceException(e);
    }
  }

  /**
   * 根据name 加工 生成各种可能的sql 查询条件值
   * 
   * @param name 名称原始字符串
   * @return
   */
  private List<String> createQueryParams(String name) {
    List<String> params = new ArrayList<String>();
    params.add(name);
    if (name.endsWith("市")) {
      String name1 = name;
      params.add(name1.replace("市", ""));
    }
    if (name.endsWith("省")) {
      String name1 = name;
      params.add(name1.replace("省", ""));
    }
    if (!name.endsWith("市")) {
      params.add(name + "市");
    }
    if (!name.endsWith("自治州")) {
      params.add(name + "自治州");
    }
    if (!name.endsWith("地区")) {
      params.add(name + "地区");
    }
    if (!name.endsWith("盟")) {
      params.add(name + "盟");
    }
    return params;
  }

}
