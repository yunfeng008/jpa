var flag;
function userTable(){
    var table = layui.table;
    table.render({
        elem: '#userTableId',
        url:'/users/pages',
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
            {field:'userId',width:"5%", align:'center',  title: '序号'},
            {field:'username',width:"30%", align:'center',  title: '账号名称'},
            {field:'status', width:"20%", align:'center', title: '用户状态',
                templet: function (item) {
                    if(item.status==1){
                        return '<span>正常</span>';
                    }else if(item.status==0){
                        return '<span>注销</span>';
                    }
                }
            },
            {field:'test', width:"20%", align:'center', title: '操作',
                templet: function (item) {
                    return '<div id="bar">\n' +
                        '    <a class="layui-btn gerren_btn layui-btn-xs" lay-event="edit">编辑</a>\n' +
                        '    <a class="layui-btn red_btn layui-btn-xs" lay-event="del">删除</a>\n' +
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
    table.on('tool(users)', function(obj){ //注：tool是工具条事件名，users是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
            ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'del'){
            del(data.userId);
        } else if(layEvent === 'edit'){
            edit(data.userId);
        }
    });
    // 执行搜索，表格重载
    $('#do_search').on('click', function () {
        // 搜索条件
        var username = $('#username1').val();
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

function del(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: "/users/userid",
            type: "delete",
            dataType:"JSON",
            data: {
                'userId': id
            },
            success: function (data) {
                if (data.code == 200) {
                    layer.msg(data.msg,{icon:1});
                    window.setTimeout("location.reload();", 1500);
                } else {
                    layer.msg(data.msg,{icon:2});
                }
            }
        });
    })
}

function edit(userId) {
    $('#uform')[0].reset();
    $.ajax({
        type: "get",
        url: "/users/userid",
        data: {"userId": userId},
        dataType:"JSON",
        success: function (data) {
            if (data.code == 200) {
                if (data.data == null) {
                    layer.msg("获取用户失败");
                    return;
                } else {
                    //赋值
                    $("#userId").val(data.data.userId);
                    $("#username").val(data.data.username);
                    $("#password").val(data.data.password);
                    $("#token").val(data.data.token);
                    $("#type").val(data.data.type);
                    $("input[name=status][value=1]").attr("checked", data.data.status == 1 ? true : false);
                    $("input[name=status][value=0]").attr("checked", data.data.status == 0 ? true : false);
                    layui.form.render(); //更新全部
                    layer.open({
                        type: 1,
                        title: '修改用户',
                        maxmin: false,
                        shadeClose: false, // 点击遮罩关闭层
                        area: ['500px', '320px'],
                        btn: ['保存', '取消'],
                        content: $('#uid'),
                        yes: function (index, layero) {
                            flag = 1;
                            submit(index);
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

function show(){
    $('#uform')[0].reset();
    layer.open({
        type: 1,
        title: '增加用户',
        maxmin: false,
        shadeClose: false,
        area: ['500px', '320px'],
        btn: ['保存', '取消'],
        content: $('#uid'),
        yes:function(index, layero){
            flag = 0;
            submit(index);
        },cancel: function(){
        }
    });
}

function submit(index) {
    var submitType;
    var username = $('#username').val();
    var name_rule = /^[\u4E00-\u9FA5A-Za-z0-9]{1,20}$/;
    if (username == ""){
        layer.msg('用户名不能为空');
        return;
    }else if (!name_rule.test(username)){
        layer.msg('用户名格式错误');
        $('#name').val("");
        return;
    }
    if(flag == 1){
        submitType = "put";
    }else if (flag == 0){
        submitType = "post";
    }else {
        return;
    }
    $.ajax({
        type : submitType,
        url : "/users",
        dataType:"JSON",
        data : $('#uform').serialize(),
        error : function(request) {
        },
        success : function(data) {
            if (data.code == 200) {
                layer.msg(data.msg,{icon:1});
                window.setTimeout("location.reload();", 1500);
                layer.close(index);
            }else {
                layer.msg(data.msg,{icon:2});
            }
        }
    });
}




