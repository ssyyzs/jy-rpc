package com.ssy.jy.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-12-07
 */
public class SpiLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpiLoader.class);
    private static final ConcurrentHashMap<Class<?>, List<?>> LOAD_SERVICES = new ConcurrentHashMap<>();

    public static <T> Optional<T> load(Class<T> clazz) {
        return load(clazz, SpiMeta.DEFAULT);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> load(Class<T> clazz, String alias) {
        List<?> list = LOAD_SERVICES.computeIfAbsent(clazz, aClass -> new ServiceSupplier<>(aClass).get());
        if (list == null || list.isEmpty()) {
            return Optional.empty();
        }
        return (Optional<T>) list.stream().filter(o -> {
            SpiMeta annotation = o.getClass().getAnnotation(SpiMeta.class);
            return annotation != null && alias.equals(annotation.alias());
        }).findFirst();
    }

    static class ServiceSupplier<T> implements Supplier<List<T>> {
        Class<T> clazz;
        public ServiceSupplier(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public List<T> get() {
            Iterator<T> iterator = ServiceLoader.load(clazz).iterator();
            List<T> res = new ArrayList<>();
            while (iterator.hasNext()) {
                T service = iterator.next();
                LOGGER.debug("load spi service[{} = {}]", clazz.getName(), service.getClass().getName());
                res.add(service);
            }
            return res;
        }
    }
}
