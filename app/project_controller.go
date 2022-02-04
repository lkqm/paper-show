package app

import (
	"github.com/go-spring/spring-core/gs"
	"github.com/go-spring/spring-core/web"
	"html/template"
	"net/http"
	"strconv"
)

//
// 项目控制器
//
type ProjectController struct {
	conf           *PaperConf
	projectService *ProjectService
	tpl            *template.Template
}

func NewProjectController(conf *PaperConf, projectService *ProjectService,
	tpl *template.Template) *ProjectController {
	return &ProjectController{
		conf:           conf,
		projectService: projectService,
		tpl:            tpl,
	}
}

func (t *ProjectController) Init() {
	gs.GetMapping("/", t.Home)
	gs.PostMapping("/api/projects", t.AddProject)
	gs.GetMapping("/api/projects/:id", t.GetProject)
	gs.GetMapping("/api/projects", t.ListProjects)
	gs.PostMapping("/api/projects/:id/upload", t.UploadProject)
}

// 主页
func (t *ProjectController) Home(c web.Context) {
	// c.HTML(http.StatusOK, "index.html", gin.H{"title": t.conf.Title,})
	err := t.tpl.ExecuteTemplate(c.ResponseWriter(), "index.html", H{
		"title": t.conf.Title,
	})
	if err != nil {
		c.SetStatus(http.StatusInternalServerError)
		c.JSON(H{"message": err.Error()})
	}
}

// 添加
func (t *ProjectController) AddProject(c web.Context) {
	project := new(Project)
	err := c.Bind(project)
	if HandleError(c, err) {
		return
	}
	err = t.projectService.AddProject(project)
	if HandleError(c, err) {
		return
	}
}

// 列表
func (t *ProjectController) ListProjects(c web.Context) {
	projects, err := t.projectService.ListProjects()
	if HandleError(c, err) {
		return
	}
	c.JSON(projects)
}

// 详情
func (t *ProjectController) GetProject(c web.Context) {
	id := c.PathParam("id")
	project, err := t.projectService.GetProject(id)
	if HandleError(c, err) {
		return
	}
	c.JSON(project)
}

// 上传
func (t *ProjectController) UploadProject(c web.Context) {
	id := c.FormValue("projectId")
	entranceUri := c.FormValue("entranceUri")
	file, _ := c.FormFile("file")
	projectType, _ := strconv.Atoi(c.FormValue("type"))
	linkUrl := c.FormValue("linkUrl")
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
