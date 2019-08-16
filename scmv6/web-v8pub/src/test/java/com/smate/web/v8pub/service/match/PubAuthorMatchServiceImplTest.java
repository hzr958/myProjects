package com.smate.web.v8pub.service.match;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.smate.web.v8pub.dto.PubMemberDTO;

class PubAuthorMatchServiceImplTest {

  private PubAuthorMatchServiceImpl pubAuthorMatchServiceImpl;

  @BeforeEach
  void beforeEach() {
    pubAuthorMatchServiceImpl = new PubAuthorMatchServiceImpl();
  }

  @Test
  void testPerfectMembers1() {

    String authorNames =
        "Hammond, Drayton A.; Smith, Melanie N.; Painter, Jacob T.; Meena, Nikhil K.; Lusardi, Katherine";
    List<PubMemberDTO> xmlAuthors = new ArrayList<>();
    PubMemberDTO p1 = new PubMemberDTO();
    p1.setCommunicable(false);
    p1.setDept(
        "Univ Arkansas Med Sci, Coll Pharm, Dept Pharm Practice, 4301 West Markham St,Slot 522, Little Rock, AR 72205 USA. .");
    p1.setEmail("1111@qq.com");
    p1.setFirstAuthor(true);
    p1.setInsNames(new ArrayList<>());
    p1.setName("Hammond, Drayton A.");
    p1.setPsnId(null);
    p1.setSeqNo(1);
    xmlAuthors.add(p1);

    PubMemberDTO p2 = new PubMemberDTO();
    p2.setCommunicable(false);
    p2.setDept(
        "Univ Arkansas Med Sci, Coll Pharm, Dept Pharm Practice, 4301 West Markham St,Slot 522, Little Rock, AR 72205 USA. .");
    p2.setEmail("2222@qq.com");
    p2.setFirstAuthor(false);
    p2.setInsNames(new ArrayList<>());
    p2.setName("Painter, Jacob T.");
    p2.setPsnId(null);
    p2.setSeqNo(2);
    xmlAuthors.add(p2);

    PubMemberDTO p3 = new PubMemberDTO();
    p3.setCommunicable(false);
    p3.setDept(
        "Univ Arkansas Med Sci, Coll Pharm, Dept Pharm Practice, 4301 West Markham St,Slot 522, Little Rock, AR 72205 USA. .");
    p3.setEmail("33333@qq.com");
    p3.setFirstAuthor(false);
    p3.setInsNames(new ArrayList<>());
    p3.setName("Lusardi, Katherine");
    p3.setPsnId(null);
    p3.setSeqNo(3);
    xmlAuthors.add(p3);

    PubMemberDTO p4 = new PubMemberDTO();
    p4.setCommunicable(false);
    p4.setDept("Univ Arkansas Med Sci, Med Ctr, Little Rock, AR 72205 USA. .");
    p4.setEmail("44444@qq.com");
    p4.setFirstAuthor(false);
    p4.setInsNames(new ArrayList<>());
    p4.setName("Hammond, Drayton A.");
    p4.setPsnId(null);
    p4.setSeqNo(4);
    xmlAuthors.add(p4);

    PubMemberDTO p5 = new PubMemberDTO();
    p5.setCommunicable(false);
    p5.setDept("Univ Arkansas Med Sci, Med Ctr, Little Rock, AR 72205 USA. .");
    p5.setEmail("5555@qq.com");
    p5.setFirstAuthor(false);
    p5.setInsNames(new ArrayList<>());
    p5.setName("Lusardi, Katherine");
    p5.setPsnId(null);
    p5.setSeqNo(5);
    xmlAuthors.add(p5);

    PubMemberDTO p6 = new PubMemberDTO();
    p6.setCommunicable(false);
    p6.setDept("Univ Florida, Hlth Sci Ctr Jacksonville, Dept Pharm, Jacksonville, FL 32209 USA. .");
    p6.setEmail("66666@qq.com");
    p6.setFirstAuthor(false);
    p6.setInsNames(new ArrayList<>());
    p6.setName("Smith, Melanie N.");
    p6.setPsnId(null);
    p6.setSeqNo(6);
    xmlAuthors.add(p6);

    PubMemberDTO p7 = new PubMemberDTO();
    p7.setCommunicable(false);
    p7.setDept("Univ Arkansas Med Sci, Coll Med, Dept Internal Med, Little Rock, AR 72205 USA..");
    p7.setEmail("77777@qq.com");
    p7.setFirstAuthor(false);
    p7.setInsNames(new ArrayList<>());
    p7.setName("Meena, Nikhil K.");
    p7.setPsnId(null);
    p7.setSeqNo(7);
    xmlAuthors.add(p7);

    PubMemberDTO p8 = new PubMemberDTO();
    p8.setCommunicable(false);
    p8.setDept(
        "Univ Arkansas Med Sci, Coll Pharm, Dept Pharm Practice, 4301 West Markham St,Slot 522, Little Rock, AR 72205 USA.");
    p8.setEmail("88888@qq.com");
    p8.setFirstAuthor(false);
    p8.setInsNames(new ArrayList<>());
    p8.setName("Hammond, DA");
    p8.setPsnId(null);
    p8.setCommunicable(true);
    p8.setSeqNo(8);
    xmlAuthors.add(p8);

    List<PubMemberDTO> memberList = pubAuthorMatchServiceImpl.perfectMembers(authorNames, xmlAuthors);
    assertEquals(memberList.get(0).getDepts().size(), 2);
    assertEquals(memberList.get(1).getDepts().size(), 1);
    assertEquals(memberList.get(2).getDepts().size(), 1);
    assertEquals(memberList.get(3).getDepts().size(), 1);
    assertEquals(memberList.get(4).getDepts().size(), 2);
    // 通讯作者
    assertEquals(memberList.get(0).isCommunicable(), true);
    // 作者数量
    assertEquals(memberList.size(), 5);
  }

