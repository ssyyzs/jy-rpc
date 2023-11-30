package com.ssy.jy.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-11-25
 */
public class JyLoggerFactory {

    /**
     * created by idea.
     *
     * @param clazz 获取日志接口的类信息
     * @return Logger 日志接口
     */
    public static Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
