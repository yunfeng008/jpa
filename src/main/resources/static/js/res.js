function treetalbe(){
    layui.use(['table', 'form', 'element', 'treetable'], function () {
        var $ = layui.jquery;
        var table = layui.table;
        var treetable = layui.treetable;
        // 渲染表格
        var renderTable = function () {
            layer.load(2);
            treetable.render({
                height:'full-100',
                treeColIndex: 0,
                treeSpid: "",
                treeIdName: 'resourceId',
                treePidName: 'parentId',
                elem: '#res-tree-table',
                url: '/resources/resourcelist',
                page: false,
                cols: [
                    [
                        {field: 'resourceName', title: '菜单名称'},
                        {field: 'resourceUrl', title: '菜单路径'},
                        {templet: '#oper-col', title: '操作'}
                    ]
                ],
                done: function () {
                    layer.closeAll('loading');
                }
            });
        };
        renderTable();
        //监听工具条
        table.on('tool(res-tree-table)', function (obj) {
            var data = obj.data;
            var layEvent = obj.event;
            if (layEvent === 'del') {
                layer.confirm('确定要删除该条记录？', {
                    btn: ['确定', '取消']
                }, function () {
                    $.ajax({
                        url: "/resources/resourceid",
                        type: "delete",
                        data: {resourceId: data.resourceId},
                        dataType:"JSON",
                        success: function (data) {
                            if (data.code == 200) {
                                layer.msg(data.data,{icon:1});
                                window.setTimeout("treetalbe();", 1500);
                            } else {
                                layer.msg(data.data,{icon:2});
                                window.setTimeout("treetalbe();", 1500);
                            }
                        }
                    });
                })
            } else if (layEvent === 'edit') {
                edit(data.resourceId);
            } else if (layEvent === 'add') {
                showmodel(data.resourceId);
            } else if (layEvent === 'detail') {
                showdetail(data);
            }
        });
        $('#btn-expand').click(function () {
            treetable.expandAll('#res-tree-table');
        });

        $('#btn-fold').click(function () {
            treetable.foldAll('#res-tree-table');
        });
    });
}

function showdetail(data) {
    $('#menuForm')[0].reset();
    $("#parentId").val(data.resourceId);
    $("#resourceName").val(data.resourceName);
    $("#resourceUrl").val(data.resourceUrl);
    $("#permission").val(data.permission);
    $("#icon").val(data.icon);
    $("#resourceOrder").val(data.resourceOrder);
    $("#resourceLevel").val(data.resourceLevel);
    $("#parentId").attr("readonly","true");
    layer.open({
        type: 1,
        title: '菜单详情',
        maxmin: false,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        btn: ['关闭', '取消'],
        content: $('#menuId'),
        cancel: function (index, layero) {

        }, cancel: function () {
        }
    });
}

function showmodel(parentId) {
	$('#menuForm')[0].reset();
	$("#parentId").val(parentId);
	$("#parentId").attr("readonly","true");
    layer.open({
        type: 1,
        title: '增加菜单',
        maxmin: false,
        shadeClose: false, // 点击遮罩关闭层
        area: ['800px', '520px'],
        btn: ['保存', '取消'],
        content: $('#menuId'),
        yes: function (index, layero) {
            subForm(index);
        }, cancel: function () {
        }
    });
}

function subForm(index) {
    var resourceName = $("#resourceName").val();
    var resourceUrl = $("#resourceUrl").val();
    var permission = $("#permission").val();
    var resourceLevel = $("#resourceLevel").val();
    if(resourceName==null||resourceName==""){
        layer.msg("请输入菜单名称",{icon:2});
        return;
    }
    if(resourceUrl==null||resourceUrl==""){
        layer.msg("请输入链接地址",{icon:2});
        return;
    }
    if(permission==null||permission==""){
        layer.msg("请输入权限标识",{icon:2});
        return;
    }
    if(resourceLevel==null||resourceLevel==""){
        layer.msg("请输入菜单级别",{icon:2});
        return;
    }
    $.ajax({
        type: "post",
        url: "/resources",
        data: $('#menuForm').serialize(),
        dataType:"JSON",
        error: function (request) {
        },
        success: function (data) {
            if (data.code == 200) {
                layer.msg(data.data,{icon:1});
                window.setTimeout("location.reload();", 1500);
                layer.close(index);
            } else {
                layer.msg(data.data,{icon:2});
            }
        }
    });
}

function edit(id) {
    $('#menuForm')[0].reset();
	$.ajax({
        type: "get",
        url: "/resources/resourceid",
        data: {resourceId: id},
        async: false,
        dataType:"JSON",
        success: function (data) {
        	if (data.data == null) {
                layer.msg("获取数据失败");
                return;
            } else {
            	$("#resourceId").val(data.data.resourceId);
            	$("#parentId").val(data.data.parentId);
                $("#parentId").attr("readonly","true");
            	$("#resourceName").val(data.data.resourceName);
            	$("#resourceUrl").val(data.data.resourceUrl);
            	$("#permission").val(data.data.permission);
            	$("#icon").val(data.data.icon);
            	$("#resourceOrder").val(data.data.resourceOrder);
            	$("#resourceLevel").val(data.data.resourceLevel);

                layui.form.render(); //更新全部
            	layer.open({
                    type: 1,
                    title: '编辑菜单',
                    maxmin: false,
                    shadeClose: false, // 点击遮罩关闭层
                    area: ['800px', '520px'],
                    btn: ['修改', '取消'],
         	        content: $('#menuId'),
         	        yes:function(index, layero){
         	        	subMenuForm(index);
         	        },cancel: function(){}
                });
            }
        }
	});
}

function subMenuForm(index){
    var resourceName = $("#resourceName").val();
    var resourceUrl = $("#resourceUrl").val();
    var permission = $("#permission").val();
    var resourceLevel = $("#resourceLevel").val();
    if(resourceName==null||resourceName==""){
        layer.msg("请输入菜单名字",{icon:2});
        return;
    }
    if(resourceUrl==null||resourceUrl==""){
        layer.msg("请输入链接地址",{icon:2});
        return;
    }
    if(permission==null||permission==""){
        layer.msg("请输入权限标识",{icon:2});
        return;
    }
    if(resourceLevel==null||resourceLevel==""){
        layer.msg("请输入菜单级别",{icon:2});
        return;
    }
	$.ajax({
        type : "put",
        url : "/resources",
        data : $('#menuForm').serialize(),
        dataType:"JSON",
        error : function(request) {
        },
        success : function(data) {
            if (data.code == 200) {
                layer.msg(data.data,{icon:1});
                window.setTimeout("location.reload();", 1500);
                layer.close(index);
            }else {
                layer.msg(data.data,{icon:2});
            }
        }
    });
}



