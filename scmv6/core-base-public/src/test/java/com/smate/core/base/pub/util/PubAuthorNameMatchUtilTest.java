package com.smate.core.base.pub.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;

class PubAuthorNameMatchUtilTest {

  @Test
  void testSpitName() {
    // 测试空值传入
    testSpitNameNull();
    // 测试正常情况返回
    testSpitNameTrue();
  }

  private void testSpitNameTrue() {
    List<String> names = null;
    String name = "wang xiao";
    names = PubAuthorNameMatchUtil.spitName(name);
    assertEquals(names.size(), 8);
  }

  private void testSpitNameNull() {
    List<String> names = null;
    // 测试空值传入 null
    String name = null;
    names = PubAuthorNameMatchUtil.spitName(name);
    assertEquals(CollectionUtils.isEmpty(names), true);
    // 测试空值传入 空字符串
    name = "";
    names = PubAuthorNameMatchUtil.spitName(name);
    assertEquals(CollectionUtils.isEmpty(names), true);
  }

  @Test
  void testCompareNameNotAllCh() {
    // 此方法外层已经过滤了空值数据，因此不需要判空操作
    // 验证正确逻辑
    testCompareNameNotAllChTrue();
  }

  private void testCompareNameNotAllChTrue() {
    boolean match = false;
    String name = "wang xiaoli";
    String compareName = "wang xinchen";
    match = PubAuthorNameMatchUtil.compareNameNotAllCh(name, compareName);
    assertEquals(match, false);

    name = "wang xl";
    compareName = "wang xinchen";
    match = PubAuthorNameMatchUtil.compareNameNotAllCh(name, compareName);
    assertEquals(match, false);

    name = "wang xc";
    compareName = "wang xinchen";
    match = PubAuthorNameMatchUtil.compareNameNotAllCh(name, compareName);
    assertEquals(match, true);
  }

  @Test
  void testCompareByFullName() {
    // 测试空值情况
    testCompareByFullNameNull();
    // 测试正常情况
    testCompareByFullNameTrue();
  }

  private void testCompareByFullNameTrue() {
    // 测试中文情况
    boolean match = false;
    String name = "张*学友";
    String compareName = "张 学-友*";
    match = PubAuthorNameMatchUtil.compareByFullName(name, compareName);
    assertEquals(match, true);

    // 测试中文匹配成功情况
    name = "张&nbsp;学-友";
    compareName = "张 学-友*";
    match = PubAuthorNameMatchUtil.compareByFullName(name, compareName);
    assertEquals(match, true);

    // 测试中文匹配不成功情况
    name = "张0学-友";
    compareName = "张 学-有*";
    match = PubAuthorNameMatchUtil.compareByFullName(name, compareName);
    assertEquals(match, false);

    // 测试英文情况
    name = "wu xiaoli";
    compareName = "wang xiaoli";
    match = PubAuthorNameMatchUtil.compareByFullName(name, compareName);
    assertEquals(match, false);

    // 测试英文情况
    name = "  wu  xiaoli1";
    compareName = "wu  xiaoli*";
    match = PubAuthorNameMatchUtil.compareByFullName(name, compareName);
    assertEquals(match, true);

    // 测试英文情况-此种情况暂时不支持
    name = "wuxiaoli1";
    compareName = "wu xiaoli*";
    match = PubAuthorNameMatchUtil.compareByFullName(name, compareName);
    assertEquals(match, false);
  }

  private void testCompareByFullNameNull() {
    boolean match = false;
    String name = "";
    String compareName = null;
    match = PubAuthorNameMatchUtil.compareByFullName(name, compareName);
    assertEquals(match, false);
  }

}
