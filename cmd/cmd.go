package cmd

import (
	"flag"
	"html/template"
	"io/ioutil"
	"log"
	"os"
	"paper-show/app"
	"paper-show/common"
	"path"
	"strings"

	"github.com/gin-gonic/gin"
	"github.com/markbates/pkger"
)

// Cmd 入口程序
type Cmd struct{}

var (
	port    string
	workDir string
	title   string
)

// Run 启动程序
func (cmd *Cmd) Run() {
	cmd.parseCommandArgs()
	cmd.runWeb()
}

func (cmd *Cmd) parseCommandArgs() {
	homeDir, _ := os.UserHomeDir()
	defaultWorkDir := path.Join(homeDir, ".paper-show")
	defaultPort := ":8080"
	defaultTitle := "原型大全"
	flag.StringVar(&port, "p", defaultPort, "paper-show -p :8080")
	flag.StringVar(&workDir, "d", defaultWorkDir, "paper-show -d /some/dir")
	flag.StringVar(&title, "t", defaultTitle, "paper-show -t title")
	flag.Parse()
}

func (cmd *Cmd) runWeb() {
	r := gin.Default()
	tpl, _ := templates(pkger.Include("/web/template"))
	r.SetHTMLTemplate(tpl)
	r.StaticFS("/static", pkger.Dir("/web/static"))
	r.Static("/v", workDir+"/projects")
	route(r)
	err := r.Run(port)
	if err != nil {
		log.Fatalf("Start server failed: %v", err)
	}
}

// 路由
func route(r *gin.Engine) {
	conf := &app.PaperConf{WorkDir: workDir, Title: title}
	projectRepository := app.NewProjectRepository(conf)
	projectService := app.NewProjectService(conf, projectRepository)
	projectController := app.NewProjectController(conf, projectService)

	common.RegisterController(r, projectController)
}

// 指定目录加载模板
func templates(dir string) (*template.Template, error) {
	tpl := template.New("")
	if !strings.HasSuffix(dir, "/") {
		dir = dir + "/"
	}

	err := pkger.Walk(dir, func(path string, info os.FileInfo, _ error) error {
		// Skip non-templates.
		if info.IsDir() {
			return nil
		}

		// Load file from pkpger virtual file, or real file if pkged.go has not
		f, _ := pkger.Open(path)
		defer f.Close()
		sl, _ := ioutil.ReadAll(f)
		name := strings.TrimPrefix(f.Path().Name, dir)
		_, err := tpl.New(name).Parse(string(sl))
		return err
	})

	return tpl, err
}
