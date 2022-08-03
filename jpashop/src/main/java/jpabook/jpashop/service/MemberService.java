package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
//@AllArgsConstructor           //생성자를 알아서 생성해준다.
@RequiredArgsConstructor        //파이널이 있는 필드만 가지고 생성자 주입을 해준다.
public class MemberService {

  private final MemberRepository memberRepository;

//  @Autowired        //생성자가 하나뿐이면 스프링이 자동으로 Autowired로 주입.
//  public MemberService(MemberRepository memberRepository) {
//    this.memberRepository = memberRepository;
//  }

  /**
   * 회원 가입
   */
  @Transactional
  public Long join(Member member) {
    validateDuplicateMember(member);   //중복 회원 검증
    memberRepository.save(member);
    return member.getId();
  }

  private void validateDuplicateMember(Member member) {
    //EXCEPTION
    List<Member> findMembers = memberRepository.findByName(member.getName());
    if (!findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

  //회원 전체 조회
  public List<Member> findMembers() {
    return memberRepository.findAll();
  }

  //회원 조회
  public Member findOne(Long memberId) {
    return memberRepository.findOne(memberId);
  }
}
