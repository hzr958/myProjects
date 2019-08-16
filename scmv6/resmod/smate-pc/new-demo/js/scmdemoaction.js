window.onload = function(){    
        if($("#divselect1").length>0){
        var screenlect = document.getElementById("divselect1").querySelector(".tp_box-select");
            $(".radio-box_select").on("click",function(){
                var classarry = this.getAttribute("class");
                var selctnum = classarry.indexOf("radio-box_unchecked");
                if(selctnum != -1){
                    $(this).closest(".radio-box").find(".radio-box_checked").attr("class","radio-box_unchecked radio-box_select");
                    this.className="radio-box_checked radio-box_select";
                    if(this.innerHTML==="检索论文"){
                        screenlect.className="dt_icon";
                    }else if(this.innerHTML==="检索专利"){
                        screenlect.className="icon-personnel";
                    }else{
                         screenlect.className="icon-paper";
                    }
                }
            })
        }

        $(".check_fx-select-total").on("click",function(){
            if(this.checked){
                $(this).closest(".contentbox__wrap").find("input[type='checkbox']").attr("checked", "checked");
            }else{
                $(this).closest(".contentbox__wrap").find("input[type='checkbox']").removeAttr("checked","checked");
            }
        });

        $("#iconQh").on("click",function(){
            if(this.getAttribute("className")==="keyboard_arrow_up"||this.getAttribute("class")==="keyboard_arrow_up"){
                $("#iconQh").attr("class","keyboard_arrow_down");
                document.getElementById("leftmenlistitem").style.display="none";
            }else{
                $("#iconQh").attr("class","keyboard_arrow_up");
                document.getElementById("leftmenlistitem").style.display="block";
            }
        }); 
}