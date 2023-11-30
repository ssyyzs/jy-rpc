package com.ssy.jy.biz;

import java.util.List;

/**
 * rpc测试接口实现.
 *
 * @author ssyyzs
 * @since 2023-11-23
 **/
public class RpcTestImpl implements RpcTest {
    @Override
    public String testStr(String str, List<String> list) {
        String res = str;
        for (String s : list) {
            res += s;
        }
        return res;
    }

    @Override
    public List<String> testList(String str, List<String> list) {
        list.add(0, str);
        return list;
    }

    @Override
    public String testError(String str1, String str2) {
        int a = 1 / 0;
        return str1 + str2;
    }
}
