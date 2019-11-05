package org.test.records.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;


@Slf4j
@RestController
@Api(tags = {"登录"}, description = "登录")
@ApiIgnore
public class LoginController {

    @GetMapping("/")
    public ModelAndView loginOpen() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");
        return mv;
    }

}