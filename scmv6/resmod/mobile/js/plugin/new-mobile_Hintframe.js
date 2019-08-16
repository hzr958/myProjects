/**
 *  不同的datacode的值 会弹出不同的内容框
 *  datacode = "1" 或者 datacode ="" 时会弹出操作成功的弹框
 *  datacode = "2" 会弹出操作失败的弹框
 *  datacode = "3" 或者其他的值 会弹出稍等的弹框
 *  showTime  提示框显示的时间，至少1秒钟
 *  当你需要弹出不同内容的提示框的时候 你只需要把你想弹出内容传进来即可
 *  当然 当你直接传入显示内容的时候 你也可能需要显示不同的提示图标 此时 你就可以根据你显示的内容来设置 tiptype的值 从而来确定你弹框内的提示图标
 *  当你显示的内容是表现出操作成功   是积极的    请把tiptype 置 1
 *  当你显示的内容是表现出操作失败   是消极的    请把tiptype 置 0
 *  当你显示的内容是表现出操作不确定 是中性的    请把tiptype  置 2
 *  如若不设置 tiptype 默认为2
 *  
 *  注意 当你的datacode为数字的时候 不需要设置tiptype  当你的datacode为字符串的时候 才可以设置tiptype
 * */
var newMobileTip = function(datacode, tiptype, showTime){
  if(typeof(showTime) == "undefined" || showTime == "" || showTime == null || showTime < 1000){
    showTime = 1000;
  }
  
    if((datacode == "")||(datacode == "1")){
        var toastcontent = '<i class="new-mobile_tip-container_img new-mobile_tip-success"></i><span class="new-mobile_tip-container_title">操作成功</span>'
    }else if(datacode == "2"){
        var toastcontent = '<i class="new-mobile_tip-container_img new-mobile_tip-fail"></i><span class="new-mobile_tip-container_title">操作失败</span>'
    }else if(datacode == "3"){
        var toastcontent = '<i class="new-mobile_tip-container_img new-mobile_tip-wait"></i><span class="new-mobile_tip-container_title">请稍后</span>'
    }else{
        
        if(tiptype == "1"){
            var toastcontent = '<i class="new-mobile_tip-container_img new-mobile_tip-success"></i><span class="new-mobile_tip-container_title">'+ datacode +'</span>'
        }else if(tiptype == "0"){
            var toastcontent = '<i class="new-mobile_tip-container_img new-mobile_tip-fail"></i><span class="new-mobile_tip-container_title">'+ datacode +'</span>'
        }else{
            var toastcontent = '<i class="new-mobile_tip-container_img new-mobile_tip-wait"></i><span class="new-mobile_tip-container_title">'+ datacode +'</span>'
        }
    }
    var toastcontainer = document.createElement("div");
    toastcontainer.className = "new-mobile_tip-container_avator";
    toastcontainer.innerHTML = toastcontent;
    if(document.getElementsByClassName("new-mobile_tip-container").length>0){
        var parentbox = document.getElementsByClassName("new-mobile_tip-container")[0];
        parentbox.appendChild(toastcontainer);
        var tipelem = document.getElementsByClassName("new-mobile_tip-container_avator")[0];
        tipelem.style.right = (window.innerWidth - tipelem.offsetWidth)/2 + "px";
        tipelem.style.bottom = (window.innerHeight - tipelem.offsetHeight)/2 + "px";
        setTimeout(function(){
            tipelem.style.bottom =  - 600 + "px";
        },showTime);
        setTimeout(function(){
            parentbox.removeChild(toastcontainer);
            document.body.removeChild(parentbox);
        },showTime + 500);
    }else{
        var parentbox = document.createElement("div");
        parentbox.className = "new-mobile_tip-container";
        document.body.appendChild(parentbox);
        parentbox.appendChild(toastcontainer);
        var tipelem = document.getElementsByClassName("new-mobile_tip-container_avator")[0];
        tipelem.style.right = (window.innerWidth - tipelem.offsetWidth)/2 + "px";
        tipelem.style.bottom = (window.innerHeight - tipelem.offsetHeight)/2 + "px";
        setTimeout(function(){
            tipelem.style.bottom =  - 600 + "px";
        },showTime);
        setTimeout(function(){
            parentbox.removeChild(toastcontainer);
            document.body.removeChild(parentbox);
        },showTime + 500);
    }
}