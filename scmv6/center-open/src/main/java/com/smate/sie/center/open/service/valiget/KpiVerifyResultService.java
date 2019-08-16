package com.smate.sie.center.open.service.valiget;

import java.util.Map;

/**
 * 科研认证结果加工
 * 
 * @author ztg
 *
 */
public interface KpiVerifyResultService {

  Map doDataValidate(Map<String, Object> paramet);

  Map constituteContent(Map<String, Object> paramet);


}
