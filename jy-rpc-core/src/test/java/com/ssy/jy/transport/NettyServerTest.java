package com.ssy.jy.transport;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class NettyServerTest {

    @Test
    void open() throws IOException {
        Enumeration<URL> resources = this.getClass().getClassLoader().getResources("com/ssy/jy/");
        while (resources.hasMoreElements()) {
            System.out.println(resources.nextElement().getFile());
        }
    }
}