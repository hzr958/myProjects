package com.smate.sie.center.open.service.valiget;

/**
 * 科研认证获取接口常量
 * 
 * @author ztg
 *
 */
public class SciVerificationGainConstant {


  public static Integer GAIN_SUCCESS = 1; // 已经验证了
  public static Integer GAIN_ERROR = 2; // 正在验证中
  public static Integer PARAME_ERROR = 3; // 参数错误
  public static Integer GAIN_HAS_RETURN = 4; // 已经返回过验证成功结果
  public static Integer GAIN_HAS_ERROR = 5; // detail 表中interface_status = 2



  public static final String UUID_EMPTY = "科研验证， 具体参数UUID不能为空或UUID不存在";
  public static final String KEY_CODE_EMPTY = "科研验证，具体参数keyCode不能为空";
  public static final String UUID_EORROR = "科研验证， 具体参数UUID不能为空或UUID不存在";
  public static final String UUID_DATA_VERIFY_UNCOMPLETE = "科研验证， 具体参数UUID对应的数据验证中，请稍后获取";
  public static final String UUID_DATA_VERIFY_SUCCESS = "科研验证， 具体参数UUID对应的数据验证完毕";
  public static final String UUID_DATA_VERIFY_HASRETURN = "科研验证， 具体参数UUID对应的验证结果已经多次返回，请勿再获取";
  public static final String UUID_DATA_VERFIY_ERROR = "科研验证， 具体参数UUID对应的验证结果发生内部异常，请勿再获取";



  public static final int MAIN_STATUS_COMPLETE = 1;// kpi_validate_main.status = 1 已经完成
  public static final int MAIN_STATUS_UNCOMPLETE = 0;// kpi_validate_main.status = 0, 待处理

  public static final int INTERFACE_STAUTS_SUCCESS = 1;
  public static final int INTERFACE_STAUTS_ERROR = 2;


  // kpi_vdlidate_main.key_type ，对应 业务系统业务类型，1申请书，2进展报告，3结题报告
  public static final String VD_REQUISITZION = "1";
  public static final String VD_PROGRESS_REPORT = "2";
  public static final String VD_CONCLUDING_REPORT = "3";
}
