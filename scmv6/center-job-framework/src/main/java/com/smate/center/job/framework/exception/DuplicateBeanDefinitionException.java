package com.smate.center.job.framework.exception;

/**
 * 重复创建bean的异常
 * @author houchuanjie
 * @date 2018/04/11 10:07
 */
public class DuplicateBeanDefinitionException extends RuntimeException{
    public DuplicateBeanDefinitionException() {
        super();
    }

    public DuplicateBeanDefinitionException(String message) {
        super(message);
    }

    public DuplicateBeanDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateBeanDefinitionException(Throwable cause) {
        super(cause);
    }

    protected DuplicateBeanDefinitionException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
