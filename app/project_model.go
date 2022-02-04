package app

const (
	ProjectTypeFile = 1
	ProjectTypeLink = 2
)

// PaperConf 配置
type PaperConf struct {
	WorkDir string `value:"${app.workdir:=}"`
	Title   string `value:"${app.title:=原型大全}"`
}

// Project 项目实体
type Project struct {
	Id          string `json:"id" form:"id"`
	Name        string `json:"name" form:"name"`
	Description string `json:"description" form:"description"`
	Uploaded    bool   `json:"uploaded" form:"uploaded"`
	EntranceUri string `json:"entranceUri" form:"entranceUri"`
	Type        int    `json:"type" form:"type"`
	LinkUrl     string `json:"linkUrl" form:"linkUrl"`
	Creator     string `json:"creator" form:"creator"`
	CreateTime  int64  `json:"createTime" form:"createTime"`
	UpdateTime  int64  `json:"updateTime" form:"updateTime"`
}

// ProjectJsonWrapper 项目存储包装实体
type ProjectJsonWrapper struct {
	Projects []*Project `json:"projects"`
}
