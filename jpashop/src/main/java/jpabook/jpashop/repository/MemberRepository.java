package jpabook.jpashop.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

  private final EntityManager em;

  public void save(Member member) {
    em.persist(member);
  }

  public Member findOne(Long id) {
    return em.find(Member.class, id);
  }

  public List<Member> findAll() {                   //전체 회원을 조회
    return em.createQuery("select m from Member m", Member.class)
      .getResultList();
  }

  public List<Member> findByName(String name) {     //회원을 이름으로 조회
    return em.createQuery("select m from Member m where m.name = :name", Member.class)
      .setParameter("name", name)
      .getResultList();
  }
}
