package com.tojaeung.datajpa.repository;

import com.tojaeung.datajpa.domain.Member;
import com.tojaeung.datajpa.domain.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void 레포지토리메서드_쿼리_테스트() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);

        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void 페이징과정렬_테스트() {
        // given
        memberRepository.save(new Member(("member1"), 10));
        memberRepository.save(new Member(("member2"), 10));
        memberRepository.save(new Member(("member3"), 10));
        memberRepository.save(new Member(("member4"), 10));
        memberRepository.save(new Member(("member5"), 10));

        int age = 10;

        // 페이지는 1이 아니라 0부터 시작이다.
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);   // Page, Slice(더보기, limit += 1), List(아마 무한스크롤?)

        // then
        assertThat(page.getContent().size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void 벌크수정쿼리_테스트() {
        // given
        memberRepository.save(new Member(("member1"), 10));
        memberRepository.save(new Member(("member2"), 10));
        memberRepository.save(new Member(("member3"), 20));
        memberRepository.save(new Member(("member4"), 21));
        memberRepository.save(new Member(("member5"), 40));

        /*
         * save하고 영속성컨텍스트에 캐시되어있다.
         * 그 다음 bulkAgePlus는 JPQL문이 실행된다.
         * JPA는 JPQL문이 시작되기전 영속성 컨텍스트를 flush하는 메커니즘이 있다.
         * */

        // when
        /*
         * 벌크연산은 영속성 컨텍스트를 무시하고 바로 디비로 쿼리를 보내버린다.
         * 즉, 벌크연산을 사용할때 영속성 컨텍스트 특성을 잘 고려하여서 사용해야한다.
         * 참고로, 마이바티스, JDBC를 JPA와 섞어 사용할때는 조심해야한다.
         * 왜냐하면 마이바티스, JDBC는 영속성 컨텍스트를 거치지않고 바로 디비로 가기 떄문이다
         * */
        int resultCount = memberRepository.bulkAgePlus(20);

        // em.clear(); // 이미 위에 bulkAgePlus의 JPQL이 실행되기전 save 쿼리가 flush되었다.

        Optional<Member> m5 = memberRepository.findById(5L);
        m5.ifPresent(member -> System.out.println(member.getAge()));

        // then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void EntityGraph실습() {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> members = memberRepository.findEntityGraphByusername("member1");

        // then
        for (Member member : members) {
            System.out.println(member.getUsername());
            System.out.println(member.getTeam().getClass());
            System.out.println(member.getTeam().getName());

        }
    }

    @Test
    public void 힌트_락_테스트() {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");
        em.flush();


        // then

    }

    @Test
    public void Audit_테스트() throws Exception {
        // given
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById(member.getId()).get();

        // then
        System.out.println(findMember.getCreatedDate());    // 생성날짜 자동생성
        System.out.println(findMember.getLastModifiedData());   // 수정날짜 자동생성

    }
}
