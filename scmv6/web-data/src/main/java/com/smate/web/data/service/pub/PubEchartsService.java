package com.smate.web.data.service.pub;

import com.smate.web.data.form.pub.PubEchartsForm;

/**
 * 成果图表展示服务
 * 
 * @author lhd
 *
 */
public interface PubEchartsService {

  /**
   * 构建成果图表展示信息
   * 
   * @param form
   * @throws Exception
   */
  public void buildChartData(PubEchartsForm form) throws Exception;

  public void buildRcmdChartData(PubEchartsForm form) throws Exception;

}
