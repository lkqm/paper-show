package app

import (
	"github.com/gin-gonic/gin"
	"net/http"
	. "paper-show/common"
	"strconv"
)

//
// 项目控制器
//
type ProjectController struct {
	conf           *PaperConf
	projectService *ProjectService
}

func NewProjectController(conf *PaperConf, projectService *ProjectService) *ProjectController {
	return &ProjectController{
		conf:           conf,
		projectService: projectService,
	}
}

// 控制器
func (t *ProjectController) RegisterPath(r *gin.Engine) {
	r.GET("/", t.Home)
	r.POST("/api/projects", t.AddProject)
	r.GET("/api/projects", t.ListProjects)
	r.GET("/api/projects/:id", t.GetProject)
	r.POST("/api/projects/:id/upload", t.UploadProject)
}

// 主页
func (t *ProjectController) Home(c *gin.Context) {
	c.HTML(http.StatusOK, "index.html", gin.H{
		"title": t.conf.Title,
	})
}

// 添加
func (t *ProjectController) AddProject(c *gin.Context) {
	project := new(Project)
	err := c.Bind(project)
	if HandleError400(c, err) {
		return
	}

	err = t.projectService.AddProject(project)
	if HandleError(c, err) {
		return
	}
}

// 列表
func (t *ProjectController) ListProjects(c *gin.Context) {
	projects, err := t.projectService.ListProjects()
	if HandleError(c, err) {
		return
	}
	c.JSON(http.StatusOK, projects)
}

// 详情
func (t *ProjectController) GetProject(c *gin.Context) {
	id := c.Param("id")
	project, err := t.projectService.GetProject(id)
	if HandleError(c, err) {
		return
	}
	c.JSON(http.StatusOK, project)
}

// 上传
func (t *ProjectController) UploadProject(c *gin.Context) {
	id := c.Param("id")
	entranceUri := c.PostForm("entranceUri")
	file, _ := c.FormFile("file")
	projectType, _ := strconv.Atoi(c.PostForm("type"))
	linkUrl := c.PostForm("linkUrl")
	project := &Project{
		Id:          id,
		EntranceUri: entranceUri,
		Type:        projectType,
		LinkUrl:     linkUrl,
	}

	err := t.projectService.UploadProject(project, file)
	if HandleError(c, err) {
		return
	}
}
