package com.mario6.paper.repository;

import com.mario6.paper.model.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JsonFileProjectRepositoryImplTest {

    @Resource
    private JsonFileProjectRepositoryImpl repository;

    @Test
    public void findOne() {
        String id = "invoice1";
        Project project = new Project();
        project.setId(id);
        project.setName("电子发票1");
        project.setCreator("魏远1");
        project.setDescription("电子发票, 依赖维修的开票能力1");
        repository.save(project);
        Project one = repository.findOne(id);
        assertNotNull(one);
    }

    @Test
    public void save() {
        Project project = new Project();
        project.setId("invoice");
        project.setName("电子发票");
        project.setCreator("魏远");
        project.setDescription("电子发票, 依赖维修的开票能力");

        Project p = repository.findOne("invoice");
        int one = repository.save(project);
        if(p == null) {
            assertEquals(1, one);
        } else {
            assertEquals(0, one);
        }
    }

    @Test
    public void findAll() {
        Project project = new Project();
        project.setId("invoice3");
        project.setName("电子发票");
        project.setCreator("魏远");
        project.setDescription("电子发票, 依赖维修的开票能力");
        repository.save(project);
        List<Project> projects = repository.findAll();
        assertTrue(projects.size() > 0);
    }
}