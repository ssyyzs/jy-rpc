package com.ssy.jy.tools;

import java.util.function.Supplier;

/**
 * created by idea.
 *
 * @author ssyyzs
 * @since 2023-12-07
 */
public class FunctionWrapper {

    /**
     * 返回单例实现.
     *
     * @param supplier 提供接口
     * @param <T>      提供的类型
     * @return Supplier<T>  单例实现的提供接口
     */
    public static <T> Supplier<T> singleWrapper(Supplier<T> supplier) {
        return new Supplier<>() {
            private volatile T ref;

            @Override
            public T get() {
                if (ref == null) {
                    synchronized (this) {
                        if (ref == null) {
                            ref = supplier.get();
                        }
                    }
                }
                return ref;
            }
        };
    }
}
