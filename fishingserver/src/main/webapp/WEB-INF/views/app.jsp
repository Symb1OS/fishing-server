<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%> 

<html>
<head>
<title>Рыбалка</title>

<link rel="shortcut icon" href="<c:url value="/resources/images/icon.png"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext4/resources/css/ext-all-neptune.css"/>">
<script type="text/javascript" src="<c:url value="/resources/ext4/ext-all.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/ext4/locale/ext-lang-ru.js"/>"></script>
</head>

<body>

<script type="text/javascript">


Ext.onReady(function showWindow() {
	
	var menu = Ext.create('Ext.panel.Panel', {
		title : 'Меню',
		region : 'west',
		width : 210,
		minSize : 300,
		maxSize : 300,
		collapsible : true,
		split : false,
		bodyStyle : {
			'padding' : '5px'
		},
		layout: {	
			 type:  'vbox',
			 align: 'center',
			 pack:  'start'
		},
		defaults:{
			xtype: 'button',
			scale: 'large',
			width: 200,
			height: 40,
			style:{
				marginBottom: '10px'
			}
		},
		items : [
			{
	  			text:'Мониторинг',
	  			handler: function(){
	  				console.log('Мониторинг')	
	  			}
			},
			{
	  			text:'Настройки',
	  			handler: function(){
	  				console.log('Настройки')
	  			}
			},
			{
	  			text:'Выйти',
	  			handler: function(){
	  				console.log('Выйти')
	  			}
			}
		
		]

	});
	
	var main = Ext.create('Ext.panel.Panel', {
		xtype : 'form',
		region : 'center',
		width : 300,
		minSize : 300,
		maxSize : 300,
		collapsible : true,
		split : false,
		bodyStyle : {
			'padding' : '5px'
		},
		items : []

	});


	var viewport = Ext.create('Ext.container.Viewport', {
		layout : 'border',
		items : [menu, main]
	});
});

</script>

</body>
</html>