package com.smate.center.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.Test;

/**
 * @author houchuanjie
 * @date 2018/04/13 20:22
 */
public class CommonTest {

    @Test
    public void buildUUID(){
        System.out.println(UUID.randomUUID());
    }

    @Test
    public void testClone() {
        HashMap<String, Integer> src = new HashMap<>();
        src.put("key1", 1);
        src.put("key2", 2);
        System.out.println("-------------------");
        print(src);
        Map<String, Integer> clone = new HashMap<>(src);
        clone.put("key1", 3);
        System.out.println("-------------------");
        print(src);
        System.out.println("-------------------");
        print(clone);
    }

    @Test
    public void test1() {
        HashMap<String, Integer> src = new HashMap<>();
        src.put("key1", 1);
        src.put("key2", 2);
        System.out.println("-------------------");
        print(src);
        Map<String, Integer> clone = new HashMap<>(src);
        ArrayList<Entry<String, Integer>> entries = new ArrayList<>(clone.entrySet());
        entries.get(1).setValue(3);
        System.out.println("-------------------");
        print(src);
        System.out.println("-------------------");
        print(clone);

    }

    private void print(Map<String, Integer> map) {
        for (Entry entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
