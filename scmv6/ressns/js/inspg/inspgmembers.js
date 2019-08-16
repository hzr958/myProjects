smate = smate ? smate : {};
smate.inspgmembers = smate.inspgmembers ? smate.inspgmembers : {};

/**
 * 加载Member
 */
smate.inspgmembers.ajaxLoad = function() {
    $.ajax({
        url : '/inspg/inspgmember/ajaxloadmembers',
        type : 'post',
        dataType : 'html',
        data : {
            "des3InspgId" : $("#des3InspgId").val()
        },
        success : function(data) {
            // 填充数据
            $(".org_member").append(data);
        },
        error : function(data, status, e) {
        }
    });
};
// 删除成员
smate.inspgmembers.delMember = function(memberId) {
    jConfirm(
            inspgmemberinternational.deleteConfirm,
            inspgmemberinternational.prompt,
            function(r) {
                if (r) {
                    $
                            .ajax({
                                url : '/inspg/inspgmember/ajaxdeletemembers',
                                type : 'post',
                                dataType : 'json',
                                data : {
                                    "memberId" : memberId,
                                    "des3InspgId" : $("#des3InspgId").val()
                                },
                                success : function(data) {
                                    window.location.href = "/inspg/inspgmember/showmembers?des3InspgId="
                                            + $("#des3InspgId").val();
                                },
                                error : function(data, status, e) {
                                }
                            });
                }
            });

};
// 进入新增成员弹窗
smate.inspgmembers.loadAddMember = function() {
    $("#add_member").attr("title", "录入");
    $("#add_member").click();
};
