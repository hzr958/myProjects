package com.smate.web.v8pub.service.sns;

import java.io.Serializable;

import com.smate.core.base.utils.model.Page;
import com.smate.web.v8pub.po.seo.PubIndexThirdLevel;

/**
 * 提供seo数据的分析
 * 
 * @author tsz
 * 
 */
public interface SystemSeoSearch extends Serializable {

  public void putFilterAndbuildPage(String code, Integer label);

  public Page<PubIndexThirdLevel> getPubByLabel2(String key1, Integer key2, Integer integer,
      Page<PubIndexThirdLevel> pages);

}
