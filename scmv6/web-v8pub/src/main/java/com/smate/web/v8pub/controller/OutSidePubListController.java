package com.smate.web.v8pub.controller;

import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PsnStatisticsService;
import com.smate.web.v8pub.service.sns.publist.PubListService;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.PubQueryModel;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 站外成果列表控制器
 * 
 * @author aijiangbin
 * @date 2018年8月7日
 */
@Controller
public class OutSidePubListController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public final static String SNS_PUB_CALLBACK = "/data/pub/query/snspubcount";

  @Resource
  private PubListService pubListService;
  @Resource
  private PsnStatisticsService psnStatisticsService;

  @Value("${domainscm}")
  private String domainscm;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  /**
   * 个人成果列表主页
   * 
   * @param pubVo
   * @return
   */
  @RequestMapping("/pub/outside/ajaxmain")
  public ModelAndView snspubajaxpubmain(PubListVO pubListVO) {

    ModelAndView view = new ModelAndView();
    try {
      if (isLogin()) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/psnweb/homepage/show?module=pub");
      }
    } catch (Exception e) {
      logger.error("站外-个人主页-成果模块-成果列表 -出错,psnId=" + pubListVO.getPsnId(), e);
    }
    pubListVO.setSelf("no");
    pubListVO.setHasPrivatePub(psnStatisticsService.hasPrivatePub(pubListVO.getPsnId()));
    pubListService.dealPubStatistics(pubListVO);
    view.addObject(pubListVO);
    view.setViewName("/outside_pub/outside_pub_main");
    return view;
  }

  /**
   * 是否已登录
   * 
   * @return
   */
  private boolean isLogin() {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId == null || psnId == 0) {
      return false;
    } else {
      return true;
    }
  }

  @RequestMapping("/pub/outside/ajaxlist")
  public ModelAndView snspubajaxpublist(PubQueryModel pubQueryModel) {

    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      if (isLogin()) {
        Struts2Utils.getResponse().sendRedirect(domainscm + "/psnweb/homepage/show?module=pub");
      }
    } catch (Exception e) {
      logger.error("站外-个人主页-成果模块-成果列表 -出错,psnId=" + pubQueryModel.getSearchPsnId(), e);
    }
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    PubListVO pubListVO = new PubListVO();
    pubListVO.setPubQueryDTO(pubQueryDTO);


    pubListVO.setSelf("no");
    pubQueryDTO.setServiceType("pubList");
    ModelAndView view = new ModelAndView();
    pubListVO.getPubQueryDTO().setServiceType("pubList");
    pubListService.showPubList(pubListVO);

    view.addObject(pubListVO);
    view.setViewName("/outside_pub/outside_pub_main_list");
    return view;
  }
}
