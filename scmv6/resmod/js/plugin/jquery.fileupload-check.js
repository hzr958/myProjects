/**
 * 上传文件框类型、大小限制性检查
 * 参数：allowType: 必填，允许的文件类型，以‘,’或‘;’隔开；任意文件类型传空字符串即可；另外：image/*表示任意图片类型，audio/*表示任意音频类型，
 *                  video/*表示任意视频类型，text/*表示任意文本类型，applicantion/*表示任意应用类型
 * 参数：allowSize: 必填，允许的文件大小，纯数字表示字节数，可以带单位 b、kb、mb，忽略单位大小写；任意文件大小传0即可
 * 参数: params: 回调函数的额外参数
 * 参数：pass: 可选，检查通过回调函数，通过文件类型、大小的限制检查后回调该函数，回调函数的作用域this指向当前节点
 * 参数：fail: 可选，检查不通过时的回调函数，文件类型、大小的限制检查不通过则回调该函数，回调函数的作用域this指向当前节点，函数的第一个参数是检查失败的类型：
 *              allowType或者allowSize，第二个参数是params
 */
$.fn.checkFile = function(settings) {
    var defaults={
            'allowType': '',
            'allowSize': 0,
            'params': {},
            'pass': function(){},
            'fail': function(){}
        },
        s = $.extend({}, defaults, settings),
        allowType = s.allowType,
        allowSize = s.allowSize,
        pass = s.pass,
        params = s.params,
        fail = s.fail;
    if(!this.is('input[type="file"]')) return this;
    
    var patt = /(\.3gpp)|(\.ac3)|(\.asf)|(\.au)|(\.css)|(\.csv)|(\.doc[x]?)|(\.dot[x]?)|(\.dtd)|(\.dwg)|(\.dxf)|(\.gif)|(\.htm[l]?)|(\.jp2)|(\.jpe[g]?)|(\.jpg)|(\.js(on)?)|(\.mp2)|(\.mp3)|(\.mp4)|(\.mpeg)|(\.mpg)|(\.mpp)|(\.ogg)|(\.pdf)|(\.png)|(\.pot[x]?)|(\.pps[x]?)|(\.ppt[x]?)|(\.rtf)|(\.svf)|(\.tif[f]?)|(\.txt)|(\.wdb)|(\.wps)|(\.xhtml)|(\.xlc)|(\.xlm)|(\.xls[x]?)|(\.xlt[x]?)|(\.xlw)|(\.xml)|(\.zip)|(\.rar)|(\.tar)|(\.gzip)|(\image\/\*)|(audio\/\*)|(video\/\*)|(text\/\*)|(application\/\*)/i;
    var accept = new Array();
    if(typeof allowType == 'string'){
        jQuery.each(allowType.split(/[,;]/), function(i,v){
            var match = patt.exec(v);
            if(match != null){
                accept.push(match[0]);
            }
        });
    }
    if(accept.length > 0){
        this.attr('accept', accept.join());
    }
    
    var limitSize = allowSize;
    //非数字类型，带有单位的处理；纯数字类型按照字节计算
    if(isNaN(limitSize)){
        if(typeof limitSize != 'string')
            throw Error('限制文件大小的参数：allowSize不是合法类型的参数！allowSize=' + allowSize);
        var unit = limitSize.substr(-1).toLowerCase();
        //最后的字符是b
        if(unit == 'b'){
            limitSize = limitSize.slice(0, -1);
            //除去最后一个字符是b后还是非数字
            if(isNaN(limitSize)){
                //再取最后一个字符
                unit = limitSize.substr(-1).toLowerCase();
                limitSize = limitSize.slice(0, -1);
                //除去单位后还是非数字类型
                if(isNaN(limitSize)){
                    throw Error('限制文件大小的参数：allowSize不是合法类型的参数！allowSize=' + allowSize);
                }else{
                    switch(unit){
                    case 'm': limitSize = limitSize * 1024;
                    case 'k': limitSize = limitSize * 1024; break;
                    default: throw Error('限制文件大小的参数：allowSize不是合法类型的参数！allowSize=' + allowSize);
                    }
                }
            }
        }else{
            throw Error('限制文件大小的参数：allowSize不是合法类型的参数！allowSize=' + allowSize);
        }
    }
    var fileLimitCheck = function(){
        var fileSize = 0;
        if($(this)[0].files[0] != undefined){
            fileSize = $(this)[0].files[0].size
        }
        var fileName = $(this).val();
        if(!BaseUtils.checkFileType(fileName)){
            //清空文件
            $(this).val("");
            return false ;
        }
        var fileExt = fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
        if(accept.length > 0 && accept.join().indexOf(fileExt) == -1){
            //alert('文件类型不允许！');
            /*$.scmtips.show('warn', '该文件类型不允许上传！');*/
            $(this).val('');
            if(typeof fail == 'function')
                fail.call(this, 'allowType', $.extend({}, params));
        }else if(limitSize > 0 && fileSize > limitSize){
            //alert('超过文件大小限制！');
            /*$.scmtips.show('warn', '超过文件大小限制！');*/
            $(this).val('');
            if(typeof fail == 'function')
                fail.call(this, 'allowSize', $.extend({}, params));
        }else{
            /*console.log('文件类型：'+ fileExt);
            console.log('限制类型：'+ accept.join());*/
            if(typeof pass == 'function')
                pass.apply(this, params);
        }
    }
    return this.off('change', fileLimitCheck).on('change', fileLimitCheck);
}

