package com.smate.center.job.framework.zookeeper.exception;

/**
 * ZooKeeper服务异常类
 *
 * @author houchuanjie
 * @date 2018/04/02 11:09
 */
public class ZooKeeperServiceException extends RuntimeException {
    public ZooKeeperServiceException() {
    }

    public ZooKeeperServiceException(String message) {
        super(message);
    }

    public ZooKeeperServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZooKeeperServiceException(Throwable cause) {
        super(cause);
    }

    public ZooKeeperServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
