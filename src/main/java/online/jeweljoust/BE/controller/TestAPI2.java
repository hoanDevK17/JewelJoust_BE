package online.jeweljoust.BE.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
public class TestAPI2 {
    @GetMapping("TestAPI2")
    public ResponseEntity API2(){
        return ResponseEntity.ok("TestAPI211");
    }
    @PostMapping("PostTestAPI")
    public ResponseEntity PostTestAPI(){
                return ResponseEntity.ok("oke");
    }
}
