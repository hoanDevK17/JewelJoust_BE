package online.jeweljoust.BE.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
public class TestAPI {

    @GetMapping("test")
    public ResponseEntity test(){
        return ResponseEntity.ok("ok nha");
    }

}
