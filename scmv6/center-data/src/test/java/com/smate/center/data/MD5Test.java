package com.smate.center.data;

import java.util.ArrayList;

import org.apache.hadoop.hbase.util.MD5Hash;
import org.junit.Test;

public class MD5Test {
    @Test
    public void testEncode() {
        String str = "12345667";
        String md5AsHex = MD5Hash.getMD5AsHex(str.getBytes());
        System.out.println(md5AsHex);
        System.out.println(md5AsHex.substring(8, 24));
    }

    @Test
    public void test3() {
        // 每个分区的平均数量
        long average = 20;
        // 分区数量
        long partCount = 5;
        /*
         * 获取每个分区的开始和结束位置序号，先获取前n-1个分区的，再获取最后一个分区的，
         * 通常来讲，最后一个分区被分到的数量小于等于平均分区数量
         */
        ArrayList<Long> seqList = new ArrayList<>();
        for (long i = 0; i < partCount - 1; i++) {
            seqList.add(average * i + 1);
            seqList.add(average * (i + 1));
        }
        seqList.add(average * (partCount - 1) + 1);
        seqList.add(96L);
        for (int i = 0; i < seqList.size() - 1; i += 2) {
            System.out.println(seqList.get(i) + "\t" + seqList.get(i + 1));
        }
    }

}
