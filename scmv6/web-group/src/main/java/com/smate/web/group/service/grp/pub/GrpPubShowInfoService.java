package com.smate.web.group.service.grp.pub;

import com.smate.web.group.action.grp.form.GrpPubInfoVO;
import com.smate.web.group.action.grp.form.GrpPubShowInfo;
import com.smate.web.group.model.grp.pub.PubSimple;

/**
 * 群组成果服务接口类
 * 
 * @author tsz
 *
 */
public interface GrpPubShowInfoService {

  /**
   * 构建显示数据
   * 
   * @param pub
   * @return
   * @throws Exception
   */
  public GrpPubShowInfo buildShowPubInfo(GrpPubInfoVO pub) throws Exception;

}
