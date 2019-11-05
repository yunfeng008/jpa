var flag;
function roleTable(){
    var table = layui.table;
    table.render({
        elem: '#roleTableId',
        url:'/roles/pages',
        method: "post",
        dataType:"JSON",
        id: 'tableOne',
        height:'full-100',
        limits:[20,40,60],
        limit: 20, //每页默认显示的数量
        cols: [[
            {field:'roleId',width:"5%", align:'center',  title: '序号'},
            {field:'name',width:"30%", align:'center',  title: '角色名称'},
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
    table.on('tool(roles)', function(obj){ //注：tool是工具条事件名，users是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
            ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'del'){
            del(data.roleId);
        } else if(layEvent === 'edit'){
            edit(data.roleId);
        }
    });
}

function del(id) {
    layer.confirm('确定要删除选中的记录？', {
        btn: ['确定', '取消']
    }, function () {
        $.ajax({
            url: "/roles/roleid",
            type: "delete",
            dataType:"JSON",
            data: {
                'roleId': id
            },
            success: function (data) {
                if (data.code == 200) {
                    layer.msg(data.data,{icon:1});
                    window.setTimeout("location.reload();", 1500);
                } else {
                    layer.msg(data.data,{icon:2});
                }
            }
        });
    })
}

function edit(id) {
    $('#rform')[0].reset();
    $("#rform").attr('action','/roles');
    $.ajax({
        type: "get",
        url: "/roles/roleid",
        data: {"roleId": id},
        dataType:"JSON",
        success: function (data) {
            if (data.code == 200) {
                if (data.data == null) {
                    layer.msg("获取失败");
                    return;
                } else {
                    //赋值
                    $("#roleId").val(data.data.roleId);
                    $("#name").val(data.data.name);
                    layui.form.render(); //更新全部
                    layer.open({
                        type: 1,
                        title: '修改角色',
                        maxmin: false,
                        shadeClose: false, // 点击遮罩关闭层
                        area: ['500px', '420px'],
                        content: $('#rid')
                    });

                    layui.config({
                        base: '/static/lib/',
                    });
                    layui.use(['jquery', 'authtree', 'form', 'layer'], function(){
                        var $ = layui.jquery;
                        var authtree = layui.authtree;
                        var form = layui.form;
                        var layer = layui.layer;
                        // 初始化
                        $.ajax({
                            url: '/resources/roleid',
                            type:"get",
                            dataType:"JSON",
                            data: {"roleId": id},
                            success: function(data){
                                if(data.code != 200){
                                    layer.msg(data.msg,{icon:2,time: 1500});
                                    return;
                                }

                                var trees = authtree.listConvert(data.data, {
                                    primaryKey: 'id'
                                    ,startPid: 0
                                    ,parentKey: 'pid'
                                    ,nameKey: 'resourceName'
                                    ,valueKey: 'id'
                                    ,checkedKey: 'checked'
                                });
                                authtree.render('#LAY-auth-tree-index', trees, {
                                    inputname: 'authids[]'
                                    ,layfilter: 'lay-check-auth'
                                    ,autowidth: true
                                });
                            },
                            error: function(xml, errstr, err) {
                                layer.alert('系统异常');
                            }
                        });

                        form.on('submit(LAY-auth-tree-submit)', function(obj){
                            var authids = authtree.getChecked('#LAY-auth-tree-index');
                            var resourcesids=authids.join(',');
                            $('#resourcesids').val(resourcesids);
                            $.ajax({
                                type : "put",
                                url : $('#rform').attr('action'),
                                data : $('#rform').serialize(),
                                dataType:"JSON",
                                error : function(request) {
                                },
                                success : function(data) {
                                    if (data.code == 200) {
                                        layer.msg(data.data,{icon:1,time: 1500});
                                        window.setTimeout("location.reload();", 1500);
                                        layer.close(index);
                                    }else {
                                        layer.msg(data.data,{icon:2,time: 1500});
                                    }
                                }
                            });
                        });
                    });

                }
            }else {
                layer.msg(data.data.msg);
            }
        }
    })
}

function show(){
    $('#rform')[0].reset();
    $("#rform").attr('action','/roles');
    layer.open({
        type: 1,
        title: '增加角色',
        maxmin: false,
        shadeClose: false,
        area: ['500px', '420px'],
        content: $('#rid')
    });

    layui.config({
        base: '/static/lib/',
    });
    layui.use(['jquery', 'authtree', 'form', 'layer'], function(){
        var $ = layui.jquery;
        var authtree = layui.authtree;
        var form = layui.form;
        var layer = layui.layer;
        // 初始化
        $.ajax({
            url: '/resources/resourcelist',
            type : "get",
            dataType:"JSON",
            success: function(data){
                if(data.code != 200){
                    layer.msg(data.msg,{icon:2,time: 1500});
                    return;
                }
                var trees = authtree.listConvert(data.data, {
                    primaryKey: 'id'
                    ,startPid: 0
                    ,parentKey: 'pid'
                    ,nameKey: 'resourceName'
                    ,valueKey: 'id'
                    ,checkedKey: 'checked'
                });
                authtree.render('#LAY-auth-tree-index', trees, {
                    inputname: 'authids[]'
                    ,layfilter: 'lay-check-auth'
                    ,autowidth: true
                });
            },
            error: function(xml, errstr, err) {
                layer.alert('系统异常');
            }
        });

        form.on('submit(LAY-auth-tree-submit)', function(obj){
            var authids = authtree.getChecked('#LAY-auth-tree-index');
            var resourcesids = authids.join(',');
            $('#resourcesids').val(resourcesids);
            $.ajax({
                url : $('#rform').attr('action'),
                type : "post",
                data : $('#rform').serialize(),
                dataType:"JSON",
                error : function(request) {
                },
                success : function(data) {
                    if (data.code == 200) {
                        layer.msg(data.data,{icon:1,time: 1500});
                        window.setTimeout("location.reload();", 1500);
                        layer.close(index);
                    }else {
                        layer.msg(data.data,{icon:2,time: 1500});
                    }
                }
            });
        });
    });
}
