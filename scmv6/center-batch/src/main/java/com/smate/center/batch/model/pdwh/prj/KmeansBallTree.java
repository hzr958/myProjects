package com.smate.center.batch.model.pdwh.prj;

import com.smate.center.batch.service.pdwh.prj.NsfcApplicationClusteringServiceImpl;

public class KmeansBallTree {
    public static KmeansPointCollection getOneInstance(KmeansPointCollection cur) {
        if (cur == null) {
            cur = new KmeansPointCollection();
            for (int i = 0; i < NsfcApplicationClusteringServiceImpl.INSTANCES.size(); ++i) {
                cur.addInstance(i);
            }
            cur.endAdding();
        }
        KmeansPointCollection[] ch = cur.split();
        for (KmeansPointCollection hp : ch) {
            if (hp.size() <= NsfcApplicationClusteringServiceImpl.MAX_INSTANCE_NUM_NOT_SPLIT) {
                continue;
            }
            getOneInstance(hp);
        }
        return cur;
    }
}
