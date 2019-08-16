package com.smate.center.batch.service.pdwh.prj;

public interface NsfcApplicationAutoAssignService {

    public void calculateSimularity(Long applicaitonId) throws Exception;

    public void spetralClustering() throws Exception;
}
