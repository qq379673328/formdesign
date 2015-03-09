var app = angular.module('app', ['ngRoute', 'page'], function($httpProvider){
	$httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
	
	/**
	   * The workhorse; converts an object to x-www-form-urlencoded serialization.
	   * @param {Object} obj
	   * @return {String}
	   */ 
	  var param = function(obj) {
	    var query = '', name, value, fullSubName, subName, subValue, innerObj, i;
	      
	    for(name in obj) {
	      value = obj[name];
	        
	      if(value instanceof Array) {
	        for(i=0; i<value.length; ++i) {
	          subValue = value[i];
	          fullSubName = name + '[' + i + ']';
	          innerObj = {};
	          innerObj[fullSubName] = subValue;
	          query += param(innerObj) + '&';
	        }
	      }
	      else if(value instanceof Object) {
	        for(subName in value) {
	          subValue = value[subName];
	          fullSubName = name + '[' + subName + ']';
	          innerObj = {};
	          innerObj[fullSubName] = subValue;
	          query += param(innerObj) + '&';
	        }
	      }
	      else if(value !== undefined && value !== null)
	        query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
	    }
	      
	    return query.length ? query.substr(0, query.length - 1) : query;
	  };
	 
	  // Override $http service's default transformRequest
	  $httpProvider.defaults.transformRequest = [function(data) {
	    return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
	  }];
	  
});

app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider
	.when('/', {
		templateUrl : 'views/formdesign/list.html',
		controller : 'FormDesignListCtrl'
	}).when('/formdesign', {
		templateUrl : 'views/formdesign/list.html',
		controller : 'FormDesignListCtrl'
	}).when('/formdesignedit/:id', {
		templateUrl : 'views/formdesign/design.html',
		controller : 'FormDesignCtrl'
	}).when('/formdesignedit', {
		templateUrl : 'views/formdesign/design.html',
		controller : 'FormDesignCtrl'
	}).when('/forminput', {
		templateUrl : 'views/forminput/list.html',
		controller : 'FormInputCtrl'
	}).when('/formdatalist/:formid', {
		templateUrl : 'views/formdesign/formdatalist.html',
		controller : 'FormDataListCtrl'
	}).otherwise({
		redirectTo : '/'
	});
} ]);

//html过滤
app.filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    };
});