  @Test
  void testPerfectMembers2() {
    String authorNames = "Wang, Xiaoli;Wang, Xincheng;Huang, Yu ";
    List<PubMemberDTO> xmlAuthors = new ArrayList<>();
    PubMemberDTO p1 = new PubMemberDTO();
    p1.setCommunicable(false);
    p1.setDept("Hohai University, Nanjing, China");
    p1.setEmail("017120910033@sjtu.edu.cn");
    p1.setFirstAuthor(false);
    p1.setInsNames(new ArrayList<>());
    p1.setName("Wang, Xiaoli");
    p1.setSeqNo(1);
    xmlAuthors.add(p1);

    PubMemberDTO p2 = new PubMemberDTO();
    p2.setCommunicable(true);
    p2.setDept("Shanghai Jiao Tong University, Shanghai, China");
    p2.setEmail("wxc2040@163.com");
    p2.setFirstAuthor(false);
    p2.setInsNames(new ArrayList<>());
    p2.setName("Wang, Xincheng");
    p2.setSeqNo(2);
    xmlAuthors.add(p2);

    PubMemberDTO p3 = new PubMemberDTO();
    p3.setCommunicable(false);
    p3.setDept("Tongji University, Shanghai, China");
    p3.setEmail("1428442120@qq.com");
    p3.setFirstAuthor(false);
    p3.setInsNames(new ArrayList<>());
    p3.setName("Huang, Yu");
    p3.setSeqNo(3);
    xmlAuthors.add(p3);

    List<PubMemberDTO> memberList = pubAuthorMatchServiceImpl.perfectMembers(authorNames, xmlAuthors);
    assertEquals(memberList.size(), 3);
    // 姓名
    assertEquals(memberList.get(0).getName(), "Wang Xiaoli");
    assertEquals(memberList.get(1).getName(), "Wang Xincheng");
    assertEquals(memberList.get(2).getName(), "Huang Yu");
    // 部门
    assertEquals(memberList.get(0).getDept(), "Hohai University, Nanjing, China");
    assertEquals(memberList.get(1).getDept(), "Shanghai Jiao Tong University, Shanghai, China");
    assertEquals(memberList.get(2).getDept(), "Tongji University, Shanghai, China");
    // 邮件
    assertEquals(memberList.get(0).getEmail(), "017120910033@sjtu.edu.cn");
    assertEquals(memberList.get(1).getEmail(), "wxc2040@163.com");
    assertEquals(memberList.get(2).getEmail(), "1428442120@qq.com");
    // 通讯作者
    assertEquals(memberList.get(1).isCommunicable(), true);
  }

  /**
   * 测试作者名称拆分的方法
   */
  @Test
  void testBuildByAuthorName() {
    String authorNames =
        "Hammond, Drayton A.; Smith, Melanie N.; Painter, Jacob T.; Meena, Nikhil K.; Lusardi, Katherine";
    List<PubMemberDTO> memberList = pubAuthorMatchServiceImpl.buildByAuthorName(authorNames, new ArrayList<>());
    assertEquals(memberList.size(), 5);
    assertEquals(memberList.get(0).getName(), "Hammond Drayton A.");
    assertEquals(memberList.get(0).isFirstAuthor(), true);

  }

}
