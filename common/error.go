package common

import (
	"errors"
	"github.com/gin-gonic/gin"
	"net/http"
)

// HandleError 接口处理异常, 返回是否处理
func HandleError(c *gin.Context, err error) bool {
	if err == nil {
		return false
	}

	code := http.StatusInternalServerError
	if errors.Is(err, new(BizError)) {
		code = http.StatusBadRequest
	}
	c.JSON(code, &gin.H{"message": err.Error()})
	return true
}

// HandleError400 接口处理异常, 返回是否处理
func HandleError400(c *gin.Context, err error) bool {
	if err == nil {
		return false
	}

	code := http.StatusBadRequest
	c.JSON(code, &gin.H{"message": err.Error()})
	return true
}

// BizError 业务异常
type BizError struct {
	Message string
}

// NewBizError 构造函数
func NewBizError(message string) *BizError {
	return &BizError{
		Message: message,
	}
}

func (err *BizError) Error() string {
	return err.Message
}
