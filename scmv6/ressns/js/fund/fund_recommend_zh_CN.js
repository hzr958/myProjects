fundRecommend = {
  addAreaSizeFail : '最多选择3个科技领域',
  addAreaEmptyFail : '最少选择1个科技领域',
  addFundSizeFail : '最多关注10个资助机构',
  addFundEmptyFail : '最少关注1个资助机构',
  addLocationsSizeFail : '最多3个关注地区',
  addAreaRepFail : '已有该科技领域',
  addLocationRepFail : '该地区已关注',
  addFail : '添加失败，请稍候再试',
  deleteFail : '删除失败，请稍候再试',
  deleteSuccess : '删除成功',
  like : "赞",
  unlike : "取消赞",
  share : "分享",
  save : "收藏",
  unsave : "取消收藏",
  focus : "关注",
  unfocus : "取消关注",
  shareSuccess : '分享成功',
  shareFail : '分享失败，请稍候再试',
  saveSuccess : '收藏成功',
  saveFail : '收藏失败',
  success : '操作成功',
  fundAgencyIsDel : '资助机构已被删除',
};
fundRecommend.getAddAreaSizeFail = function(num) {
  if (num) {
    return "最多选择" + num + "个科技领域";
  }
};