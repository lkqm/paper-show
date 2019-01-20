package com.mario6.paper.service;

import com.mario6.paper.common.ArchiveUtilsExt;
import com.mario6.paper.common.FileUtils;
import com.mario6.paper.config.PaperProperties;
import com.mario6.paper.model.Project;
import com.mario6.paper.repository.ProjectRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.mario6.paper.common.AssertUtils.checkParam;
import static com.mario6.paper.common.AssertUtils.checkParamNotNull;

/**
 * ProjectService
 *
 * @author Mario Luo
 * @date 2019.01.19 10:51
 */
@Service
public class ProjectService {

    @Resource
    private PaperProperties config;

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
            id = RandomStringUtils.random(6, "ABCDEFGHIJQMNOPQRSTUVWXYZ0123456789");
            project.setId(id);
        }
        long time = System.currentTimeMillis();
        project.setCreateTime(time);
        project.setUpdateTime(time);
        project.setUploaded(false);
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
     * @param params
     * @param file
     */
    public void saveProjectFile(Project params, MultipartFile file) {
        String projectId = params.getId();
        Project project = checkSaveProjectFileParams(projectId, file);

        String workDir = config.getWorkDir();
        String targetFilePath = workDir + File.separator + "upload" + File.separator + project.getId() + '-' + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String projectPath = workDir + File.separator + "projects" + File.separator + project.getId() + "/";
        FileUtils.makeParent(targetFilePath);
        File targetFile = new File(targetFilePath);
        try {
            file.transferTo(targetFile);
            FileUtils.emptyDir(projectPath);
            File projectFile = new File(projectPath);
            if (!projectFile.exists()) {
                projectFile.mkdirs();
            }
            ArchiveUtilsExt.unZip(targetFilePath, projectPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        project.setUploaded(true);
        project.setEntranceUri(params.getEntranceUri());
        project.setUpdateTime(System.currentTimeMillis());
        repository.save(project);
    }

    private Project checkSaveProjectFileParams(String projectId, MultipartFile file) {
        checkParam(StringUtils.isNotEmpty(projectId), "参数projectId不能为空");
        checkParamNotNull(file, "上传文件不能为空");
        checkParam(!file.isEmpty(), "上传文件不能为空");
        projectId = StringUtils.trim(projectId);
        Project project = repository.findOne(projectId);
        checkParamNotNull(project, "项目不存在");
        return project;
    }

    /**
     * 获取存储文件目录
     * @param id
     * @return
     */
    public String getProjectFilePath(String id) {
        String workDir = config.getWorkDir();
        String projectPath = workDir + File.separator + "projects" + File.separator + id;
        return projectPath;
    }
}
