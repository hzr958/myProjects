package com.smate.center.job.framework.zookeeper.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.shared.SharedCountListener;
import org.apache.curator.framework.recipes.shared.SharedCountReader;
import org.apache.curator.framework.state.ConnectionState;

/**
 * 节点id共享计数器的监听器
 * @author houchuanjie
 * @date 2018/04/02 11:59
 */
public class NodeIDSharedCountListener implements SharedCountListener {
    @Override
    public void countHasChanged(SharedCountReader sharedCount, int newCount) throws Exception {

    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        switch (newState){
            case LOST: break;
            default: break;
        }
    }
}
