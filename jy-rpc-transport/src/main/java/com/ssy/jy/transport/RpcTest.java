package com.ssy.jy.transport;

import java.util.List;

/**
 * @author ssyyzs
 * @since 2023-11-23
 **/
public interface RpcTest {
    String testStr(String str, List<String> list);
    List<String> testList(String str, List<String> list);
}