$.extend({
   MIME_TYPES: {
       '.3gpp' : 'audio/3gpp, video/3gpp',
       '.ac3' : 'audio/ac3',
       '.asf' : 'allpication/vnd.ms-asf',
       '.au' : 'audio/basic',
       '.css' : 'text/css',
       '.csv' : 'text/csv',
       '.doc' : 'application/msword',
       '.docx' : 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
       '.dot' : 'application/msword',
       '.dotx' : 'application/vnd.openxmlformats-officedocument.wordprocessingml.template',
       '.dtd' : 'application/xml-dtd',
       '.dwg' : 'image/vnd.dwg',
       '.dxf' : 'image/vnd.dxf',
       '.gif' : 'image/gif',
       '.htm' : 'text/html',
       '.html' : 'text/html',
       '.jp2' : 'image/jp2',
       '.jpe' : 'image/jpeg',
       '.jpeg' : 'image/jpeg',
       '.jpg' : 'image/jpeg',
       '.js' : 'text/javascript, application/javascript',
       '.json' : 'application/json',
       '.mp2' : 'audio/mpeg, video/mpeg',
       '.mp3' : 'audio/mpeg',
       '.mp4' : 'audio/mp4, video/mp4',
       '.mpeg' : 'video/mpeg',
       '.mpg' : 'video/mpeg',
       '.mpp' : 'application/vnd.ms-project',
       '.ogg' : 'application/ogg, audio/ogg',
       '.pdf' : 'application/pdf',
       '.png' : 'image/png',
       '.pot' : 'application/vnd.ms-powerpoint',
       '.potx' : 'application/vnd.openxmlformats-officedocument.presentationml.template',
       '.pps' : 'application/vnd.ms-powerpoint',
       '.ppsx' : 'application/vnd.openxmlformats-officedocument.presentationml.slideshow',
       '.ppt' : 'application/vnd.ms-powerpoint',
       '.pptx' : 'application/vnd.openxmlformats-officedocument.presentationml.presentation',
       '.rtf' : 'application/rtf, text/rtf',
       '.svf' : 'image/vnd.svf',
       '.tif' : 'image/tiff',
       '.tiff' : 'image/tiff',
       '.txt' : 'text/plain',
       '.wdb' : 'application/vnd.ms-works',
       '.wps' : 'application/vnd.ms-works',
       '.xhtml' : 'application/xhtml+xml',
       '.xlc' : 'application/vnd.ms-excel',
       '.xlm' : 'application/vnd.ms-excel',
       '.xls' : 'application/vnd.ms-excel',
       '.xlsx' : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
       '.xlt' : 'application/vnd.ms-excel',
       '.xltx' : 'application/vnd.openxmlformats-officedocument.spreadsheetml.template',
       '.xlw' : 'application/vnd.ms-excel',
       '.xml' : 'text/xml, application/xml',
       '.zip' : 'aplication/zip',
       '.rar' : 'application/x-rar-compressed',
       '.tar' : 'application/x-tar',
       '.gzip' : 'application/x-gzip',
       'image/*' : 'image/*',
       'audio/*' : 'audio/*',
       'video/*' : 'video/*',
       'application/*' : 'application/*'
   }
});