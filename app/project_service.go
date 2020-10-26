package app

import (
	"io"
	"mime/multipart"
	"os"
	"paper-show/common"
	"path"
	"strconv"
	"time"
)

//
// 项目业务层
//
type ProjectService struct {
	paperConf         *PaperConf
	projectRepository *ProjectRepository
}

func NewProjectService(conf *PaperConf, repository *ProjectRepository) *ProjectService {
	return &ProjectService{
		paperConf:         conf,
		projectRepository: repository,
	}
}

// AddProject 添加项目
func (t *ProjectService) AddProject(project *Project) error {
	if len(project.Id) == 0 {
		project.Id = RandomString(8, "ABCDEFGHIJQMNOPQRSTUVWXYZ0123456789")
	}
	project.CreateTime = time.Now().Unix()
	project.UpdateTime = time.Now().Unix()
	project.Uploaded = false
	project.EntranceUri = ""
	return t.projectRepository.SaveProject(project)
}

// ListProjects 项目列表
func (t *ProjectService) ListProjects() ([]*Project, error) {
	return t.projectRepository.FindAll()
}

// GetProject 获取项目
func (t *ProjectService) GetProject(id string) (*Project, error) {
	return t.projectRepository.FindOne(id)
}

// UploadProject 上传项目文件
func (t *ProjectService) UploadProject(project *Project, fileHeader *multipart.FileHeader) error {
	project2save, err := t.projectRepository.FindOne(project.Id)
	if err != nil {
		return err
	}
	if project2save == nil {
		return common.NewBizError("project not exists.")
	}

	if project.Type == ProjectTypeFile {
		err := t.handleArchiveFile(project2save, fileHeader)
		if err != nil {
			return err
		}
	} else if project.Type == ProjectTypeLink {
		project2save.LinkUrl = project.LinkUrl
	}

	// 更新项目信息
	project2save.Type = project.Type
	project2save.Uploaded = true
	project2save.EntranceUri = project.EntranceUri
	project2save.UpdateTime = time.Now().Unix()
	err = t.projectRepository.SaveProject(project2save)
	return err
}

func (t *ProjectService) handleArchiveFile(project *Project, fileHeader *multipart.FileHeader) error {
	file, err := fileHeader.Open()
	if err != nil {
		return err
	}
	defer file.Close()

	// 保存
	filename := project.Id + "_" + strconv.FormatInt(time.Now().Unix(), 10) + "_" + fileHeader.Filename
	targetZipPath := path.Join(t.paperConf.WorkDir, "upload", filename)
	err = os.MkdirAll(path.Dir(targetZipPath), os.ModePerm)
	if err != nil {
		return err
	}

	targetZipFile, err := os.Create(targetZipPath)
	if err != nil {
		return err
	}
	defer targetZipFile.Close()

	_, err = io.Copy(targetZipFile, file)
	if err != nil {
		return err
	}

	// 解压
	targetDir := path.Join(t.paperConf.WorkDir, "projects", project.Id)
	err = Unzip(targetZipPath, targetDir)
	return err
}
