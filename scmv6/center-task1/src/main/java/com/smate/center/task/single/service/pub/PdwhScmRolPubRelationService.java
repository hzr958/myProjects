package com.smate.center.task.single.service.pub;

import java.util.List;

import com.smate.center.task.model.sns.pub.PubPdwhScmRol;

public interface PdwhScmRolPubRelationService {

  public List<PubPdwhScmRol> getPdwhToHandlePub(Integer size);

  public Integer handleRolPub(PubPdwhScmRol pubPdwhScmRol);

  public Integer handleScmPub(PubPdwhScmRol pubPdwhScmRol) throws Exception;

  public void updatePubPdwhScmRol(PubPdwhScmRol pubPdwhScmRol, Integer status);

  public List<Long> getScmPubIds();

  public void savePubPdwhScmRol(PubPdwhScmRol pubPdwhScmRol);

  public List<PubPdwhScmRol> getPdwhToHandlePub(Integer size, Long startPubId, Long endPubId);
}
