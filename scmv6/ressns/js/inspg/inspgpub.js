var smate = smate ? smate : {};
smate.inspgpub = smate.inspgpub ? smate.inspgpub : {};

/**
 * 获取成果系统机构主页的成果列表
 */
smate.inspgpub.ajaxpubdata = function() {
    var des3InspgId = $("#des3InspgId").val();
    var psnType = $("#psnType").val();
    var datajson = {
        "des3InspgId" : des3InspgId,
        "psnType" : psnType
    };
    $.ajax({
        url : '/pubweb/inspg/ajaxfindpub',
        type : 'post',
        dataType : 'text',
        data : datajson,
        success : function(data) {
            $("#content").html(data);
        },
        error : function(data, status, e) {
            $.smate.scmtips.show('warn', data.msg);
        }
    });
}