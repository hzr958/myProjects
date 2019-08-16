var JobStatus = {
  /**
   * 执行失败
   */
  FAILED: new Enum("FAILED", -1, "执行失败", "label label-white label-danger middle"),

  /**
   * 未执行
   */
  UNPROCESS: new Enum("UNPROCESS", 0, "未执行", "label label-white middle"),

  /**
   * 已加载
   */
  LOADED: new Enum("LOADED", 1, "已加载", "label label-white label-light middle"),

  /**
   * 已分配
   */
  DISTRIBUTED: new Enum("DISTRIBUTED", 2, "已分配", "label label-white label-yellow middle"),

  /**
   * 等待执行
   */
  WAITING: new Enum("WAITING", 3, "等待执行", "label label-purple label-white middle"),

  /**
   * 正在执行
   */
  PROCESSING: new Enum("PROCESSING", 4, "正在执行", "label label-info label-white middle"),

  /**
   * 执行完毕
   */
  PROCESSED: new Enum("PROCESSED", 5, "执行完毕", "label label-success label-white middle")
};
JobStatus.valueOf = function (val) {
  for(var k in JobStatus) {
    if(val == JobStatus[k].value){
      return JobStatus[k];
    }
  }
  return null;
}

function Enum(n, v, d, c) {
  this.name = n;
  this.value = v;
  this.desc = d;
  this.classes = c;
}
