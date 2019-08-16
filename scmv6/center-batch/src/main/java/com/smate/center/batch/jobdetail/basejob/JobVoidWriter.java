package com.smate.center.batch.jobdetail.basejob;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 只是用来连接decider，无实际意义
 * 
 * @author hzr
 * @version 6.0.1
 */
public class JobVoidWriter implements ItemWriter<String> {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void write(List<? extends String> items) throws BatchTaskException, JSONException {

    logger.debug("batchtask，准备进入decider");
  }
}
