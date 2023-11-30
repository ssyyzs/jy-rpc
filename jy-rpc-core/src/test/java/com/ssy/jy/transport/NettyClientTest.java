package com.ssy.jy.transport;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class NettyClientTest {

    @Test
    void open() {
        System.out.println(beautifulSubstrings("uuouuaifnboeiulttio", 3));
    }

    public int beautifulSubstrings(String s, int k) { // 20
        int res = 0;
        List<Integer> vowelList = new ArrayList<>();
        vowelList.add(-1);
        for (int i = 0; i < s.length(); i++) {
            if (isVowel(s.charAt(i))) {
                vowelList.add(i);
            }
        }
        vowelList.add(s.length());
        int maxLen = Math.min(s.length() - vowelList.size() + 2, vowelList.size());
        int limitK = maxLen * maxLen;
        for (int lenM = k; lenM <= limitK; lenM += k) {
            int len = (int) Math.sqrt(lenM);
            if (len * len != lenM) {
                continue;
            }
            for (int j = 1; j < vowelList.size() - len; j++) {
                int extraLen = len;
                for (int i = 1; i < len; i++) {
                    extraLen -= (vowelList.get(j + i)  - vowelList.get(j + i - 1)  - 1);
                }
                if (extraLen < 0) {
                    continue;
                }
                int leftLen = vowelList.get(j) - vowelList.get(j - 1) - 1;
                int rightLen = vowelList.get(j + len) - vowelList.get(j + len - 1) - 1;
                res += comb(leftLen, rightLen, extraLen);
            }
        }
        return res;
    }

    private int comb(int left, int right, int num) {
        if (num == 0) {
            return 1;
        }
        if (num >= left + right) {
            return num == left + right ? 1 : 0;
        }
        if (left == 0 || right == 0) {
            return 1;
        }
        return Math.min(left, right) + 1;
    }

    private boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }

    public int[] lexicographicallySmallestArray(int[] nums, int limit) { // 5
        return null;
    }
}