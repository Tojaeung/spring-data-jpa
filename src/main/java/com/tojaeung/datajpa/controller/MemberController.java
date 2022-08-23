package com.tojaeung.datajpa.controller;

import com.tojaeung.datajpa.domain.Member;
import com.tojaeung.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members")
    /*
     * pagable로 받으면 자동으로 파라미터를 읽어 pageRequest에 바인딩한다.
     * ex) members?page=0&size=5&sort=id,desc&sort=username
     * 아니면 @PageableDefault을 이용해서 기본값을 정해줄수도 잇다
     * 만약, 페이지를 1부터 시작하고 싶다면 Pagable로 안받고 직접 pageRequest를 정의해서 사용하거나....
     * indexd-parameters로 페이지를 1부터 설정할수 있지만 page가 업데이트되지 않는다.
     * */
    public Page<Member> list(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        // 쿼리의 마지막인자에 pagable을 넣으면 된다.
        return memberRepository.findAll(pageable);
    }

    @PostConstruct
    public void init() {

        for (int i = 0; i < 20; i++) {
            memberRepository.save(new Member("member" + i, i));
        }
    }

}
