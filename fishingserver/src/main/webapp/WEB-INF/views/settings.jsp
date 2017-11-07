<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%> 

<html>
<head>
<title>Настройки</title>

<link rel="shortcut icon" href="<c:url value="/resources/images/icon.png"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext6/build/classic/theme-neptune/resources/theme-neptune-all.css"/>">
<script type="text/javascript" src="<c:url value="/resources/ext6/build/ext-all.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/ext6/build/classic/locale/locale-ru.js"/>"></script>
</head>
<body>

<script type="text/javascript">

var hashPanel = Ext.create('Ext.panel.Panel', {
	title: 'Ключ продукта',
	height: 80,
	items: [{
		xtype: 'textfield',
		editable: false,
		width: 500,
		value: 'bef1c08eedddbe9f9d83a0f07d0d26ce9b360a55'
	}]
})

var passwordPanel = Ext.create('Ext.panel.Panel', {
	title: 'Смена пароля',
	height: 200,
	defaults:{
		inputType: 'password',
		minLength: 8,
		width : 400,
		labelWidth : 170,
		allowBlank: false
	},
	items:[{
		xtype: 'textfield',
		id: 'oldPassword',
		fieldLabel: 'Старый пароль'
	},{
		xtype: 'textfield',
		id: 'newPassword',
		fieldLabel: 'Новый пароль',
		validator: function(val){
    		var oldPas = Ext.getCmp('oldPassword').value;
    		return oldPas != val ? true : "Новый пароль должен отличаться от старого!";
		} 
	},{
		xtype: 'textfield',
		id: 'newRepeatPassword',
		fieldLabel: 'Повторите новый пароль',
		validator: function(val){
    		var oldPas = Ext.getCmp('newPassword').value;
    		return oldPas == val ? true : "Пароли не совпадают"; 
		} 
	}],
	buttons:[
		{
			text: 'Сохранить',
			handler: function(){
				Ext.Ajax.request({
	           		url: 'changePassword',
	        	    method: 'POST', 
	            	params: {
	            		'oldPassword' : Ext.getCmp('oldPassword').value, 
	                	'password' : Ext.getCmp('newPassword').value,
	                	"${_csrf.parameterName}" : "${_csrf.token}"
	            	},
	            	success: function(response){
	            		console.log(response)
	            		var status = response.responseText.success;
	            		var message = response.responseText.message;
	            		Ext.Msg.alert(status, message);
	        		},
	        		failure: function(response) {
	        			console.log(response)
	        			var status = action.result.success;
	            		var message = action.result.message;
	            		Ext.Msg.alert(status, message); 
	        		}
	        	})
				
			}
		}
	]
})

var viewport = Ext.create('Ext.container.Viewport', {
	layout		: {
		type  : 'vbox',
		align : 'stretch',
		pack  : 'start'
	},
	defaults:{
		bodyPadding: 10,
		margin: '0 0 10 0'
	},
	items : [hashPanel, passwordPanel ]
});

</script>

</body>
</html>