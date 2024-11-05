package study.datajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Test   // 순수 JPA AUDITING
    public void JpaEventBaseEntity() throws Exception {
        Member member = new Member("user1");
        memberRepository.save(member);

        Thread.sleep(100);

        member.setUsername("user2");
        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).get();

//        System.out.println("findMember.getCreateTime() = " + findMember.getCreateTime());
//        System.out.println("findMember.getModifiedTime() = " + findMember.getModifiedTime());

    }

    @Test   // 스프링데이터 JPA AUDITING
    public void eventBaseEntity() throws Exception {
        Member member = new Member("user1");
        memberRepository.save(member);

        Thread.sleep(100);

        member.setUsername("user2");
        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).get();

        System.out.println("findMember.getCreateTime() = " + findMember.getCreatedDate());
        System.out.println("findMember.getModifiedTime() = " + findMember.getLastModifiedDate());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());

    }
}