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
			
			if (trnId == 'INSERT_ARRAY') {
				if (data.result == 'false') {
					alert('失败');
				} else {
					alert('成功');
				}
			} 
		}
    
		function init() {
			query();
		}
		
		function query() {
			
			var inputArray = [];
			
			for (var i=0; i<23; i++) {
				
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
				
				inputArray.push(vo);
			}
			
			var jsonSet = new JsonSet();
			jsonSet.put('input', inputArray);
			
			transaction({
				id: 'INSERT_ARRAY',
				url: '<%=request.getContextPath()%>/demo/insertarray',
				jsonSet: jsonSet
			});
		}
	</script>
  </head>

  <body onload="init();" onkeydown="onEnterDown(query);" >
	<div id="list_div" style="width:auto;">
		<table id="list"></table>
		<div id="pager"></div>
	</div>
  </body>
</html>
