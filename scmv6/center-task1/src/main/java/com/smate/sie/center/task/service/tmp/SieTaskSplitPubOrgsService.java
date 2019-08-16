package com.smate.sie.center.task.service.tmp;

import java.util.List;

import com.smate.sie.center.task.model.tmp.SieTaskSplitPubOrgs;


/**
 * 拆分task_split_pub_orgs 表pub_json 服务层
 * 
 * @author ztg
 *
 */
public interface SieTaskSplitPubOrgsService {

  Long countNeedHandleKeyId();

  List<SieTaskSplitPubOrgs> loadNeedHandleKeyId(int batchSize);

  void doSplit(SieTaskSplitPubOrgs splitPubOrgs);

}
