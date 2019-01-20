package com.mario6.paper.core.config;

import com.mario6.paper.common.FileUtils;
import com.mario6.paper.core.PaperProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.io.File;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Resource
    PaperProperties properties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String workDir = properties.getWorkDir();
        FileUtils.makeParent(workDir);
        File dir = new File(workDir);
        String path = dir.getAbsolutePath();
        String url = "/v/**";
        String location = "file:" + path + File.separator + PaperProperties.PROJECTS_DIR_NAME + File.separator;
        registry.addResourceHandler(url).addResourceLocations(location);
        super.addResourceHandlers(registry);
    }
}
