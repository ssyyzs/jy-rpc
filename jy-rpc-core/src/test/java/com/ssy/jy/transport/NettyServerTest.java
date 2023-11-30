package com.ssy.jy.transport;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class NettyServerTest {

    @Test
    void open() throws IOException {
        Enumeration<URL> resources = this.getClass().getClassLoader().getResources("com/ssy/jy/");
        while (resources.hasMoreElements()) {
            System.out.println(resources.nextElement().getFile());
        }
    }
H
    public int[] productExceptSelf(int[] nums) {
        int[] pre = new int[nums.length + 1];
        int[] suf = new int[nums.length + 1];
        pre[0] = 1;
        suf[nums.length] = 1;
        for (int i = 0; i < nums.length; i++) {
            pre[i + 1] = pre[i] * nums[i];
            suf[nums.length - 1 - i] = suf[nums.length - i] * nums[nums.length - 1 - i];
        }
        int[] ans = new int[nums.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = suf[i + 1] * pre[i];
        }
        return ans;
    }
}