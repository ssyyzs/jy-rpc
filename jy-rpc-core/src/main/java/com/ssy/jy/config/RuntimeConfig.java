package com.ssy.jy.config;

import lombok.Data;

/**
 * runtime 配置类.
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
