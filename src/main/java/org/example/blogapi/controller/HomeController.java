package org.example.blogapi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/hello")
    public String hello(){
        return "Blog Api";
    }

    @GetMapping("/hello-secured")
    public String helloSecured(
        Authentication authentication
    ){
        String userName = authentication.getName();

        return "Welcome " + userName + " to Blog Api";
    }

}
