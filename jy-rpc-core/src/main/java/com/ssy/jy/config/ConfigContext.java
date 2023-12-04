package com.ssy.jy.config;

import lombok.Data;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.inspector.TagInspector;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.Tag;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Properties;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-12-04
 */
@Data
public class ConfigContext {
    private StubConfig stub;
    private RuntimeConfig client;
    private RuntimeConfig server;

    public static ConfigContext parse(String configFileName) throws FileNotFoundException {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setTagInspector((tag) ->  (ConfigContext.class.getName()).equals(tag.getClassName()));
        ConfigContext configContext = new Yaml(new Constructor(ConfigContext.class, loaderOptions)).load(new FileInputStream(configFileName));
        System.out.println(configContext);
        return configContext;
    }
}
