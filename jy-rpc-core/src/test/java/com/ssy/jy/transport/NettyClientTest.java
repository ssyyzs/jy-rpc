package com.ssy.jy.transport;

import com.ssy.jy.runtime.RpcClientRuntime;
import com.ssy.jy.runtime.RpcServerRuntime;
import com.ssy.jy.stub.StubFactory;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.*;

class NettyClientTest {

    @Test
    void open() {
    }

    public int firstCompleteIndex(int[] arr, int[][] mat) {
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            indexMap.put(arr[i], i);
        }
        int[] row = new int[mat.length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                row[i] = Math.max(row[i], indexMap.get(mat[i][j]));
            }
        }
        int[] column = new int[mat[0].length];
        for (int i = 0; i < column.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                column[i] = Math.max(column[i], indexMap.get(mat[j][i]));
            }
        }
        int[][] map = new int[arr.length + 1][2];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                map[mat[i][j]] = new int[]{i, j};
            }
        }
        for (int i = 0; i < arr.length; i++) {
            if (Math.min(row[map[arr[i]][0]], row[map[arr[i]][1]]) <= i) {
                return i;
            }
        }
        return -1;
    }
}