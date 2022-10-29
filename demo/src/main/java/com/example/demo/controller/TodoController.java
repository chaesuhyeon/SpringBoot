package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo(){

        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);

        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){

        try {
//            String temporaryUserId = "temporary-user";

            //(1) TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            //(2) id를 null로 초기화한다. 생성 당시에는 id가 없어야하기 때문이다.
            entity.setId(null);

            //(3) Authentication Bearer Token을 통해 받은 userId를 넘긴다.
            entity.setUserId(userId);

            //(4) 서비스를 이용해 Todo 엔티티를 생성한다.
            List<TodoEntity> entities = service.create(entity);

            //(5) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            //(6) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            //(7) ResponseEntity를 리턴한다.
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            //(8) 예외가 나는 경우 dto 대신 error에 메시지를 넣어 리턴

            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){
//        String temporaryUserId = "temporary-user";

        //(1) 서비스 메서드의 retrieve 메서드를 사용해 Todo 리스트를 가져온다
        List<TodoEntity> entities = service.retrieve(userId);

        //(2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        //(3) 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        //(4) ResponseEntity를 리턴한다.
        return ResponseEntity.ok().body(response);

    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){

//        String temporaryUserId = "temporary-user";

        //dto를 entity로 변환
        TodoEntity entity = TodoDTO.toEntity(dto);


        //Authentication Bearer Token을 통해 받은 userId를 넘긴다.
        entity.setUserId(userId);

        //서비스를 이용해 entity를 업데이트
        List<TodoEntity> entities = service.update(entity);

        //자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        //변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        //ResponseEntity를 리턴한다.
        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){

        try {
//            String temporaryUserId = "temporary-user";

            //TodoEntity로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            //Authentication Bearer Token을 통해 받은 userId를 넘긴다.
            entity.setUserId(userId);

            //서비스를 이용해 entity 삭제
            List<TodoEntity> entities = service.delete(entity);

            //자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            //변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        } catch (Exception e){

            //혹시 예외가 나는 경우 dto 대신 error에 메시지를 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }








}
