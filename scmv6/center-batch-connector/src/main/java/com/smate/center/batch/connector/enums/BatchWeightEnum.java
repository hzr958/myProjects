package com.smate.center.batch.connector.enums;

/**
 * 权重常量
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @param job
 */
public enum BatchWeightEnum {

  A("A"), B("B"), C("C"), D("D");

  private String value;

  private BatchWeightEnum(String value) {

    this.value = value;

  }

  @Override
  public String toString() {

    return this.value;

  }
}

