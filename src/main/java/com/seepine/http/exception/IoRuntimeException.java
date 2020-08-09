package com.seepine.http.exception;

import com.seepine.http.util.StrUtil;

/**
 * @author Seepine
 */
public class IoRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public IoRuntimeException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public IoRuntimeException(String message) {
        super(message);
    }


    public IoRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 导致这个异常的异常是否是指定类型的异常
     *
     * @param clazz 异常类
     * @return 是否为指定类型异常
     */
    public boolean causeInstanceOf(Class<? extends Throwable> clazz) {
        final Throwable cause = this.getCause();
        return null != clazz && clazz.isInstance(cause);
    }
}
