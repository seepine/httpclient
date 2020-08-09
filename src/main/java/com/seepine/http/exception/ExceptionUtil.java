package com.seepine.http.exception;

import com.seepine.http.util.StrUtil;

/**
 * @author Seepine
 */
public class ExceptionUtil {
    /**
     * 获得完整消息，包括异常名，消息格式为：{SimpleClassName}: {ThrowableMessage}
     *
     * @param e 异常
     * @return 完整消息
     */
    public static String getMessage(Throwable e) {
        if (null == e) {
            return StrUtil.NULL;
        }
        return String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage());
    }
}
