package common

import "github.com/gin-gonic/gin"

// Controller 控制器接口
type Controller interface {
	RegisterPath(r *gin.Engine)
}

// RegisterController 注册控制器
func RegisterController(r *gin.Engine, controllers ...Controller) {
	for _, ctl := range controllers {
		ctl.RegisterPath(r)
	}
}
