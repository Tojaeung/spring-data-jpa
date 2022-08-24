package com.tojaeung.datajpa.repository;

import com.tojaeung.datajpa.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
