package com.web.controller;

import com.web.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping({"", "/"})
    public String board(@RequestParam(value = "idx" , defaultValue = "0") Long idx , Model model){
        model.addAttribute("board" , boardService.findBoardByIdx(idx));
        return "/board/form";
    }

    @GetMapping("/list")
    public String list(@PageableDefault Pageable pageable, Model model){ // @PageableDefault : Pageable 쓸 때 size, sort, direction 등을 사용하여 페이징 처리에 대한 규약을 정의할 수 있음 -> Default : 기본으로 사용
        model.addAttribute("boardList", boardService.findBoardList(pageable));
        return "/board/list";
    }
}
