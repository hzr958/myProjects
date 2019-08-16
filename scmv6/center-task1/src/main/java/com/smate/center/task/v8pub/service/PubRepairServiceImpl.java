package com.smate.center.task.v8pub.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.journal.BaseJournalDao;
import com.smate.center.task.dao.sns.quartz.ConstRefDbDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.pub.BaseJournal;
import com.smate.center.task.model.sns.quartz.ConstRefDb;
import com.smate.center.task.utils.DataFormatUtils;
import com.smate.center.task.utils.data.PubLocaleUtils;
import com.smate.center.task.v8pub.dao.pdwh.PdwhDataTaskDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubJournalDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubMemberDAO;
import com.smate.center.task.v8pub.dao.sns.PubDataTaskDAO;
import com.smate.center.task.v8pub.dao.sns.PubMemberDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.dao.sns.PubSituationDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDetailDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubJournalPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubMemberPO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;
import com.smate.center.task.v8pub.sns.po.PubMemberPO;
import com.smate.center.task.v8pub.sns.po.PubSituationPO;
import com.smate.center.task.v8pub.sns.po.PubSnsPO;
import com.smate.center.task.v8pub.strategy.PubTypeInfoConstructor;
import com.smate.center.task.v8pub.strategy.PubTypeInfoDriver;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.date.DateStringSplitFormateUtil;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pub.BriefFormatter;
import com.smate.web.v8pub.dom.BookInfoBean;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.MemberInsBean;
import com.smate.web.v8pub.dom.PubMemberBean;
import com.smate.web.v8pub.dom.PubSituationBean;
import com.smate.web.v8pub.dom.PubTypeInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

