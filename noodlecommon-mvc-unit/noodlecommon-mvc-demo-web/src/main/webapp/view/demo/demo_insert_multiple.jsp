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
			
			if (trnId == 'INSERT_MULTIPLE') {
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
			
			var vo1 = new Object();
			
			vo1['id'] = '1';
			vo1['byteTest'] = 127;
			vo1['byteClassTest'] = 127;
			vo1['intTest'] = 2147483647;
			vo1['intClassTest'] = 2147483647;
			vo1['longTest'] = 223372036854775807;
			vo1['longClassTest'] = 223372036854775807;
			vo1['floatTest'] = 1.11111;
			vo1['floatClassTest'] = 1.11111;
			vo1['doubleTest'] = 1.11111;
			vo1['doubleClassTest'] = 1.11111;
			vo1['charTest'] = 'A';
			vo1['booleanTest'] = true;
			vo1['booleanClassTest'] = true;
			vo1['stringTest'] = 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA';
			//vo1['byteArrayTest'] = new Array(65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65);
			vo1['byteArrayTest'] = [65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65]
			//vo1['dateTest'] = '2014-12-02 16:49:18';
			//vo1['dateTest'] = '2014-12-02 16:49:18.222';
			vo1['dateTest'] = new Date().Format("yyyy-MM-dd HH:mm:ss.SSS");
			
			var vo2 = new Object();
			
			vo2['id'] = '2';
			vo2['byteTest'] = 127;
			vo2['byteClassTest'] = 127;
			vo2['intTest'] = 2147483647;
			vo2['intClassTest'] = 2147483647;
			vo2['longTest'] = 223372036854775807;
			vo2['longClassTest'] = 223372036854775807;
			vo2['floatTest'] = 1.11111;
			vo2['floatClassTest'] = 1.11111;
			vo2['doubleTest'] = 1.11111;
			vo2['doubleClassTest'] = 1.11111;
			vo2['charTest'] = 'A';
			vo2['booleanTest'] = true;
			vo2['booleanClassTest'] = true;
			vo2['stringTest'] = 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA';
			//vo2['byteArrayTest'] = new Array(65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65);
			vo2['byteArrayTest'] = [65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65]
			//vo2['dateTest'] = '2014-12-02 16:49:18';
			//vo2['dateTest'] = '2014-12-02 16:49:18.222';
			vo2['dateTest'] = new Date().Format("yyyy-MM-dd HH:mm:ss.SSS");
			
			var jsonSet = new JsonSet();
			jsonSet.put('input1', vo1);
			jsonSet.put('input2', vo2);
			
			transaction({
				id: 'INSERT_MULTIPLE',
				url: '<%=request.getContextPath()%>/demo/insertmultiple',
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
