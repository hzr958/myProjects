package com.smate.center.batch.service.pub;

import com.smate.center.batch.model.sns.pub.PublicationForm;

/**
 * 第二阶段成果处理类
 * 
 * @author lxz
 *
 */
public interface PublicationBatchService {

  public void saveSuccessSync(String from, PublicationForm model);
}
