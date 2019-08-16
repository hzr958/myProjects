package com.smate.center.open.service.register;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.open.model.register.PersonRegister;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 人员注册后台任务服务实现
 *
 * @author wsn
 *
 */
@Service("registerPersonTaskService")
@Transactional(rollbackFor = Exception.class)
public class RegisterPersonTaskServiceImpl implements RegisterPersonTaskService {


  @Autowired
  private BatchJobsService batchJobsService;
  // @Resource(name="batchJobsNormalFactory")
  // private BatchJobsFactory batchJobsNormalFactory;


  @Override
  public void saveTaskJob(PersonRegister personRegister) {
    BatchJobs batchJobs = new BatchJobs();
    Map<String, String> map = new HashMap<String, String>();
    map.put("msg_id", JacksonUtils.jsonObjectSerializer(personRegister));
    batchJobs.setJobContext(JacksonUtils.jsonMapSerializer(map));
    batchJobs.setStatus(0);
    batchJobs.setWeight(BatchWeightEnum.A.toString());
    batchJobs.setStrategy("cetvceq6");
    boolean result = batchJobsService.saveJob(batchJobs);
  }

}
