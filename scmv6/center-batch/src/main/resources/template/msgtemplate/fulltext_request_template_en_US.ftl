<div style="margin-bottom:10px" id="main_content">
    Dear!
    <p style="padding-left:2em;">
       Please view ${title?replace("Request","")}!
    </p>
</div>
<div style="display:none" id="attachFileBlock">
    <div ismine="1" class="add_word mdown5" id="attachFileDiv" style="width: 448px;">
        <i class="annex_icon Fleft"></i>
        <div class="attachName inp_bg3 Fleft" style="width: 410px; color: #999;text-overflow:ellipsis; white-space:nowrap; overflow:hidden;">
            <a class="Blue pub_attach_link" style="cursor:pointer" href="${fullUrl}" target="_blank">${fullTextName}</a>
        </div>
        <input value="${fullTextId}" class="input_attachFileId" type="hidden"/>
        <input value="${des3FullTextId}" class="input_des3attachFileId" type="hidden"/>
        <a href="#" onclick="msgboxSend.deleteAttachFile(this)" class="box-delete mleft5" style="display:none"></a>
    </div>
</div>