package com.tojaeung.datajpa.repository;

import com.tojaeung.datajpa.domain.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void save메서드_테스트() {
        /*
         * save메서드는 식별자 있다면 존재하는 데이터로 인식
         * 식별자가 존재한다면 persist 시키지 않고 디비에 식별자를 조회해서 정말 존재하는 데이터인지 확인한다. (select 쿼리발생)
         * 존재하는 데이터가 있다면 merge를 발생시킨다. (merge는 업데이트에 사용하기 부적절하다. 변경감지를 사용하자)
         * 존재하지 않는 데아터라면 persist를 해준다 (비효율적)
         * */
        Item item = new Item("A");
        itemRepository.save(item);
    }
}
