package com.smate.center.job.test;

import com.smate.center.job.framework.zookeeper.exception.ZooKeeperServiceException;
import com.smate.center.job.framework.zookeeper.service.ZKClientService;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.zookeeper.data.Stat;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ZooKeeper客户端服务实现类的测试类
 *
 * @author houchuanjie
 * @date 2018/04/02 14:45
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-zookeeper.xml"})
public class ZKClientServiceImplTest {

  private ZKClientService ZKClientService;
  @Autowired
  private CuratorFramework client;

  @Test
  public void testGetState() throws Exception {
    Stat stat = client.checkExists().forPath("/examples");
    System.out.println(stat.getCtime());
    System.out.println(stat.getMtime());
  }

  @Test
  public void testNextID() throws ZooKeeperServiceException, InterruptedException {
    Thread.sleep(new Random().nextInt(10000));
    Integer nextID = ZKClientService.nextID();
    System.out.println("nextId: " + nextID);
    Assert.assertNotNull(nextID);
  }

  @Test
  public void testGetChildren() throws Exception {
    List<String> pathList = client.getChildren().forPath("/examples/node");
    for (String s : pathList) {
      System.out.println(s);
    }
  }

  @Test
  public void testCheck() {
    try {
      Stat stat = client.checkExists().forPath("/examples/testCreate");
      System.out.println("节点检查：" + (Objects.nonNull(stat) ? "存在" : "不存在"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testCreate() {
    try {
      client.create().creatingParentContainersIfNeeded()
          .forPath("/examples/testCreate");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testTxOp() throws Exception {
    String nodePath = "/examples/testCreate";
    CuratorOp checkOp = client.transactionOp().check().forPath(nodePath);
    CuratorOp deleteOp = client.transactionOp().delete().forPath(nodePath);
    List<CuratorTransactionResult> txResult = client.transaction()
        .forOperations(checkOp, deleteOp);
    for (CuratorTransactionResult result : txResult) {
      System.out.println("-------------------------");
      System.out.println("Type:\t" + result.getType());
      System.out.println("Stat:\t" + result.getResultStat());
      System.out.println("Error:\t" + result.getError());
      System.out.println("ForPath:\t" + result.getForPath());
      System.out.println("ResultPath:\t" + result.getResultPath());
    }
  }
}
