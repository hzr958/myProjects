package com.smate.center.job.framework.support;

import com.sun.istack.internal.NotNull;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * 中介者抽象类（适用于中介者模式，具体中介继承此类即可）
 *
 * @param <T> 中介者包含的具体同事类型
 * @param <E> 包装通知内容的对象
 * @author Created by hcj
 * @date 2018/07/19 15:01
 */
public abstract class AbstractMediator<T extends Colleague<E>, E> {

  protected ConcurrentHashMap<String, T> colleagues = new ConcurrentHashMap<>();

  /**
   * 通知某同事执行某种操作
   *
   * @param who 被通知的对象标识
   * @param msg 包装被通知的具体内容，可以为消息、命令等
   * @return 执行对应的操作成功返回true，失败返回false，不存在被通知的对象时返回false
   */
  public boolean notify(String who, @NotNull E msg) {
    if (who == null) {
      return false;
    }
    Colleague colleague = colleagues.get(who);
    if (colleague == null) {
      return false;
    }
    return colleague.execute(msg);
  }

  public void notifyAll(@NotNull E msg) {
    notifyEach(msg, null);
  }

  public void notifyEach(@NotNull E msg, BiConsumer consumer) {
    colleagues.keySet().forEach(who -> {
      boolean result = this.notify(who, msg);
      if (consumer != null) {
        consumer.accept(who, result);
      }
    });
  }

  public void addColleague(String name, T colleague) {
    colleagues.put(name, colleague);
  }

  public void removeColleague(String who) {
    colleagues.remove(who);
  }

  public boolean hasColleague(String who) {
    return colleagues.containsKey(who);
  }

  public Optional<T> getColleague(String who) {
    return Optional.ofNullable(colleagues.get(who));
  }
}
