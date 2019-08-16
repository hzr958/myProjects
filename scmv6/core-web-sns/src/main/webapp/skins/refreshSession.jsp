<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$(document).ready(function(){
    refreshSession();
});
//保持session
var time = 900;
function refreshSession()
{
    time = time-1;
    if(time>0){
        setTimeout("refreshSession()", 1000);
    }else{
        $.ajax({
            url: "/oauth/session/ajaxrefresh",
            type: "post",
            data: {
                
            },
            dataType: "json",
            success: function(data){
                time = 900;
                refreshSession();
            },
            error: function(data){
                time = 900;
                refreshSession();
            }
        });
    }
}
</script>