package com.uih.uihstartsecond.controller;

import com.uih.uihstartsecond.bean.CourseBean;
import com.uih.uihstartsecond.service.CourseService;
import io.kubernetes.client.openapi.models.V1Pod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {




    @Autowired
    private CourseService courseService;


    @GetMapping("/list")
    @ResponseBody
    public List<String> queryAllStudy(){
        return courseService.queryCourse();
    }

    @GetMapping
    @ResponseBody
    public CourseBean createCourseInstance(String name, Integer port) throws InterruptedException {
        return courseService.createCourseInstance(name, port);
    }

    @GetMapping("/check")
    @ResponseBody
    public Boolean checkPodStatus(Integer port){
        return courseService.checkPodStatus(port);
    }


    @GetMapping("/pod")
    @ResponseBody
    public V1Pod getPod(String name){
        return courseService.getV1Pod(name);
    }



}

