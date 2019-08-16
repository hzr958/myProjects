package com.smate.core.web.sns.menu.filter;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.WebApplicationContext;

import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.model.menu.UserRolData;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.security.InsRoleService;
import com.smate.core.web.sns.menu.service.MainInitialService;

/**
 * 单位数据处理.
 * 
 * @author zym
 * 
 */
public class SnsMenuData {

  /**
   * 填充单位信息.
   * 
   * @param request
   * @param remotingServiceFactory
   * @param domain
   * @param userRolData
   * @param rolInsId
   * @return
   */
  public static String fillInsInfo(ServletRequest request, WebApplicationContext ctx, String domain,
      UserRolData userRolData, Long rolInsId, Logger logger) {
    // 本处代码为scmsns专用代码,为了能在SNS程序中也展现与ROL相同的菜单,本代码由怪异的需求引发.

    // 设置菜单在 ROL跳转状态时的主要域名

    // 设置domain变量，让ROL跳转过来的链接注销时也能获得对应返回路径

    // +
    // authorityManager.getIndexPageByInsId(rolInsId)这段预留以后扩展自定义主页

    // 根据rolInsId状态，读取登录logo
    try {
      if (StringUtils.isNotBlank(userRolData.getRolDomain())) {
        domain = userRolData.getRolDomain();
      }

      // 基金委logo
      if (rolInsId.longValue() == ServiceConstants.NSFC_INS_2565) {
        userRolData.setRolInsId(ServiceConstants.NSFC_INS_2565);
        userRolData.setRolLogoUrl("/resscmwebsns/images/logo_kyzx.gif");
        userRolData.setFromIsis("nsfcscm");

      } else if (rolInsId.longValue() == ServiceConstants.NSFC_INS_2566) {

        userRolData.setRolInsId(ServiceConstants.NSFC_INS_2566);
        userRolData.setRolLogoUrl("/resscmwebsns/images/logo_cgzx.gif");
        userRolData.setFromIsis("nsfcr");

      } else {
        // 普通单位
        MainInitialService mainInitialService = (MainInitialService) ctx.getBean("mainInitialService");
        String[] insLogo = mainInitialService.loadMainLogo(rolInsId);
        // 修改了获取单位名称的代码_MaoJianGuo_2013-01-30_ROL-396.
        String rolTitle = null;
        if ("zh".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
          rolTitle = insLogo[0] == null ? insLogo[2] : insLogo[0];
        } else {
          rolTitle = insLogo[2] == null ? insLogo[0] : insLogo[2];
        }
        userRolData.setRolTitle(rolTitle);
        userRolData.setRolLogoUrl(insLogo[1]);
        userRolData.setRolTitleCh(insLogo[0]);
        userRolData.setRolTitleEn(insLogo[2]);
        // 是否有多角色，否则需要切换角色功能
        InsRoleService insRoleService = (InsRoleService) ctx.getBean("insRoleService");
        userRolData.setRolMultiRole(insRoleService.hasMultiRole(rolInsId, SecurityUtils.getCurrentUserId()));
      }

      String webContext = (rolInsId.longValue() == ServiceConstants.NSFC_INS_2565.longValue()
          || rolInsId.longValue() == ServiceConstants.NSFC_INS_2566.longValue()
          || rolInsId.longValue() == ServiceConstants.NSFC_INS_2567.longValue()) ? "/scm" : "/scmwebrol";
      ((HttpServletRequest) request).getSession().setAttribute("logoutindex",
          java.net.URLEncoder.encode("http://" + domain + webContext + "/", "utf-8"));

    } catch (Exception e) {
      logger.error("ROL_LOGO_URL error", e);
    }
    return domain;
  }
}
