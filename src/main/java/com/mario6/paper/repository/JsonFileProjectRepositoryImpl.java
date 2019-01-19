package com.mario6.paper.repository;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mario6.paper.config.PaperProperties;
import com.mario6.paper.model.Project;
import com.mario6.paper.model.ProjectDataWrapper;
import com.mario6.paper.model.ProjectVersion;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * JsonFileProjectRepositoryImpl
 *
 * @author Mario Luo
 * @date 2019.01.19 11:53
 */
@Component
public class JsonFileProjectRepositoryImpl implements ProjectRepository {

    @Resource
    private PaperProperties configuration;

    @Override
    public Project findOne(String id) {
        ProjectDataWrapper data = readData();
        return doGetProjectById(data, id);
    }

    @Override
    public int save(Project project) {
        ProjectDataWrapper data = readData();
        List<Project> projects = data.getProjects();
        Project orgProject = doGetProjectById(data, project.getId());
        if(orgProject != null) {
            return 0;
        }
        projects.add(0, project);
        writeData(data);
        return 1;
    }

    @Override
    public List<Project> findAll() {
        ProjectDataWrapper data = readData();
        return data.getProjects();
    }

    @Override
    public int delete(String id) {
        ProjectDataWrapper data = readData();
        List<Project> projects = data.getProjects();

        int count = 0;
        Iterator<Project> it = projects.iterator();
        while(it.hasNext()) {
            Project one = it.next();
            String pid = one.getId();
            if(StringUtils.equals(id, pid)) {
                it.remove();
                count ++;
            }
        }
        return count;
    }

    @Override
    public int saveProjectVersion(ProjectVersion projectVersion) {
        String pid = projectVersion.getProjectId();
        ProjectDataWrapper data = readData();
        Project project = doGetProjectById(data, pid);
        if(project == null) return 0;
        String vid = projectVersion.getId();
        ProjectVersion orgVersion = doGetVersionById(project, vid);
        long updateTime = System.currentTimeMillis();
        if(orgVersion != null) {
            // 修改
            orgVersion.setUpdateTime(updateTime);
            orgVersion.setDescription(projectVersion.getDescription());
        } else {
            // 添加
            ProjectVersion newVersion = new ProjectVersion();
            newVersion.setId(projectVersion.getId());
            newVersion.setProjectId(projectVersion.getProjectId());
            newVersion.setCreateTime(updateTime);
            newVersion.setDescription(projectVersion.getDescription());
            newVersion.setUpdateTime(updateTime);
            List<ProjectVersion> versions = project.getVersions();
            if(versions == null) {
                versions = new ArrayList<>();
                project.setVersions(versions);
            }
            versions.add(0, newVersion);
        }
        project.setUpdateTime(updateTime);
        writeData(data);
        return 1;
    }

    private Project doGetProjectById(ProjectDataWrapper data, String id) {
        List<Project> projects = data.getProjects();
        for(Project one: projects) {
            if(one == null) continue;
            if(StringUtils.equals(id, one.getId())) {
                return one;
            }
        }
        return null;
    }

    private ProjectVersion doGetVersionById(Project project, String id) {
        List<ProjectVersion> versions = project.getVersions();
        if(versions == null) return null;
        for(ProjectVersion one: versions) {
            if(one == null) continue;
            if(StringUtils.equals(id, one.getId())) {
                return one;
            }
        }
        return null;
    }

    private void writeData(ProjectDataWrapper data) {
        File file = getProjectMetaJsonFile();
        doWriteJsonFile(file, data);
    }

    private ProjectDataWrapper readData() {
        File file = getProjectMetaJsonFile();
        try (Reader reader = new FileReader(file)) {
            JSONReader jsonReader = new JSONReader(reader);
            ProjectDataWrapper data = jsonReader.readObject(ProjectDataWrapper.class);
            if(data.getProjects() == null) {
                data.setProjects(new ArrayList<Project>());
            }
            return data;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getProjectMetaJsonFile() {
        String workDir = configuration.getWorkDir();
        String path = workDir + File.separator + PaperProperties.PROJECT_META_FILE_NAME;
        File file = new File(path);
        if(!file.exists()) {
            File parentFile = file.getParentFile();
            if(!parentFile.exists()) {
                parentFile.mkdirs();
            }
            ProjectDataWrapper data = new ProjectDataWrapper();
            data.setProjects(new ArrayList<Project>());
            doWriteJsonFile(file, data);
        }
        return file;
    }

    private void doWriteJsonFile(File file, ProjectDataWrapper data) {
        try(FileWriter writer = new FileWriter(file)) {
            JSONObject.writeJSONString(writer, data,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.PrettyFormat,
                SerializerFeature.SortField);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
