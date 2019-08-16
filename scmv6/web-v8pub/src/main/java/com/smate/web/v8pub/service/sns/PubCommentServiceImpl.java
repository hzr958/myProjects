package com.smate.web.v8pub.service.sns;

import com.smate.core.base.email.service.EmailSendService;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.v8pub.dao.sns.PubCommentDAO;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubCommentPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.pdwh.PdwhPubCommentService;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;
import com.smate.web.v8pub.vo.sns.PubOperateVO;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 成果评论服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PubCommentServiceImpl implements PubCommentService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubCommentDAO pubCommentDAO;
  @Autowired
  private PubStatisticsService newPubStatisticsService;
  @Autowired
  private PubSnsService pubSnsService;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private EmailSendService commentedYourPubEmailService;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PdwhPubCommentService pdwhPubCommentService;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;

  @Override
  public void saveOrUpdate(PubCommentPO entity) throws ServiceException {
    try {
      pubCommentDAO.save(entity);
    } catch (Exception e) {
      logger.error("成果评论服务 保存 or 更新 实现对象出现异常,pubId=" + entity.getPubId(), e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      pubCommentDAO.delete(id);
    } catch (Exception e) {
      logger.error("成果评论服务 通过id删除对象异常,pubId=" + id, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public List<PubCommentPO> findByPubId(Long pubId) throws ServiceException {
    try {
      List<PubCommentPO> list = pubCommentDAO.findByPubId(pubId);
      return list;
    } catch (Exception e) {
      logger.error("成果评论服务 :通过成id 查找评论列表异常,pubId=" + pubId, e);
      throw new ServiceException(e);
    }

  }

  /**
   * 处理评论内容
   * 
   * @param pdwhPubId
   */
  public void dealContent(Long pdwhPubId, Long ownerPsnId, PubOperateVO pubOperateVO) {
    List<String> psnNameList = new ArrayList();
    List<Long> psnIds = new ArrayList<>();
    psnIds.add(ownerPsnId);
    if (NumberUtils.isNotNullOrZero(pdwhPubId)) {
      List<Long> psnIdByPubId = pubPdwhSnsRelationDAO.findSnsPsnIds(pdwhPubId);
      if (CollectionUtils.isNotEmpty(psnIdByPubId)) {
        psnIds.addAll(psnIdByPubId);
      }
    }
    if (CollectionUtils.isNotEmpty(psnIds)) {
      for (Long psnId : psnIds) {
        Person person = personDao.get(psnId);
        if (person == null)
          continue;
        isContainPersonName(person, pubOperateVO);
      }
    }
  }

  boolean isContainPersonName(Person person, PubOperateVO pubOperateVO) {
    String content = pubOperateVO.getContent();
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(person.getPersonId());
    String psnUrl = "";
    if (psnProfileUrl != null) {
      psnUrl = domainscm + "/P/" + psnProfileUrl.getPsnIndexUrl();
    }
    String preStr = "<a target=\"_blank\"  class=\"commnt_a\" href=\"" + psnUrl + "\"><span>@</span>";
    String endStr = "</a>";
    // item.put("psnUrl", );
    if (StringUtils.isNotBlank(person.getName())) {
      if (content.contains("@" + person.getName())){
        content = content.replaceAll("@" + person.getName(), preStr + person.getName() + endStr);
        pubOperateVO.setContent(content);
        return true;
      }
    }
    if (StringUtils.isNotBlank(person.getEname())) {
      if (content.contains("@" + person.getEname())){
        content = content.replaceAll("@" + person.getEname(), preStr + person.getEname() + endStr);
        pubOperateVO.setContent(content);
        return true;
      }
    }
    if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
      if (content.contains("@" + person.getFirstName() + " " + person.getLastName())){
        content = content.replaceAll("@" + person.getFirstName() + " " + person.getLastName(),
            preStr + person.getFirstName() + " " + person.getLastName() + endStr);
        pubOperateVO.setContent(content);
        return true;
      }
    }
    return false;
  }

  /**
   * 个人库 成果评论操作
   */
  @Override
  public void commentOpt(PubOperateVO pubOperateVO) throws ServiceException {
    Long pubId = pubOperateVO.getPubId();
    Long psnId = pubOperateVO.getPsnId();
    try {
      Long ownerPsnId = null;
      PubSnsPO pubSns = pubSnsService.get(pubId);
      if (pubSns != null) {
        ownerPsnId = pubSns.getCreatePsnId();
      }
      // SCM-23420 数据来源调整 跟基准库关联的成果，操作要通知所有关联成果的所属用户
      Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(pubId);
      dealContent(pdwhPubId, ownerPsnId, pubOperateVO);
      // SCM-23563 个人库成果先直接操作，如果是关联成果，再来同步数据
      snsCommentOpt(pubOperateVO, pubId, ownerPsnId);
      if (pdwhPubId != null && pdwhPubId > 0L) {
        pdwhCommentOpt(pubOperateVO, pdwhPubId);// 基准库数据备份
        List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
        if (CollectionUtils.isNotEmpty(snsPubIds)) {
          for (Long snsPubId : snsPubIds) {
            if (!snsPubId.equals(pubId)) {
              updateSnsCommentStatistics(pubOperateVO, snsPubId);// 个人库关联成果同步数据
            }
          }
          // 发送邮件
          if (!psnId.equals(ownerPsnId)) {
            buildEmail(pubId, psnId, snsPubIds, ownerPsnId);
          }
        }
      }
    } catch (Exception e) {
      logger.error("成果评论异常,pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  public void buildEmail(Long pubId, Long psnId, List<Long> snsPubIds, Long ownerPsnId) {
    Long count = pubCommentDAO.getCommentCount(pubId, psnId);
    if (count < 1 && !psnId.equals(ownerPsnId)) {
      List<Long> pubOwnerList = pubSnsDAO.getSnsPubList(snsPubIds);
      List<Long> newPubOwnerList = pubOwnerList.stream().distinct().collect(Collectors.toList());
      List<Map<Long, Long>> pubPsbList = pubSnsDAO.getSnsPub(snsPubIds);
      if (CollectionUtils.isNotEmpty(newPubOwnerList)) {
        for (Long ownerId : newPubOwnerList) {// 操作要通知所有关联成果的所属用户
          if (pubPsbList != null && pubPsbList.size() > 0) {
            Long newPubId = null;
            for (Map<Long, Long> map : pubPsbList) {
              if (ownerId.equals(map.get("createPsnId"))) {
                newPubId = map.get("pubId");
              }
            }
            if (!ownerPsnId.equals(ownerId)) {// 防止重复发送
              noticeCommentEmail(newPubId, psnId, ownerId);// 发送邮件通知用户
            }
          }
        }
      }
    }
  }

  public void pdwhCommentOpt(PubOperateVO pubOperateVO, Long pdwhPubId) {
    PdwhPubOperateVO pdwhPubOperateVO = new PdwhPubOperateVO();
    pdwhPubOperateVO.setPdwhPubId(pdwhPubId);
    pdwhPubOperateVO.setPsnId(pubOperateVO.getPsnId());
    pdwhPubOperateVO.setContent(pubOperateVO.getContent());
    pdwhPubCommentService.pdwhComment(pdwhPubOperateVO);
  }

  public void snsCommentOpt(PubOperateVO pubOperateVO, Long pubId, Long ownerPsnId) {
    updateSnsCommentStatistics(pubOperateVO, pubId);
    // 发送评论邮件 对同一成果评论，每人每天只能产生一条邮件
    Long psnId = pubOperateVO.getPsnId();
    Long count = pubCommentDAO.getCommentCount(pubId, psnId);
    if (count < 1) {
      noticeCommentEmail(pubId, psnId, ownerPsnId);
    }
  }

  @Override
  public void updateSnsCommentStatistics(PubOperateVO pubOperateVO, Long snsPubId) {
    // 往成果评论表插入记录
    insertComment(pubOperateVO, snsPubId);
    // 更新成果评论统计数
    newPubStatisticsService.updateCommentStatistics(snsPubId);
  }

  public void noticeCommentEmail(Long pubId, Long psnId, Long ownerPsnId) {
    if (!psnId.equals(ownerPsnId)) {
      sendCommentEmail(psnId, ownerPsnId, pubId);
    }
  }

  private void insertComment(PubOperateVO pubOperateVO, Long snsPubId) {
    Long psnId = pubOperateVO.getPsnId();
    try {
      PubCommentPO pubCommentPO = new PubCommentPO();
      pubCommentPO.setContent(HtmlUtils.htmlUnescape(pubOperateVO.getContent()));
      pubCommentPO.setPsnId(psnId);
      pubCommentPO.setPubId(snsPubId);
      pubCommentPO.setStatus(0);// 状态 0=正常 ； 9=删除
      pubCommentPO.setGmtCreate(new Date());
      pubCommentPO.setGmtModified(new Date());
      saveOrUpdate(pubCommentPO);
    } catch (Exception e) {
      logger.error("成果评论插入异常,pubId=" + snsPubId, e);
    }
  }

  private void sendCommentEmail(Long commentedPsnId, Long ownerPsnId, Long pubId) {
    try {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("commentedPsnId", commentedPsnId);
      map.put("psnId", ownerPsnId);
      map.put("pubId", pubId);
      commentedYourPubEmailService.syncEmailInfo(map);
    } catch (Exception e) {
      logger.error("发送评论邮件错误：pubId:" + pubId + ",commentedPsnId:" + commentedPsnId, e);
    }
  }

}
