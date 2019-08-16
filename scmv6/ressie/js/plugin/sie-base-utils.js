var SieBaseUtils = SieBaseUtils ? SieBaseUtils : {};


//检查是否为空
SieBaseUtils.checkIsNull = function(param){
  var check = false;
  if(param == null || param == "" || typeof(param) == "undefined"){
    check = true;
  }else{
    param = BaseUtils.trimLeftAndRightSpace(param);
    if(param == ""){
      check = true;
    }
  }
  return check;
}

//去掉左右两端空格
SieBaseUtils.trimLeftAndRightSpace = function(str){ //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}


/**
 * 时间格式转换
 */
SieBaseUtils.dateFormat = function(fmt,date){ 
  var o = {   
    "M+" : date.getMonth()+1,                 //月份   
    "d+" : date.getDate(),                    //日   
    "h+" : date.getHours(),                   //小时   
    "m+" : date.getMinutes(),                 //分   
    "s+" : date.getSeconds(),                 //秒   
    "q+" : Math.floor((date.getMonth()+3)/3), //季度   
    "S"  : date.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt)){
    fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
  }   
  for(var k in o){
    if(new RegExp("("+ k +")").test(fmt)){
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
    }   
  }
  return fmt;   
}