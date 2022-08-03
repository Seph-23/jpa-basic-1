package jpabook.jpashop.service;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)      //Junit 실행할때 스프링이랑 같이 엮어서 실행할래.
@SpringBootTest                   //스프링 부트 띄운채로 테스트 할래.
@Transactional                    //트랜잭션 걸고 테스트 후 모두 롤백.
public class MemberServiceTest {

  @Autowired MemberService memberService;
  @Autowired MemberRepository memberRepository;
  @Autowired EntityManager em;

  @Test
  public void 회원가입() throws Exception {
    //given
    Member member = new Member();
    member.setName("lyu");

    //when
    Long savedId = memberService.join(member);

    //then
    assertEquals(member, memberRepository.findOne(savedId));
  }

  @Test(expected = IllegalStateException.class)
  public void 중복_회원_예외() throws Exception {
    //given
    Member member1 = new Member();
    member1.setName("lyu");

    Member member2 = new Member();
    member2.setName("lyu");

    //when
    memberService.join(member1);
    memberService.join(member2);
    //then
    fail("예외가 발생해야 한다.");
  }
}