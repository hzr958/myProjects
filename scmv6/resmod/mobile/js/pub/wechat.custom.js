//微信默认示开放分享
var smatewechat = smatewechat ? smatewechat : {};
smatewechat.initWeiXinShare = function(aid, tstp, nonstr, snt) {
  try {
    wx.config({
      // debug: true,
      appId : aid,
      timestamp : tstp,
      nonceStr : nonstr,
      signature : snt,
      jsApiList : ["hideMenuItems"]
    });
    wx.ready(function() {
      wx.hideOptionMenu();
    });
    wx.error(function(res) {
    });
  } catch (e) {

  }
}

smatewechat.customWeiXinShare = function(aid, tstp, nonstr, snt, objTitle, objLink, objImgUrl, objDesc) {
  // alert(aid+" tstp="+tstp+" nonstr="+nonstr+" snt="+snt+" objTitle="+objTitle+"
  // objLink="+objLink+" "+objImgUrl+" "+objDesc)
  wx.config({
    // debug:true,
    appId : aid,
    timestamp : tstp,
    nonceStr : nonstr,
    signature : snt,
    // jsApiList: ["showMenuItems"]
    // jsApiList: ["hideMenuItems"]
    jsApiList : ["showMenuItems", 'onMenuShareTimeline', 'onMenuShareAppMessage',
    // 'onMenuShareQQ',
    // 'onMenuShareQZone',
    'onMenuShareWeibo', 'hideMenuItems']
  });
  wx.ready(function() {
    wx.showOptionMenu();
    wx.hideMenuItems({
      menuList : ['menuItem:share:qq', 'menuItem:share:QZone']
    // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
    });
    // wx.hideOptionMenu();
    // 分享到朋友圈
    wx.onMenuShareTimeline({
      title : objTitle, // 分享标题
      link : objLink, // 分享链接
      imgUrl : objImgUrl, // 分享图标
      success : function() {
        // 用户确认分享后执行的回调函数
        // scmpublictoast("分享成功",1000);
      },
      cancel : function() {
        // 用户取消分享后执行的回调函数
      }
    });
    // 分享给朋友
    wx.onMenuShareAppMessage({
      title : objTitle, // 分享标题
      desc : objDesc, // 分享描述
      link : objLink, // 分享链接
      imgUrl : objImgUrl, // 分享图标
      type : 'link', // 分享类型,music、video或link，不填默认为link
      dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
      success : function() {
        // 用户确认分享后执行的回调函数
        // scmpublictoast("分享成功", 1000);
      },
      cancel : function() {
        // 用户取消分享后执行的回调函数
      }
    });
    // 分享到QQ
    wx.onMenuShareQQ({
      title : objTitle, // 分享标题
      desc : objDesc, // 分享描述
      link : objLink, // 分享链接
      imgUrl : objImgUrl, // 分享图标
      success : function() {
        // 用户确认分享后执行的回调函数
        scmpublictoast("分享成功", 1000);
      },
      cancel : function() {
        // 用户取消分享后执行的回调函数
      }
    });
    // 分享到腾讯微博
    wx.onMenuShareWeibo({
      title : objTitle, // 分享标题
      desc : objDesc, // 分享描述
      link : objLink, // 分享链接
      imgUrl : objImgUrl, // 分享图标
      success : function() {
        // 用户确认分享后执行的回调函数
        scmpublictoast("分享成功", 1000);
      },
      cancel : function() {
        // 用户取消分享后执行的回调函数
      }
    });
    // 分享到QQ空间
    wx.onMenuShareQZone({
      title : objTitle, // 分享标题
      desc : objDesc, // 分享描述
      link : objLink, // 分享链接
      imgUrl : objImgUrl, // 分享图标
      success : function() {
        // 用户确认分享后执行的回调函数
      },
      cancel : function() {
        // 用户取消分享后执行的回调函数
      }
    });
    wx.error(function(res) {

    });
  });

}