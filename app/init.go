package app

import (
	"github.com/go-spring/spring-core/gs"
	"os"
	"path"
)

func init() {
	gs.Object(new(PaperConf)).Init(func(paperConfig *PaperConf) {
		if paperConfig.WorkDir == "" {
			userHome, _ := os.UserHomeDir()
			paperConfig.WorkDir = path.Join(userHome, ".paper-show")
		}
	})
	gs.Provide(NewProjectRepository)
	gs.Provide(NewProjectService)
	gs.Provide(NewProjectController).Init(func(c *ProjectController) { c.Init() })
}
