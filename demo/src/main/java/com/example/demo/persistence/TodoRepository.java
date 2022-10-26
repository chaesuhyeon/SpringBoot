package com.example.demo.persistence;

import com.example.demo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

    List<TodoEntity> findByUserId(String userId);

/*    @Query("select * from TodoEntity t where t.userId = ?1") ?1은 메서드의 매개변수의 순서 위치
    List<TodoEntity> findByUserIdQuery(String userId);*/
}
