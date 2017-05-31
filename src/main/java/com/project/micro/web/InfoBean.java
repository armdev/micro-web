package com.project.micro.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.micro.model.Post;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import org.apache.http.HttpEntity;
import org.apache.http.client.cache.CacheResponseStatus;
import org.apache.http.client.cache.HttpCacheContext;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author armdev
 */
@Named(value = "infoBean")
@SessionScoped
public class InfoBean implements Serializable{

    private static final long serialVersionUID = 1L;

    private CloseableHttpClient cachingClient;

    public InfoBean() {
    }

    @PostConstruct
    public void init() {
        CacheConfig cacheConfig = CacheConfig.custom()
                .setMaxCacheEntries(1000)
                .setMaxObjectSize(8192)
                .build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setSocketTimeout(30000)
                .build();
        cachingClient = CachingHttpClients.custom()
                .setCacheConfig(cacheConfig)
                .setDefaultRequestConfig(requestConfig)
                .build();

    }

    @SuppressWarnings("unchecked")
    public List<Post> getAllPosts() {
        System.out.println("New request sent getter");
        List<Post> model = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("https://jsonplaceholder.typicode.com/posts");
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                HttpEntity entity = httpResponse.getEntity();
                ObjectMapper mapper = new ObjectMapper();
                model = mapper.readValue(EntityUtils.toString(entity), List.class);
            }
        } catch (IOException e) {
        }
        return model;

    }

    public String findAllPosts() {
        String result = null;
        HttpCacheContext context = HttpCacheContext.create();
        HttpGet httpget = new HttpGet("https://jsonplaceholder.typicode.com/posts");
        try {
            try (CloseableHttpResponse response = cachingClient.execute(httpget, context)) {
                CacheResponseStatus responseStatus = context.getCacheResponseStatus();
                switch (responseStatus) {
                    case CACHE_HIT:
                        System.out.println("A response was generated from the cache with "
                                + "no requests sent upstream");
                        result = EntityUtils.toString(response.getEntity());
                        break;
                    case CACHE_MODULE_RESPONSE:
                        System.out.println("The response was generated directly by the "
                                + "caching module");
                        result = EntityUtils.toString(response.getEntity());

                        break;
                    case CACHE_MISS:
                        System.out.println("The response came from an upstream server");
                        result = EntityUtils.toString(response.getEntity());
                        break;
                    case VALIDATED:
                        System.out.println("The response was generated from the cache "
                                + "after validating the entry with the origin server");
                        result = EntityUtils.toString(response.getEntity());
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(InfoBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;

    }

}
