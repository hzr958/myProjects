package com.smate.core.web.sns.menu.tag;

import java.io.IOException;

import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 科研之友 新系统获取 成果，群组的 节点. 7100为 成果模块 7200为 群组模块
 * 
 * @author ajb
 */
public class SnsNodeIdTag extends BodyTagSupport {


  private static final long serialVersionUID = 4923619449908172794L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());


  @Override
  public int doStartTag() {
    if (StringUtils.isNotBlank(System.getenv("NODE_URL_ID"))) {
      String nodeUrlIdTemp = System.getenv("NODE_URL_ID");
      try {
        pageContext.getOut().write(nodeUrlIdTemp);
      } catch (Exception e) {
        logger.error("科研之新系统获取节点失败 ，获取节点参数NODE_URL_ID时出错 ！");
      }
    } else {
      logger.error("科研之新系统获取节点失败 ， 节点NODE_URL_ID为空！");
    }
    return EVAL_BODY_INCLUDE;
  }

}
