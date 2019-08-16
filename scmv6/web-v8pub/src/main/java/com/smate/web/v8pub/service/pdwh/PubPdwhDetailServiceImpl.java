package com.smate.web.v8pub.service.pdwh;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.pub.util.AuthorNamesUtils;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dynamicds.InspgDynamicUtil;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.dao.pdwh.PdwhInsAddrConstDao;
import com.smate.web.v8pub.dao.pdwh.PdwhPubAddrInsRecordDao;
import com.smate.web.v8pub.dao.pdwh.PdwhPubCommentDAO;
import com.smate.web.v8pub.dao.pdwh.PubPdwhDetailDAO;
import com.smate.web.v8pub.dao.sns.FriendDao;
import com.smate.web.v8pub.dao.sns.PubAssignLogDAO;
import com.smate.web.v8pub.dao.sns.PubAssignLogDetailDao;
import com.smate.web.v8pub.dao.sns.psn.PersonPmNameDao;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubAddrInsRecord;
import com.smate.web.v8pub.po.pdwh.PdwhPubCommentPO;
import com.smate.web.v8pub.po.sns.PubAssignLogDetail;
import com.smate.web.v8pub.po.sns.PubAssignLogPO;
import com.smate.web.v8pub.service.sns.PsnStatisticsService;
import com.smate.web.v8pub.vo.AutorPsnInfo;
import com.smate.web.v8pub.vo.PubCommentVO;

/**
 * 基准库成果详情服务实现类
 * 
 * @author houchuanjie
 * @date 2018/05/31 17:04
 */
