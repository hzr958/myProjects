/**
 * 群组管理
 * @type {{}}
 */
var GrpManage = GrpManage ? GrpManage : {}  ;



GrpManage.myGrpMainlist = {} ;
GrpManage.getMyGrpList = function(){
    GrpManage.myGrpMainlist = window.Mainlist({
        name : "mygrp_list",
        listurl : "/scmmanagement/grpmanage/grplist",
        listdata : {},
        method : "pagination",
        listcallback : function(xhr) {
            addFormElementsEvents();
        },
        /*statsurl: ""
        ,*/
        drawermethods:{"批量导入成果":function(a){
            if(a.length>0){
                GrpManage.showSearchPdwhBox();
            }
        }
        }
    });
}

/**
 * 显示基准库的弹框
 */
GrpManage.showSearchPdwhBox = function () {
    GrpManage.pubBoxInterval = setInterval(function() {// 确定按钮的监听
        GrpManage.is_pubsend();
    }, 1000);
    showDialog("search_pdwhpub_import_dialog");
    //GrpManage.showMyPubList();
}

// 关闭成果UI
GrpManage.hidePubUI = function() {
    $("#search_pdwhpub_key").val("");
    clearInterval(GrpManage.pubBoxInterval);// 取消确定按钮的监听
    hideDialog("search_pdwhpub_import_dialog");
}
/**
 * 保存基准库成果到 群组里面
 */
GrpManage.savePdwhpubToGrps = function (obj) {
    var  des3GrpIds = "";
    $(".drawer-batch__box").find(".main-list__item").each(function () {
        var id = $(this).attr("drawer-id");
        des3GrpIds = des3GrpIds + id + ";" ;
    })
    var  pubIds = "";
    $("#pdwhpublistId").find("input:checkbox[name='pub-type']:checked").each(function () {
        var pubid = $(this).closest(".main-list__item").attr("pubid");
        pubIds = pubIds + pubid + ";";
    })
   //console.log(des3GrpIds);
   //console.log(pubIds);
    var click  = BaseUtils.unDisable(obj)
    $.ajax({
        url:"/scmmanagement/grpmanage/savepdwhpubtogrp",
        type : "post",
        dataType : "json",
        data :{
            desGrpIds:des3GrpIds,
            pdwhPubIds:pubIds
        },
        success:function(data){
            BaseUtils.disable(obj,click);
            if(data.result == "success"){
                scmpublictoast(data.count+"条成果导入成功", 4000);
            }else {
                scmpublictoast("导入失败", 1000);
            }
        },
        error:function(data){
            alert("请求数据出错!");
        }
    });
}

//   监听发送按钮的disabled设置
GrpManage.is_pubsend = function() {
    if ($("#pdwhpublistId").find("input:checkbox[name='pub-type']:checked").length > 0) {
        $("#select_pdwh_pub_import").find(".button_primary-reverse").removeAttr("disabled");
    } else {
        $("#select_pdwh_pub_import").find(".button_primary-reverse").attr("disabled", true);
    }
}

//  检索基准库成果
GrpManage.showPdwhPubList = function() {
    $("#pdwhpublistId").doLoadStateIco({
        style : "height: 38px; width:38px; margin:auto ; margin-top:10px;",
        status : 1
    });
    var  params= {
         "searchKey":$("#search_pdwhpub_key").val()
    };
    $.ajax({
        url:"/scmmanagement/grpmanage/searchpdwhpub",
        type : "post",
        dataType : "html",
        data :params,
        success:function(data){
            $("#pdwhpublistId").html(data);
        },
        error:function(data){
            alert("请求数据出错!");
        }
    });

}


GrpManage.exportGrp = function () {
    location.href = "/scmmanagement/grpmanage/importgrp";
}
