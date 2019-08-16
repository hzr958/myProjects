package com.smate.center.oauth.model.consts.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 移动端一些常量
 * 
 * @author wsn
 * @date Jan 4, 2019
 */
public class MobileConsts {

  public static final String APP_TYPE_ANDROID = "android";// android类型app

  public static final String APP_TYPE_IOS = "IOS";// ios类型app

  public static final List<String> APP_TYPES = new ArrayList<String>(Arrays.asList(APP_TYPE_ANDROID, APP_TYPE_IOS));// app类型list

  public static final String SMATE_IOS_APP_STORE_URL = "https://itunes.apple.com/cn/app/id1369636776";
}
