package com.smate.center.task.sys.quartz.task;

public class QuartzTestTask {

  public void run() {
    try {
      Long start = 0L;
      while ((start++) < 5) {
        System.out.println(start + "............QuartzTestTask ..............");
        Thread.sleep(1000);
      }
    } catch (Exception e) {

    }
  }
}
