package com.smate.center.job;

import java.util.List;

import org.junit.Test;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.StringUtils;

/**
 *
 * @author houchuanjie
 * @date 2018年3月2日 下午5:00:16
 */
public class TestStringUtils {

    @Test
    public void test() {
        System.out.println(NumberUtils.parseLong(null, null));
        System.out.println(NumberUtils.parseLong("   ", null));
        System.out.println(NumberUtils.parseLong("123", null));
        System.out.println(NumberUtils.parseLong(" 123 ", null));
        System.out.println(NumberUtils.parseLong("abc", null));
        System.out.println(NumberUtils.parseLong("abc123", null));
    }

    @Test
    public void testSplit2List() {
        List<Long> idList = StringUtils.split2List("1,2,  3 ,  , 4,5", ",", s -> NumberUtils.parseLong(s), true);
        idList.forEach(id -> System.out.println(id));
    }

    @Test
    public void testXor(){
        boolean flag = true;
        System.out.println(flag ^ false);
        System.out.println(flag ^ true);
    }

}
