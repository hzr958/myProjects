package com.smate.web.dyn.action.msg.mid;

import java.io.Serializable;

import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.dyn.form.msg.MsgShowForm;

/**
 * 操作-移动端消息页面MainAction
 * 
 * @author zzx
 *
 */
@Results({})
public class OptMidMsgMainAction extends WechatBaseAction
    implements ModelDriven<MsgShowForm>, Preparable, Serializable {
  private static final long serialVersionUID = 1L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MsgShowForm form;



  @Override
  public MsgShowForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new MsgShowForm();
    }
  }

}
