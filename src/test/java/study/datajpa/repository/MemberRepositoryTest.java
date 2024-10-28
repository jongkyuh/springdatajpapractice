package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void test(){
        Member member = new Member("AAA",10);
        memberRepository.save(member);
        Member member1 = memberRepository.findById(member.getId()).get();
        Assertions.assertThat(member1).isEqualTo(member);
    }

    @Test
    public void findUsernameList(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }

    }

    @Test
    public void findMemberDto(){

        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA", 10);
        member1.setTeam(team);
        memberRepository.save(member1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }


    }

    @Test
    public void findByNames(){

        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("BBB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }


    }

    @Test
    public void returnType(){
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        Optional<Member> findMember = memberRepository.findOptionalMemberByUsername("AAA");
        System.out.println("findMember = " + findMember);


    }

    @Test
    public void paging(){

        //given
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        int age = 10;

        PageRequest pageRequest = PageRequest.of(0,3,Sort.by(Sort.Direction.DESC,"username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> map = page.map(m -> new MemberDto(m.getUsername(), m.getId(), null));

        //then
        List<Member> content = page.getContent();   //조회된 데이터
        Assertions.assertThat(content.size()).isEqualTo(3); // 조회된 데이터 수
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);    // 전체 데이터 수
        Assertions.assertThat(page.getNumber()).isEqualTo(0);   // 페이지 번호
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 번호
        Assertions.assertThat(page.isFirst()).isTrue();     // 첫번째 항목인가?
        Assertions.assertThat(page.hasNext()).isTrue();     // 다음 페이지가 있는가?



    }

}