//表单列表
app.controller('FormDesignListCtrl',function($scope, $http) {
	
	$scope.pageConf = {
        currentPage: 1,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
    		$scope.reload();
        }
    };
	/*
	 * 加载数据
	 */
	$scope.reload = function(){
		var	pageParams = {page: $scope.pageConf.currentPage, rows: $scope.pageConf.itemsPerPage};
		$http.post("formdesign/getforms", pageParams).success(function(data){
			$scope.forms = data.pagingResult.rows;
			$scope.pageConf.totalItems = data.pagingResult.total;
			$scope.pageConf.numberOfPages = Math.ceil(data.pagingResult.total/$scope.pageConf.itemsPerPage);
		});
	};
	/*
	 * 发布
	 */
	$scope.pub = function(id){
		$http.post("formdesign/pub", {id: id}).success(function(data){
			$scope.reload();
		});
	};
	/*
	 * 撤销发布
	 */
	$scope.canclepub = function(id){
		$http.post("formdesign/canclepub", {id: id}).success(function(data){
			$scope.reload();
		});
	};
	/*
	 * 删除
	 */
	$scope.del = function(id){
		$http.post("formdesign/del", {id: id}).success(function(data){
			$scope.reload();
		});
	};
});
//表单数据列表
app.controller('FormDataListCtrl',function($scope, $http, $routeParams) {
	$scope.pageConf = {
			currentPage: 1,
			itemsPerPage: 10,
			perPageOptions: [10, 20, 30, 40, 50],
			onChange: function(){
				$scope.reload();
			}
	};
	
	/*
	 * 加载表单信息
	 */
	$http.post("formdesign/getformbyid", {id: $routeParams.formid}).success(function(data){
		 $scope.formtitle = data.formResult.data.title;
		 $scope.formdtcreate = data.formResult.data.dtCreate;
	 });
	
	/*
	 * 加载表单列表数据
	 */
	 $scope.reload = function(){
		 var pageParams = {
				 page: $scope.pageConf.currentPage, 
				 rows: $scope.pageConf.itemsPerPage,
				 formid: $routeParams.formid};
		 $http.post("formdesign/getformdatas", pageParams).success(function(data){
			 $scope.formdatas = data.pagingResult.rows;
			 $scope.pageConf.totalItems = data.pagingResult.total;
			 $scope.pageConf.numberOfPages = Math.ceil(data.pagingResult.total/$scope.pageConf.itemsPerPage);
		 });
	 };
	 /*
	 * 删除
	 */
	$scope.del = function(id){
		$http.post("formdesign/formdatadel", {id: id}).success(function(data){
			$scope.reload();
		});
	};
});
//表单设计页面
app.controller('FormDesignCtrl',function($scope, $routeParams, $http) {
	UE.delEditor('formdesigncontainer');
	var ue = null;
	ue = UE.getEditor('formdesigncontainer',{
		toolleipi: true,
		toolbars:[['fullscreen',
		           'undo', 'redo', '|',
		           'bold', 'italic', 'underline', 'fontborder', 'strikethrough',  'removeformat', '|',
		           'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist','|',
		           'fontfamily', 'fontsize', '|',
		           'indent', '|',
		           'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|',
		           'link', 'unlink',  '|',
		           'horizontal',  'spechars',  'wordimage', '|',
		           'inserttable', 'deletetable',  'mergecells',  'splittocells', '|']],
		           wordCount:false,//关闭字数统计
		           elementPathEnabled:false//关闭elementPath
	});
	ue.addListener( 'ready', function( editor ) {
		if($routeParams.id){
			//字段数
			$http.post("formdesign/getformbyid", {id: $routeParams.id}).success(function(data){
				$scope.title = data.formResult.data.title;
				ue.execCommand('focus');
				ue.setContent(data.formResult.data.contentSrc);
			});
		}
	});
	//加载控件
	$scope.exec = function(method){
		ue.execCommand(method);
	};
	//保存
	$scope.save = function(type){
		if(confirm("确认保存？注意：保存之后原始表单以及对应表单已上报数据会清空。") == false) return;
		
		if(ue.queryCommandState( 'source' ))
			ue.execCommand('source');//切换到编辑模式才提交，否则有bug
		if($scope.title === undefined) {
			alert("标题不能为空");
			return;
		}
        if(ue.hasContents()){
        	ue.sync();/*同步内容*/
            //获取表单设计器里的内容
            var contentSrc = ue.getContent();
            //解析表单设计器控件
            var formContent = JSON.parse(parseForm(contentSrc, 0)).parse;
            $http.post("formdesign/add", {
            		'title': $scope.title,
            		'content': formContent,
            		'contentSrc': contentSrc,
            		'id': $routeParams.id
            	}).success(function(data){
            		if(data.formResult.success == "0"){
            			alert(data.formResult.message);
            		}else{
            			alert('表单保存成功');
            		}
	            }).error(function(){
	            	alert("表单保存失败");
	            });
        } else {
            alert('表单为空！');
        }
	};
	//预览
	$scope.preview = function(method){
		if(ue.queryCommandState( 'source' ))
            ue.execCommand('source');/*切换到编辑模式才提交，否则部分浏览器有bug*/
        if(ue.hasContents()){
            ue.sync();       /*同步内容*/
            //获取表单设计器里的内容
            var content = ue.getContent();
            //解析表单设计器控件
            var formContent = parseForm(content, 0);
            $scope.designZone = JSON.parse(formContent).parse;
            $scope.isDesignZoneActive = true;
        } else {
            alert('表单内容不能为空！');
            return false;
        }
	};
	//解析表单
	function parseForm(template, fields){
		var preg =  /(\|-<span(((?!<span).)*plugins=\"(radios|checkboxs|select)\".*?)>(.*?)<\/span>-\||<(img|input|textarea|select).*?(<\/select>|<\/textarea>|\/>))/gi,
			preg_attr =/(\w+)=\"(.?|.+?)\"/gi,
			preg_group =/<input.*?\/>/gi;
        if(!fields) fields = 0;
        var template_parse = template,template_data = new Array(),add_fields=new Object(),checkboxs=0;

        var pno = 0;
        template.replace(preg, function(plugin,p1,p2,p3,p4,p5,p6){
            var parse_attr = new Array(),
            	attr_arr_all = new Object(),
            	name = '', 
            	select_dot = '' , 
            	is_new=false;
            var p0 = plugin;
            var tag = p6 ? p6 : p4;
            if(tag == 'radios' || tag == 'checkboxs'){
                plugin = p2;
            }else if(tag == 'select'){
                plugin = plugin.replace('|-','');
                plugin = plugin.replace('-|','');
            }
            plugin.replace(preg_attr, function(str0,attr,val) {
                if(attr=='name'){
                    if(val=='NEWFIELD'){
                        is_new=true;
                        fields++;
                        val = 'data_'+fields;
                    }
                    name = val;
                }
                if(tag=='select' && attr=='value'){
                    if(!attr_arr_all[attr]) attr_arr_all[attr] = '';
                    attr_arr_all[attr] += select_dot + val;
                    select_dot = ',';
                }else{
                    attr_arr_all[attr] = val;
                }
                var oField = new Object();
                oField[attr] = val;
                parse_attr.push(oField);
            });
            if(tag =='checkboxs') /*复选组  多个字段 */{
                plugin = p0;
                plugin = plugin.replace('|-','');
                plugin = plugin.replace('-|','');
                var name = 'checkboxs_'+checkboxs;
                attr_arr_all['parse_name'] = name;
                attr_arr_all['name'] = '';
                attr_arr_all['value'] = '';
                attr_arr_all['content'] = '<span plugins="checkboxs"  title="'+attr_arr_all['title']+'">';
                var dot_name ='', dot_value = '';
                p5.replace(preg_group, function(parse_group) {
                    var is_new=false,option = new Object();
                    parse_group.replace(preg_attr, function(str0,k,val) {
                        if(k=='name'){
                        	if(val=='NEWFIELD'){
                                is_new=true;
                                fields++;
                                val = 'data_'+fields;
                            }
                            attr_arr_all['name'] += dot_name + val;
                            dot_name = ',';
                        }else if(k=='value'){
                            attr_arr_all['value'] += dot_value + val;
                            dot_value = ',';
                        }
                        option[k] = val;
                    });
                    if(!attr_arr_all['options']) attr_arr_all['options'] = new Array();
                    attr_arr_all['options'].push(option);
                    if(!option['checked']) option['checked'] = '';
                    var checked = option['checked'] ? 'checked="checked"' : '';
                    attr_arr_all['content'] +=
                    		'<input type="checkbox" name="'
                    		+option['name']
                    		+'" value="'+option['value']
                    		+'" fieldname="' + attr_arr_all['fieldname'] 
                    		+ option['fieldname'] 
                    		+ '"' + checked+'/>'+option['value']+'&nbsp;';
                    if(is_new){
                        var arr = new Object();
                        arr['name'] = option['name'];
                        arr['plugins'] = attr_arr_all['plugins'];
                        arr['fieldname'] = attr_arr_all['fieldname'] + option['fieldname'];
                        arr['fieldflow'] = attr_arr_all['fieldflow'];
                        add_fields[option['name']] = arr;
                    }
                });
                attr_arr_all['content'] += '</span>';
                //parse
                template = template.replace(plugin,attr_arr_all['content']);
                template_parse = template_parse.replace(plugin,'{'+name+'}');
                template_parse = template_parse.replace('{|-','');
                template_parse = template_parse.replace('-|}','');
                template_data[pno] = attr_arr_all;
                checkboxs++;
            }else if(name){
                if(tag =='radios'){ /*单选组  一个字段*/
                    plugin = p0;
                    plugin = plugin.replace('|-','');
                    plugin = plugin.replace('-|','');
                    attr_arr_all['value'] = '';
                    attr_arr_all['content'] = '<span plugins="radios" name="'+attr_arr_all['name']+'" title="'+attr_arr_all['title']+'">';
                    var dot='';
                    p5.replace(preg_group, function(parse_group) {
                        var option = new Object();
                        parse_group.replace(preg_attr, function(str0,k,val) {
                            if(k=='value'){
                                attr_arr_all['value'] += dot + val;
                                dot = ',';
                            }
                            option[k] = val;
                        });
                        option['name'] = attr_arr_all['name'];
                        if(!attr_arr_all['options']) attr_arr_all['options'] = new Array();
                        attr_arr_all['options'].push(option);
                        if(!option['checked']) option['checked'] = '';
                        var checked = option['checked'] ? 'checked="checked"' : '';
                        attr_arr_all['content'] +='<input type="radio" name="'+attr_arr_all['name']+'" value="'+option['value']+'"  '+checked+'/>'+option['value']+'&nbsp;';
                    });
                    attr_arr_all['content'] += '</span>';
                }else{
                    attr_arr_all['content'] = is_new ? plugin.replace(/NEWFIELD/,name) : plugin;
                }
                template = template.replace(plugin,attr_arr_all['content']);
                template_parse = template_parse.replace(plugin,'{'+name+'}');
                template_parse = template_parse.replace('{|-','');
                template_parse = template_parse.replace('-|}','');
                if(is_new){
                    var arr = new Object();
                    arr['name'] = name;
                    arr['plugins'] = attr_arr_all['plugins'];
                    arr['title'] = attr_arr_all['title'];
                    arr['orgtype'] = attr_arr_all['orgtype'];
                    arr['fieldname'] = attr_arr_all['fieldname'];
                    arr['fieldflow'] = attr_arr_all['fieldflow'];
                    add_fields[arr['name']] = arr;
                }
                template_data[pno] = attr_arr_all;
            }
            pno++;
        });
        var view = template.replace(/{\|-/g,'');
        view = view.replace(/-\|}/g,'');
        var parse_form = new Object({
            'fields':fields,//总字段数
            'template':template,//完整html
            'parse':view,
            'data':template_data,//控件属性
            'add_fields':add_fields//新增控件
        });
        return JSON.stringify(parse_form);
		
	}
	
});
//表单录入页面
app.controller('FormInputCtrl',function($scope, $routeParams) {
    $scope.id = $routeParams.id;
});