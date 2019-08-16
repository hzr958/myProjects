package com.smate.sie.core.base.utils.model.statistics;

public class ImpactConsts {

  /* Type */
  public static final Integer SIE_PRJ = 1;
  public static final Integer SIE_PUB = 2;
  public static final Integer SIE_PATENT = 3;

  /* CAL Type */
  public static final Integer IMPACT_CAL_COUNTRY = 1;
  public static final Integer IMPACT_CAL_PROV = 2;
  public static final Integer IMPACT_CAL_CITY = 3;
  public static final Integer IMPACT_CAL_PSNINSNAME = 4;
  public static final Integer IMPACT_CAL_PSNSUB = 5;
  public static final Integer IMPACT_CAL_PSNPOS = 6;
  public static final Integer IMPACT_CAL_DATE = 7;

  /* Index CAL Type */
  public static final Integer IMPACT_INDEX_CAL_IP = 1;
  public static final Integer IMPACT_INDEX_CAL_DATE = 2;


  public static final String PUB = "2,4,6";
  public static final String PAT = "3,5,7";
  public static final String PRJ = "1";
  /* status */
  public static final Integer STATUS_SUC = 1;
  public static final Integer STATUS_WAIT = 0;
  public static final Integer STATUS_FAIL = 99;

  public static final String FIELD_YEAR = "fieldyear";
  public static final String FIELD_MONTH = "fieldmonth";
  public static final String FIELD_DAY = "fieldday";

  public static final String FIELD_COUNTRY = "country";
  public static final String FIELD_PROV = "prov";
  public static final String FIELD_CITY = "city";
  public static final String FIELD_PSNPOS = "psnPos";
  public static final String FIELD_PSNSUB = "psnSub";
  public static final String FIELD_PSNINSNAME = "psnInsName";
  public static final String FIELD_IP = "ip";

  public static final String[] CAL_ITEM_LIST = {"country", "prov", "city", "psnPos", "psnSub", "psnInsName"};
  public static final Integer[] CAL_TYPE_LIST = {1, 2, 3, 4, 5, 6};
  public static final Integer[] DATA_TYPE_LIST = {1, 2, 3};
  public static final Integer[] INDEX_CAL_TYPE_LIST = {1};

  public static final Integer ST_IP_BATCH_ONE = 1;
  public static final Integer ST_IP_BATCH_TWO = 2;

  public static final String BH_VIEW = "SieBhRead";
  public static final String BH_AWARD = "SieBhAward";
  public static final String BH_SHARE = "SieBhShare";
  public static final String BH_DOWNLOAD = "SieBhDownload";
}
