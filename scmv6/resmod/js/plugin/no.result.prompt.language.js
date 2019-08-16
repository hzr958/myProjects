scmpublicprompt=function(obj, text, height){	
      $(obj).append("<div class='prompt_section'>"+text+"</div>");
      $(".prompt_section").css("margin-top",height);
}