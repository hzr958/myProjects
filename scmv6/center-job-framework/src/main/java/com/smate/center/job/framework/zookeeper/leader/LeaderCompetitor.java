package com.smate.center.job.framework.zookeeper.leader;

import com.smate.center.job.framework.service.JobServerNodeService;
import com.smate.center.job.framework.support.JobConfig;
import com.smate.center.job.framework.support.LeadershipObservable;
import com.smate.center.job.framework.zookeeper.config.ZKConfig;
import com.smate.center.job.framework.zookeeper.service.ZKClientService;
import com.smate.core.base.utils.number.NumberUtils;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

/**
 * 主服务器领导权竞争管理器
 *
 * @author houchuanjie
 * @date 2018/04/02 10:25
 */
@Component
@EnableScheduling
@Lazy(false)
@Order(2)
public class LeaderCompetitor implements InitializingBean, DisposableBean {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ZKClientService ZKClientService;

    @Autowired
    private JobServerNodeService jobServerNodeService;

    @Autowired
    private LeadershipObservable leadershipObservable;

    /**
     * 当前服务器节点ID
     */
    private String currentServerID = "";

    /**
     * 当前领导
     */
    private LeaderLatch leaderLatch;
    /**
     * 日志打印时间戳
     */
    private long logTimestamp = 0L;
    /**
     * 日志打印时间间隔，单位：秒
     */
    private int logInterval = 60;

    /**
     * 定时3秒轮询检查当前服务是否是leader
     *
     * @throws Exception
     */
    @Scheduled(fixedDelay = 3000)
    public void leadershipCompete() throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("{}'s method leadershipCompete is called.", getClass().getSimpleName());
        }
        if (!ZKClientService.isConnected()) {
            logger.warn(
                    "The ZKClient is not connected successfully yet, thus {} could not join the leadership election.",
                    getClass().getSimpleName());
            return;
        }
        /*
        考虑网络阻塞，ZK连接异常、数据丢失等情况，利用serverId来判断自己当前是否还是Leader
         */
        Collection<Participant> participants = leaderLatch.getParticipants();

        boolean isLeader = false;
        for (Participant participant : participants) {
            if (this.currentServerID.equals(participant.getId())&&participant.isLeader()) {
                isLeader = true;
                break;
            }
        }
        // 如果不存在，则重新加入选举
        if (!isLeader) {
            // 曾经是leader
            if (leadershipObservable.hasLeadership()) {
                logger.info("当前节点已失去领导权，将重新加入选举。");
                leaderLatch.close();
                leaderLatch = new LeaderLatch(ZKClientService.getClient(), ZKConfig.LEADER_PATH,
                        currentServerID);
                leaderLatch.start();
                logger.info("当前节点已重新加入选举！");
            }
        }
        /*
        查看当前leader是否是自己，注意，leaderLatch.hasLeadership()因为有zk数据丢失的不确定性，所以这里
        利用serverId对比确认是否主为自己
        */
        Participant leader = leaderLatch.getLeader();
        leadershipObservable.setLeader(jobServerNodeService.getServerNode(NumberUtils.toInt(leader.getId())));
        boolean hasLeadership = currentServerID.equals(leader.getId());
        // 当前是leader，且次数不到3次，进行命中递减
        if (hasLeadership && ZKConfig.leaderCountDownLatch.getCount() > 0L) {
            ZKConfig.leaderCountDownLatch.countDown();
            if (ZKConfig.slaveCountDownLatch.getCount() < 3L) {
                ZKConfig.slaveCountDownLatch = new CountDownLatch(3);
            }
        }
        // 当前不是leader且次数不到3次，进行命中递减
        if (!hasLeadership && ZKConfig.slaveCountDownLatch.getCount() > 0L) {
            ZKConfig.slaveCountDownLatch.countDown();
            if (ZKConfig.leaderCountDownLatch.getCount() < 3L) {
                ZKConfig.leaderCountDownLatch = new CountDownLatch(3);
            }
        }
        if (ZKConfig.leaderCountDownLatch.getCount() == 0L) {
            leadershipObservable.setLeadership(true);
        }
        if (ZKConfig.slaveCountDownLatch.getCount() == 0L) {
            leadershipObservable.setLeadership(false);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Initializing {}", getClass().getSimpleName());
        currentServerID = JobConfig.getCurrentServerNode().getId().toString();
        // 启动LeaderLatch竞争主领导权
        leaderLatch = new LeaderLatch(ZKClientService.getClient(), ZKConfig.LEADER_PATH, currentServerID);
        leaderLatch.start();
    }

    /**
     * 根据时间戳间隔来确定能否打印日志
     *
     * @return
     */
    private boolean isLogEnable() {
        Instant now = Instant.now();
        boolean enable = now.minusSeconds(logInterval).isAfter(Instant.ofEpochMilli(logTimestamp));
        if (enable) {
            logTimestamp = now.toEpochMilli();
        }
        return enable;
    }

    public String getCurrentServerID() {
        return currentServerID;
    }

    public void setCurrentServerID(String currentServerID) {
        this.currentServerID = currentServerID;
    }

    @Override
    public void destroy() throws Exception {
        logger.info("Closing LeaderLatch");
        this.leaderLatch.close();
    }
}
