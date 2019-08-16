package com.smate.center.task.service.publicpub;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.v8pub.dao.pdwh.PdwhPubDuplicateDAO;
import com.smate.center.task.v8pub.dao.sns.PubDuplicateDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubDuplicatePO;
import com.smate.center.task.v8pub.sns.po.PubDuplicatePO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.string.StringUtils;

@Service("handlePubRelationService")
@Transactional(rollbackFor = Exception.class)
public class HandlePubRelationServiceImpl implements HandlePubRelationService {
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PdwhPubDuplicateDAO pdwhPubDuplicateDAO;
  @Autowired
  private PubDuplicateDAO pubDuplicateDAO;

  @Override
  public List<Long> getPdwhPubIds(Long lastPubId, Integer size) {
    return pubPdwhSnsRelationDAO.getPdwhPubIds(lastPubId, size);
  }

  @Override
  public void deleteErrorData(Long pdwhPubId) {
    PdwhPubDuplicatePO pdwhPubDup = pdwhPubDuplicateDAO.get(pdwhPubId);
    if (pdwhPubDup != null) {
      // 找到相关联的个人成果
      List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsPubIdListByPdwhId(pdwhPubId);
      for (Long snsPubId : snsPubIds) {
        boolean delete = true;
        PubDuplicatePO snsPubDup = pubDuplicateDAO.get(snsPubId);
        if (snsPubDup != null) {
          if (StringUtils.isNotBlank(pdwhPubDup.getHashDoi())) {
            if (pdwhPubDup.getHashDoi().equals(snsPubDup.getHashDoi())) {
              delete = false;
            } else {
              delete = true;
            }
          }
          if (delete && StringUtils.isNotBlank(pdwhPubDup.getHashEiSourceId())) {
            if (pdwhPubDup.getHashEiSourceId().equals(snsPubDup.getHashEiSourceId())) {
              delete = false;
            } else {
              delete = true;
            }
          }
          if (delete && StringUtils.isNotBlank(pdwhPubDup.getHashIsiSourceId())) {
            if (pdwhPubDup.getHashIsiSourceId().equals(snsPubDup.getHashIsiSourceId())) {
              delete = false;
            } else {
              delete = true;
            }
          }
          if (delete && StringUtils.isNotBlank(pdwhPubDup.getHashApplicationNo())) {
            if (pdwhPubDup.getHashApplicationNo().equals(snsPubDup.getHashApplicationNo())) {
              delete = false;
            } else {
              delete = true;
            }
          }
          if (delete && StringUtils.isNotBlank(pdwhPubDup.getHashPublicationOpenNo())) {
            if (pdwhPubDup.getHashPublicationOpenNo().equals(snsPubDup.getHashPublicationOpenNo())) {
              delete = false;
            } else {
              delete = true;
            }
          }
          if (delete && StringUtils.isNotEmpty(pdwhPubDup.getHashTP())) {
            if (StringUtils.isNotEmpty(pdwhPubDup.getHashTPP())) {// 基准库title，year,type都不为空的情况下
              if (StringUtils.isNotEmpty(snsPubDup.getHashTPP())) {// 个人title，year,type都不为空的情况下
                if (pdwhPubDup.getHashTPP().equals(snsPubDup.getHashTPP())) {
                  delete = false;
                } else {
                  delete = true;
                }
              } else {// 个人title，type都不为空的情况下
                if (pdwhPubDup.getHashTP().equals(snsPubDup.getHashTP())) {
                  delete = false;
                } else {
                  delete = true;
                }
              }
            } else {// 基准库title，type都不为空的情况下
              if (pdwhPubDup.getHashTP().equals(snsPubDup.getHashTP())) {
                delete = false;
              } else {
                delete = true;
              }
            }
          }
          if (delete) {
            pubPdwhSnsRelationDAO.deleteRelation(pdwhPubId, snsPubId);
          }

        }
      }
    }
  }

}
