package com.example.makemytrip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class railway {
    @GetMapping("/railway")
    public String getData() {return  "Please book railways ticket from local at 30% discount" ; }
    public String getData() {return  "Please book railways ticket from local1 at 30% discount" ; }
}