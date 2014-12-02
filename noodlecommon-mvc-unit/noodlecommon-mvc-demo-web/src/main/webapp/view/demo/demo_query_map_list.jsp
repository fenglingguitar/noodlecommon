<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>demo-query-page</title>
       
    <link href="<%=request.getContextPath()%>/common/tool/jqgrid/css/jquery-ui.css" rel="stylesheet" type="text/css" />
    <link href="<%=request.getContextPath()%>/common/tool/jqgrid/css/ui.jqgrid.css" rel="stylesheet" type="text/css" />
    
    <script src="<%=request.getContextPath()%>/common/tool/jqgrid/js/jquery-1.8.2.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/common/tool/jqgrid/js/jquery-ui-1.9.1.custom.min.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/common/tool/jqgrid/js/i18n/grid.locale-en.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/common/tool/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/common/js/common.js" type="text/javascript"></script>
	
    <script type="text/javascript">
    	
	    function callback(trnId, data) {
			
			if (trnId == 'QUERY_MAP_LIST') {
				for(var i=0; i<=data['input'].length; i++) {					
					jQuery("#list1").jqGrid('addRowData', i+1, data['input'][i]);
				}
				for(var i=0; i<=data['output'].length; i++) {					
					jQuery("#list2").jqGrid('addRowData', i+1, data['output'][i]);
				}
			} 
		}
    
		function init() {
			
			$('#list1').jqGrid({
			   	colNames: [
					'ID',
					'字节',
					'字节类',
					'整型',
					'整型类',
					'长整型',
					'长整类类',
					'浮点型',
					'浮点型类',
					'双精度',
					'双精度类',
					'字符型',
					'布尔型',
					'布尔型类',
					'字符串',
					'字节数组',
					'日期'
					],
			   	colModel: [
					{name:'id', index:'id', width:120, align:'center'},
					{name:'byteTest', index:'byteTest', width:120, align:'center'},
					{name:'byteClassTest', index:'byteClassTest', width:120, align:'center'},
					{name:'intTest', index:'intTest', width:120, align:'center'},
					{name:'intClassTest', index:'intClassTest', width:120, align:'center'},
					{name:'longTest', index:'longTest', width:180, align:'center'},
					{name:'longClassTest', index:'longClassTest', width:180, align:'center'},
					{name:'floatTest', index:'floatTest', width:120, align:'center'},
					{name:'floatClassTest', index:'floatClassTest', width:120, align:'center'},
					{name:'doubleTest', index:'doubleTest', width:180, align:'center'},
					{name:'doubleClassTest', index:'doubleClassTest', width:180, align:'center'},
					{name:'charTest', index:'charTest', width:80, align:'center'},
					{name:'booleanTest', index:'booleanTest', width:80, align:'center'},
					{name:'booleanClassTest', index:'booleanClassTest', width:80, align:'center'},
					{name:'stringTest', index:'stringTest', width:200, align:'center'},
					{name:'byteArrayTest', index:'byteArrayTest', width:200, align:'center'},
					{name:'dateTest', index:'dateTest', width:200, align:'center', formatter:'date', formatoptions:{srcformat:'Y-m-d H:i:s.sss', newformat:'Y-m-d H:i:s'}},

			   	],
			   	sortname: 'id',
			    viewrecords: true,
			    autowidth: true,
			    shrinkToFit: false,
			    height: 248,
			    sortorder: 'desc',
			    multiselect: true,
			    jsonReader: {
					repeatitems : false
				},
				ondblClickRow: false,
			    caption: '查询结果'
			});
				
			$('#list2').jqGrid({
			   	colNames: [
					'ID',
					'字节',
					'字节类',
					'整型',
					'整型类',
					'长整型',
					'长整类类',
					'浮点型',
					'浮点型类',
					'双精度',
					'双精度类',
					'字符型',
					'布尔型',
					'布尔型类',
					'字符串',
					'字节数组',
					'日期'
					],
			   	colModel: [
					{name:'id', index:'id', width:120, align:'center'},
					{name:'byteTest', index:'byteTest', width:120, align:'center'},
					{name:'byteClassTest', index:'byteClassTest', width:120, align:'center'},
					{name:'intTest', index:'intTest', width:120, align:'center'},
					{name:'intClassTest', index:'intClassTest', width:120, align:'center'},
					{name:'longTest', index:'longTest', width:180, align:'center'},
					{name:'longClassTest', index:'longClassTest', width:180, align:'center'},
					{name:'floatTest', index:'floatTest', width:120, align:'center'},
					{name:'floatClassTest', index:'floatClassTest', width:120, align:'center'},
					{name:'doubleTest', index:'doubleTest', width:180, align:'center'},
					{name:'doubleClassTest', index:'doubleClassTest', width:180, align:'center'},
					{name:'charTest', index:'charTest', width:80, align:'center'},
					{name:'booleanTest', index:'booleanTest', width:80, align:'center'},
					{name:'booleanClassTest', index:'booleanClassTest', width:80, align:'center'},
					{name:'stringTest', index:'stringTest', width:200, align:'center'},
					{name:'byteArrayTest', index:'byteArrayTest', width:200, align:'center'},
					{name:'dateTest', index:'dateTest', width:200, align:'center', formatter:'date', formatoptions:{srcformat:'Y-m-d H:i:s.sss', newformat:'Y-m-d H:i:s'}},

			   	],
			   	sortname: 'id',
			    viewrecords: true,
			    autowidth: true,
			    shrinkToFit: false,
			    height: 248,
			    sortorder: 'desc',
			    multiselect: true,
			    jsonReader: {
					repeatitems : false
				},
				ondblClickRow: false,
			    caption: '查询结果'
			});
			
			$(window).resize(function(){ 
				$("#list1").setGridWidth($(window).width() - 14);
				$("#list2").setGridWidth($(window).width() - 14);
			});
			
			query();
		}
		
		function query() {
			
			var vo = new Object();
			
			vo['id'] = '1';
			vo['byteTest'] = 127;
			vo['byteClassTest'] = 127;
			vo['intTest'] = 2147483647;
			vo['intClassTest'] = 2147483647;
			vo['longTest'] = 223372036854775807;
			vo['longClassTest'] = 223372036854775807;
			vo['floatTest'] = 1.11111;
			vo['floatClassTest'] = 1.11111;
			vo['doubleTest'] = 1.11111;
			vo['doubleClassTest'] = 1.11111;
			vo['charTest'] = 'A';
			vo['booleanTest'] = true;
			vo['booleanClassTest'] = true;
			vo['stringTest'] = 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA';
			//vo['byteArrayTest'] = new Array(65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65);
			vo['byteArrayTest'] = [65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65]
			//vo['dateTest'] = '2014-12-02 16:49:18';
			//vo['dateTest'] = '2014-12-02 16:49:18.222';
			vo['dateTest'] = new Date().Format("yyyy-MM-dd HH:mm:ss.SSS");
			
			var jsonSet = new JsonSet();
			jsonSet.put('input', vo);
			
			transaction({
				id: 'QUERY_MAP_LIST',
				url: '<%=request.getContextPath()%>/demo/querymaplist',
				jsonSet: jsonSet
			});
		}
	</script>
  </head>

  <body onload="init();" onkeydown="onEnterDown(query);" >
	<div id="list_div_1" style="width:auto;margin-bottom:10px;">
		<table id="list1"></table>
	</div>
	<div id="list_div_2" style="width:auto;">
		<table id="list2"></table>
	</div>
  </body>
</html>
