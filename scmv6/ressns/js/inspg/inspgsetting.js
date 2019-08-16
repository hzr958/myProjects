smate = smate ? smate : {};
smate.inspgsetting = smate.inspgsetting ? smate.inspgsetting : {};
/**
 * 点击机构主页设置.
 */
smate.inspgsetting.setting = function(inspgId) {
    // TODO inspgId填入表单
    var actionUrl = '/inspg/inspgsetting/setting?inspgId=' + inspgId;
    $('#mainForm').attr("action", actionUrl);
    $('#mainForm').submit();
};

/**
 * 保存机构主页设置.
 */
smate.inspgsetting.save = function(psnId) {

    var inspgId = $.trim($("#des3InspgId").val());
    var insName = $.trim($("#insName").val());
    var industryIds = $.trim($("#industryIds").val());
    // var industry = $.trim($("#industry").val());
    var disciplineFieldIds = $.trim($("#disciplineFieldIds").val());
    // var disciplineField = $.trim($("#disciplineField").val());
    var skillsFieldIds = $.trim($("#skillsFieldIds").val());
    // var skillsField = $("#skillsField").val();
    var inspgLogo = $.trim($("#inspgLogo").attr("src"));
    var inspgPhoto = $.trim($("#inspgPhoto").attr("src"));
    // TODO 获取封装主页选项的设置内容.
    var totalOption = smate.inspgsetting.caloptions();
    var post_data = {
        'des3InspgId' : inspgId,
        'insName' : insName,
        'industryIds' : industryIds,
        'disciplineFieldIds' : disciplineFieldIds,
        'skillsFieldIds' : skillsFieldIds,
        'inspgLogo' : inspgLogo,
        'inspgPhoto' : inspgPhoto,
        'viewModules' : totalOption
    };
    $
            .ajax({
                url : '/inspg/inspgsetting/savedata',
                type : 'post',
                dataType : 'json',
                data : post_data,
                success : function(data) {
                    if (data.result == 'success') {
                        $.smate.scmtips.show('success', data.msg);
                        window.location.href = "/inspg/inspgdynamic/showhome?des3InspgId="
                                + data.des3InspgId;
                    } else {
                        if (data.ajaxSessionTimeOut == "yes") {
                            jConfirm("系统超时或未登录，你要登录吗？", "提示", function(r) {
                                if (r) {
                                    var url = document.location.href;
                                    url = url.replace('#', '');
                                    document.location.href = url;
                                }
                            });
                        }
                        return false;
                    }
                },
                error : function(data, status, e) {
                }
            });
};
/**
 * 计算主页选项值.
 */
smate.inspgsetting.caloptions = function() {
    var str = document.getElementsByName("optionBox");
    var objarray = str.length;
    var totalNum = 0;
    for (i = 0; i < objarray; i++) {
        if (str[i].checked == true) {
            totalNum += parseInt(str[i].value);
        }
    }
    return totalNum;
}
smate.inspgsetting.cancel = function(inspgId) {
    window.location.href = "/inspg/inspgdynamic/showhome?des3InspgId="
            + inspgId;
}
