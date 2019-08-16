package com.smate.web.v8pub.service.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dynamicds.InspgDynamicUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.dao.pdwh.PdwhInsAddrConstDao;
import com.smate.web.v8pub.dao.sns.AccountsMergeDao;
import com.smate.web.v8pub.dao.sns.FriendDao;
import com.smate.web.v8pub.dao.sns.MsgContentDao;
import com.smate.web.v8pub.dao.sns.MsgRelationDao;
import com.smate.web.v8pub.dao.sns.PubCommentDAO;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.dao.sns.PubSnsDetailDAO;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;
import com.smate.web.v8pub.enums.PubConfigEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhInsAddrConst;
import com.smate.web.v8pub.po.sns.MsgContent;
import com.smate.web.v8pub.po.sns.MsgRelation;
import com.smate.web.v8pub.po.sns.PubCommentPO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.utils.AuthorNameUtils;
import com.smate.web.v8pub.vo.PubCommentVO;

/**
 * 个人库成果详情服务实现类
 * 
 * @author houchuanjie
 * @date 2018/05/31 17:04
 */
@Service("pubSnsDetailService")
@Transactional(rollbackFor = Exception.class)
public class PubSnsDetailServiceImpl implements PubSnsDetailService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;
  @Autowired
  private PubCommentDAO pubCommentDAO;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private PsnCnfService psnCnfService;
  @Autowired
  private PubMemberService pubMemberService;
  @Autowired
  private MsgRelationDao msgRelationDao;
  @Autowired
  private MsgContentDao msgContentDao;
  @Autowired
  private PsnPubService psnPubService;
  @Autowired
  private GroupPubService groupPubService;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private AccountsMergeDao accountsMergeDao;
  @Autowired
  private InsPortalDao insPortalDao;
  @Autowired
  private PdwhInsAddrConstDao pdwhInsAddrConstDao;

  @Override
  public PubSnsDetailDOM get(Long pubId) throws ServiceException {
    try {
      PubSnsDetailDOM pubSnsDetailDOM = pubSnsDetailDAO.findById(pubId);
      return pubSnsDetailDOM;
    } catch (Exception e) {
      logger.error("查询成果详情时出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException {
    try {
      pubSnsDetailDAO.save(pubSnsDetailDOM);
    } catch (Exception e) {
      logger.error("保存成果详情时出错！{}", pubSnsDetailDOM, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException {
    try {
      pubSnsDetailDAO.update(pubSnsDetailDOM);
    } catch (Exception e) {
      logger.error("更新成果详情时出错！{}", pubSnsDetailDOM, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException {
    try {
      pubSnsDetailDAO.save(pubSnsDetailDOM);
    } catch (Exception e) {
      logger.error("保存或更新成果详情时出错！{}", pubSnsDetailDOM, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long pubId) throws ServiceException {
    try {
      Query query = new Query();
      query.addCriteria(Criteria.where("pubId").is(pubId));
      pubSnsDetailDAO.remove(query);
    } catch (Exception e) {
      logger.error("删除成果详情时出错！pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException {
    try {
      pubSnsDetailDAO.remove(pubSnsDetailDOM);
    } catch (Exception e) {
      logger.error("删除成果详情时出错！{}", pubSnsDetailDOM, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void getPubComment(PubCommentVO pubCommentVO) throws ServiceException {
    String languageVersion = LocaleContextHolder.getLocale().toString();
    List<PubCommentVO> newPubComments = new ArrayList<>();
    // 传参过来的pageNo
    if (NumberUtils.isNotZero(pubCommentVO.getPageNo())) {
      Page<?> page = pubCommentVO.getPage();
      page.setPageNo(pubCommentVO.getPageNo());
      pubCommentVO.setPage(page);
    }
    try {
      List<PubCommentPO> pubComments = pubCommentDAO.queryComments(pubCommentVO, pubCommentVO.getPage());
      for (PubCommentPO pubComment : pubComments) {
        PubCommentVO comment = new PubCommentVO();
        if (Locale.CHINA.equals(LocaleContextHolder.getLocale())) {
          comment.setDateTimes(InspgDynamicUtil.formatDate(pubComment.getGmtCreate()));
        } else {
          comment.setDateTimes(InspgDynamicUtil.formatDateUS(pubComment.getGmtCreate()));
        }
        comment.setCommentId(pubComment.getCommentId());
        comment.setPubId(pubComment.getPubId());
        comment.setPsnId(pubComment.getPsnId());
        comment.setDes3PsnId(Des3Utils.encodeToDes3(String.valueOf(pubComment.getPsnId())));
        comment.setCommentsContent(pubComment.getContent());
        buildPubCommentInfo(pubComment.getPsnId(), comment);
        newPubComments.add(comment);
      }
      Page page = pubCommentVO.getPage();
      page.setResult(newPubComments);
    } catch (Exception e) {
      logger.error("个人库获取成果评论列表出错！{}", pubCommentVO.getPubId(), e);
      throw new ServiceException(e);
    }
  }

  public void buildPubCommentInfo(Long psnId, PubCommentVO comment) {
    Person person = personDao.get(psnId);
    if (person != null) {
      wrapPubComment(comment, person);
    } else {
      // 若个人账号为空，则可能是被合并账号
      Long firstPsnId = accountsMergeDao.findPsnIdByMergePsnId(psnId);
      if (NumberUtils.isNotNullOrZero(firstPsnId)) {
        buildPubCommentInfo(firstPsnId, comment);
      }
    }
  }

  public void wrapPubComment(PubCommentVO pubCommentVO, Person commentPerson) {
    try {
      if (Locale.CHINA.equals(LocaleContextHolder.getLocale())) {
        pubCommentVO.setPsnName(commentPerson.getName() == null ? commentPerson.getEname() : commentPerson.getName());
      } else {
        pubCommentVO.setPsnName(commentPerson.getEname() == null ? commentPerson.getName() : commentPerson.getEname());
      }
      if (commentPerson.getAvatars().startsWith("http")) {
        pubCommentVO.setPsnAvatars(commentPerson.getAvatars());
      } else {
        pubCommentVO.setPsnAvatars(commentPerson.getAvatars());
      }
    } catch (Exception e) {
      logger.error("个人库封装保存的评论！pubComment=" + pubCommentVO + " commentPerson=" + commentPerson, e);
    }
  }

  @Override
  public PubSnsDetailDOM getByPubId(Long pubId) throws ServiceException {
    try {

      PubSnsDetailDOM pubSnsDetailDOM = pubSnsDetailDAO.findById(pubId);
      return pubSnsDetailDOM;
    } catch (Exception e) {
      logger.error("查询成果详情时出错！object={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getCommentNumber(Long pubId) throws ServiceException {
    Long pubCommentsCount = pubCommentDAO.getCommentsCount(pubId);
    return pubCommentsCount;
  }

  @Override
  public void getPubOwnerInfo(PubDetailVO pubDetailVO) throws ServiceException {
    if (pubDetailVO.getPubOwnerPsnId() != null && pubDetailVO.getPubOwnerPsnId() > 0l) {
      Person person = personDao.get(pubDetailVO.getPubOwnerPsnId());
      PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(pubDetailVO.getPubOwnerPsnId());
      if (psnProfileUrl != null) {
        pubDetailVO.setPsnIndexUrl(psnProfileUrl.getPsnIndexUrl());
      }
      if (person != null) {
        if (person.getAvatars() != null) {
          pubDetailVO.setPubOwnerAvatars(person.getAvatars());
        }
        pubDetailVO.setPubOwnerName(PsnInfoUtils.buildPsnName(person, null));
        pubDetailVO.setPubOwnerTitle(PsnInfoUtils.buildPsnTitoloInfo(person, null));
        PsnStatistics ps = psnStatisticsService.getPsnStatistics(pubDetailVO.getPubOwnerPsnId());
        if (ps != null) {
          pubDetailVO.setPubOwnerhIndex(ps.getHindex() != null ? ps.getHindex() : 0);
        }
      }
      Long friend = friendDao.isFriend(SecurityUtils.getCurrentUserId(), pubDetailVO.getPubOwnerPsnId());
      if (friend != null && friend > 0l) {
        pubDetailVO.setFriend(true);
      }
    }

  }

  /**
   * 获取人员的头像信息
   */
  @Override
  public void getPsnAvatars(PubDetailVO pubDetailVO) throws ServiceException {
    if (pubDetailVO.getPsnId() != null && pubDetailVO.getPsnId() != 0) {
      Person person = personDao.get(pubDetailVO.getPsnId());
      if (person.getAvatars() != null) {
        pubDetailVO.setPsnAvatars(person.getAvatars());
      }
    }
  }

  @Override
  public String splitPubKeywords(String keywords) throws ServiceException {
    StringBuffer result = new StringBuffer();
    if (StringUtils.isNotBlank(keywords)) {
      keywords = HtmlUtils.htmlUnescape(keywords);
      String[] words = keywords.split(";");
      if (words != null && words.length > 0) {
        for (String word : words) {
          if (StringUtils.isNotBlank(word)) {
            if (StringUtils.isBlank(result.toString())) {
              result.append("<span>" + HtmlUtils.htmlEscape(word) + "</span>");
            } else {
              result.append("; <span>" + HtmlUtils.htmlEscape(word) + "</span>");
            }
          }
        }
      }
    }
    return result.toString();
  }

  @Override
  public String buildPubAuthorNames(Long pubId) throws ServiceException {
    StringBuffer authorNames = new StringBuffer();
    try {
      List<PubMemberPO> members = pubMemberService.findByPubId(pubId);
      if (CollectionUtils.isNotEmpty(members)) {
        for (PubMemberPO mem : members) {
          if (StringUtils.isNotBlank(mem.getName())) {
            String name = HtmlUtils.htmlUnescape(mem.getName()).replaceAll("<|>", "");
            if (mem.getOwner() == 1) {// 本人的作者加粗
              name = "<strong>" + name + "</strong>";
            }
            if (mem.getCommunicable() == 1) {// 通信作者加*
              name = "*" + name;
            }
            if (StringUtils.isBlank(authorNames.toString())) {
              authorNames.append("<span>" + name + "</span>");
            } else {
              authorNames.append("; <span>" + name + "</span>");
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("构建成果作者信息出错, pubId = " + pubId, e);
      throw new ServiceException(e);
    }
    return authorNames.toString();
  }

  @Override
  public void checkPubAuthority(PubDetailVO pubDetailVO) throws ServiceException {
    try {
      // 刷新权限
      if (pubDetailVO.getPubOwnerPsnId() != null && pubDetailVO.getPubOwnerPsnId() > 0L) {
        PsnConfigPub cnfPub = new PsnConfigPub();
        cnfPub.getId().setPubId(pubDetailVO.getPubId());
        PsnConfigPub cnfPubExists = psnCnfService.get(pubDetailVO.getPubOwnerPsnId(), cnfPub);
        if (cnfPubExists == null) {
          pubDetailVO.setPermission(PsnCnfConst.ALLOWS);
        } else {
          pubDetailVO.setPermission(cnfPubExists.getAnyUser());
        }
      } else {
        pubDetailVO.setPermission(PsnCnfConst.ALLOWS);
      }

    } catch (Exception e) {
      logger.error("获取成果隐私设置出错", e);
    }
  }

  @Override
  public boolean sharePubView(String des3relationid, Long pubId, Long pubOwnerPsnId, Long psnId)
      throws ServiceException {
    Long relationid = NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3relationid));
    MsgRelation msgRelation = msgRelationDao.getUnprocessed(relationid);
    if (msgRelation != null && msgRelation.getSenderId().equals(pubOwnerPsnId)
        && msgRelation.getReceiverId().equals(psnId)) {// 发送人是成果的拥有者，接收人是查看详情的人
      MsgContent msgContent = msgContentDao.get(msgRelation.getContentId());
      if (msgContent != null) {
        Map<String, Object> content = JacksonUtils.jsonToMap(msgContent.getContent());
        Long sharePubId = (Long) content.get("pubId");
        if (pubId.equals(sharePubId)) {// 有分享记录
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Long getPubOwnerPsnId(Long pubId) {
    Long pubOwnerPsnId = psnPubService.getPubOwnerId(pubId);
    if (NumberUtils.isNullOrZero(pubOwnerPsnId)) {
      GroupPubPO groupPub = groupPubService.getByPubId(pubId);
      if (groupPub != null) {
        pubOwnerPsnId = groupPub.getOwnerPsnId();
      }
    }
    return pubOwnerPsnId;
  }

  @Override
  public boolean getPsnHasPrivatePub(Long psnId) {
    Long psnPrivatePubCount = pubSnsDAO.getPsnPrivatePubCount(psnId);
    if (psnPrivatePubCount != null && psnPrivatePubCount > 0) {
      return true;
    }
    return false;
  }

  @Override
  public String getPubInsName(Long pubId) throws ServiceException {
    List<PubMemberPO> members = pubMemberService.findByPubId(pubId);
    StringBuilder insNames = new StringBuilder();
    if (CollectionUtils.isNotEmpty(members)) {
      for (PubMemberPO pubMemberPO : members) {
        String insName = pubMemberPO.getInsName();
        if (StringUtils.isNotBlank(insName)) {
          Long insId = pubMemberPO.getInsId();
          if (insId == null) {
            insId = getInsId(pubId, pubMemberPO, insId);
          }
          if (insId != null) {
            InsPortal insPortal = insPortalDao.getInsPortalByInsId(insId);
            if (insPortal != null) {
              String insUrl = "https://" + insPortal.getDomain();
              String preStr = "<a target=\"_blank\" class=\"commnt_a\" href=\"" + insUrl + "\">";
              String endStr = "</a>";
              if (Locale.CHINA.equals(LocaleContextHolder.getLocale())) {
                insName = insPortal.getZhTitle() == null ? insPortal.getEnTitle() : insPortal.getZhTitle();
              } else {
                insName = insPortal.getEnTitle() == null ? insPortal.getZhTitle() : insPortal.getEnTitle();
              }
              insName = preStr + insName + endStr;
            }
          }
          if (StringUtils.isBlank(insNames.toString())) {
            insNames.append(insName);
          } else if (insNames.indexOf(insName) == -1) {
            insNames.append("; " + insName);
          }
        }
      }
    }
    return insNames.toString();
  }

  public Long getInsId(Long pubId, PubMemberPO pubMemberPO, Long insId) {
    Map<String, Set<String>> extractInsName = getExtractInsName(AuthorNameUtils.replaceChars(pubMemberPO.getInsName()));
    Set<String> addrlist = extractInsName.get("scm_ins_name");
    logger.error("pubId:" + pubId + "的memberId" + pubMemberPO.getId() + "匹配到的单位地址" + addrlist);
    if (!CollectionUtils.isEmpty(addrlist)) {
      for (String addr : addrlist) {
        Long addrHash = PubHashUtils.cleanPubAddrHash(addr);
        List<PdwhInsAddrConst> insInfo = pdwhInsAddrConstDao.getInsInfoByNameHash(addrHash);
        if (!CollectionUtils.isEmpty(insInfo)) {
          insId = insInfo.get(0).getInsId();
          break;
        }
      }
    }
    return insId;
  }

  @Override
  public String updatePubPermission(Long psnId, Long pubId) throws ServiceException {
    Long pubOwnerPsnId = getPubOwnerPsnId(pubId);
    if (psnId.equals(pubOwnerPsnId)) {
      PsnConfigPub psnConfigPub = pubSnsDAO.getPsnConfigPub(pubId);
      if (psnConfigPub.getAnyUser().equals(PubConfigEnum.PUB_SLEF.getValue())) {
        pubSnsDAO.updatePubPermission(pubId, 7);
      } else {
        pubSnsDAO.updatePubPermission(pubId, 4);
      }
      return "success";
    }
    return "error";
  }

  // 从词典提取单位
  public Map<String, Set<String>> getExtractInsName(String str) throws ServiceException {
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    str = AuthorNameUtils.replaceChars(str);
    // 直接使用，在服务器启动时加载
    Result kwRs = DicAnalysis.parse(str);
    logger.error("------------------------------词典包换的信息" + kwRs);
    Set<String> ins = new TreeSet<String>();
    Map<String, Set<String>> mp = new HashMap<String, Set<String>>();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }

      if ("scm_ins_name".equals(t.getNatureStr())) {
        logger.info("-----------------------------scm_ins_name:" + t.getNatureStr());
        if (StringUtils.isNotEmpty(t.getName())) {
          logger.error("----------------------------匹配到的单个地址:" + t.getName());
          ins.add(AuthorNameUtils.resetChars(t.getName()));
        }
      }
    }
    if (ins.size() > 0) {
      logger.info("---------------------------匹配到的单位地址" + ins);
      mp.put("scm_ins_name", ins);
    }
    return mp;
  }
}
