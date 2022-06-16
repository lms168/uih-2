package com.uih.uihstartsecond.controller;
import cn.hutool.json.JSONUtil;


import cn.hutool.setting.yaml.YamlUtil;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("控制器管理")
@RestController
@RequestMapping("k8s")
@Slf4j
public class K8sTestController {



    public static void main(String[] args) throws IOException, ApiException {

        String kubeConfigPath = "/home/mushengli/.kube/config";

        //加载k8s,confg
        ApiClient client =
                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

        //将加载confi的client设置为默认的client
        Configuration.setDefaultApiClient(client);

        //创建一个api
        CoreV1Api api = new CoreV1Api(client);

//        V1Pod v1Pod = api.readNamespacedPodStatus("nginx-deployment-6956dcf8c-lccpx", "default", null);
//        System.out.println(JSONUtil.toJsonPrettyStr(v1Pod));


        V1PodList v1PodList = api.listNamespacedPod("ccb", null, null, null, null, "app=ccbtest", null, null, null, null, null);
        System.out.println(JSONUtil.toJsonPrettyStr(v1PodList.getItems().get(0)));
        v1PodList.getItems().stream().forEach(x->{
            System.out.println("================"+ x.getMetadata().getName()+":\t"+x.getStatus().getConditions().get(1).getStatus());
        });
        //创建空间
//        createNamespace(api);

//        AppsV1Api apiInstance = new AppsV1Api(client);
//        //发布应用
//        createDeployment(apiInstance,"ccb");

        //创建service
//        createService(api,"ccb");
//
//        //服务升级
//        applyDeployment(apiInstance,"default");


    }


    /**
     * 创建命名空间  创建namespace
     * @param api
     */
    public static void createNamespace(CoreV1Api api){
        V1Namespace body = new V1Namespace();
        Boolean includeUninitialized =null;
        String pretty = null;
        String dryRun = null;
        body.setApiVersion("v1");
        body.setKind("Namespace");
        V1ObjectMeta metadata = new V1ObjectMeta();
        metadata.setName("ccb");
        Map<String,String> labelMap = new HashMap<>();
        labelMap.put("app","ccb");
        metadata.setLabels(labelMap);
        body.setMetadata(metadata);
        try{
            V1Namespace v1Namespace = api.createNamespace(body, pretty, dryRun, null, null);
            System.out.println("创建空间"+ JSONUtil.toJsonStr(v1Namespace));
        }catch (ApiException e){
            log.error("Exception when calling CoreV1Api#createNamespace");
            log.error("Status code: {}", e.getCode());
            log.error("Reason: {}", e.getResponseBody());
            log.error("Response headers: {}", e.getResponseHeaders());
            e.printStackTrace();
        }
    }


    /**
     * 发布应用  创建Deployment
     * @param api
     */
    public static void createDeployment(AppsV1Api api,String namespace){
        try {
            V1Deployment body = new V1Deployment();
            body.apiVersion("apps/v1");
            body.setKind("Deployment");
            V1ObjectMeta metadata = new V1ObjectMeta();
            //metadata.setNamespace(namespace);
            metadata.setName("ccbtest");
            Map<String,String> labelsMap = new HashMap<>();
            labelsMap.put("app","ccbtest");
            metadata.setLabels(labelsMap);
            body.setMetadata(metadata);
            V1DeploymentSpec spec = new V1DeploymentSpec();
            spec.setReplicas(1);
            V1LabelSelector selector = new V1LabelSelector();
            Map<String,String> matchLabelsMap = new HashMap<>();
            matchLabelsMap.put("app","ccbtest");
            selector.setMatchLabels(matchLabelsMap);
            spec.setSelector(selector);

            V1PodTemplateSpec template = new V1PodTemplateSpec();
            V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
            Map<String,String> templLableMap = new HashMap<>();
            templLableMap.put("app","ccbtest");
            v1ObjectMeta.setLabels(templLableMap);

            V1PodSpec v1PodSpec = new V1PodSpec();
            List<V1Container> containers = new ArrayList<>();
            V1Container v1Container = new V1Container();
            v1Container.setName("ccbtest");
            v1Container.setImage("127.0.0.1:80/library/ccbtest:latest");
            v1Container.setImagePullPolicy("IfNotPresent");
            List<V1ContainerPort> ports = new ArrayList<>();
            V1ContainerPort v1ContainerPort = new V1ContainerPort();
            v1ContainerPort.setContainerPort(8081);
            ports.add(v1ContainerPort);
            v1Container.setPorts(ports);
            containers.add(v1Container);
            v1PodSpec.setContainers(containers);

            template.setMetadata(v1ObjectMeta);
            template.setSpec(v1PodSpec);
            spec.setTemplate(template);
            body.setSpec(spec);

            System.out.println(JSONUtil.toJsonPrettyStr(body));
            V1Deployment result = api.createNamespacedDeployment(namespace, body, null, null, null, null);


            System.out.println("发布应用"+JSONUtil.toJsonPrettyStr(result));

        } catch (ApiException e) {

            log.error("Exception when calling AppsV1Api#createNamespacedDeployment");
            log.error("Status code: {}", e.getCode());
            log.error("Reason: {}", e.getResponseBody());
            log.error("Response headers: {}", e.getResponseHeaders());
            e.printStackTrace();
        }
    }


