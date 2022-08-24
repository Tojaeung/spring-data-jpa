package com.tojaeung.datajpa.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity implements Persistable<String> {

    /*
     * 아래와 같이 @genaeratedValule를 사용하지 않고 직접 PK를 넣어줄때 save메서드는 비효율적으로 작동한다.
     * 이미 PK가 존재하기 때문에 이미 존재하는 데이터로 인식하여 merge가 호출되고 디비에 SELECT쿼리를 날린다.
     * 하지만, 새로운 데이터이기 때문에 디비에 데이터가 존재하지 않는다. 그제서야 jpa는 새로운데이터라고 인지하게 된다.
     * 그러면 새로운 데이터를 save하는데도 SELECT 쿼리와 INSERT쿼리 2개가 나가게되어 비효율적이다.
     * */
    @Id
    private String id;


    public Item(String id) {
        this.id = id;
    }

    // 위의 문제를 persistable로 임의로 조정하여 해결할수 있다.
    @Override
    public boolean isNew() {
        /*
         * @created를 이용해서 새로운 데이터인지 판단 유무에 도움을 줄수도 있지만..
         * 데이터 변경을 merge로 호출하는것보다 변경감지를 활용하는게 더 좋기 떄문에
         * 나는 save메서드를 insert용으로 사용하기 위해 무조건 true를 리턴하였다.
         * */
        return true;
    }
}
