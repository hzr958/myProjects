
/*
   data-worktype     工作种类
   data-workplace    工作地点
   data-programe     项目数
   data-achievement  成果数
   data-title        头衔
   data-avator       头像
*/
var frametitle = function(){
    if(document.getElementsByClassName("data-showtitle_target")){
        var showlist = document.getElementsByClassName("data-showtitle_target");
        Array.from(document.getElementsByClassName("data-showtitle_target")).forEach(function(x){
                    addSpecificEventListener(x,"mousemove","mouseentershow",mouseentershow);
                    addSpecificEventListener(x,"mouseleave","mouseenterhiden",mouseenterhiden);
        })
        function addSpecificEventListener(o, evt, fname, f){
            const $object = {};
            $object.node = o;
            $object.eventType = evt;
            $object.functionName = fname;
            $object.function = f;
            o.addEventListener(evt, f);
        }
    }
}
const mouseentershow = function(){
    if(this.querySelector(".new-similartitle_container")){
        this.querySelector(".new-similartitle_container").style.display = "block";
        this.querySelector(".new-similartitle_container").style.opacity = 1;
    }else{
        var  showname = this.innerHTML.replace("\"","");
        if(this.getAttribute("data-worktype")!=null){
            var  worktype = this.getAttribute("data-worktype");
        }else{
            var  worktype = "";
        };
        if(this.getAttribute("data-workplace")!=null){
            var  workplace = this.getAttribute("data-workplace");
        }else{
            var  workplace = "";
        };
        if(this.getAttribute("data-programe")!=""){
            var  programecont = this.getAttribute("data-programe");
        }else{
            var  programecont = "";
        };
        if(this.getAttribute("data-achievement")!=null){
            var  Achievecount =  this.getAttribute("data-achievement");
        }else{
            var  Achievecount = "";
        };
        if(this.getAttribute("data-avator")!=null){
            var  avatorurl =  this.getAttribute("data-avator");
        }else{
            var  avatorurl = "gyy.png";
        };
        if(this.getAttribute("data-title")!=null){
            var showtitle = this.getAttribute("data-title");
        }else{
            var showtitle = "未知";
        }
        var showcontent = '<div class="new-similartitle_container-content">'
            +' <div class="new-similartitle_container-tip">'
            +'</div>'
            +'<div class="new-similartitle_container-avator">'
            +'<img src="' + avatorurl +'">'
            +'</div>'
            +'<div class="new-similartitle_container-detail">'
            +'<div class="new-similartitle_container-infor">'
            +'<span class="new-similartitle_container-name">' + showname + ' </span>'
            +'<span class="new-similartitle_container-title">' + showtitle + ' </span>'
            +'<div class="new-similartitle_work">'
            +'<span class="new-similartitle_work-title">'+ worktype +'</span>'
            +'<span class="new-similartitle_work-space">'+ workplace +'</span>'
            +'</div>'
            +'<div class="new-similartitle_psninfor">'
            +'<div class="new-similartitle_psninfor-programe">'
            +'<span class="new-similartitle_psninfor-programe_title">项目:</span>'
            +'<span class="new-similartitle_psninfor-programe_count">'+ programecont +'</span>'
            +'</div>'
            +'<div class="new-similartitle_psninfor-Achieve">'
            +'<span class="new-similartitle_psninfor-Achieve_title">成果:</span>'
            +'<span class="new-similartitle_psninfor-Achieve_count">'+Achievecount+'</span>'
            +'</div>'
            +'</div>'       
            +'</div>'
            +'<div class="new-similartitle_container-func">'
            +'<div class="new-similartitle_container-add">添加好友</div>'
            +'<div class="new-similartitle_container-visit">访问主页</div>'
            +'</div>'
            +'</div>'
            +'</div>';
        var showelem = document.createElement("div");
        showelem.className = "new-similartitle_container";
        showelem.innerHTML = showcontent;
        this.appendChild(showelem);
        showelem.style.display = "blcok";
        showelem.style.opacity = 1;
    }
        };
const mouseenterhiden = function(){
    var selfthis = this;
    if(selfthis.querySelector(".new-similartitle_container")){
        selfthis.querySelector(".new-similartitle_container").style.opacity = 0;
        setTimeout(function(){
            selfthis.querySelector(".new-similartitle_container").style.display = "none";
        },350)
        
    }
}