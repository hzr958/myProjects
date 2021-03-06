<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
.preloader {
  opacity: 0;
  transition: all 0.3s cubic-bezier(1, 0, 1, 0);
  border-color: #4285f4;
  display: flex;
  justify-content: center;
  align-items: center;
}
.preloader.active {
  opacity: 1;
  transition: all 0.3s cubic-bezier(1, 0, 1, 0);
  margin-top: 40px;
}
.preloader.green-style {
  border-color: #349800;
}
.preloader .preloader-ind-cir__box {
  width: 100%;
  height: 100%;
  border-color: inherit;
  position: relative;
  animation: preloader-ind-cir__box 1568ms linear infinite;
}
.preloader .preloader-ind-cir__box .preloader-ind-cir__fill {
  position: absolute;
  width: 100%;
  height: 100%;
  border-color: inherit;
  animation: preloader-ind-cir__fill 5332ms cubic-bezier(0.4, 0, 0.2, 1) infinite both;
}
.preloader .preloader-ind-cir__box .preloader-ind-cir__fill .preloader-ind-cir__arc-box {
  display: inline-block;
  position: relative;
  width: 50%;
  height: 100%;
  overflow: hidden;
  border-color: inherit;
}
.preloader .preloader-ind-cir__box .preloader-ind-cir__fill .preloader-ind-cir__arc-box.left-half {
  float: left;
}
.preloader .preloader-ind-cir__box .preloader-ind-cir__fill .preloader-ind-cir__arc-box.left-half .preloader-ind-cir__arc {
  border-right-color: transparent;
  transform: rotate(129deg);
  animation: preloader-ind-cir__left-spin 1333ms cubic-bezier(0.4, 0, 0.2, 1) infinite both;
}
.preloader .preloader-ind-cir__box .preloader-ind-cir__fill .preloader-ind-cir__arc-box.right-half {
  float: right;
}
.preloader .preloader-ind-cir__box .preloader-ind-cir__fill .preloader-ind-cir__arc-box.right-half .preloader-ind-cir__arc {
  left: -100%;
  border-left-color: transparent;
  transform: rotate(-129deg);
  animation: preloader-ind-cir__right-spin 1333ms cubic-bezier(0.4, 0, 0.2, 1) infinite both;
}
.preloader .preloader-ind-cir__box .preloader-ind-cir__fill .preloader-ind-cir__arc-box .preloader-ind-cir__arc {
  width: 200%;
}
.preloader .preloader-ind-cir__box .preloader-ind-cir__fill .preloader-ind-cir__gap {
  position: absolute;
  box-sizing: border-box;
  top: 0;
  left: 45%;
  width: 10%;
  height: 100%;
  overflow: hidden;
  border-color: inherit;
}
.preloader .preloader-ind-cir__box .preloader-ind-cir__fill .preloader-ind-cir__gap .preloader-ind-cir__arc {
  width: 1000%;
  left: -450%;
}
.preloader-ind-cir__arc {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  box-sizing: border-box;
  height: 100%;
  border-width: 3px;
  border-style: solid;
  border-color: inherit;
  border-bottom-color: transparent;
  border-radius: 50%;
  animation: none;
}

.preloader_ind-linear .preloader-ind-linear__box {
  display: block;
  overflow: hidden;
  width: 100%;
  height: 4px;
  position: relative;
  background-color: #aad2fa;
}
.preloader_ind-linear .preloader-ind-linear__box .preloader-ind-linear__bar1 {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background-color: #4285f4;
  animation: preloader-ind-linear__bar1-scale 2s infinite,preloader-ind-linear__bar1-position 2s infinite;
  transition: transform 0.2s linear;
}
.preloader_ind-linear .preloader-ind-linear__box .preloader-ind-linear__bar2 {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  background-color: #4285f4;
  animation: preloader-ind-linear__bar2-scale 2s infinite,preloader-ind-linear__bar2-position 2s infinite;
  transition: 0.2s linear;
}

@keyframes preloader-ind-cir__box {
  to {
    transform: rotate(360deg);
  }
}
@keyframes preloader-ind-cir__fill {
  12.5% {
    transform: rotate(135deg);
  }
  25% {
    transform: rotate(270deg);
  }
  37.5% {
    transform: rotate(405deg);
  }
  50% {
    transform: rotate(540deg);
  }
  62.5% {
    transform: rotate(675deg);
  }
  75% {
    transform: rotate(810deg);
  }
  87.5% {
    transform: rotate(945deg);
  }
  to {
    transform: rotate(1080deg);
  }
}
@keyframes preloader-ind-cir__left-spin {
  0% {
    transform: rotate(130deg);
  }
  50% {
    transform: rotate(-5deg);
  }
  to {
    transform: rotate(130deg);
  }
}
@keyframes preloader-ind-cir__right-spin {
  0% {
    transform: rotate(-130deg);
  }
  50% {
    transform: rotate(5deg);
  }
  to {
    transform: rotate(-130deg);
  }
}
@keyframes preloader-ind-linear__bar1-scale {
  0% {
    transform: scaleX(0.1);
    animation-timing-function: linear;
  }
  36.6% {
    transform: scaleX(0.1);
    animation-timing-function: cubic-bezier(0.33, 0.12, 0.79, 1);
  }
  69.15% {
    transform: scaleX(0.83);
    animation-timing-function: cubic-bezier(0.23, 0, 0.23, 1.37);
  }
  to {
    transform: scaleX(0.1);
  }
}
@keyframes preloader-ind-linear__bar1-position {
  0% {
    left: -105%;
    animation-timing-function: linear;
  }
  20% {
    left: -105%;
    animation-timing-function: cubic-bezier(0.5, 0, 0.7, 0.5);
  }
  69.15% {
    left: 21.5%;
    animation-timing-function: cubic-bezier(0.3, 0.38, 0.55, 0.96);
  }
  to {
    left: 95%;
  }
}
@keyframes preloader-ind-linear__bar2-scale {
  0% {
    transform: scaleX(0.1);
    animation-timing-function: cubic-bezier(0.2, 0.06, 0.58, 0.45);
  }
  19.15% {
    transform: scaleX(0.57);
    animation-timing-function: cubic-bezier(0.15, 0.2, 0.65, 1);
  }
  44.15% {
    transform: scaleX(0.91);
    animation-timing-function: cubic-bezier(0.26, 0, 0.2, 1.38);
  }
  to {
    transform: scaleX(0.1);
  }
}
@keyframes preloader-ind-linear__bar2-position {
  0% {
    left: -55%;
    animation-timing-function: cubic-bezier(0.15, 0, 0.52, 0.4);
  }
  25% {
    left: -17.25%;
    animation-timing-function: cubic-bezier(0.3, 0.28, 0.8, 0.72);
  }
  48.35% {
    left: 29.5%;
    animation-timing-function: cubic-bezier(0.4, 0.62, 0.6, 0.9);
  }
  to {
    left: 118%;
  }
}

.single_btn{
    width: 94%;
    margin-right: 3%;
}

.pub_detail_info{
    padding: 0px 16px;
    margin-bottom: 12px;
    margin-top: 80px;
}
</style>