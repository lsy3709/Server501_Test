package com.busanit501.api5012.controller;

import com.busanit501.api5012.dto.*;
import com.busanit501.api5012.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/todo")
@Log4j2
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@RequestBody TodoDTO todoDTO) {
        log.info("Received Todo: {}", todoDTO);

        // 실제로 todoService.register(todoDTO)를 호출하여 저장된 tno를 반환하도록 수정
        Long tno = todoService.register(todoDTO);
        return Map.of("tno", tno);
    }

    @GetMapping("/{tno}") // 경로 수정 (불필요한 공백 제거)
    public TodoDTO read(@PathVariable("tno") Long tno) {
        log.info("read tno: {}", tno);
        return todoService.read(tno);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("Received list: {}", pageRequestDTO);
        PageResponseDTO<TodoDTO> result = todoService.list(pageRequestDTO);
        return result;
    }

    @GetMapping(value = "/list2", produces = MediaType.APPLICATION_JSON_VALUE)
    public CursorPageResponseDTO<TodoDTO> list2(CursorPageRequestDTO pageRequestDTO) {
        log.info("Received list cursor: {}", pageRequestDTO.getCursor());
        // ✅ cursor가 null일 때 첫 번째 데이터부터 조회하도록 처리
//        Long cursorValue = (cursor == null) ? todoService.getMaxTno() : cursor;
        CursorPageResponseDTO<TodoDTO> result = todoService.list2(pageRequestDTO);
        log.info("Received list cursor result: {}", result);
        return result;
    }

    @DeleteMapping(value = "/{tno}")
    public Map<String, String> delete(@PathVariable Long tno) {
        todoService.remove(tno);
        return Map.of("result", "success");
    }

    @PutMapping(value = "/{tno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> modify(@PathVariable("tno") Long tno, @RequestBody TodoDTO todoDTO) {
        // 경로 변수 tno와 요청 본문의 tno가 다를 경우를 방지하기 위해 DTO에 경로 변수 tno를 설정합니다.
        log.info("Received modify: {}", todoDTO);
        todoDTO.setTno(tno);
        todoService.modify(todoDTO);
        return Map.of("result", "success");
    }

}