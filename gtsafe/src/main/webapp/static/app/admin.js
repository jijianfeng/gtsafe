// 全局变量
var MaxTab = false;//页面是否已经最大化
var Tabsobj = {};
var CurrentDate = new Date();
var CurrentDateStr = CurrentDate.toLocaleDateString();
var isIE8 = !! navigator.userAgent.match(/MSIE 8.0/);
var isIE9 = !! navigator.userAgent.match(/MSIE 9.0/);
var isIE10 = !! navigator.userAgent.match(/MSIE 10/);
var ServerTime;
var HashUrl,// hash值
    ViewDialog,// 弹出层对象
    PanelContent,// 定义panel dom对象变量
    GridElement,// 定义当前tabs下的datagrid对象
    LoadingMask,LoadingMsg;// 定义加载层
var loading = {
    addMsk: function() {
        LoadingMask = $('<div class="datagrid-mask"></div>').css({display:"block",'z-index':'999',width:"100%",height:$(window).height()}).appendTo("body");
        LoadingMsg = $('<div class="datagrid-mask-msg"></div>').html("努力加载中，请稍候。。。").appendTo("body").css({display:"block",'z-index':'1000',left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
    },
    removeMsk: function() {
        LoadingMask.remove();
        LoadingMsg.remove();
    }
};
$(function(){
    // 显示时间
    $('.bs_navright').text(CurrentDateStr);
    // 选项卡设置
    Tabsobj = $('#content_tabs').tabs({
        onLoad: function(){
            // 获取tabs的panel dom对象
            PanelContent = Tabsobj.tabs("getSelected");
            GridElement = PanelContent.find(".J_grid");
        },
        onSelect: function() {
            PanelContent = $(this).tabs("getSelected");
            GridElement = PanelContent.find(".J_grid");
            if (HashUrl) {
                // 匹配地址栏
                var url = $(this).tabs('getSelected').panel('options').href;
                var selectedUrl = url.split('/').slice(-2).join('/');
                location.href = "#"+selectedUrl;
            }
        },
        onContextMenu: function(e, title){
            // $(this).tabs('select', title);
            e.preventDefault();
            comtextMenuTitle = title;
            $('#contextMenu').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
        },
        onDblclick: function(e, title){
            changeScreen();
        }
    });
    
    // 绑定tabs的dblclick事件
    $('#content_tabs').children("div.tabs-header").find("ul.tabs").undelegate('li', 'dblclick').delegate('li', 'dblclick', function(){
        changeScreen();
    });
    // 【菜单】刷新
    $('#menuRefresh').click(function(){
        var tab = Tabsobj.tabs('getSelected');
        tab.panel('refresh');
        // Tabsobj.tabs('update', {
        //     tab: tab,
        //     options: {}
        // });
        // //解析页面
        // $.parser.parse();
    });
    // 【菜单】关闭当前
    $('#menuCloseThis').click(function(){
        Tabsobj.tabs('close', Tabsobj.tabs('getSelected').panel('options').title);
    });
    // 【菜单】关闭其他
    $('#menuCloseOther').click(function(){
        var selectTabTitle = Tabsobj.tabs('getSelected').panel('options').title;
        var tabs = Tabsobj.tabs('tabs');
        var tabTitles = [];
        for (var i = 0; i < tabs.length; i++) {
            tabTitles.push(tabs[i].panel('options').title);
        }
        $.each(tabTitles, function(){
            if (this != selectTabTitle) 
                Tabsobj.tabs('close', this);
        });
    });
    // 【菜单】关闭所有
    $('#menuCloseAll').click(function(){
        var tabs = Tabsobj.tabs('tabs');
        var tabTitles = [];
        for (var i = 0; i < tabs.length; i++) {
            tabTitles.push(tabs[i].panel('options').title);
        }
        $.each(tabTitles, function(){
            Tabsobj.tabs('close', this);
        });
    });
    // 左侧菜单树
    $('.navSub').tree({
        onClick: function(node){
            if ($(this).tree('isLeaf')) {
                var urlNode = node.id;
                var parentNode = $(this).tree("getParent",node.target);
                if (parentNode) {
                    var parentName = parentNode.text;
                }
                var childName = node.text;
                var title = parentName?parentName + "&nbsp;>&nbsp;" + childName : childName;
                if (urlNode) {
                    location.href = "#"+urlNode;
                } 
                if (urlNode == HashUrl) {
                    addTabs(urlNode,title);
                }
            }
        }
    });
    // hash描点定位
    $(window).hashchange( function(){
        var hash = location.hash;
        HashUrl =  hash.replace( /^#/,'');
        var search = HashUrl.split('?');
        if (search.length > 1) {
             search.pop();
             HashUrl = search;
        }
        hash && treeHash();
        function treeHash() {
            $(".navSub .tree-node").each(function() {
                var nodeUrl = $(this).attr("node-id");
                $(this).removeClass('tree-node-selected');
                if (HashUrl == nodeUrl) {
                    var navIndex = parseInt($(this).parents('.navSub').attr('data-index'));
                    var parentName = $(this).parent().parent().siblings('.tree-node').find('.tree-title').text();
                    var childName = $(this).find('.tree-title').text();
                    var title = parentName?parentName + "&nbsp;>&nbsp;" + childName : childName;
                    $("#navLeft").accordion('select',navIndex);
                    addTabs(HashUrl,title);
                    $(this).addClass('tree-node-selected');
                }
            });
        }
    })
    $(window).hashchange();
});
// ui加载完毕后的处理事件
$.parser.onComplete = function(){
    $("#loading").remove();
}
function openW(href,options,callback) {
    var _default = {
        title: '编辑',
        formId: '#add',
        gridId: GridElement,
        LoadingMask: true,
        width: 370,
        height: 'auto'
    };
    var opts = $.extend({},_default,options);
    var title = opts.title;
    var formId = opts.formId;
    var gridId = opts.gridId;
    var id = opts.id;
    var LoadingMask = opts.LoadingMask;
    var formAction = opts.formAction || href;
    var width = opts.width;
    var height = opts.height;
    var top = opts.top;
    if (LoadingMask == true) {
        loading.addMsk();
    }
    if(href.indexOf("?")>-1){
    	href = href+"&_="+Math.random()
    }else{
    	href = href+"?_="+Math.random();
    }
    ViewDialog = $.dialog({
        title: title,
        width: width,
        height: height,
        top: top,
        href:href,
        shadow: false,
        onLoad: function() {
            loading.removeMsk();
            if (formAction) {
                submitForm(formId,gridId);
                $(formId).attr('action',formAction);
                $(".form-actions").append("<input type='hidden' name='ajax' value='true' />");
            }
            if (id) {
                $(".form-actions").append("<input type='hidden' name='id' value='"+id+"' />");
            }
            callback && callback();
        }
    });
}

/**
 * 表单处理封装
 */
function submitForm(formId,gridId,type) {
    var gridId = gridId || GridElement;
    if (arguments.length == 2) {
        var type = arguments[1];
    }
    $(formId).form({
        onSubmit: function() {
           var isValid = $(this).form('validate');
            if (isValid){
               $(this).find("button[type='submit']").attr('disabled','disabled').text('保存中');
            } else {
                return false;
            }
        },
        success:function(data){
        	//alert(data);
            var data = new Function("return" + data)(); 
            handleForm(data,gridId,type);
        }
    });
    //取消按钮触发关闭
   $(".J_close").on('click',function(){
        ViewDialog.dialog('destroy');
    });
}
/**
 * 表单提交提示方式封装
 * 依赖HTML结构
 * <form>
 *      省略表单结构...
 *      <div class="form-actions"></div>
 * </form>
 */
function handleForm(data,gId,type) {
    if (data.status==1){
        var t_html = $(".form-actions").append('<span class="txt-success" id="submit_tip">&nbsp;&nbsp;'+data.info+'</span>');
        setTimeout(function(){
        	$(".form-actions").find("button[type='submit']").removeAttr('disabled').text('确定');
            $("#submit_tip").remove();
            if (type == 2) {
                location.reload();
                return false;
            }
            // 关闭弹出窗口
            ViewDialog.dialog('destroy');
            // 刷新数据表格
            reloadGrid();
        },800);
    } else {
        $.alert(data.info);
        $(".form-actions").find("button[type='submit']").removeAttr('disabled').text('确定');
    }   
}
/**
 * 新建标签页，主要使用场景有：复杂的添加信息、字段值多的datagrid等
 * @param {string} [url]:接受一个url参数，格式为'path/to/goods/cateadd'
 * @param {string} [element]:最外层的标签页id或class，默认为'#content_tabs'
 */
function openTab(url,element) {
    var element = element || '#content_tabs';
    var tab = $(element).tabs('getSelected');
    tab.panel('refresh', url);      
}

/**
 * 清空datagrid封装
 * @param {string} [element]:grid列表的元素id或class，默认为'.gridList'
 */
function clearData(element) {
    var element = element || GridElement;
    $(element).datagrid('loadData',{total:0,rows:[]});
}
/**
 * 数据表格刷新
 * @param {number} [type]:接受0和1，0代表要刷新的对象为datagrid，1为treegrid，默认为0
 * @param {string} [element]:grid列表的元素id或class，默认为'.gridList'
 */
function reloadGrid(element) {
    var element = element || GridElement;
    var isTreeGrid = $(element).hasClass("jq-treegrid");
    if (isTreeGrid) {
        $(element).treegrid('reload');
    } else {
        $(element).datagrid('reload');
    }
}
// 删除
function updateItem(url,options) {
    var _default = {
        title: "确定要删除吗？"
    };
    var opts = $.extend({},_default,options);
    var title = opts.title;
    var gridId = opts.gridId;
    var gridType = opts.gridType;
    var param = opts.param;
    $.confirm (title,function(){
        $.post(url,param,function(data){
            $.alert(data.info, function() {
                reloadGrid(gridId);
            });
        },'json');
    });
}
// 更新数据
// function updateData(url,options) {
//     var title = options.title || "确定要删除吗？";
//     var param = options.param;
//     var gridId = options.gridId || GridElement;
//     var type = options.type || 0;
//     $.confirm (title,function(){
//         $.post(url,obj,function(data){
//             formHandle(data,'pop',type,gridId);
//         },'json');
//     })
// }

// 搜索
function searchItem(url,type,element){
    var type = type || 0;
    var element = element || GridElement;
    var val = url.split('wd=')[1];
    if (val == '') {
        $.alert("请输入搜索内容","info");
    } else {
        if (type == 0) {
            $(element).datagrid({
                url: url
            });
        } else {
            $(element).datagrid({
                url: url
            });
        }
    }
}
//采用正则表达式获取地址栏参数
function getUrlParam(name) {
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}
// 批量删除
function delItems(){
    var ids = [];
    var checked = $("#list_data").datagrid('getChecked');
    for(var i=0; i<checked.length; i++){
        ids.push(checked[i].id);
    }
    // 获取选中id
    alert(ids);
    var msg = '确定要删除所选的吗';
    if(checked && checked.length>0){
        $.confirm(1,msg+'?',function(){
            //ajax
        });
    }
}
// 全屏切换
function changeScreen(){
    if(MaxTab){
        $('body').layout('show','all');
        MaxTab = false;
        $('#tabToolsOne').linkbutton({iconCls:"icon-window-max",text:"全屏"});
    }else{
        $('body').layout('hidden','all');
        MaxTab = true;
        $('#tabToolsOne').linkbutton({iconCls:"icon-window-min",text:"正常"});
    }
}

// 打开选项卡(已经存在的则选中)
function addTabs(url,title){
    if (Tabsobj.tabs('exists', title)) {
        Tabsobj.tabs('select', title);
    }
    else {
        Tabsobj.tabs('add', {
            title: title,
            href: ZLZ.ROOT+'/'+url,
            //href: "../"+url,
            closable: true,
            cache: true,
            iconCls: "tree-file",
            bodyCls: 'content-tab-padding'
        });
    }
    // 清空该tab panel下的dom，用来解决不同页面id、函数名相同冲突问题
    // 弹出层的js函数名不能和主窗口重复，因为关闭弹出层没有panel，所以相同函数名没被覆盖
    if (Tabsobj.tabs("getSelected").panel("options").cache == false) {
        $("#content .panel:hidden .panel-body").html('');
    }
}
/**
* 返回datagrid操作单元格的操作元素
* 用于解决各页面用相同class造成点击事件遍历所有页面的问题
 */
function popHandle() {
    return $(PanelContent).find('.J_pop');
}
/**
* 截取datagrid下单元格文字过多的部分，并添加(明细)操作
 */
var handleWord = {
    cut: function(val,index,number,gridId) {
        if (val.length > number) {
            var v = val.substring(0,number);
            return v+'<a href="javascript:;" class="text-info J_openDetails" data-index='+index+'>(明细)</a>';
        } else {
            return val;
        }
    },
    look: function(gridId) {
        $(".J_openDetails").on('click',function(e) {
            e.stopPropagation();
            var index = $(this).data("index");
            var data = $(gridId).datagrid("getRows")[index];
            var $content = $('<div style="padding: 8px;">'+data.info+'</div>');
            $content.dialog({
                title: '查看明细',
                width: 600,
                modal: true,
                onClose: function() {
                    $(this).dialog('destroy');
                }
            });
        });
    }
};
/**
 * datagrid数据格式化
 */
 function dataFmt(v,r,i) {
    if (v == '' || typeof v === 'undefined') {
        v = '/';
    } 
    return v;
 }
 function isBool(v, r ,i) {
    v = v?"是":"否";
    return v;
 }
/**
 * jqui window(窗口)封装
 * $.dialog使用方法：$.dialog();参数与jqui原生的dialog一样
 * $.alert使用方法：$.alert(msg,icon,fn);或$.alert(msg,fn);
   --- @param {string} [msg]:提示信息内容
   --- @param {string} [icon]:提示框图标，图标样式有error/warning/info/question
   --- @param {funtion} [fn]:回调函数   
 * $.confirm使用方法：$.confirm(msg,fn);
   --- @param {string} [msg]:提示信息内容
   --- @param {funtion} [fn]:回调函数
 * $.prompt使用方法：$.prompt使用方法：(msg,fn);
   --- @param {string} [msg]:提示信息内容
   --- @param {funtion} [fn]:回调函数
 */
;(function(){
    // 加载表单的时候需要'form'
    $.dialog = function(options) {
        var _default = {
            minimizable: false,
            modal: true,
            collapsible: false,
            maximizable: false,
            onClose: function() {
                $(this).dialog('destroy');
            }
        };
        options = $.extend(_default, options);
        var dialog = $(options.el || "<div style='padding: 5px;'></div>");
        $.get(options.href, function(rsp) {
            try{
                var data = new Function('return'+rsp)();
                LoadingMask && loading.removeMsk();
                alert(data.info);
            }catch(e){
                LoadingMask && loading.removeMsk();
                options.href = null;
                dialog.dialog(options);
                var content = dialog.find('div.dialog-content');
                content.html($.fn.panel.defaults.extractor(rsp));
                dialog.dialog(options);
                if ($.parser) {
                    $.parser.parse(content);
                }
                if (options.onLoad) {
                    options.onLoad.call(dialog);
                }
            }
            
        }, "html");
        return dialog;
    };
    $.alert = function() {
        if (arguments.length === 1) {
            $.messager.alert("提示", arguments[0]);
        } else if (arguments.length === 2 && typeof arguments[1] === "string") {
            $.messager.alert("提示", arguments[0], arguments[1]);
        } else if (arguments.length === 2 && typeof arguments[1] === "function") {
            $.messager.alert("提示", arguments[0], "", arguments[1]);
        } else if (arguments.length === 3) {
            $.messager.alert("提示", arguments[0], arguments[1], arguments[2]);
        }
    };
    $.confirm = function(msg, fn) {
        $.messager.confirm("确认", msg, function(r){
            if (r){
                fn();
            };
        });
    };
    $.prompt = function(msg, fn) {
        $.messager.prompt("提示", msg, fn);
        // $.messager.prompt("提示", msg, function(r){
        //     if (r){
        //         fn(r);
        //     };
        // });
    }; 
})();
$.ajaxSetup({
	complete:function(XMLHttpRequest,textStatus){
		var accessStatus=XMLHttpRequest.getResponseHeader("Access-Status");
		if(accessStatus=="-1"){
			$.messager.alert("提示","登录超时,请重新登录!","warning",function(){
				window.location.reload();
			});
		}
	}
});

