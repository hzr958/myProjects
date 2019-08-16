fundRecommend = {
  addAreaSizeFail : 'MaxiMun enter up to 3 areas only',
  addAreaEmptyFail : 'Please select at least 1 area',
  addFundSizeFail : 'Follow up to 10 funding agencies',
  addFundEmptyFail : 'Please select at least 1 funding agencies',
  addLocationsSizeFail : 'MaxiMun enter up to 3 locations only',
  addAreaRepFail : 'The area entered is existed already',
  addLocationRepFail : 'The location entered is existed already',
  addFail : 'Add failed, please try again later',
  deleteFail : 'Delete failed, please try again later',
  deleteSuccess : 'Delete success',
  like : "Like",
  unlike : "Unlike",
  share : "Share",
  save : "Save",
  unsave : "Cancel",
  focus : "Follow",
  unfocus : "Unfollow",
  shareSuccess : 'Shared successfully',
  shareFail : 'Share failed, please try again later',
  saveSuccess : 'Saved successfully',
  saveFail : 'Saved failed',
  success : 'Operated successfully',
  fundAgencyIsDel : 'The Funding agency has been deleted',
};
fundRecommend.getAddAreaSizeFail = function(num){
  if (num) {
      return "MaxiMun enter up to "+num+" areas only";
  }
};