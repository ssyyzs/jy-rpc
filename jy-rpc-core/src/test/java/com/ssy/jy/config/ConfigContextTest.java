package com.ssy.jy.config;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ConfigContextTest {

    @Test
    void parse() throws FileNotFoundException {
        ConfigContext parse = ConfigContext.parse("D:\\IdeaProjects\\jy-rpc\\jy-rpc-core\\src\\main\\resources\\jy.yaml");
    }
}