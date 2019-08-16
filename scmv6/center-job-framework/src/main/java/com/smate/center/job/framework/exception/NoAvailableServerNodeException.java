package com.smate.center.job.framework.exception;

/**
 * 没有可用服务器节点异常，运行时异常
 * @author houchuanjie
 * @date 2018/04/13 11:06
 */
public class NoAvailableServerNodeException extends RuntimeException {
    public NoAvailableServerNodeException() {
        super();
    }

    public NoAvailableServerNodeException(String message) {
        super(message);
    }

    public NoAvailableServerNodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAvailableServerNodeException(Throwable cause) {
        super(cause);
    }

    protected NoAvailableServerNodeException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
