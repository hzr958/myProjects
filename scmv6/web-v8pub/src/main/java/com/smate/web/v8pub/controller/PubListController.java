package com.smate.web.v8pub.controller;

import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigPubPk;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PsnStatisticsService;
import com.smate.web.v8pub.service.sns.psnconfigpub.PsnConfigPubService;
import com.smate.web.v8pub.service.sns.psnconfigpub.PsnConfigService;
import com.smate.web.v8pub.service.sns.publist.PubListService;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.PubQueryModel;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * 成果列表控制器
 * 
 * @author aijiangbin
 * @date 2018年8月7日
 */
@Controller
public class PubListController {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  public final static String SNS_PUB_CALLBACK = "/data/pub/query/snspubcount";
  @Resource
  private PubListService pubListService;
  @Resource
  private PsnStatisticsService psnStatisticsService;
  @Resource
  private PsnConfigPubService psnConfigPubService;
  @Resource
  private PsnConfigService psnConfigService;
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  /**
   * 个人成果列表主页
   * 
   * @param pubListVO
   * @return
   */
  @RequestMapping("/pub/psn/ajaxmain")
  public ModelAndView snspubajaxpubmain(PubListVO pubListVO) {
    ModelAndView view = new ModelAndView();
    // 获取当前运行环境
    String sys = System.getenv("RUN_ENV");
    if (StringUtils.isBlank(sys)) {
      sys = "run";
    }
    Long userId = SecurityUtils.getCurrentUserId();
    if (userId != null && userId != 0L && pubListVO.getPsnId() != null
        && userId.longValue() == pubListVO.getPsnId().longValue()) {
      pubListVO.setSelf("yes");
    } else {
      pubListVO.setSelf("no");
      pubListVO.setHasPrivatePub(psnStatisticsService.hasPrivatePub(pubListVO.getPsnId()));
    }
    pubListService.dealPubStatistics(pubListVO);
    pubListVO.setSnsDomain(domainscm.replace("http://", ""));
    pubListVO.setRun_env(sys);
    view.addObject(pubListVO);
    view.setViewName("/pub/main/pub_main");
    return view;
  }

  @RequestMapping("/pub/psn/ajaxlist")
  public ModelAndView snspubajaxpublist(PubQueryModel pubQueryModel) {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getParamPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    PubListVO pubListVO = new PubListVO();
    pubListVO.setPubQueryDTO(pubQueryDTO);
    if (pubQueryDTO.getSearchPsnId().longValue() == SecurityUtils.getCurrentUserId().longValue()) {
      pubListVO.setSelf("yes");
    } else {
      pubListVO.setSelf("no");
    }
    //// 加
    pubQueryDTO.setServiceType("pubList");
    ModelAndView view = new ModelAndView();
    // 编辑成果 默认排序 成果更新时间
    if (pubQueryModel.isEditPubFlag()) {
      pubListVO.getPubQueryDTO().setOrderBy("updateDate");
    }
    pubListVO.getPubQueryDTO().setServiceType("pubList");
    // pubListVO.getPubQueryDTO().setSearchPsnId(pubListVO.getPsnId());
    pubListService.showPubList(pubListVO);
    // 当前人员的配置主键
    Long cnfIdByPsnId = psnConfigService.getCnfIdByPsnId(pubQueryDTO.getSearchPsnId());
    List<PubInfo> resultList = pubListVO.getResultList();
    for (PubInfo pi : resultList) {
      PsnConfigPub psnConfigPub = psnConfigPubService.get(new PsnConfigPubPk(cnfIdByPsnId, pi.getPubId()));
      if (psnConfigPub != null) {
        pi.setIsAnyUser(psnConfigPub.getAnyUser());
      }
    }
    view.addObject(pubListVO);
    view.setViewName("/pub/main/pub_main_list");
    return view;
  }
}
