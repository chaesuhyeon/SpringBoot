package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.entity.Memo;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> { // JpaRepository<Entity타입 정보, @Id타입>

    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from , Long to); // 쿼리 메서드 : 메서드 이름 자체가 쿼리문이 되는 것

    Page<Memo> findByMnoBetween(Long from, Long to , Pageable pageable);

    void deleteMemoByMnoLessThan(Long num);

}
