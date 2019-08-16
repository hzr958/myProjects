package com.smate.web.group.service.grp.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.web.group.action.grp.form.GrpPubInfoVO;
import com.smate.web.group.action.grp.form.GrpPubShowInfo;
import com.smate.web.group.dao.group.pub.PubFullTextReqBaseDao;
import com.smate.web.group.dao.group.pub.PubFulltextDao;

/**
 * 群组成果服务实现类
 * 
 * @author tsz
 *
 */
@Service("grpPubShowInfoService")
@Transactional(rollbackFor = Exception.class)
public class GrpPubShowInfoServiceImpl implements GrpPubShowInfoService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubFulltextDao pubFulltextDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private PubFullTextReqBaseDao pubFTReqBaseDao;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private PsnCnfService psnCnfService;

  /*
   * 重新构建显示数据
   */
  @Override
  public GrpPubShowInfo buildShowPubInfo(GrpPubInfoVO pub) throws Exception {
    GrpPubShowInfo pubInfo = new GrpPubShowInfo();
    // TODO
    // pubInfo.setUpdateMark(pub.getUpdateMark());
    pubInfo.setPubId(pub.getPubId());
    pubInfo.setAuthors(pub.getAuthorNames());
    pubInfo.setZhBrif(pub.getBriefDesc());
    pubInfo.setZhTitle(pub.getTitle());
    pubInfo.setPublishYear(pub.getPublishYear());
    pubInfo.setEnBrif(pub.getBriefDesc());
    pubInfo.setEnTitle(pub.getTitle());
    pubInfo.setPubIndexUrl(pub.getPubIndexUrl());
    pubInfo.setNoneHtmlLableAuthorNames(HtmlUtils.Html2Text(pub.getAuthorNames()));
    if (pub.getHasFulltext() == 0) {
      pubInfo.setHasFulltext(0);
    } else {
      pubInfo.setFullTextImaUrl(pub.getFullTextImgUrl());
      pubInfo.setHasFulltext(1);
      pubInfo.setFullTextField(pub.getFullTextFieId().toString());
      pubInfo.setFullTextPermission(pub.getFullTextPermission());
      // pubInfo.setFullTextUrl(fileDownUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT,
      // pub.getPubId()));
      pubInfo.setFullTextUrl(pub.getFullTextDownloadUrl());
    }
    checkPubAuthority(pub, pubInfo);
    return pubInfo;
  }

  public void checkPubAuthority(GrpPubInfoVO pub, GrpPubShowInfo pubInfo) {
    try {
      // 刷新权限
      if (pub.getOwnerPsnId() != null && pub.getOwnerPsnId() > 0L) {
        PsnConfigPub cnfPub = new PsnConfigPub();
        cnfPub.getId().setPubId(pub.getPubId());
        PsnConfigPub cnfPubExists = psnCnfService.get(pub.getOwnerPsnId(), cnfPub);
        if (cnfPubExists == null) {
          pubInfo.setPermission(PsnCnfConst.ALLOWS);
        } else {
          pubInfo.setPermission(cnfPubExists.getAnyUser());
        }
      } else {
        pubInfo.setPermission(PsnCnfConst.ALLOWS);
      }

    } catch (Exception e) {
      logger.error("获取成果隐私设置出错", e);
    }
  }
}
