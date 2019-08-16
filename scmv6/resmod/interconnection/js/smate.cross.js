var smate = smate || {};
smate.crossMsg = function(msg) {
  alert("业务系统未实现该回调函数");
};

// open消息发送到业务系统
smate.sendMsg = function(msg) {
  run_env = typeof run_env == 'undefined' ? 'www' : run_env;
  // run_env 做个自动切换
  if ("opendev" == run_env) {
    run_env = "test";
  } else if ("opentest" == run_env) {
    run_env = "test";
  } else if ("openuat" == run_env) {
    run_env = "uat";
  } else if ("open" == run_env) {
    run_env = "www";
  }
  if (typeof exec_obj == 'undefined') {
    exec_obj = document.createElement('iframe');
    exec_obj.name = 'tmp_frame';
    exec_obj.src = cross_url + 'smate_cross_' + run_env + '.html?msg=' + msg;
    exec_obj.style.display = 'none';
    document.body.appendChild(exec_obj);
  } else {
    exec_obj.src = cross_url + 'smate_cross_' + run_env + '.html?' + Math.random() + '&msg=' + msg;
  }
}
// 获取open传到业务系统的msg
smate.receiveMsg = function() {
  var url = window.location.search; // 获取url中"?"符后的字串
  var theRequest = new Object();
  if (url.indexOf("?") != -1) {
    var str = url.substr(1);
    strs = str.split("&");
    for (var i = 0; i < strs.length; i++) {
      theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
    }
  }
  return theRequest["msg"];
}