package com.tojaeung.datajpa.repository;

import com.tojaeung.datajpa.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /*
     * 레파지토리 메서드에 쿼리 정의해서 사용하기
     * 어플실행시 미리 sql로 파싱해놓기 때문에 런타임이전에 에러를 파악할 수 있는 장점
     * */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    /*
     * 쉽게하는 스프링 데이터 jpa의 페이징과 정렬
     * 조인이 되는 상태에서는 카운트 쿼리를 별도로 최적화해서 보내준다.
     * 왜냐하면, 데이터 갯수만 세면 되는데 굳이... 조인을 해서 카운트를 하기 때문에 비효율적이다.
     * */
    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
}