@Service("pubRepairService")
@Transactional(rollbackFor = Exception.class)
public class PubRepairServiceImpl implements PubRepairService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhDataTaskDAO pdwhDataTaskDAO;
  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;
  @Autowired
  private PubMemberDAO pubMemberDAO;
  @Autowired
  private PdwhPubMemberDAO pdwhPubMemberDAO;
  @Autowired
  private PubDataTaskDAO pubDataTaskDAO;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubSituationDAO pubSituationDAO;
  @Autowired
  private ConstRefDbDao constRefDbDao;
  @Autowired
  private PdwhPubJournalDAO pdwhPubJournalDAO;
  @Autowired
  private BaseJournalDao baseJournalDao;
  @Autowired
  private PubTypeInfoConstructor pubTypeInfoConstructor;


  @Override
  public List<PubDataTaskPO> findPubId(Long startId, Long endId, Integer size) {
    return pubDataTaskDAO.findPubId(startId, endId, size);
  }

  @Override
  public List<PdwhDataTaskPO> findPdwhId(Integer size, Long startId, Long endId) {
    return pdwhDataTaskDAO.findPdwhPubId(startId, endId, size);
  }


  @Override
  public void save(PubDataTaskPO pubData) {
    pubDataTaskDAO.saveOrUpdate(pubData);
  }

  @Override
  public void save(PdwhDataTaskPO pubData) {
    pdwhDataTaskDAO.saveOrUpdate(pubData);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void perferPdwhMemberId(PdwhDataTaskPO pdwhData) {
    try {
      PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDAO.findByPubId(pdwhData.getPubId());
      List<PdwhPubMemberPO> memberPos = pdwhPubMemberDAO.findByPubId(pdwhData.getPubId());
      if (pubPdwhDetailDOM != null && CollectionUtils.isNotEmpty(memberPos)) {
        List<PubMemberBean> memberBeans = pubPdwhDetailDOM.getMembers();
        if (CollectionUtils.isNotEmpty(memberBeans)) {
          for (PubMemberBean pubMemberBean : memberBeans) {
            loop: for (PdwhPubMemberPO pdwhPubMemberPO : memberPos) {
              if (pubMemberBean.getSeqNo().equals(pdwhPubMemberPO.getSeqNo())) {
                pubMemberBean.setMemberId(pdwhPubMemberPO.getId());
                break loop;
              }
            }
          }
          pubPdwhDetailDOM.setMembers(memberBeans);
        }
        pubPdwhDetailDAO.save(pubPdwhDetailDOM);
        pdwhData.setStatus(1);
      } else {
        pdwhData.setStatus(-1);
        pdwhData.setError("作者无数据，或者mongodb中无数据");
      }
      save(pdwhData);
    } catch (Exception e) {
      logger.error("完善基准库memberId字段数据出错！", e);
      throw new ServiceException(e);
    }

  }

  @SuppressWarnings("unchecked")
  @Override
  public void perferSnsMemberId(PubDataTaskPO pubData) {
    try {
      PubSnsDetailDOM pubDetail = pubSnsDetailDAO.findByPubId(pubData.getPubId());
      List<PubMemberPO> memberPos = pubMemberDAO.getAllMember(pubData.getPubId());
      if (pubDetail != null && CollectionUtils.isNotEmpty(memberPos)) {
        List<PubMemberBean> memberBeans = pubDetail.getMembers();
        if (CollectionUtils.isNotEmpty(memberBeans)) {
          for (PubMemberBean pubMemberBean : memberBeans) {
            loop: for (PubMemberPO pubMemberPO : memberPos) {
              if (pubMemberBean.getSeqNo().equals(pubMemberPO.getSeqNo())) {
                pubMemberBean.setMemberId(pubMemberPO.getId());
                break loop;
              }
            }
          }
          pubDetail.setMembers(memberBeans);
        }
        pubSnsDetailDAO.save(pubDetail);
        pubData.setStatus(1);
      } else {
        pubData.setStatus(-1);
        pubData.setError("作者无数据，或者mongodb中无数据");
      }
      save(pubData);
    } catch (Exception e) {
      logger.error("完善个人库memberId字段数据出错！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void rebuildPdwhBerifDesc(PdwhDataTaskPO pubData) {
    try {
      PubPdwhPO pubPdwhPO = pubPdwhDAO.get(pubData.getPubId());
      PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailDAO.findByPubId(pubData.getPubId());
      if (pubPdwhDetailDOM != null && pubPdwhPO != null
          && pubPdwhDetailDOM.getPubType().equals(PublicationTypeEnum.BOOK)) {

        Map<String, String> result = buildBookData(pubPdwhDetailDOM.getTypeInfo(), pubPdwhDetailDOM.getPublishDate());
        // 根据书名去判断中英文，而不是章节名，因为章节名存在数字的情况
        BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(result.get("NAME")), result);
        String briefDesc = formatter.format(BriefFormatter.BOOKBRIEF_PATTERN);
        // 1.更新mongodb中数据
        pubPdwhDetailDOM.setBriefDesc(briefDesc);
        pubPdwhDetailDAO.save(pubPdwhDetailDOM);
        // 2.更新v_pub_pdwh表数据
        pubPdwhPO.setBriefDesc(briefDesc);
        pubPdwhDAO.saveOrUpdate(pubPdwhPO);
        pubData.setStatus(1);
      } else {
        pubData.setStatus(-1);
        pubData.setError("mongodb中无数据，表中无记录或者成果类型不为2（书籍章节）");
      }
      save(pubData);
    } catch (Exception e) {
      logger.error("重构基准库编目信息数据出错！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 构建书籍/著作数据
   *
   * @param typeInfo
   * @return
   */
  private Map<String, String> buildBookData(PubTypeInfoBean typeInfo, String publishDate) {
    BookInfoBean a = (BookInfoBean) typeInfo;
    Map<String, String> map = new HashMap<String, String>();
    if (a != null) {
      map.put("publisher".toUpperCase(), a.getPublisher());
      if (a.getTotalPages() == null || a.getTotalPages() == 0) {
        map.put("total_pages".toUpperCase(), "");
      } else {
        map.put("total_pages".toUpperCase(), a.getTotalPages() + "");
      }
      String language = a.getLanguage();
      language = language.replace("null", "");
      if (StringUtils.isBlank(language)) {
        Locale locale = getLocale(a.getName());
        if (Locale.CHINA.equals(locale)) {
          language = XmlUtil.getLanguageSpecificText(locale.getLanguage(), "中文", "Chinese");
        } else {
          language = XmlUtil.getLanguageSpecificText(locale.getLanguage(), "外文", "Foreign Language");
        }
      }
      map.put("language".toUpperCase(), language);
    }
    map.put("publish_date".toUpperCase(), DataFormatUtils.parseDate(publishDate, "-"));
    return map;
  }

  private static Locale getLocale(String title) {
    if (StringUtils.isEmpty(title)) {
      return Locale.US;
    }
    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
    if (p.matcher(title).find()) {
      return Locale.CHINA;
    }
    return Locale.US;
  }

  @Override
  public void rebuildSnsBerifDesc(PubDataTaskPO pubData) {
    try {
      PubSnsPO pubSnsPO = pubSnsDAO.get(pubData.getPubId());
      PubSnsDetailDOM pubDetail = pubSnsDetailDAO.findByPubId(pubData.getPubId());
      if (pubDetail != null && pubSnsPO != null && pubDetail.getPubType().equals(PublicationTypeEnum.BOOK)) {
        Map<String, String> result = buildBookData(pubDetail.getTypeInfo(), pubDetail.getPublishDate());
        // 根据书名去判断中英文，而不是章节名，因为章节名存在数字的情况
        BriefFormatter formatter = new BriefFormatter(PubLocaleUtils.getLocale(result.get("NAME")), result);
        String briefDesc = formatter.format(BriefFormatter.BOOKBRIEF_PATTERN);
        // 1.保存mongodb数据
        pubDetail.setBriefDesc(briefDesc);
        pubSnsDetailDAO.save(pubDetail);

        // 2.保存v_pub_sns表数据
        pubSnsPO.setBriefDesc(briefDesc);
        pubSnsDAO.saveOrUpdate(pubSnsPO);
        pubData.setStatus(1);
      } else {
        pubData.setStatus(-1);
        pubData.setError("mongodb中无数据，表中无记录或者成果类型不为2（书籍章节）");
      }
      save(pubData);
    } catch (Exception e) {
      logger.error("重构个人库编目信息数据出错！", e);
      throw new ServiceException(e);
    }
  }



  @Override
  public void rebuildSnsPublishDate(PubDataTaskPO pubData) {
    try {
      PubSnsDetailDOM pubDetail = pubSnsDetailDAO.findByPubId(pubData.getPubId());
      PubSnsPO pubSnsPO = pubSnsDAO.get(pubData.getPubId());
      if (pubDetail != null && pubSnsPO != null && StringUtils.isNotBlank(pubDetail.getPublishDate())) {
        Integer publishYear = null, publishMonth = null, publishDay = null;
        String publishDate;
        Map<String, String> publishDateMap = DateStringSplitFormateUtil.split(pubDetail.getPublishDate());
        if (publishDateMap.get("year") != null) {
          publishYear = Integer.valueOf(publishDateMap.get("year"));
        }
        if (publishDateMap.get("month") != null) {
          publishMonth = Integer.valueOf(publishDateMap.get("month"));
        }
        if (publishDateMap.get("day") != null) {
          publishDay = Integer.valueOf(publishDateMap.get("day"));
        }
        publishDate = publishDateMap.get("fomate_date");
        // 1. 保存mongodb的数据
        pubDetail.setPublishDate(publishDate);
        pubSnsDetailDAO.save(pubDetail);
        // 2.保存v_pub_pdwh表数据
        pubSnsPO.setPublishYear(publishYear);
        pubSnsPO.setPublishMonth(publishMonth);
        pubSnsPO.setPublishDay(publishDay);
        pubSnsDAO.saveOrUpdate(pubSnsPO);
        pubData.setStatus(1);
      } else {
        pubData.setError("mongodb数据不存在，v_pub_pdwh表无记录，或者publishDate为空");
        pubData.setStatus(-1);
      }
      save(pubData);
    } catch (Exception e) {
      logger.error("重构个人库出版日期数据出错！", e);
      throw new ServiceException(e);
    }
  }


  @Override
  public void rebuildPdwhPublishDate(PdwhDataTaskPO pubData) {
    try {
      PubPdwhDetailDOM pdwhDetail = pubPdwhDetailDAO.findByPubId(pubData.getPubId());
      PubPdwhPO pubPdwhPO = pubPdwhDAO.get(pubData.getPubId());
      if (pdwhDetail != null && pubPdwhPO != null && StringUtils.isNotBlank(pdwhDetail.getPublishDate())) {
        Integer publishYear = null, publishMonth = null, publishDay = null;
        String publishDate;
        Map<String, String> publishDateMap = DateStringSplitFormateUtil.split(pdwhDetail.getPublishDate());
        if (publishDateMap.get("year") != null) {
          publishYear = Integer.valueOf(publishDateMap.get("year"));
        }
        if (publishDateMap.get("month") != null) {
          publishMonth = Integer.valueOf(publishDateMap.get("month"));
        }
        if (publishDateMap.get("day") != null) {
          publishDay = Integer.valueOf(publishDateMap.get("day"));
        }
        publishDate = publishDateMap.get("fomate_date");
        // 1. 保存mongodb的数据
        pdwhDetail.setPublishDate(publishDate);
        pubPdwhDetailDAO.save(pdwhDetail);
        // 2.保存v_pub_pdwh表数据
        pubPdwhPO.setPublishYear(publishYear);
        pubPdwhPO.setPublishMonth(publishMonth);
        pubPdwhPO.setPublishDay(publishDay);
        pubPdwhDAO.saveOrUpdate(pubPdwhPO);
        pubData.setStatus(1);
      } else {
        pubData.setError("mongodb数据不存在，v_pub_pdwh表无记录，或者publishDate为空");
        pubData.setStatus(-1);
      }
      save(pubData);
    } catch (Exception e) {
      logger.error("重构基准库出版日期数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void rebuildPubSituation(PubDataTaskPO pubData) {
    try {
      PubSnsDetailDOM pubDetail = pubSnsDetailDAO.findByPubId(pubData.getPubId());
      List<PubSituationPO> sitList = pubSituationDAO.getByPubId(pubData.getPubId());
      if (pubDetail == null) {
        pubData.setStatus(-1);
        pubData.setError("mongodb中无数据");
        return;
      }
      if (CollectionUtils.isEmpty(sitList)) {
        pubData.setStatus(-1);
        pubData.setError("成果收录情况无数据！");
        return;
      }
      buildPubSituation(sitList, pubDetail);
    } catch (Exception e) {
      logger.error("修复个人库收录情况数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private void buildPubSituation(List<PubSituationPO> sitList, PubSnsDetailDOM pubDetail) {
    if (CollectionUtils.isEmpty(sitList)) {
      return;
    }
    Set<PubSituationBean> situations = new HashSet<>();
    for (PubSituationPO pubSituationPO : sitList) {
      PubSituationBean sitBean = new PubSituationBean();
      sitBean.setLibraryName(pubSituationPO.getLibraryName());
      sitBean.setSitOriginStatus(pubSituationPO.getSitOriginStatus() == 1);
      sitBean.setSitStatus(pubSituationPO.getSitStatus() == 1);
      String dbCode = pubSituationPO.getLibraryName();
      if (StringUtils.isNotBlank(dbCode)) {
        ConstRefDb constRefDb = constRefDbDao.getConstRefDbByCode(dbCode);
        if (constRefDb != null) {
          pubSituationPO.setSrcDbId(constRefDb.getId() + "");
        }
        pubSituationDAO.save(pubSituationPO);
      }
      sitBean.setSrcDbId(pubSituationPO.getSrcDbId());
      sitBean.setSrcId(pubSituationPO.getSrcId());
      sitBean.setSrcUrl(pubSituationPO.getSrcUrl());
      situations.add(sitBean);
    }
    pubDetail.setSituations(situations);
    pubSnsDetailDAO.save(pubDetail);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void repairPdwhJournal(PdwhDataTaskPO pubData) {
    try {
      PubPdwhDetailDOM pdwhDetail = pubPdwhDetailDAO.findByPubId(pubData.getPubId());
      PubPdwhPO pubPdwhPO = pubPdwhDAO.get(pubData.getPubId());
      PdwhPubJournalPO pdwhPubJournalPO = pdwhPubJournalDAO.get(pubData.getPubId());
      if (pdwhDetail == null || pubPdwhPO == null || pdwhPubJournalPO == null) {
        pubData.setStatus(-1);
        pubData.setError("pdwhDetail或者pubPdwhPO或者pdwhPubJournalPO为空");
        return;
      }
      // 1.通过成果id获取到jid
      Long jnlId = pdwhPubJournalPO.getJid();
      if (NumberUtils.isNullOrZero(jnlId)) {
        pubData.setStatus(-1);
        pubData.setError("pubJournalPO实体中jid为空");
        return;
      }
      // 2.通过jid取base_journal表数据，取出期刊名进行赋值
      BaseJournal b2 = baseJournalDao.get(jnlId);
      if (b2 == null) {
        pubData.setStatus(-1);
        pubData.setError("BaseJournal找不到数据jnlId=" + jnlId);
        return;
      }
      boolean isChina = PubLocaleUtils.getLocale(pdwhDetail.getTitle()).equals(Locale.CHINA);
      String jName = isChina ? StringUtils.isNotBlank(b2.getTitleXx()) ? b2.getTitleXx() : b2.getTitleEn()
          : StringUtils.isNotBlank(b2.getTitleEn()) ? b2.getTitleEn() : b2.getTitleXx();
      // 3.重新构造编目信息字段
      JournalInfoBean journalInfoBean = (JournalInfoBean) pdwhDetail.getTypeInfo();
      if (journalInfoBean == null) {
        pubData.setStatus(-1);
        pubData.setError("pdwhDetail中typeInfo数据为空，pubId=" + pdwhDetail.getPubId());
        return;
      }
      journalInfoBean.setName(jName);
      pdwhDetail.setTypeInfo(journalInfoBean);
      String briefDesc = buildBriefDesc(pdwhDetail);

      // 4.更新期刊数据
      pdwhDetail.setBriefDesc(briefDesc);
      pubPdwhPO.setBriefDesc(briefDesc);
      pdwhPubJournalPO.setName(jName);

      // 保存数据
      pubPdwhDetailDAO.save(pdwhDetail);
      pubPdwhDAO.save(pubPdwhPO);
      pdwhPubJournalDAO.save(pdwhPubJournalPO);

    } catch (Exception e) {
      logger.error("修复基准库期刊数据出错！", e);
      throw new ServiceException(e);
    }

  }

  private String buildBriefDesc(PubPdwhDetailDOM pdwhDetail) throws Exception {
    PubTypeInfoDriver pubTypeInfoDriver = pubTypeInfoConstructor.getPubTypeInfoDriver(pdwhDetail.getPubType());
    JournalInfoBean typeInfo = (JournalInfoBean) pdwhDetail.getTypeInfo();
    Locale locale = PubLocaleUtils.getLocale(pdwhDetail.getTitle());
    String briefDesc =
        pubTypeInfoDriver.constructBriefDesc(typeInfo, locale, pdwhDetail.getCountryId(), pdwhDetail.getPublishDate());
    return briefDesc;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void repairSnsPubMember(PubDataTaskPO pubData) {
    try {
      PubSnsDetailDOM pubDetail = pubSnsDetailDAO.findByPubId(pubData.getPubId());
      List<PubMemberPO> memberList = pubMemberDAO.getAllMember(pubData.getPubId());
      List<PubMemberBean> members = pubDetail.getMembers();
      if (CollectionUtils.isEmpty(members) && CollectionUtils.isNotEmpty(memberList)) {
        // 处理第一种数据，mongodb中members无数据，但是v_pub_member中有数据
        // v_pub_member补全完善mongodb中members数据
        members = new ArrayList<>();
        for (PubMemberPO pubMemberPO : memberList) {
          PubMemberBean bean = new PubMemberBean();
          bean.setName(pubMemberPO.getName());
          bean.setEmail(pubMemberPO.getEmail());
          bean.setPsnId(pubMemberPO.getPsnId());
          bean.setSeqNo(pubMemberPO.getSeqNo());
          bean.setMemberId(pubMemberPO.getId());
          bean.setCommunicable(pubMemberPO.getCommunicable() == 1);
          bean.setFirstAuthor(pubMemberPO.getFirstAuthor() == 1);
          List<MemberInsBean> insNames = new ArrayList<>();
          if (StringUtils.isNotBlank(pubMemberPO.getInsName())) {
            MemberInsBean mbean = new MemberInsBean();
            mbean.setInsId(pubMemberPO.getInsId());
            mbean.setInsName(pubMemberPO.getInsName());
            insNames.add(mbean);
          }
          bean.setInsNames(insNames);
          members.add(bean);
        }
        pubDetail.setMembers(members);
        pubSnsDetailDAO.save(pubDetail);
      }

      if (CollectionUtils.isEmpty(memberList) && CollectionUtils.isNotEmpty(members)) {
        // 处理第二种数据，v_pub_member无数据，但是mongodb中members中有数据
        // mongodb中members数据补全完善v_pub_member
        memberList = new ArrayList<>();
        for (PubMemberBean bean : members) {
          PubMemberPO pubMemberPO = new PubMemberPO();
          pubMemberPO.setName(bean.getName());
          pubMemberPO.setEmail(bean.getEmail());
          pubMemberPO.setPubId(pubData.getPubId());
          pubMemberPO.setPsnId(bean.getPsnId());
          List<MemberInsBean> insList = bean.getInsNames();
          if (insList != null && insList.size() > 0) {
            pubMemberPO.setInsId(insList.get(0).getInsId());
            pubMemberPO.setInsName(insList.get(0).getInsName());
          }
          pubMemberPO.setSeqNo(bean.getSeqNo());
          pubMemberPO.setCommunicable(bean.isCommunicable() ? 1 : 0);
          pubMemberPO.setFirstAuthor(bean.isFirstAuthor() ? 1 : 0);
          // mongodb中没有存这个关系，因此直接给默认即可
          pubMemberPO.setOwner(0);
          pubMemberDAO.save(pubMemberPO);
          bean.setMemberId(pubMemberPO.getId());
        }
        pubDetail.setMembers(members);
        pubSnsDetailDAO.save(pubDetail);
      }

    } catch (Exception e) {
      logger.error("个人库修复作者数据出错！", e);
      throw new ServiceException(e);
    }

  }



}
