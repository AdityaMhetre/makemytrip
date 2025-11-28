package com.example.makemytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class railway {
    @GetMapping("/railway")
    public String getData() {return  "Please book railways ticket from irctc at 30% discount" ; }
}