package com.smate.center.batch.service.rol.pub;

import java.util.Locale;

/**
 * 单位特殊智能提示service.
 * 
 * @author liqinghua
 * 
 */
public interface AutoCompleteRolService {

  String getShowName(String zhName, String enName, Locale locale);

}
