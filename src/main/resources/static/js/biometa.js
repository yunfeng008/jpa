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
        elem: '#biometaTableId',
        url:'/biometa/pages',
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
            {field:'faName',width:"7%", align:'center',  title: '父亲姓名'},
            {field:'maName',width:"7%", align:'center',  title: '母亲姓名'},
            {field:'childName',width:"7%", align:'center',  title: '孩子姓名'},
            {field:'childSex',width:"7%", align:'center',  title: '孩子性别'},
            {field:'childBirthday',width:"15%", align:'center',  title: '孩子生日',
                templet: function (item) {
                    return util.toDateString(item.childBirthday, "yyyy-MM-dd HH:mm:ss");
                }},
            {field:'birthWeeks',width:"7%", align:'center',  title: '出生周数'},
            {field:'companion',width:"10%", align:'center',  title: '长期陪伴者'},
            {field:'firstLanguage', width:"10%", align:'center', title: '第一语言'},
            {field:'familyLanguage', width:"10%", align:'center', title: '家族语言'},
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
    table.on('tool(biometa)', function(obj){ //注：tool是工具条事件名，users是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
            ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'edit'){
            edit(data.baseinfoId);
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

function edit(baseinfoId) {
    $('#biometaform')[0].reset();
    $.ajax({
        type: "get",
        url: "/biometa/baseinfoid",
        data: {"baseinfoId": baseinfoId},
        dataType:"JSON",
        success: function (data) {
            if (data.code == 200) {
                if (data.data == null) {
                    layer.msg("获取数据失败");
                    return;
                } else {
                    //赋值
                    $("#faName").val(data.data.faName);
                    $("#faBirthday").val(changeTime(data.data.faBirthday));
                    $("#maName").val(data.data.maName);
                    $("#maBirthday").val(changeTime(data.data.maBirthday));
                    $("#childName").val(data.data.childName);
                    $("#childSex").val(data.data.childSex);
                    $("#childhoodName").val(data.data.childhoodName);
                    $("#childBirthday").val(changeTime(data.data.childBirthday));
                    $("#birthAddress").val(data.data.birthProvince+data.data.birthCity+data.data.birthDistrict+data.data.birthDetailAddress);
                    $("#otherThing").val(data.data.otherThing);
                    $("#birthWeeks").val(data.data.birthWeeks);
                    $("#familyAddress").val(data.data.familyProvince+data.data.familyCity+data.data.familyDistrict+data.data.familyDetailAddress);
                    $("#population").val(data.data.population);
                    $("#companion").val(data.data.companion);
                    $("#firstLanguage").val(data.data.firstLanguage);
                    $("#familyLanguage").val(data.data.familyLanguage);
                    $("#houseArea").val(data.data.houseArea);
                    layui.form.render(); //更新全部
                    layer.open({
                        type: 1,
                        title: '查看信息',
                        maxmin: false,
                        shadeClose: false, // 点击遮罩关闭层
                        area: ['500px', '700px'],
                        btn: ['关闭', '取消'],
                        content: $('#biometaid'),
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
