package com.tojaeung.datajpa.repository;

import com.tojaeung.datajpa.domain.Member;
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
}
