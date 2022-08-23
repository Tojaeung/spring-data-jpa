package com.tojaeung.datajpa.repository;

import com.tojaeung.datajpa.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    /*
     * @modifying 붙엳야함
     * 벌크수정된 row 수를 반환한다.
     * clearAutomatically를 사용하면 벌크수정쿼리가 실행된 후 자동으로 영속성컨텍스트를 clear해준다.
     * */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /*
     * 엔티티그래프 사용으로 페치조인을 간단히 할수 있다.
     * 단, 간단한 쿼리에는 엔티티그래프를 사용, 복잡한 쿼리는 JPQL을 사용하길 권장
     * */
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByusername(@Param("username") String username);
}
