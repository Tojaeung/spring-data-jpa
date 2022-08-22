package com.tojaeung.datajpa.repository;

import com.tojaeung.datajpa.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

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
}