package com.mario6.paper.service;

import static com.mario6.paper.common.AssertUtils.checkParam;
import static com.mario6.paper.common.AssertUtils.checkParamNotNull;

import com.mario6.paper.model.Project;
import com.mario6.paper.model.ProjectVersion;
import com.mario6.paper.repository.ProjectRepository;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * ProjectService
 *
 * @author Mario Luo
 * @date 2019.01.19 10:51
 */
@Service
public class ProjectService {

    @Resource
    private ProjectRepository repository;

    /**
     * 添加项目
     * @param project
     * @return
     */
    public String saveProject(Project project) {
        String id = project.getId();
        if(StringUtils.isNotEmpty(id)) {
            Project p = repository.findOne(id);
            checkParam(p==null, "项目标示已存在");
        } else {
            id = RandomStringUtils
                .random(6, "ABCDEFGHIJQMNOPQRSTUVWXYZ0123456789");
        }
        repository.save(project);
        return id;
    }

    /**
     * 获得所有项目
     * @return
     */
    public List<Project> getProjects() {
        return repository.findAll();
    }

    /**
     * 获得单个项目
     * @param id
     * @return
     */
    public Project getProject(String id) {
        return repository.findOne(id);
    }

    /**
     * 保存一个一个项目版本
     * @param projectVersion
     * @param file
     */
    public void saveProjectVersion(ProjectVersion projectVersion, MultipartFile file) {
        Project project = checkSaveProjectVersionParams(projectVersion);
        // TODO: 文件存储
        String id = projectVersion.getId();
        if(StringUtils.isEmpty(id)) {
            List<ProjectVersion> versions = project.getVersions();
            if(versions == null || versions.size() == 0) {
                id = "v1";
            } else {
                ProjectVersion pversion = versions.get(0);
                String lastId = pversion.getId();
                id = "v1.1";
            }
            projectVersion.setId(id);
        }
        repository.saveProjectVersion(projectVersion);
    }

    private Project checkSaveProjectVersionParams(ProjectVersion projectVersion) {
        String projectId = projectVersion.getProjectId();
        checkParam(StringUtils.isNotEmpty(projectId), "参数projectId不能为空");
        projectId = StringUtils.trim(projectId);
        Project project = repository.findOne(projectId);
        checkParamNotNull(project, "项目不存在");
        return project;
    }

    /**
     * 获取存储文件目录
     * @param id
     * @param version
     * @return
     */
    public String getProjectFilePath(String id, String version) {
        return null;
    }

    private String getProjectDir(String projectId, String version) {
        return null;
    }
}
