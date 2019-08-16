package com.smate.web.v8pub.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.v8pub.po.seo.PubIndexThirdLevel;
import com.smate.web.v8pub.service.pdwh.indexurl.PdwhPubIndexUrlService;
import com.smate.web.v8pub.service.sns.SystemSeoSearch;
import com.smate.web.v8pub.vo.pdwh.PdwhPubSeoVo;

@Controller
public class PubSeoSearchController {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${domainscm}")
  private String domainscm;
  @Resource
  private SystemSeoSearch systemSeoSearch;
  @Resource
  private PdwhPubIndexUrlService pdwhPubIndexUrlService;

  @RequestMapping(value = "/pub/viewfromfirstlevel", produces = "application/html;charset=UTF-8")
  public ModelAndView searchSecondIndexUser(PdwhPubSeoVo pdwhPubSeoVo) throws Exception {
    systemSeoSearch.putFilterAndbuildPage(pdwhPubSeoVo.getKey1(), pdwhPubSeoVo.getKey2());
    if (StringUtils.isNotBlank(pdwhPubSeoVo.getLan())) {
      if ("en_us".equalsIgnoreCase(pdwhPubSeoVo.getLan())) {
        pdwhPubSeoVo.setLan("en_US");
      } else {
        pdwhPubSeoVo.setLan("zh_CN");
      }
    }
    return new ModelAndView("redirect:" + domainscm + "/indexhtml/pub2_" + pdwhPubSeoVo.getKey1() + "_"
        + pdwhPubSeoVo.getLan() + ".html?t=" + (new Date().getTime()));// 后面增加参数解决ie浏览器缓存问题
  }

  /**
   * 首页-论文 查看第二层级
   * 
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/pub/viewfromSecondlevel")
  public ModelAndView searchIndexUser(PdwhPubSeoVo pdwhPubSeoVo) throws Exception {
    ModelAndView view = new ModelAndView();
    Integer pageNo = null;
    if (StringUtils.isNotBlank(pdwhPubSeoVo.getPageNo())) {
      if (pdwhPubSeoVo.getPageNo().matches("\\-[0-9]+")) {
        pageNo = Integer.valueOf(pdwhPubSeoVo.getPageNo().replace("-", ""));
        pdwhPubSeoVo.getPage().setPageNo(pageNo);
      }
    } else {
      pageNo = 1;
      pdwhPubSeoVo.getPage().setPageNo(1);
    }
    // if (StringUtils.isNotBlank(pdwhPubSeoVo.getLan())) {
    // if ("en_us".equalsIgnoreCase(pdwhPubSeoVo.getLan())) {
    // } else {
    // }
    // }
    Page<PubIndexThirdLevel> page =
        systemSeoSearch.getPubByLabel2(pdwhPubSeoVo.getKey1(), pdwhPubSeoVo.getKey2(), pageNo, pdwhPubSeoVo.getPage());
    List<PubIndexThirdLevel> list = page.getResult();
    for (PubIndexThirdLevel t : list) {
      String indexUrl = pdwhPubIndexUrlService.getIndexUrlByPubId(t.getPubId());
      if (StringUtils.isNotBlank(indexUrl)) {
        t.setIndexUrl(domainscm + "/" + ShortUrlConst.S_TYPE + "/" + indexUrl);
      } else {
        t.setIndexUrl(
            domainscm + "/pub/outside/pdwhdetails?des3PubId=" + Des3Utils.encodeToDes3(t.getPubId().toString()));
      }
    }
    pdwhPubSeoVo.setPage(page);
    view.setViewName("pub/seo/seo_pub_second_level");
    return view;
  }

}
