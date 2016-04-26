package com.dessert.test.controller;

import com.dessert.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * Created by ding-Admin on 2016/4/21.
 */

@Controller
@RequestMapping("test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping(value = "/getmap.action")
    public String getmap(HttpServletRequest request) {
        Map<String, Object> testMap = testService.getMap();
        request.setAttribute("testMap", testMap);
        return "/test/testMap";
    }

    @RequestMapping(value = "/getmapError.action")
    public String getmapError(HttpServletRequest request) throws Exception {
        Map<String, Object> testMap = testService.getMap();
        request.setAttribute("testMap", testMap);

        return "/test/testMap";
    }

}
