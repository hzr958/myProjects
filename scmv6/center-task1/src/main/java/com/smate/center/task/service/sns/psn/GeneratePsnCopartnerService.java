package com.smate.center.task.service.sns.psn;

import java.util.List;


public interface GeneratePsnCopartnerService {

  List<Long> gethandlePrjPsnId(Long lastPsnId, int batchSize);

  List<Long> gethandlePubPsnId(Long lastPsnId, int batchSize);

  List<Long> getPsnPubIdList(Long psnId);

  List<Long> getpdwhPubIds(List<Long> psnPubIdList);

  void savePsnPubCopartner(Long psnId, List<Long> pdwhPubIds);

  void savePsnPrjCopartner(Long psnId, List<Long> coPsnIdList, Long prjGroupId);

  List<Long> findPrjGroupIdsByPsnId(Long psnId);

  List<Long> getPrjCoBygrpId(Long prjGroupId, Long psnId);

  void upAppSettingConstant(String snsPsnPrjCopartnerStart, Long lastPubId);

  void deletePsnCopartner(Long psnId, int coType);



}
