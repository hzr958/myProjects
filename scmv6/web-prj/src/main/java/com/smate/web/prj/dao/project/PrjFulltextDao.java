package com.smate.web.prj.dao.project;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.prj.model.common.PrjFulltext;

/**
 * 项目全文DAO
 * 
 * @author houchuanjie
 * @date 2018年3月23日 上午10:01:25
 */
@Repository
public class PrjFulltextDao extends SnsHibernateDao<PrjFulltext, Long> {
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 通过项目id删除全文记录
   *
   * @author houchuanjie
   * @date 2018年3月23日 下午2:51:27
   * @param prjId
   */
  public void deleteByPrjId(Long prjId) {
    Optional.ofNullable(get(prjId)).ifPresent(pf -> delete(pf));
  }
}
