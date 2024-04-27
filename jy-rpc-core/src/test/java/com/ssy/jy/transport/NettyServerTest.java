package com.ssy.jy.transport;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

class NettyServerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerTest.class);

    @Test
    void open() throws IOException {
        Enumeration<URL> resources = this.getClass().getClassLoader().getResources("com/ssy/jy/");
        while (resources.hasMoreElements()) {
            LOGGER.info(resources.nextElement().getFile());
        }
    }
}