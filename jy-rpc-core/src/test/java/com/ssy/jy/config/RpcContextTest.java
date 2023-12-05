package com.ssy.jy.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RpcContextTest {

    @Test
    void testConstruct() {
        System.out.println(new RpcContext().getConfigContext());
    }
}