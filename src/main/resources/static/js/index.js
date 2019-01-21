(function (window) {
    var api = 'api';
    var ctx = '';

    function init() {
        initEvents();
        loadProjects();
    }

    /**
     * 初始化事件
     */
    function initEvents() {
        // 添加项目
        $('#title').on('dblclick', function () {
            showAddProject();
        });
        // 添加表单
        $('#addProjectForm').submit(function (evt) {
            evt.preventDefault();
            var submitBtn = $('#addProjectForm button[type=submit]').attr('disabled', true);
            submitBtn.text('提交中...');
            $('#addProjectTips').html('');

            var dataArray = $(this).serializeArray();
            var postData = $(this).serialize();
            $.ajax({
                url: api + '/projects',
                type: 'post',
                data: postData,
                success: function () {
                    layer.closeAll();
                    loadProjects();
                },
                error: function (request, text) {
                    var data = request.responseJSON;
                    var message = '内部错误';
                    if (data) {
                        message = data.message;
                    }
                    $('#addProjectTips').html(message);
                },
                complete: function() {
                    submitBtn.attr('disabled', false);
                    submitBtn.text('提交');
                }
            });
        });
        // 预览跳转
        $('#projects').on('click', 'li', function () {
            var projectId = $(this).data('id');
            var project = getProjectDataById(projectId);
            if (project && project.uploaded) {
                var url = 'v/' + projectId + '/';
                window.open(url);
            } else {
                layer.msg("项目暂未上传原型");
            }
            return;
        });
        // 上传版本表单
        $('#addVersionFrom').submit(function (evt) {
            evt.preventDefault();
            // 提示
            var layerIdx = layer.load(1, { shade: [0.2,'#000'] });
            var submitBtn = $('#addVersionFrom button[type=submit]').attr('disabled', true);
            $('#addVersionTips').html('上传中...');

            var dataArray = $(this).serializeArray();
            var dataMap = {};
            for (var i in dataArray) {
                var one = dataArray[i];
                dataMap[one.name] = one.value;
            }
            var projectId = dataMap['projectId'];
            var formData = new FormData(this);
            $.ajax({
                url: api + '/projects/' + projectId + '/upload',
                type: 'post',
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                success: function () {
                    layer.closeAll();
                    layer.msg('上传成功');
                    loadProjects();
                },
                error: function (request) {
                    var data = request.responseJSON;
                    var message = '内部错误';
                    if (data) {
                        message = data.message;
                    }
                    $('#addVersionTips').html(message);
                },
                complete: function() {
                    submitBtn.attr('disabled', false);
                    layer.close(layerIdx);
                    $('#addVersionTips').html('');
                }
            });
        });

        // 文件拖动
        $('#projects').on('dragenter', 'li', function (evt) {
            evt.preventDefault();
            evt.stopPropagation();
        });
        $('#projects').on('dragover', 'li', function (evt) {
            $(this).addClass("drag");
            evt.preventDefault();
            evt.stopPropagation();
        });
        $('#projects').on('dragleave', 'li', function (evt) {
            $(this).removeClass("drag");
            evt.preventDefault();
            evt.stopPropagation();
        });
        $('#projects').on('drop', 'li', function (evt) {
            $(this).removeClass("drag");
            evt.preventDefault();
            evt.stopPropagation();
            var projectId = $(this).data('id');
            var df = evt.dataTransfer ? evt.dataTransfer : evt.originalEvent.dataTransfer;
            var files = df.files;
            showAddProjectVersion(projectId, files);
        });
    }

    /**
     * 加载项目列表
     */
    var projectsData = [];

    function loadProjects() {
        var url = api + '/projects';
        $.get(url, function (data) {
            fillProjectsView(data);
            projectsData = data;
        });

        function fillProjectsView(data) {
            var text = '';
            for (var i = 0; i < data.length; i++) {
                var one = data[i];
                text += '<li data-id="' + one.id + '">';
                text += '   <p>' + one.name + '</p>'
                text += '   <span>' + one.creator + '</span>'
                text += '</li>';
            }
            $('#projects').html(text);
        }
    }

    /**
     * 显示添加项目弹框
     */
    function showAddProject() {
        layer.open({
            type: 1,
            title: '创建项目',
            area: '600px',
            content: $('#addProjectDialog'),
            resize: false
        });
    }

    /**
     * 显示添加版本弹框
     */
    function showAddProjectVersion(projectId, files) {
        var project = getProjectDataById(projectId);
        if (!project) {
            layer.alert('未找到项目id=' + projectId);
            return;
        }
        layer.open({
            type: 1,
            title: '上传文件',
            area: '600px',
            content: $('#addVersionDialog'),
            resize: false,
            success: function () {
                $('#addVersionFrom .jsProjectName').html(project.name);
                $('#addVersionFrom [name=projectId]').val(project.id);
                $('#addVersionFrom [name=entranceUri]').val(project.entranceUri);
                var allowTypes = ["application/zip"];
                if (files && files.length == 1 && allowTypes.indexOf(files[0].type) != -1) {
                    $('#addVersionFrom [name=file]')[0].files = files;
                } else {
                    $('#addVersionFrom [name=file]').val('');
                }
            }
        });
    }

    function getProjectDataById(id) {
        var projects = projectsData || [];
        for (var i in projects) {
            var one = projects[i];
            if (one.id == id) {
                return one;
            }
        }
        return null;
    }

    /**
     * 显示添加版本弹框
     */
    var projectInfo;
    function showProjectInfo(projectData) {
        projectInfo;
        layer.open({
            type: 1,
            title: false,
            area: '600px',
            content: $('#projectInfoDialog'),
            resize: true,
            success: function () {
                loadProjectInfo(projectData.id);
            }
        });
    }

    function loadProjectInfo(id) {
        $.ajax({
            url: api + "/projects/" + id,
            type: 'get',
            success: function (data) {
                if (!data) {
                    layer.closeAll();
                    layer.alert("项目不存在");
                }
                fillView(data);
                projectInfo = data;
            },
            error: function (resp) {
                layer.closeAll();
                layer.msg("加载项目数据错误");
            }
        });

        function fillView(data) {
            var jq = $('#projectInfoDialog');
            jq.find(".jsName").html(data.name);
            jq.find(".jsCreator").html(data.creator);
            if (data.createTime) {
                jq.find(".jsCreateTime").html(formatTime(data.createTime)).parent().show();
            } else {
                jq.find(".jsCreateTime").parent().hide();
            }
            if (data.updateTime) {
                jq.find(".jsUpdateTime").html(formatTime(data.updateTime)).parent().show();
            } else {
                jq.find(".jsUpdateTime").parent().hide();
            }
            jq.find(".jsDescription").html(data.description);
            if (data.versions && data.versions.length > 0) {
                var vtext = '';
                for (var i in data.versions) {
                    var one = data.versions[i];
                    vtext += '<span data-id="' + one.id + '">' + one.id + '</span> '
                }
                jq.find('.jsVersions').html(vtext);
            } else {
                jq.find('.jsVersions').html('无');
            }
        }
    }

    function formatTime(date, fmt) {
        if (date == null || date == undefined) return '';

        if (typeof date === 'number') {
            date = new Date(date);
        }
        fmt = fmt ? fmt : 'yyyy-MM-dd hh:mm';
        var o = {
            "M+": date.getMonth() + 1,                 //月份
            "d+": date.getDate(),                    //日
            "h+": date.getHours(),                   //小时
            "m+": date.getMinutes(),                 //分
            "s+": date.getSeconds(),                 //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }


    window.paperPage = {
        init: init
    };
})(window);

$(function () {
    paperPage.init();
});