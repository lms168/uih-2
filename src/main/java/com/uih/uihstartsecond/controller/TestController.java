package com.uih.uihstartsecond.controller;

import com.uih.uihstartsecond.bean.City;
import com.uih.uihstartsecond.bean.StudyBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {
    @GetMapping("/list/{id}")
    public String getOne(@PathVariable("id") Integer id, ModelMap map) {
        City city = new City(1, "");
        map.addAttribute("city", city);
        //System.out.println("hell  jsp");
        return "list";
    }

    @GetMapping("/list")
    @ResponseBody
    public List<StudyBean> queryAllStudy(){
        List<StudyBean> studyBeanList = new ArrayList<>();
        studyBeanList.add(new StudyBean("mysql",2,"http://192.168.49.1:8080/?arg=my-mysql-study1-79fcdb7d47-hkzb7&arg=/bin/bash"));
        studyBeanList.add(new StudyBean("nginx",1,"http://192.168.49.1:8080/?arg=nginx-9100&arg=/bin/bash"));
        return studyBeanList;
    }

}

