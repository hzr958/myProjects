common = common ? common : {};
common.view = common.view ? common.view : {};
/*
 * 常量 定义时与viewEnum一致
 */
common.view.news = 1001; // 新闻阅览
common.view.photo = 2001; // 图片阅览
common.view.publication = 3001; // 成果阅览
common.view.project = 4001; // 项目阅览
common.view.inspg = 5001; // 动态阅览

/**
 * Inspg-阅览通用JS path:根据系统定义,Action代码完成时可用
 * 
 * 
 * 加载返回data格式: {result:[{psnId:psnId,psnImg:psnImg,\npsnNameZh:psnNameZh,
 * psnNameEn:psnNameEn,id:id},{},{}......],message:success/fail/error}
 */

/*
 * 加载阅读数 参数: objectId:阅览的对象id type:赞类型 callback:回调函数
 */
common.view.load = function(type, callback) {
    var objIdArr = common.view.objs();
    var post_data = {
        type : type,
        des3ObjectIds : objIdArr
    }
    $.ajax({
        url : '/inspg/view/ajaxcountviews',
        type : 'post',
        dataType : "json",
        data : post_data,
        success : function(data) {
            common.view.filldata(data);
        },
        error : function() {
            return false;
        }
    });
};

common.view.filldata = function(result) {
    var objArr = document.getElementsByName("view_obj");
    if (result.views == 0) {
    } else {
        for (var i = 0; i < objArr.length; i++) {
            var obj = "view_" + $(objArr[i]).attr("resId");
            var des3obj = $(objArr[i]).attr("des3ResId");
            $("#" + obj).text(result[des3obj]);
        }
    }
}

common.view.objs = function() {
    var objIdArr = "";
    var objArr = document.getElementsByName("view_obj");
    for (var i = 0; i < objArr.length; i++) {
        objIdArr += $(objArr[i]).attr("des3ResId") + ",";
    }
    return objIdArr;
}