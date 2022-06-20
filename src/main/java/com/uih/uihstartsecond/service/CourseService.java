package com.uih.uihstartsecond.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.uih.uihstartsecond.bean.CourseBean;
import io.kubernetes.client.openapi.models.V1Pod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CourseService {

    @Autowired
    private K8sService k8sService;

    @Value("${k8s-host}")
    private String k8sHost;

    @Value("${k8s-study-namespace}")
    private String k8sStudyNamespace;

    public List<String> queryCourse(){
//        List<CourseBean> studyBeanList = new ArrayList<>();
//        studyBeanList.add(new CourseBean(1, "mysql",2,"http://192.168.49.1:8080/?arg=my-mysql-study1-79fcdb7d47-hkzb7&arg=/bin/bash"));
//        studyBeanList.add(new CourseBean(2, "nginx",1,"http://192.168.49.1:8080/?arg=nginx-9100&arg=/bin/bash"));
        List<String> courseList = new ArrayList<>();
        courseList.add("mysql");
        courseList.add("nginx");
        return  courseList;
    }

    public CourseBean createCourseInstance(String name, Integer port) throws InterruptedException {
        CourseBean courseBean = new CourseBean();
        boolean used = false;
        if (port ==null){
            do {
                port = RandomUtil.randomInt(2000,65534);
                used = telnet(k8sHost, port, 3000);
            }while (used);
        }

        if (used){
            return courseBean;
        }

        String podName = null;
        switch (name){
            case "nginx":
                podName = k8sService.createNginxPod(k8sStudyNamespace,port);
                courseBean.setType(1);
                break;
            case "mysql":
                podName = k8sService.createMysqlPod(k8sStudyNamespace,port);
                courseBean.setType(2);
                break;
        }

        courseBean.setPort(port);
        courseBean.setPodName(podName);
        return courseBean;
    };


    public boolean checkPodStatus(Integer port){
        return telnet(k8sHost, port, 3000);
    }



    public V1Pod getV1Pod(String podName){
        return k8sService.getPodList(k8sStudyNamespace,podName);
    }



    private  boolean telnet(String hostname, int port, int timeout){
        Socket socket = new Socket();
        boolean isConnected = false;
        try {
            socket.connect(new InetSocketAddress(hostname, port), timeout); // 建立连接
            isConnected = socket.isConnected(); // 通过现有方法查看连通状态
//            System.out.println(isConnected);    // true为连通
        } catch (IOException e) {
            System.out.println("false");        // 当连不通时，直接抛异常，异常捕获即可
        }finally{
            try {
                socket.close();   // 关闭连接
            } catch (IOException e) {
                System.out.println("false");
            }
        }
        return isConnected;
    }

//    public  boolean isSocketAliveUitlitybyCrunchify(String hostName, int port) {
//        boolean isAlive = false;
//
//        // 创建一个套接字
//        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
//        Socket socket = new Socket();
//
//        // 超时设置，单位毫秒
//        int timeout = 2000;
//
//        log("hostName: " + hostName + ", port: " + port);
//        try {
//            socket.connect(socketAddress, timeout);
//            socket.close();
//            isAlive = true;
//
//        } catch (SocketTimeoutException exception) {
//            System.out.println("SocketTimeoutException " + hostName + ":" + port + ". " + exception.getMessage());
//        } catch (IOException exception) {
//            System.out.println(
//                    "IOException - Unable to connect to " + hostName + ":" + port + ". " + exception.getMessage());
//        }
//        return isAlive;
//    }
////
//    private  void log(String string) {
//        System.out.println(string);
//    }
//
//    private  void log(boolean isAlive) {
//        System.out.println("是否真正在使用: " + isAlive + "\n");
//    }




}
