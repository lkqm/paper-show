package main

import (
	"github.com/go-spring/spring-core/gs"
	"github.com/markbates/pkger"
	"html/template"
	"io/ioutil"
	"os"
	"paper-show/app"
	"strings"
)

func init() {
	// 静态资源和模板配置
	gs.Provide(templates)
	gs.Static("/static", pkger.Include("web/static"))
	gs.Provide(func(conf *app.PaperConf) bool {
		gs.Static("/v", conf.WorkDir+"/projects")
		return false
	})
}

func templates() (*template.Template, error) {
	dir := pkger.Include("/web/template")
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
