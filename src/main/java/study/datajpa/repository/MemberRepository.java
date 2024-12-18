package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username,@Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.username,m.id,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username);   //컬렉션
    Member findMemberByUsername(String username);       // 단건
    Optional<Member> findOptionalMemberByUsername(String username); //단건 optional


    @Query(value = "select m from Member m left join m.team t where m.age = :age",countQuery = "select count(m.id) from Member m")
    Page<Member> findByAge(@Param("age") int age,Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m join fetch m.team t")
    List<Member> findMemberFetchJoin();

    @Query("select m from Member m join fetch m.team t")
    List<Member> findjpqlAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findDataJpa();


    @EntityGraph(attributePaths = {"team"})
    List<Member> findDataJpaByUsername(@Param("username") String username);

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph("Member.all")
    @Query("select m from Member m")
    List<Member> findNameEntity();

    // jpa hint
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly",value = "true"))
    Member findQueryhintByUsername(String username);

    // 비관적 락
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Member findLockByUsername(String username);

 }
