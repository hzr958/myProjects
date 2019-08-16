package com.smate.center.open.service.group.prj;


import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.group.GroupPsnDao;
import com.smate.center.open.dao.group.prj.PrjGroupRelationDao;
import com.smate.center.open.model.group.prj.PrjGroupRelation;

/**
 * @author xieyushou
 */
@Service("prjGroupRelationService")
@Transactional(rollbackFor = Exception.class)
public class PrjGroupRelationServiceImpl implements PrjGroupRelationService, Serializable {

  private static final long serialVersionUID = 2325399379877876321L;
  private static Logger logger = LoggerFactory.getLogger(PrjGroupRelationServiceImpl.class);
  @Autowired
  private PrjGroupRelationDao prjGroupRelationDao;
  @Autowired
  private GroupPsnDao groupPsnDao;

  @Override
  public void buildPrjGroupRelation(Long prjId, Long groupId) throws Exception {
    PrjGroupRelation prjGroupRel = new PrjGroupRelation();
    prjGroupRel.setPrjId(prjId);
    prjGroupRel.setGroupId(groupId);
    prjGroupRelationDao.save(prjGroupRel);
  }

  @Override
  public Long findGroupIdByPrjId(Long prjId) throws Exception {
    try {
      return prjGroupRelationDao.findGroupIdByPrjId(prjId);
    } catch (Exception e) {
      logger.error("根据项目id查找群组id出错了！", e);
      throw new Exception(e);
    }
  }


}
