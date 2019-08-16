package com.smate.web.dyn.service.msg;

import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.core.base.pub.enums.PubSnsStatusEnum;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.dyn.dao.msg.MsgContentDao;
import com.smate.web.dyn.dao.msg.MsgRelationDao;
import com.smate.web.dyn.dao.pub.PubSnsDAO;
import com.smate.web.dyn.dao.pub.PubSnsFullTextDAO;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.model.msg.MsgContent;
import com.smate.web.dyn.model.msg.MsgRelation;
import com.smate.web.dyn.model.msg.MsgShowInfo;
import com.smate.web.dyn.model.pub.PubFullTextPO;
import com.smate.web.dyn.model.pub.PubSnsPO;

/**
 * 站内信消息显示实现服务类
 * 
 * @author ajb
 *
 */
@Transactional(rollbackOn = Exception.class)
public class BuildPubFulltextRequestMsgInfoServiceImpl extends BuildMsgInfoBase {

  @Resource
  private MsgContentDao msgContentDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private MsgRelationDao msgRelationDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubSnsFullTextDAO pubSnsFullTextDAO;

  @SuppressWarnings("unlikely-arg-type")
  @Override
  public void buildMsgShowInfo(MsgShowForm form, MsgRelation m) throws Exception {

    MsgContent msgContent = msgContentDao.get(m.getContentId());
    if (msgContent != null && JacksonUtils.isJsonString(msgContent.getContent())) {
      MsgShowInfo msi = JacksonUtils.jsonObject(msgContent.getContent(), MsgShowInfo.class);
      Person senderInfo = personDao.findPersonBase(m.getSenderId());
      Map<String, Object> pubInfoMap = findPubInfoByPubId(msi.getPubId());
      // 如果成果别删除了，则不显示这条信息
      if (pubInfoMap.get("pubId") == null) {
        // 成果被删除
        msgRelationDao.updateMsgByContentId(m.getContentId(), 11);
        return;
      }
      Long pubSnsId = Long.parseLong(pubInfoMap.get("pubId").toString());
      PubSnsPO pubSns = pubSnsDAO.getpubBriefAndTitle(pubSnsId);
      if (pubSns == null || pubSns.getStatus().equals(PubSnsStatusEnum.DELETED)) {
        // 成果被删除
        msgRelationDao.updateMsgByContentId(m.getContentId(), 11);
        return;
      }
      this.buildPsnInfo(msi, senderInfo, "sender");
      this.handlePubInfo(msi);
      if (StringUtils.isNotBlank(form.getSearchKey())) {
        /*
         * 检索条件筛选，对消息发送人姓名和成果标题进行筛选
         */
        /*
         * StringBuilder sb = new StringBuilder(); if (msi.getSenderZhName() != null) {
         * sb.append(msi.getSenderZhName()); } if (msi.getSenderEnName() != null) {
         * sb.append(msi.getSenderEnName()); } if (msi.getPubTitleZh() != null) {
         * sb.append(msi.getPubTitleZh()); } if (msi.getPubTitleEn() != null) {
         * sb.append(msi.getPubTitleEn()); } // 筛选，不符合条件直接返回 if
         * (!sb.toString().toLowerCase().contains(form.getSearchKey(). trim().toLowerCase())) { return; }
         */
      }
      msi.setType(m.getType());
      msi.setCreateDate(m.getCreateDate());
      msi.setMsgRelationId(m.getId());
      msi.setPsnId(form.getPsnId());
      msi.setSenderId(m.getSenderId());
      msi.setStatus(m.getStatus());
      // 中文标题的符号转义
      msi.setPubTitleZh(StringEscapeUtils.unescapeHtml4(msi.getPubTitleZh()));
      checkWhetherUploadPubFulltext(m, msi);
      // 全文请求去重
      Long reqPsnId = m.getSenderId();
      Long pubId = msi.getPubId();
      if (form.getDupReqPubFulltext().containsKey(reqPsnId)
          && form.getDupReqPubFulltext().get(reqPsnId).longValue() == pubId.longValue()) {
        return;
      } else {
        form.getDupReqPubFulltext().put(reqPsnId, pubId);
      }
      // 全文请求去重
      form.getMsgShowInfoList().add(msi);
    }
  }

  public MsgShowInfo getMsgShowInfo(Long msgRelationId) {
    MsgRelation msgRelation = msgRelationDao.get(msgRelationId);
    MsgShowInfo msgShowInfo = new MsgShowInfo();
    if (msgRelation != null) {
      Person sender = personDao.get(msgRelation.getSenderId());
      buildPsnInfo(msgShowInfo, sender, "sender");
      handlePubInfo(msgShowInfo);
      checkWhetherUploadPubFulltext(msgRelation, msgShowInfo);
      return msgShowInfo;
    }
    return null;
  }

  /**
   * 如果是没有上传全文的请求，则需判断， 自己是否上传全文。如果上传了全文， 则要修改状态。
   * 
   * 如果消息显示，上传全文，但是全文有被删除，需求在此判断是否上传全文 -- 2017-09-27 -ajb
   * 
   * @param m
   * @param msi
   */
  private void checkWhetherUploadPubFulltext(MsgRelation m, MsgShowInfo msi) {
    if (msi.getPubId() != null) {
      PubFullTextPO pubFulltext = pubSnsFullTextDAO.getPubFullTextByPubId(msi.getPubId());
      if (pubFulltext != null) {
        msi.setHasPubFulltext("true");
        msi.setPubFulltextId(pubFulltext.getFileId());
        if (StringUtils.isNotBlank(pubFulltext.getThumbnailPath())) {
          msi.setPubFulltextExt(FileUtils.getFileNameExtensionStr(pubFulltext.getThumbnailPath()));
        }
        msi.setPubFulltextImagePath(pubFulltext.getThumbnailPath());
      } else {
        msi.setHasPubFulltext("false");
      }
    }
  }

  /**
   * 构建成果标题和摘要的中英文显示
   * 
   * @param msi
   */

  void handlePubInfo(MsgShowInfo msi) {

    if (StringUtils.isBlank(msi.getPubTitleZh()) && StringUtils.isNotBlank(msi.getPubTitleEn())) {
      msi.setPubTitleZh(msi.getPubTitleEn());
    }
    if (StringUtils.isBlank(msi.getPubTitleEn()) && StringUtils.isNotBlank(msi.getPubTitleZh())) {
      msi.setPubTitleEn(msi.getPubTitleZh());
    }
    if (StringUtils.isBlank(msi.getPubBriefZh()) && StringUtils.isNotBlank(msi.getPubBriefEn())) {
      msi.setPubBriefZh(msi.getPubBriefEn());
    }
    if (StringUtils.isBlank(msi.getPubBriefEn()) && StringUtils.isNotBlank(msi.getPubBriefZh())) {
      msi.setPubBriefEn(msi.getPubBriefZh());
    }
    msi.setNoneHtmlLablepubAuthorName(HtmlUtils.Html2Text(msi.getPubAuthorName()));
  }

}
