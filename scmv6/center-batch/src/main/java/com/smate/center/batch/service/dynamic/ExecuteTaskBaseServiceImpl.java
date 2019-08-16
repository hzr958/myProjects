package com.smate.center.batch.service.dynamic;

import java.util.Date;

import com.smate.center.batch.model.dynamic.Inspg;
import com.smate.center.batch.model.dynamic.InspgDynRelationInspg;
import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;


public class ExecuteTaskBaseServiceImpl {
  public InspgDynRelationInspg compareRelation(InspgDynamicRefresh refresh, Inspg inspg) {
    InspgDynRelationInspg relation = new InspgDynRelationInspg(refresh.getDynId(), inspg.getId(), refresh.getPsnId(),
        inspg.getId(), refresh.getDynType(), refresh.getCreateTime().getTime(), new Date(), 1);
    relation.setInspgAvatar(inspg.getLogoUrl());
    relation.setInspgName(inspg.getName());
    relation.setInspgZhName(inspg.getZhName());
    relation.setInspgEnName(inspg.getEnName());
    relation.setInspgHomePage(inspg.getInspgUrl());
    relation.setContent(refresh.getContent());
    relation.setCreatePsnId(refresh.getPsnId());
    return relation;
  }
}
