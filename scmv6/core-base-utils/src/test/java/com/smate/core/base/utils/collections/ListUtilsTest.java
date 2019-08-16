package com.smate.core.base.utils.collections;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author houchuanjie
 * @date 2018/05/11 10:20
 */
public class ListUtilsTest {

    @Test
    public void split() {
        List<String> list = null;

        assertNull("list != null", ListUtils.split(list, 2));
        list = Arrays.asList("apple", "pair", "orange");
        assertTrue("分割后List数组大小不为2！", ListUtils.split(list, 2).length == 2);
        list = new ArrayList<>();
        list.add("apple");
        list.add("pair");
        list.add("orange");
        list.add("banana");
        List<String>[] splitList = ListUtils.split(list, 2);
        assertTrue("分割后List数组大小不为2！", splitList.length == 2);
        splitList[0].add("peach");
        assertTrue("分割后的list数组发生结构性改变影响了原始list！", list.size() == 4);
        assertTrue("分割后的list数组发生结构性改变，但元素未增加！", splitList[0].size() == 3);
        list.set(0, "peach");
        assertTrue("原始list元素发生非结构性改变，影响了分割后的List数组中的元素！", splitList[0].get(0).equals("apple"));
    }
}