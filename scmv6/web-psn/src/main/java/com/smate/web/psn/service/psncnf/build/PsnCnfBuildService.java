package com.smate.web.psn.service.psncnf.build;

import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;


/**
 * 个人配置：封装
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCnfBuildService {

  PsnCnfBuild get(Long psnId) throws ServiceException, PsnCnfException;

}
