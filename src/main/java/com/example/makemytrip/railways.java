package com.example.makemytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class railways {
    @GetMapping("/railways")
    public String getData() {return  "Please book railways ticket from makemytrip at 10% discount" ; }
}