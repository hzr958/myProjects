package com.smate.sie.core.base.utils.string;

import java.util.Map;

public class JpinyinUtilTest {

    public static void main(String[] args) {
        testJPinyinUtil();
    }

    private static void testJPinyinUtil() {
        // String str1 = "覃艾格";
        // String str2 = "筽知道";
        // String str3 = "东门白虎";
        String str1 = "壤驷ing";
        String str2 = "Jia知道";
        String str3 = "ou少鹏";

        Map<String, String> map1 = JPinyinUtil.parseFullNamePinYin(str1);
        Map<String, String> map2 = JPinyinUtil.parseFullNamePinYin(str2);
        Map<String, String> map3 = JPinyinUtil.parseFullNamePinYin(str3);

        System.out.println(str1 + "--解析后:--" + map1.get("lastName") + " " + map1.get("firstName"));
        System.out.println(str2 + "--解析后:--" + map2.get("lastName") + " " + map2.get("firstName"));
        System.out.println(str3 + "--解析后:--" + map3.get("lastName") + " " + map3.get("firstName"));
    }

}
