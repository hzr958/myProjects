package com.smate.web.dyn.action.news.mobile;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.dyn.form.news.NewsForm;
import com.smate.web.dyn.service.news.NewsOptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 新闻操作Action
 * 
 * @author YHX
 *
 */
public class MobileNewsOptAction extends ActionSupport implements Preparable, ModelDriven<NewsForm> {

  private static final long serialVersionUID = -9090557623490845340L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private NewsForm form;
  @Autowired
  private NewsOptService newsOptService;


  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new NewsForm();
    }
    form.setPsnId(SecurityUtils.getCurrentUserId());
  }

  @Override
  public NewsForm getModel() {
    return form;
  }

}
