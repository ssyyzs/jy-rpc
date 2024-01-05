package com.ssy.jy.spi;

import com.ssy.jy.stub.JdkProxyStubFactory;
import com.ssy.jy.stub.StubFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpiLoaderTest {

    @Test
    void load() {
        StubFactory load = SpiLoader.load(StubFactory.class).get();
        assertNotNull(load);
        assertTrue(load instanceof JdkProxyStubFactory);
    }
}