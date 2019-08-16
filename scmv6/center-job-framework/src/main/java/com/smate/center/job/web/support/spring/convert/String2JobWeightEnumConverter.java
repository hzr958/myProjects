package com.smate.center.job.web.support.spring.convert;

import com.smate.center.job.common.enums.JobWeightEnum;

/**
 * 用于将字符串("A"、"B"、"C"、"D")转{@link JobWeightEnum}类型
 *
 * @author Created by hcj
 * @date 2018/06/30 15:51
 */
public class String2JobWeightEnumConverter extends
    AbstractCustomEnumPropertyConverter<String, JobWeightEnum> {

}
