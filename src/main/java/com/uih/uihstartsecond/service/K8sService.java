package com.uih.uihstartsecond.service;

import cn.hutool.json.JSONUtil;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class K8sService {

    @Autowired
    private CoreV1Api coreV1Api;

    /**
     *     */
    public String createNginxPod(String namespace, Integer port){
        try {
            String imageName= "nginx";
            String name = "nginx-"+port;

            V1Pod body = new V1Pod();
            body.apiVersion("v1");
            body.setKind("Pod");
            V1ObjectMeta metadata = new V1ObjectMeta();
            metadata.setNamespace(namespace);
            metadata.setName(name);
            Map<String,String> labelsMap = new HashMap<>();
            labelsMap.put("app",name);
            metadata.setLabels(labelsMap);
            body.setMetadata(metadata);

            V1PodSpec v1PodSpec = new V1PodSpec();
            List<V1Container> containers = new ArrayList<>();
            V1Container v1Container = new V1Container();
            v1Container.setName(name);
            v1Container.setImage(imageName);
            v1Container.setImagePullPolicy("IfNotPresent");
            List<V1ContainerPort> ports = new ArrayList<>();
            V1ContainerPort v1ContainerPort = new V1ContainerPort();
            v1ContainerPort.setContainerPort(80);
            //映射到node主机的端口
            v1ContainerPort.setHostPort(port);
            ports.add(v1ContainerPort);
            v1Container.setPorts(ports);
            containers.add(v1Container);
            v1PodSpec.setContainers(containers);
            body.setSpec(v1PodSpec);
//            System.out.println(JSONUtil.toJsonPrettyStr(body));
            V1Pod result = coreV1Api.createNamespacedPod(namespace, body, null, null, null, null);
//            System.out.println("发布应用"+JSONUtil.toJsonPrettyStr(result));
            return name;
        } catch (ApiException e) {
            log.error("Exception when calling AppsV1Api#createNamespacedDeployment");
            log.error("Status code: {}", e.getCode());
            log.error("Reason: {}", e.getResponseBody());
            log.error("Response headers: {}", e.getResponseHeaders());
            e.printStackTrace();
        }

        return null;

    }

    public String createMysqlPod(String namespace, Integer port) {
        try {
            String imageName= "mysql:8.0.28";
            String name = "mysql-"+port;

            V1Pod body = new V1Pod();
            body.apiVersion("v1");
            body.setKind("Pod");
            V1ObjectMeta metadata = new V1ObjectMeta();
            metadata.setNamespace(namespace);
            metadata.setName(name);
            Map<String,String> labelsMap = new HashMap<>();
            labelsMap.put("app",name);
            metadata.setLabels(labelsMap);
            body.setMetadata(metadata);

            V1PodSpec v1PodSpec = new V1PodSpec();

            List<V1Container> containers = new ArrayList<>();
            V1Container v1Container = new V1Container();

            V1EnvVar v1EnvVar0 = new V1EnvVar();
            v1EnvVar0.setName("MYSQL_ROOT_PASSWORD");
            v1EnvVar0.setValue("root");

            V1EnvVar v1EnvVar1 = new V1EnvVar();
            v1EnvVar1.setName("MYSQL_USER");
            v1EnvVar1.setValue("test");


            V1EnvVar v1EnvVar2 = new V1EnvVar();
            v1EnvVar2.setName("MYSQL_PASSWORD");
            v1EnvVar2.setValue("test");

            v1Container.addEnvItem(v1EnvVar0);
            v1Container.addEnvItem(v1EnvVar1);
            v1Container.addEnvItem(v1EnvVar2);

            v1Container.setName(name);
            v1Container.setImage(imageName);
            v1Container.setImagePullPolicy("IfNotPresent");
            List<V1ContainerPort> ports = new ArrayList<>();
            V1ContainerPort v1ContainerPort = new V1ContainerPort();
            v1ContainerPort.setContainerPort(3306);
            v1ContainerPort.setProtocol("TCP");
            //映射到node主机的端口
            v1ContainerPort.setHostPort(port);
            ports.add(v1ContainerPort);
            v1Container.setPorts(ports);
            containers.add(v1Container);
            v1PodSpec.setContainers(containers);
            body.setSpec(v1PodSpec);
//            System.out.println(JSONUtil.toJsonPrettyStr(body));
            V1Pod result = coreV1Api.createNamespacedPod(namespace, body, null, null, null, null);
//            System.out.println("发布应用"+JSONUtil.toJsonPrettyStr(result));
            return name;
        } catch (ApiException e) {
            log.error("Exception when calling AppsV1Api#createNamespacedDeployment");
            log.error("Status code: {}", e.getCode());
            log.error("Reason: {}", e.getResponseBody());
            log.error("Response headers: {}", e.getResponseHeaders());
            e.printStackTrace();
        }

        return null;


    }



    /**
     * 查询节点
     * @param
     */
    public  V1Pod getPodList(String namespace, String name){
        try{

            V1Pod v1Pod = coreV1Api.readNamespacedPod(name, namespace, null);

            return v1Pod;

        }catch (ApiException e){
            e.printStackTrace();
        }
        return null;

    }



}
