package com.smate.web.psn.service.site;

import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.web.psn.action.site.InsPortalShow;
import com.smate.web.psn.action.site.UserRolDataForm;
import com.smate.web.psn.dao.scnf.ScnfWebDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 切换站点的服务类
 * 
 * @author Administrator
 *
 */
@Service("switchSiteService")
@Transactional(rollbackOn = Exception.class)
public class SwitchSiteServiceImpl implements SwitchSiteService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InsPortalDao InsPortalDao;
  @Autowired
  private ScnfWebDao scnfWebDao;
  @Value("${domainscm}")
  private String domainsns;
  @Value("${domainrol}")
  private String domainrol;

  /**
   * 查找用户的机构站点
   * 
   * @throws Exception
   */
  @Override
  public void findUserRolSite(UserRolDataForm form) throws Exception {
    List<InsPortalShow> showList = new ArrayList<InsPortalShow>();
    form.setPortal(showList);
    addSnsSite(showList);
    // 隐藏下面功能 2019-07-04
    /*List<InsPortal> insPortalList = InsPortalDao.findUserRolUrl(form.getPsnId());
    if (insPortalList != null && insPortalList.size() > 0) {
      for (InsPortal insPortal : insPortalList) {
        // <%--屏蔽跳转到基金委成果在线的链接_MJG_SCM-5175 --%>
        if (insPortal.getDomain().contains("rol.nsfc.gov.cn")) {
          continue;
        }
        InsPortalShow show = new InsPortalShow();
        buildInsProtalShowInfo(show, insPortal);
        showList.add(show);

      }
    }*/

  }

  /**
   * /新增一个科研之友站点。
   * 
   * @param showList
   */
  private void addSnsSite(List<InsPortalShow> showList) {

    InsPortalShow snsShow = new InsPortalShow();
    snsShow.setDomain(this.domainsns);
    snsShow.setZhTitle("科研之友");
    snsShow.setEnTitle("ScholarMate");
    snsShow.setInsId(0L); // 默认为0
    snsShow.setLogo("/resmod/images/insimg/scm_wx.jpg");
    snsShow.setWebCtx("scmwebsns");
    snsShow.setVersion(0);
    showList.add(snsShow);
  }

  /**
   * 构建显示的信息
   * 
   * @param show
   * @param insPortal
   */
  private void buildInsProtalShowInfo(InsPortalShow show, InsPortal insPortal) {
    show.setDomain("http://" + insPortal.getDomain());
    show.setInsId(insPortal.getInsId());
    if (insPortal.getInsId() != 0L) {
      show.setLogo(domainrol + insPortal.getLogo());
    }
    // show.setVersion(insPortal.getVersion());
    if (StringUtils.isNotBlank(insPortal.getZhTitle())) {
      show.setZhTitle(insPortal.getZhTitle());
    } else {
      show.setZhTitle(insPortal.getEnTitle());
    }
    if (StringUtils.isNotBlank(insPortal.getEnTitle())) {
      show.setEnTitle(insPortal.getEnTitle());
    } else {
      show.setEnTitle(insPortal.getZhTitle());
    }

    // Long rolNodeId = Integer.valueOf(insPortal.getRolNodeId()).longValue();
    // ScnfWeb scnfWeb = scnfWebDao.get(rolNodeId);
    // if (scnfWeb != null) {
    // show.setWebCtx(scnfWeb.getContextPath());
    // } else {
    // }
    show.setWebCtx("scmwebsns");
  }

}
