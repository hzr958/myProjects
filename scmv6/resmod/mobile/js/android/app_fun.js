var appFun = appFun ? appFun : {};

appFun.showShareMenu = function(shareParams){
//  var shareParams = {
//      "shareUrl":  "http://dev.scholarmate.com/resmod/html/test_share.html",
//      "shareImgUrl": "http://www.scholarmate.com/pubFulltextImage/ba/6c/a1/1000004888515_img_0.jpeg",
//      "title" : "测试的标题",
//      "description" : "测试的描述",
//    };
    if(typeof androidAppProxy !== "undefined"){
      androidAppProxy.showShareMenus(ObjectToJsonString(shareParams));
    } else {
      console.log("Running outside Android app");
    }
}


appFun.getVersionName = function(){
  if(userAgent.indexOf("smate-android") != -1){
    var version = ""+userAgent.match(/smate-android\/[\d.]+/gi);
    if(version != null && version != ""){
      return version.replace("smate-android/", "");
    }
    return "";
  }
}


/**
 * 对象转JSON字符串
 * @param obj           对象
 * @returns {string}    JSON字符串
 */
var ObjectToJsonString = function (obj)
{
    var S = [];
    var J = null;

    var type = Object.prototype.toString.apply(obj);

    if (type === '[object Array]')
    {
        for (var i = 0; i < obj.length; i++)
        {
            S.push(ObjectToJsonString(obj[i]));
        }
        J = '[' + S.join(',') + ']';
    }
    else if (type === '[object Date]')
    {
        J = "new Date(" + obj.getTime() + ")";
    }
    else if (type === '[object RegExp]'
        || type === '[object Function]')
    {
        J = obj.toString();
    }
    else if (type === '[object Object]')
    {
        for (var key in obj)
        {
            var value = ObjectToJsonString(obj[key]);
            if (value != null)
            {
                S.push('"' + key + '":' + value);
            }
        }
        J = '{' + S.join(',') + '}';
    }
    else if (type === '[object String]')
    {
        J = '"' + obj.replace(/\\/g, '\\\\').replace(/"/g, '\\"').replace(/\n/g, '') + '"';
    }
    else if (type === '[object Number]')
    {
        J = obj;
    }
    else if (type === '[object Boolean]')
    {
        J = obj;
    }

    return J;
};


