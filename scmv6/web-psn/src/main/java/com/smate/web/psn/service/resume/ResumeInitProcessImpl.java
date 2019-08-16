package com.smate.web.psn.service.resume;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.smate.core.base.psn.service.resume.ResumeInitProcess;



/**
 * 当用户未设置公开信息时，根据不同用户，初始化不同的公开信息.
 * 
 * @author liqinghua
 * 
 */
@Component("resumeInitProcess")
@Deprecated
public class ResumeInitProcessImpl implements Serializable, ResumeInitProcess {

  /**
   * 
   */
  private static final long serialVersionUID = 4460889167918180243L;


}