    /**
     * 创建service
     * @param api
     * @param namespace
     */
    public static void createService(CoreV1Api api,String namespace){
        V1Service body = new V1Service();
        body.setApiVersion("v1");
        body.setKind("Service");

        V1ObjectMeta metadata = new V1ObjectMeta();
        metadata.setName("ccbtest");
        metadata.setNamespace(namespace);
        body.setMetadata(metadata);
        Map<String,String> labelMap = new HashMap<>();
        labelMap.put("app","ccbtest");
        metadata.setLabels(labelMap);
        body.setMetadata(metadata);

        V1ServiceSpec spec = new V1ServiceSpec();
        spec.setType("NodePort");
        List<V1ServicePort> v1ServicePortList = new ArrayList<>();
        V1ServicePort v1ServicePort = new V1ServicePort();
        v1ServicePort.setPort(8081);
        v1ServicePort.setNodePort(30091);
        v1ServicePortList.add(v1ServicePort);
        spec.setPorts(v1ServicePortList);

        Map<String,String> selectorMap = new HashMap<>();
        selectorMap.put("app","ccbtest");
        spec.setSelector(selectorMap);
        body.setSpec(spec);
        try{
            V1Service v1Service= api.createNamespacedService(namespace,body,null,null,null, null);
            System.out.println(JSONUtil.toJsonPrettyStr(v1Service));
        }catch (ApiException e){
            log.error("Exception when calling AppsV1Api#createNamespacedDeployment");
            log.error("Status code: {}", e.getCode());
            log.error("Reason: {}", e.getResponseBody());
            log.error("Response headers: {}", e.getResponseHeaders());
            e.printStackTrace();
        }
    }
//
//
//
//    /**
//     * 升级服务 ,将镜像升级 ，这里将镜像从ccbtest:latest换成了v2
//     * @param api
//     */
//    public static void applyDeployment(AppsV1Api api,String namespace){
//        try {
//            V1Deployment body = new V1Deployment();
//            body.apiVersion("apps/v1");
//            body.setKind("Deployment");
//            V1ObjectMeta metadata = new V1ObjectMeta();
//            //metadata.setNamespace(namespace);
//            metadata.setName("ccbtest");
//            Map<String,String> labelsMap = new HashMap<>();
//            labelsMap.put("app","ccbtest");
//            metadata.setLabels(labelsMap);
//            body.setMetadata(metadata);
//            V1DeploymentSpec spec = new V1DeploymentSpec();
//            spec.setReplicas(1);
//            V1LabelSelector selector = new V1LabelSelector();
//            Map<String,String> matchLabelsMap = new HashMap<>();
//            matchLabelsMap.put("app","ccbtest");
//            selector.setMatchLabels(matchLabelsMap);
//            spec.setSelector(selector);
//
//            V1PodTemplateSpec template = new V1PodTemplateSpec();
//            V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
//            Map<String,String> templLableMap = new HashMap<>();
//            templLableMap.put("app","ccbtest");
//            v1ObjectMeta.setLabels(templLableMap);
//
//            V1PodSpec v1PodSpec = new V1PodSpec();
//            List<V1Container> containers = new ArrayList<>();
//            V1Container v1Container = new V1Container();
//            v1Container.setName("ccbtest");
//            v1Container.setImage("127.0.0.1:80/library/ccbtest:v2");
//            v1Container.setImagePullPolicy("IfNotPresent");
//            List<V1ContainerPort> ports = new ArrayList<>();
//            V1ContainerPort v1ContainerPort = new V1ContainerPort();
//            v1ContainerPort.setContainerPort(8081);
//            ports.add(v1ContainerPort);
//            v1Container.setPorts(ports);
//            containers.add(v1Container);
//            v1PodSpec.setContainers(containers);
//
//            template.setMetadata(v1ObjectMeta);
//            template.setSpec(v1PodSpec);
//            spec.setTemplate(template);
//            body.setSpec(spec);
//
//            String name = "ccbtest";
//            System.out.println(JSONObject.toJSONString(body));
//            V1Deployment result = api.replaceNamespacedDeployment(name,namespace, body, null, null);
//
//
//            System.out.println("发布应用"+JSONObject.toJSONString(result));
//
//        } catch (ApiException e) {
//
//            log.error("Exception when calling AppsV1Api#createNamespacedDeployment");
//            log.error("Status code: {}", e.getCode());
//            log.error("Reason: {}", e.getResponseBody());
//            log.error("Response headers: {}", e.getResponseHeaders());
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 查询节点
//     * @param api
//     */
//    public static void getPodList(CoreV1Api api){
//        try{
//            //打印所有的pod
//            V1PodList list = api.listNamespacedPod("default", null, null, null, null, null, null, null, null,null);
//
//            for (V1Pod item : list.getItems()) {
//                System.out.println(JSONUtil.toJsonStr(item.getMetadata().getName()));
//            }
//
//            AppsV1Api appsV1Api = new AppsV1Api();
//            V1DeploymentList v1DeploymentList = appsV1Api.listDeploymentForAllNamespaces(null, null, null, null, null, null, null, null,null);
//            List<V1Deployment> v1DeploymentListItems = v1DeploymentList.getItems();
//            for(V1Deployment v1Deployment:v1DeploymentListItems){
//                System.out.println(JSONUtil.toJsonStr(v1Deployment.getMetadata().getName()));
//            }
//        }catch (ApiException e){
//            e.printStackTrace();
//        }
//
//    }
}