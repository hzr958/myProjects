package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.GroupRefDao;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.CiteWriteDataBuilder;
import com.smate.center.batch.model.sns.pub.GroupRefs;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationEndNote;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * @author LY
 * 
 */
@Service("groupRefsService")
@Transactional(rollbackFor = Exception.class)
public class GroupRefsServiceImpl implements GroupRefsService {

  /**
   * 
   */
  private static final long serialVersionUID = -5877561919926616972L;
  /**
   * 
   */
  @Autowired
  private GroupRefDao groupRefDao;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MyPublicationQueryService myPublicationQueryService;
  @Autowired
  private ConstPubTypeService publicationTypeService;
  @Autowired
  private CiteWriteDataBuilder citeWriteDataBuilder;
  @Autowired
  private PublicationService publicationService;

  @Override
  public GroupRefs findGroupRef(Long groupId, Long pubId, Integer nodeId) throws ServiceException {
    try {
      return this.groupRefDao.findGroupRefs(groupId, pubId, nodeId);
    } catch (DaoException e) {
      logger.error("查找群组成果失败", e);
      throw new ServiceException("查找群组成果失败");
    }
  }

  @Override
  public void saveGroupRef(GroupRefs groupRef) throws ServiceException {
    this.groupRefDao.save(groupRef);
  }

  @Override
  public void deleteGroupRef(Long groupId, Long pubId) throws ServiceException {
    groupRefDao.deletePub(groupId, pubId);
  }

  @Override
  public List<PublicationEndNote> findGroupPubByIds(List<Long> pubIds) throws ServiceException {
    try {
      logger.debug("queryOutput : dbquery start at: {}", new Date());
      Map<Integer, List<Long>> pubMap = this.groupRefDao.getPubNodeIdByPubIds(pubIds);
      Integer curNodeId = ServiceConstants.SCHOLAR_NODE_ID_1;
      List<Publication> pubList = new ArrayList<Publication>();
      if (pubMap.get(curNodeId).size() > 0) {
        pubList.addAll(this.publicationService.getPubListByPubIds(pubMap.get(curNodeId)));
        for (Publication pub : pubList) {
          pub.setNodeId(curNodeId);
        }
      }
      for (int i = 1; i < SecurityUtils.getCurrentAllNodeId().size(); i++) {
        Integer tmpNodeId = SecurityUtils.getCurrentAllNodeId().get(i);
        if (pubMap.get(tmpNodeId).size() > 0) {
          pubList.addAll(this.publicationService.getPubListByPubIds(pubMap.get(tmpNodeId)));
          for (Publication pub : pubList) {
            pub.setNodeId(tmpNodeId);
          }
        }
      }
      List<PublicationEndNote> endNoteList = new ArrayList<PublicationEndNote>();
      if (CollectionUtils.isNotEmpty(pubList)) {
        for (Publication pub : pubList) {
          PublicationEndNote endNote = new PublicationEndNote();
          this.myPublicationQueryService.wrapQueryResultTypeName(pub);
          endNote = this.citeWriteDataBuilder.populateDataForEndNote(pub, false);
          endNoteList.add(endNote);
        }
      }
      return endNoteList;
    } catch (DaoException e) {
      logger.error(String.format("queryOutput查询成果错误。args=%s", pubIds), e);
      throw new ServiceException(e);
    } catch (ServiceException e) {
      logger.error(String.format("queryOutput查询成果错误。args=%s", pubIds), e);
      throw new ServiceException(e);
    }
  }
}
