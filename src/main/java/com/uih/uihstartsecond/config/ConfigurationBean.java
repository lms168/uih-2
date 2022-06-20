package com.uih.uihstartsecond.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;


@Slf4j
@Component
public class ConfigurationBean {
    @Bean
    public CoreV1Api coreV1Api(){
        try {
            String kubeConfigPath = "/home/mushengli/.kube/config";

            //加载k8s,confg
            ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
            //将加载confi的client设置为默认的client
            Configuration.setDefaultApiClient(client);
            //创建一个api
            return new CoreV1Api(client);
        } catch (IOException e) {
            log.error("create k8s api failed",e);
            return null;
        }
    }
}
