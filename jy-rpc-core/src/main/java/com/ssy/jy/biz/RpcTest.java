package com.ssy.jy.biz;

import java.util.List;

/**
 * rpc测试接口.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public interface RpcTest {

    String testStr(String str, List<String> list);

    List<String> testList(String str, List<String> list);

    String testError(String str1, String str2);
}
