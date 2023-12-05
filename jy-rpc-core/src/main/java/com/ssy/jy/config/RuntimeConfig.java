package com.ssy.jy.config;

import lombok.Data;

import java.util.Map;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-12-04
 */
@Data
public class RuntimeConfig {
    private String hostName = "localhost";
    private int port;
    private StubConfig stub;
}
