package com.smate.web.v8pub.enums;

/**
 * dbId与优先级的关系 优先级：9 to 1,逐渐减小；当为1时不参与优先级比较； 格式说明：数据库名（scm对应编号，优先级）,CROSSREF 优先级最低
 * 
 */
public enum DbIdPriorityEnum {
  SSCI(17, 9), SCI(16, 9), ISTP(15, 9), EI(14, 8), SCOPUS(8, 7), CNKI(4, 6), WANGFANG(10, 5), CNKIPAT(21, 5), CNIPR(11,
      4), CROSSREF(36, 0);

  private int dbId;
  private int priority;

  private DbIdPriorityEnum(int dbId, int priority) {
    this.dbId = dbId;
    this.priority = priority;
  }

  public int getDbId() {
    return dbId;
  }

  public void setDbId(int dbId) {
    this.dbId = dbId;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public static int getPriority(int dbId) {
    for (DbIdPriorityEnum db : DbIdPriorityEnum.values()) {
      if (db.getDbId() == dbId) {
        return db.getPriority();
      }
    }
    return 1;
  }

}
