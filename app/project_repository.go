package app

import (
	"encoding/json"
	"fmt"
	"log"
	"os"
	"path"
)

//
// 项目持久层
//
type ProjectRepository struct {
	paperConf *PaperConf `autowire:""`
}

func NewProjectRepository(paperConf *PaperConf) *ProjectRepository {
	return &ProjectRepository{
		paperConf: paperConf,
	}
}

// 保存项目
func (t *ProjectRepository) SaveProject(project *Project) error {
	data, err := t.readDataFromFile()
	if err != nil {
		return err
	}
	projects := data.Projects

	found := false
	for idx, p := range projects {
		if p.Id == project.Id {
			found = true
			projects[idx] = project
		}
	}

	// 添加
	if !found {
		data.Projects = append(projects, project)
	}
	return t.writeDataToFile(data)
}

// 所有项目
func (t *ProjectRepository) FindAll() ([]*Project, error) {
	data, err := t.readDataFromFile()
	if err != nil {
		return nil, err
	}
	return data.Projects, nil
}

// 某个项目
func (t *ProjectRepository) FindOne(id string) (*Project, error) {
	data, err := t.readDataFromFile()
	if err != nil {
		return nil, err
	}

	projects := data.Projects
	for _, p := range projects {
		if p != nil && p.Id == id {
			return p, nil
		}
	}
	return nil, nil
}

func (t *ProjectRepository) readDataFromFile() (*ProjectJsonWrapper, error) {
	data := new(ProjectJsonWrapper)
	p := path.Join(t.paperConf.WorkDir, "project.json")

	if _, err := os.Stat(p); os.IsNotExist(err) {
		data.Projects = make([]*Project, 0)
		return data, nil
	}

	file, err := os.Open(p)
	if err != nil {
		err = fmt.Errorf("can't access store file 'project.json'")
		return nil, err
	}
	defer file.Close()

	err = json.NewDecoder(file).Decode(data)
	if err != nil {
		err = fmt.Errorf("store file content is illegaled json 'project.json'")
		return nil, err
	}
	return data, nil
}

func (t *ProjectRepository) writeDataToFile(data *ProjectJsonWrapper) error {
	p := path.Join(t.paperConf.WorkDir, "project.json")
	_ = os.MkdirAll(t.paperConf.WorkDir, os.ModeDir)
	file, err := os.Create(p)
	if err != nil {
		log.Printf("Faield create file: %v", err)
		return err
	}
	defer file.Close()

	// 创建Json编码器
	err = json.NewEncoder(file).Encode(data)
	if err != nil {
		log.Printf("Failed write data to file: %v", err)
		return err
	}
	return nil
}
