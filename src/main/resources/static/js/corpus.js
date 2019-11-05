var flag;
var userList;
function userTable(){
    $.ajax({
        type: "get",
        url: "/users/userlist",
        dataType:"JSON",
        success: function (data) {
            if (data.code == 200) {
                userList = data.data;
            }
        }
    })
    var table = layui.table;
    var util = layui.util;
    table.render({
        elem: '#corpusTableId',
        url:'/corpus/pages',
        method: "post",
        dataType:"JSON",
        id: 'tableOne',
        height:'full-100',
        limits:[20,40,60],
        limit: 20, //每页默认显示的数量
        where: {
            'flag':"0"
        },
        cols: [[
            {field:'userId',width:"10%", align:'center',  title: '用户账号',
                templet: function (item) {
                    return getUserName(item.userId);
                }
            },
            {field:'name',width:"5%", align:'center',  title: '姓名'},
            {field:'scene',width:"10%", align:'center',  title: '场景'},
            {field:'opeThing',width:"10%", align:'center',  title: '操作物'},
            {field:'excLanguage',width:"10%", align:'center',  title: '刺激语种'},
            {field:'bgLanguage',width:"10%", align:'center',  title: '背景语种'},
            {field:'mediaName',width:"20%", align:'center',  title: '文件名称'},
            {field:'createTime', width:"15%", align:'center', title: '创建时间',
                templet: function (item) {
                    return util.toDateString(item.createTime, "yyyy-MM-dd HH:mm:ss");
                }
            },
            {field:'test', width:"10%", align:'center', title: '操作',
                templet: function () {
                    return '<div id="bar">\n' +
                        '    <a class="layui-btn gerren_btn layui-btn-xs" lay-event="edit">查看详情</a>\n' +
                        '    </div>';
                }
            }
        ]],
        page: true,
        parseData: function (res) {
            if(res.code == 400){
                layer.msg(res.msg,{icon:2});
            }
            else{
                return {
                    "code": res.code, //解析接口状态
                    "msg": res.msg, //解析提示文本
                    "count": res.data.totalElements, //解析数据总长度
                    "data": res.data.content //解析数据列表
                };
            }

        }
    });
    table.on('tool(corpus)', function(obj){ //注：tool是工具条事件名，users是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
            ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'edit'){
            edit(data.corpusId);
        }
    });
    // 执行搜索，表格重载
    $('#do_search').on('click', function () {
        var username = $('#username').val();
        if(username == ""){
            layer.msg("请输入查询内容",{icon:2});
            return
        }
        table.reload('tableOne', {
            method: 'post',
            dataType:"JSON",
            where: {
                'username': username,
                'flag':"1",
            },
            page: true,
            parseData: function (res) {
                if(res.code == 400){
                    layer.msg(res.msg,{icon:2});
                }
                else{
                    return {
                        "code": res.code, //解析接口状态
                        "msg": res.msg, //解析提示文本
                        "count": res.data.totalElements, //解析数据总长度
                        "data": res.data.content //解析数据列表
                    };
                }
            }
        });
    });
}

function getUserName(v) {
    var showGroup = '';
    if (v == null || v == undefined) return showGroup;
    if (userList != null && userList != undefined) {
        for (var i = 0; i < userList.length; i++) {
            if ( parseInt(userList[i].userId)== parseInt(v)) {
                showGroup = userList[i].username;
                return showGroup;
            }
        }
    }
    return showGroup;
}

function edit(corpusId) {
    $('#corform')[0].reset();
    $.ajax({
        type: "get",
        url: "/corpus/corpusid",
        data: {"corpusId": corpusId},
        dataType:"JSON",
        success: function (data) {
            if (data.code == 200) {
                if (data.data == null) {
                    layer.msg("获取数据失败");
                    return;
                } else {
                    //赋值
                    $("#name").val(data.data.name);
                    $("#birthday").val(changeTime(data.data.birthday));
                    $("#createTime").val(changeTime(data.data.createTime));
                    $("#scene").val(data.data.scene);
                    $("#opeThing").val(data.data.opeThing);
                    $("#excLanguage").val(data.data.excLanguage);
                    $("#bgLanguage").val(data.data.bgLanguage);
                    $("#keywords").val(data.data.keywords);
                    $("#remarks").val(data.data.remarks);
                    $("#mediaName").val(data.data.mediaName);
                    layui.form.render(); //更新全部
                    layer.open({
                        type: 1,
                        title: '查看信息',
                        maxmin: false,
                        shadeClose: false, // 点击遮罩关闭层
                        area: ['500px', '700px'],
                        btn: ['关闭', '取消'],
                        content: $('#corid'),
                        cancel: function () {

                        }, cancel: function () {

                        }
                    });
                }
            }else {
                layer.msg(data.data.msg);
            }
        }
    })
}

function changeTime(date) {
    var newDate = new Date(date).toJSON();
    return new Date(+new Date(newDate) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
}
