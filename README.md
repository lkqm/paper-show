# paper-show
一个在线原型托管工具，产品经理将原型导出为HTML静态资源，上传到原型大全，实现开发/测试人员同步预览原型。

**四步: 导出 -> 压缩 -> 上传 -> 预览**

其他语言版本: 
[Java](https://github.com/lkqm/paper-show/tree/java), 
[Kotlin](https://github.com/lkqm/paper-show/tree/kotlin),
[Groovy](https://github.com/lkqm/paper-show/tree/groovy)

## 安装
访问: http://127.0.0.1:8080

注: 默认数据存储目录: ${HOME}/.paper-show, 项目元信息: project.json, 静态资源: project/

## 指南
1. 添加项目: 双击"原型大全"标题行, 显示添加项目弹框.
2. 上传文件: 拖动文件到某个项目, 显示上传文件弹框.
3. 预览: 单击某个项目, 跳转到地址: /v/{projectId}/{entrance}
