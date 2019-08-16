package com.smate.web.v8pub.controller;


import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigPubPk;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.service.sns.homepage.PsnInfluenceIndexPubService;
import com.smate.web.v8pub.service.sns.psnconfigpub.PsnConfigPubService;
import com.smate.web.v8pub.service.sns.psnconfigpub.PsnConfigService;
import com.smate.web.v8pub.vo.PsnInfluencePubVO;

@Controller
public class PsnInfluenceIndexPubController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnInfluenceIndexPubService psnInfluenceIndexPubService;
  @Resource
  private PsnConfigPubService psnConfigPubService;
  @Resource
  private PsnConfigService psnConfigService;

  /**
   * 显示H-index提升推荐的成果列表
   * 
   * @return
   */
  @RequestMapping(value = {"/pub/influence/ajaxhindexlist"})
  public ModelAndView hindexList(PsnInfluencePubVO pubVo) {
    ModelAndView model = new ModelAndView("/pub/homepage/pubhindex/psn_influence_pubhindex");
    try {
      if (pubVo.getPsnId().equals(SecurityUtils.getCurrentUserId())) {
        pubVo.setSelf("yes");
      } else {
        pubVo.setSelf("no");
      }
      if (SecurityUtils.getCurrentUserId().equals(0L)) {
        pubVo.setHasLogin("no");
      } else {
        pubVo.setHasLogin("yes");
      }
      psnInfluenceIndexPubService.pubHindexShow(pubVo);
      Long cnfIdByPsnId = psnConfigService.getCnfIdByPsnId(pubVo.getPsnId());
      List<PubInfo> resultList = pubVo.getPubHindexList();
      for (PubInfo pi : resultList) {
        PsnConfigPub psnConfigPub = psnConfigPubService.get(new PsnConfigPubPk(cnfIdByPsnId, pi.getPubId()));
        if (psnConfigPub != null) {
          pi.setIsAnyUser(psnConfigPub.getAnyUser());
        }
      }
      model.addObject("pubVo", pubVo);
    } catch (Exception e) {
      logger.error("显示H-index提升推荐的成果列表出错， psnId = " + pubVo.getPsnId(), e);
    }
    return model;
  }

  @RequestMapping(value = {"/pub/outside/influence/ajaxhindexlist"})
  public ModelAndView outsideHindexList(PsnInfluencePubVO pubVo) {
    ModelAndView model = new ModelAndView("/outside_pub/homepage/pubhindex/outside_psn_influence_pubhindex");
    try {
      if (pubVo.getPsnId().equals(SecurityUtils.getCurrentUserId())) {
        pubVo.setSelf("yes");
      } else {
        pubVo.setSelf("no");
      }
      if (SecurityUtils.getCurrentUserId().equals(0L)) {
        pubVo.setHasLogin("no");
      } else {
        pubVo.setHasLogin("yes");
      }
      psnInfluenceIndexPubService.pubHindexShow(pubVo);
      model.addObject("pubVo", pubVo);
    } catch (Exception e) {
      logger.error("显示H-index提升推荐的成果列表出错， psnId = " + pubVo.getPsnId(), e);
    }
    return model;
  }
}
