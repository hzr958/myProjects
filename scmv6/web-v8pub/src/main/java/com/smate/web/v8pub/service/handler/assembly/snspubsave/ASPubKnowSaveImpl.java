package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.pub.enums.PubSnsStatusEnum;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.v8pub.consts.PublicationArticleType;
import com.smate.web.v8pub.dao.sns.PubKnowDao;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.enums.PubHandlerEnum;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubKnow;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

/**
 * 保存pubKnow
 * 
 * @author wsn
 * @date Oct 24, 2018
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubKnowSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PubKnowDao pubKnowDao;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      if (PubHandlerEnum.DELETE_PSN_PUB.name.equals(pub.pubHandlerName)) {
        PubKnow pubKnow = pubKnowDao.get(pub.pubId);
        if (pubKnow != null) {
          pubKnowDao.delete(pub.pubId);
        }
      } else {
        // 构建isPubAuthors值
        int isOwner = 0;
        JSONArray members = pub.members;
        if (members != null) {
          List<PubMemberDTO> memberList = members.toJavaList(PubMemberDTO.class);
          if (CollectionUtils.isNotEmpty(memberList)) {
            for (PubMemberDTO member : memberList) {
              if (member.isOwner()) {
                isOwner = 1;
                break;
              }
            }
          }
          // 构建authors以及他们的序列号
          int seq = this.getMatchPubAuthor(memberList, pub.psnId);
          // 保存或更新PubKnow
          this.saveOrUpdatePubKnow(pub, isOwner, seq);
        }
      }
    } catch (Exception e) {
      logger.error("更新PubKnow表失败， pubHandlerName = " + pub.pubHandlerName + ", pubId = " + pub.pubId, e);
    }
    return null;
  }

  // 得到当前用户匹配上的成果作者的序列号（第几个）
  protected Integer getMatchPubAuthor(List<PubMemberDTO> memberList, Long psnId) throws ServiceException {
    Person currPsn = personDao.getPersonNameNotId(psnId);
    int seq = -1;
    if (currPsn != null) {
      // 获取选择的成员序号,在此通过id来匹配具有第一优先级
      for (PubMemberDTO member : memberList) {
        if (member.getPsnId() != null & psnId != null) {
          if (psnId.intValue() == member.getPsnId().intValue()) {
            seq = member.getSeqNo().intValue();
            return seq;
          }
        }
      }
      // 获取第一个匹配的成员序号，通过名称来匹配，返回第一个匹配上的名字
      for (PubMemberDTO member : memberList) {
        if (!StringUtils.isBlank(member.getName()) && !StringUtils.isBlank(currPsn.getName())
            && currPsn.getName().equalsIgnoreCase(member.getName())) {
          seq = member.getSeqNo().intValue();
          return seq;
        } else {
          String firstName = currPsn.getFirstName();
          String lastName = currPsn.getLastName();
          if (StringUtils.isBlank(firstName)
              || StringUtils.isBlank(lastName) && StringUtils.isBlank(member.getName())) {
            continue;
          }
          String preF = firstName.substring(0, 1).toLowerCase();
          lastName = lastName.toLowerCase();
          // 尝试z lin 是否匹配上alen z lin或者 z alen lin
          int index = member.getName().toLowerCase().indexOf(preF);
          if (index > -1 && member.getName().substring(index).toLowerCase().endsWith(lastName)) {
            seq = member.getSeqNo().intValue();
            return seq;
          }
          index = member.getName().toLowerCase().lastIndexOf(preF);
          if (index > 0 && member.getName().substring(0, index).toLowerCase().startsWith(lastName)) {
            seq = member.getSeqNo().intValue();
            return seq;
          }
        }
      }
    }
    return seq;
  }

  // 保存或更新PubKnow
  protected void saveOrUpdatePubKnow(PubDTO pub, int isOwner, int seq) {
    PubKnow pubKnow = pubKnowDao.get(pub.pubId);
    if (pubKnow == null) {
      pubKnow = new PubKnow();
      pubKnow.setId(pub.pubId);
      pubKnow.setPsnId(pub.psnId);
    }
    pubKnow.setArticleType(PublicationArticleType.OUTPUT);
    pubKnow.setTypeId(pub.pubType);
    pubKnow.setStatus(PubSnsStatusEnum.DEFAULT.getValue());
    Long titleHash = NumberUtils.toLong(pub.hashTitle);
    pubKnow.setZhTitleHash(titleHash);
    pubKnow.setEnTitleHash(titleHash);
    if (isOwner == 1 && seq != -1) {
      pubKnow.setSeqNo(seq);
    } else {
      pubKnow.setSeqNo(null);
    }
    pubKnow.setIsPubAuthors(isOwner);
    pubKnow.setJnlId(pub.baseJournalId);
    pubKnowDao.save(pubKnow);
  }
}
