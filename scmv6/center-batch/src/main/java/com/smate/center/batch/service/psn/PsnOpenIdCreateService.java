package com.smate.center.batch.service.psn;

import java.util.List;

import com.smate.center.batch.model.sns.pub.OpenUserCreateByIns;
import com.smate.core.base.utils.model.cas.security.SysRolUser;

public interface PsnOpenIdCreateService {
  public List<OpenUserCreateByIns> getInsList(Integer status);

  public Long generateOpenId(String token, Long psnId, int createType) throws Exception;

  public List<SysRolUser> getPsnIdByInsId(Long insId);
}