@Service("pubPdwhDetailService")
@Transactional(rollbackFor = Exception.class)
public class PubPdwhDetailServiceImpl implements PubPdwhDetailService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PdwhPubCommentDAO pdwhPubCommentDAO;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PubAssignLogDetailDao pubAssignLogDetailDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private FriendDao friendDao;
  @Autowired
  private PsnStatisticsService psnStatisticsService;
  @Autowired
  private PdwhPubAddrInsRecordDao pdwhPubAddrInsRecordDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PersonPmNameDao personPmNameDao;
  @Autowired
  private PdwhInsAddrConstDao pdwhInsAddrConstDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private PubAssignLogDAO pubAssignLogDAO;
  @Autowired
  private InsPortalDao insPortalDao;

  @Override
  public PubPdwhDetailDOM get(Long pubId) throws ServiceException {
    try {
      PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDAO.findById(pubId);
      return pubPdwhDetailDOM;
    } catch (Exception e) {
      logger.error("查询成果详情时出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException {
    try {
      pubPdwhDetailDAO.save(pubPdwhDetailDOM);
    } catch (Exception e) {
      logger.error("保存成果详情时出错！{}", pubPdwhDetailDOM, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException {
    try {
      pubPdwhDetailDAO.update(pubPdwhDetailDOM);
    } catch (Exception e) {
      logger.error("更新成果详情时出错！{}", pubPdwhDetailDOM, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException {
    try {
      pubPdwhDetailDAO.save(pubPdwhDetailDOM);
    } catch (Exception e) {
      logger.error("保存或更新成果详情时出错！{}", pubPdwhDetailDOM, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long pubId) throws ServiceException {
    try {
      Query query = new Query();
      query.addCriteria(Criteria.where("pubId").is(pubId));
      pubPdwhDetailDAO.remove(query);
    } catch (Exception e) {
      logger.error("删除成果详情时出错！pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException {
    try {
      pubPdwhDetailDAO.remove(pubPdwhDetailDOM);
    } catch (Exception e) {
      logger.error("删除成果详情时出错！{}", pubPdwhDetailDOM, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void getPdwhComment(PubCommentVO pubCommentVO) throws ServiceException {
    List<PubCommentVO> pubComments = new ArrayList<>();
    try {
      buildPdwhComment(pubCommentVO.getPubId(), pubCommentVO, pubComments);
      Page page = pubCommentVO.getPage();
      page.setResult(pubComments);
    } catch (Exception e) {
      logger.error("基准库获取成果评论列表出错！{}", pubCommentVO.getPubId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void buildPdwhComment(Long pubId, PubCommentVO pubCommentVO, List<PubCommentVO> pubComments) {
    String languageVersion = LocaleContextHolder.getLocale().toString();
    List<PdwhPubCommentPO> pdwhPubComments = pdwhPubCommentDAO.queryCommentsById(pubId, pubCommentVO.getPage());
    for (PdwhPubCommentPO pdwhPubComment : pdwhPubComments) {
      PubCommentVO comment = new PubCommentVO();
      if (Locale.CHINA.toString().equals(languageVersion)) {
        comment.setDateTimes(InspgDynamicUtil.formatDate(pdwhPubComment.getGmtCreate()));
      }
      comment.setCommentId(pdwhPubComment.getCommentId());
      comment.setPubId(pdwhPubComment.getPdwhPubId());
      comment.setPsnId(pdwhPubComment.getPsnId());
      comment.setCommentsContent(pdwhPubComment.getContent());
      comment.setDes3PsnId(Des3Utils.encodeToDes3(String.valueOf(pdwhPubComment.getPsnId())));
      Person person = personDao.get(pdwhPubComment.getPsnId());
      if (person != null) {
        wrapPdwhPubComment(comment, person);
      }
      pubComments.add(comment);
    }
  }

  public void wrapPdwhPubComment(PubCommentVO pubCommentVO, Person commentPerson) {
    try {
      if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
        pubCommentVO.setPsnName(
            StringUtils.isBlank(commentPerson.getEname()) ? commentPerson.getName() : commentPerson.getEname());
      } else {
        pubCommentVO.setPsnName(
            StringUtils.isBlank(commentPerson.getName()) ? commentPerson.getEname() : commentPerson.getName());
      }
      if (commentPerson.getAvatars().startsWith("http")) {
        pubCommentVO.setPsnAvatars(commentPerson.getAvatars());
      } else {
        pubCommentVO.setPsnAvatars(commentPerson.getAvatars());
      }
    } catch (Exception e) {
      logger.error("封装保存的评论！pubComment=" + pubCommentVO + " commentPerson=" + commentPerson, e);
    }
  }

  @Override
  public PubPdwhDetailDOM getByPubId(Long pubId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("_id").is(pubId));
    List<PubPdwhDetailDOM> list = pubPdwhDetailDAO.find(query);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 获取基准库的成果统计数
   */
  @Override
  public Long getPdwhCommentNumber(Long pubId) throws ServiceException {
    Long pubCommentsCount = pdwhPubCommentDAO.getCommentsCount(pubId);
    return pubCommentsCount;
  }

  @Override
  public void buildPdwhPsnInfo(PubDetailVO pubDetailVO) throws ServiceException {
    List<Map<String, Object>> authorPsnInfoList = new ArrayList<Map<String, Object>>();
    String authorNames = handleAuthorNames(pubDetailVO);
    int i = 0;
    if (StringUtils.isNotBlank(authorNames)) {
      List<String> split = AuthorNamesUtils.parsePubAuthorNames(authorNames);
      List<String> authorEmailList = new ArrayList<>();
      if (split != null && split.size() > 0) {
        findAuthorEmail(pubDetailVO, authorEmailList);
        for (String one : split) {
          i++;
          pubDetailVO.setAuthorName(one);
          if (StringUtils.isNotBlank(pubDetailVO.getAuthorName())) {
            Map<String, Object> map = new HashMap<String, Object>();
            pubDetailVO.setMatchStatus(0);
            pubDetailVO.setFirstAvatars("");
            try {
              matchHandle(pubDetailVO, i, authorEmailList, map);
            } catch (Exception e) {
              logger.error("基准库成果详情匹配作者名字出错，name=" + pubDetailVO.getAuthorName(), e);
            }
            setAuthorNameBox(pubDetailVO, map);
            authorPsnInfoList.add(map);
          }
        }
      }
    }
    pubDetailVO.setAuthorPsnInfoList(authorPsnInfoList);

  }

  private String handleAuthorNames(PubDetailVO pubDetailVO) {
    String authorNames = pubDetailVO.getAuthorNames();
    if (StringUtils.isNotBlank(authorNames)) {
      authorNames = StringUtils.replace(authorNames, "<strong>", "");
      authorNames = StringUtils.replace(authorNames, "</strong>", "");
    }
    return authorNames;
  }

  private void setAuthorNameBox(PubDetailVO pubDetailVO, Map<String, Object> map) {
    map.put("status", pubDetailVO.getMatchStatus());
    map.put("name", pubDetailVO.getAuthorName());
    setFirstAvatars(map, pubDetailVO);
  }

  private void matchHandle(PubDetailVO pubDetailVO, int i, List<String> authorEmailList, Map<String, Object> map)
      throws Exception {
    // pubDetailVO.setAuthorName(handleName(pubDetailVO.getAuthorName()));
    List<PubAssignLogDetail> list =
        pubAssignLogDetailDao.findByPubIdAndName(pubDetailVO.getPubId(), pubDetailVO.getAuthorName());
    if (list == null || list.size() == 0) {
      if (authorEmailList != null && authorEmailList.size() >= i) {
        String email = authorEmailList.get(i - 1);
        if (StringUtils.isNotBlank(email)) {
          list = pubAssignLogDetailDao.findByPubIdAndEmail(pubDetailVO.getPubId(), email.trim());
        }
      }
    }
    if (list != null && list.size() > 0) {
      pubDetailVO.setMatchStatus(1);
      // 处理匹配到的list
      handlePubAssignLogDetailsList(pubDetailVO, map, list);
    }
  }

  private void handlePubAssignLogDetailsList(PubDetailVO pubDetailVO, Map<String, Object> map,
      List<PubAssignLogDetail> list) {
    List<AutorPsnInfo> infoList = new ArrayList<AutorPsnInfo>();
    for (PubAssignLogDetail p : list) {
      // 指派分数不为0或者自动认领了才显示在界面
      PubAssignLogPO assignLog = pubAssignLogDAO.getPubAssignLog(p.getPubId(), p.getPsnId());
      if (assignLog != null) {
        // 构造匹配到的人员信息
        loopBuildPsnInfo(pubDetailVO, infoList, p);
      }
    }
    Collections.sort(infoList);
    if (infoList != null && infoList.size() > 0) {
      pubDetailVO.setFirstAvatars(infoList.get(0).getAvatars());
    }
    map.put("infoList", infoList);
  }

  private void loopBuildPsnInfo(PubDetailVO pubDetailVO, List<AutorPsnInfo> infoList, PubAssignLogDetail p) {
    AutorPsnInfo psnInfo = new AutorPsnInfo();
    psnInfo.setPsnId(p.getPsnId());
    Person psn = personDao.findPersonInsAndPos(p.getPsnId());
    if (psn != null) {
      setPsnInfo(pubDetailVO, psnInfo, psn);
      findFriendStatus(psnInfo, psn.getPersonId());
      findPsnStatis(psnInfo, psn.getPersonId());
      findPsnUrl(psnInfo, psn.getPersonId());
    }
    infoList.add(psnInfo);
  }

  private void setPsnInfo(PubDetailVO pubDetailVO, AutorPsnInfo psnInfo, Person psn) {
    String locale = LocaleContextHolder.getLocale().getLanguage();
    psnInfo.setName(PsnInfoUtils.getPersonName(psn, locale));
    psnInfo.setAvatars(psn.getAvatars());
    psnInfo.setDes3PsnId(Des3Utils.encodeToDes3(psn.getPersonId().toString()));
    /**
     * 根据语言及insId去institution表中取,若insId不存在则放默认的(Person表中的insName)
     */
    StringBuffer psnInfoStr = new StringBuffer();
    if (NumberUtils.isNotNullOrZero(psn.getInsId())) {
      Institution institution = institutionDao.get(psn.getInsId());
      if (Objects.nonNull(institution)) {
        if ("zh".equalsIgnoreCase(locale) || "zh_CN".equalsIgnoreCase(locale)) {
          psnInfoStr.append(
              StringUtils.isNotEmpty(institution.getZhName()) ? institution.getZhName() : institution.getEnName());
        } else {
          psnInfoStr.append(
              StringUtils.isNotEmpty(institution.getEnName()) ? institution.getEnName() : institution.getZhName());
        }
      }
    }
    if (psnInfoStr.length() == 0) {
      psnInfoStr.append(psn.getInsName());
    }
    if (psnInfoStr.length() > 0 && StringUtils.isNotEmpty(psn.getPosition())) {
      psnInfoStr.append(", ");
    }
    if (StringUtils.isNotEmpty(psn.getPosition())) {
      psnInfoStr.append(psn.getPosition());
    }
    psnInfo.setPsnInfo(psnInfoStr.toString());
    // if (StringUtils.isNotBlank(psn.getInsName()) && StringUtils.isNotBlank(psn.getPosition())) {
    // psnInfo.setPsnInfo(psn.getInsName() + ", " + psn.getPosition());
    // } else {
    // psnInfo.setPsnInfo(toStr(psn.getInsName()) + toStr(psn.getPosition()));
    // }
    if (StringUtils.isBlank(pubDetailVO.getFirstAvatars())) {
      pubDetailVO.setFirstAvatars(psn.getAvatars());
    }
  }

  private void findAuthorEmail(PubDetailVO pubDetailVO, List<String> authorEmailList) {
    List<PubMemberDTO> members = pubDetailVO.getMembers();
    if (CollectionUtils.isNotEmpty(members)) {
      for (PubMemberDTO m : members) {
        authorEmailList.add(m.getEmail());
      }
    }
  }

  private void setFirstAvatars(Map<String, Object> map, PubDetailVO pubDetailVO) {
    if (StringUtils.isBlank(pubDetailVO.getFirstAvatars())) {
      pubDetailVO.setFirstAvatars("/resmod/smate-pc/img/logo_psndefault.png");
    }
    map.put("avatars", pubDetailVO.getFirstAvatars());
  }

  private void findPsnUrl(AutorPsnInfo psnInfo, Long personId) {
    PsnProfileUrl psnUrl = psnProfileUrlDao.find(personId);
    if (psnUrl != null && StringUtils.isNotBlank(psnUrl.getPsnIndexUrl())) {
      psnInfo.setPsnUrl(domainscm + "/P/" + psnUrl.getPsnIndexUrl());
    }
  }

  private void findPsnStatis(AutorPsnInfo psnInfo, Long personId) {
    PsnStatistics psnStatistics = psnStatisticsService.getPsnStatistics(personId);
    if (psnStatistics != null) {
      psnInfo.setPubCount(psnStatistics.getPubSum());
      psnInfo.setPrjCount(psnStatistics.getPrjSum());
      psnInfo.sethIndex(psnStatistics.getHindex());
    }
  }

  private void findFriendStatus(AutorPsnInfo psnInfo, Long personId) {
    Long userId = SecurityUtils.getCurrentUserId();
    if (userId != null && userId != 0L && checkFriend(userId, personId)) {
      psnInfo.setIsFriend("1");
    }
  }

  private String handleName(String one) throws Exception {
    one = one.trim();
    return cleanXMLAuthorChars(one);
  }

  /**
   * 将xml作者常见字符替换为空格 如果有连续空格则只保留一个并转小写；;分隔符不能替换
   * 
   * @param string
   * @return
   * @author LIJUN
   * @throws UnsupportedEncodingException
   * @date 2018年3月26日
   */
  protected String cleanXMLAuthorChars(String string) throws Exception {
    string = HtmlUtils.htmlUnescape(URLDecoder.decode(string, "utf-8"));
    String regEx = "[`~!@#$%^&*()+=|{}':',\\[\\].\\-<>/?~！@#￥%……&*（）——+|{}【】‘：”“’。·《》，、·？\" ]";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(string);
    string = m.replaceAll(" ").trim();
    while (string.contains("  ")) {
      string = string.replace("  ", " ");
    }
    return string;
  }

  /**
   * 将xml作者常见字符替换为空格 如果有连续空格则只保留一个并转小写；;分隔符不能替换，不清除英文逗号
   * 
   * @param string
   * @return
   * @author LIJUN
   * @throws UnsupportedEncodingException
   * @date 2018年3月26日
   */
  protected String cleanXMLAuthorChars2(String string) throws Exception {
    string = HtmlUtils.htmlUnescape(URLDecoder.decode(string, "utf-8"));
    String regEx = "[`~!@#$%^&*()+=|{}':'\\[\\].\\-<>/?~！@#￥%……&*（）——+|{}【】‘：”“’。·《》，、·？\" ]";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(string);
    string = m.replaceAll(" ").trim();
    while (string.contains("  ")) {
      string = string.replace("  ", " ");
    }
    return string;
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
              result.append("<span>" + word + "</span>");
            } else {
              result.append("; <span>" + word + "</span>");
            }
          }
        }
      }
    }
    return result.toString();
  }

  protected boolean checkFriend(Long userId, Long personId) {
    Long friend = friendDao.isFriend(userId, personId);
    if (friend != null && friend > 0) {
      return true;
    }
    return false;
  }

  private String toStr(Object obj) {
    if (obj == null) {
      return "";
    }
    return obj.toString();
  }

  @Override
  public String getPubInsName(Long pubId) throws ServiceException {
    List<PdwhPubAddrInsRecord> pubAddrInsList = pdwhPubAddrInsRecordDao.getPubAddrInsRecordByPubId(pubId);
    StringBuilder insNames = new StringBuilder();
    if (CollectionUtils.isNotEmpty(pubAddrInsList)) {
      for (PdwhPubAddrInsRecord pdwhPubAddrInsRecord : pubAddrInsList) {
        Long insId = pdwhPubAddrInsRecord.getInsId();
        String insName = pdwhPubAddrInsRecord.getInsName();
        if (StringUtils.isNotBlank(insName)) {
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
          if (StringUtils.isBlank(insNames)) {
            insNames.append(insName);
          } else if (insNames.indexOf(insName) == -1) {
            insNames.append("; " + insName);
          }
        }
      }
    }
    return insNames.toString();
  }

  @Override
  public List<String> getInsDetailsById(Long insId) {
    return this.pdwhInsAddrConstDao.getInsNameByInsId(insId);
  }

  @Override
  public List<String> getAllUserName(Long psnId) {
    return this.personPmNameDao.getUserNameByPsnId(psnId);
  }

  @Override
  public List<String> getUserNameByPsnIdByType(Long psnId, Integer type) {
    return this.personPmNameDao.getUserNameByPsnIdByType(psnId, type);
  }

}
