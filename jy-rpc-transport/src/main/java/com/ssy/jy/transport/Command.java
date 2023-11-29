package com.ssy.jy.transport;

/**
 * 命令类型.
 *
 * @author y30010171
 * @since 2022-08-24
 **/
public interface Command {
    byte REQUEST_COMMAND = 1;
    byte REQUEST_RESPONSE_COMMAND = 2;
}
