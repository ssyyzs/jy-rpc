package com.ssy.jy.config;

import lombok.Data;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

/**
 * 配置文件上下文.
 *
 * @author ssyyzs
 * @since 2023-12-04
 */
@Data
public class ConfigContext {
    private RuntimeConfig client;
    private RuntimeConfig server;

    public static ConfigContext parse(InputStream inputStream) {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setTagInspector((tag) ->  (ConfigContext.class.getName()).equals(tag.getClassName()));
        return new Yaml(new Constructor(ConfigContext.class, loaderOptions)).load(inputStream);
    }
}
