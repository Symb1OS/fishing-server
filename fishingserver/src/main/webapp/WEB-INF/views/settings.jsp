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

Ext.onReady(function(){	
	
var hashPanel = Ext.create('Ext.form.Panel', {
	title: 'Ключ продукта',
	height: 120,
	defaults:{
		width: 500,
		labelWidth : 120,
		editable: false,
	},
	items: [{
		xtype: 'textfield',
		id: 'licence_key',
		fieldLabel : 'Ключ:',
		
	},{
		xtype: 'textfield',
		id: 'date_valid',
		fieldLabel : 'Действителен до:'
		
	}],
	listeners: {
		beforeshow : function( form, eOpts ){
				
			Ext.Ajax.request({
           		url: 'loadHash',
        	    method: 'GET', 
            	success: function(response){
            		var data= Ext.JSON.decode(response.responseText);
            		Ext.getCmp("licence_key").setValue(data.licence_key);
    			    Ext.getCmp("date_valid").setValue(data.date_valid);
        		},
        		failure: function(response) {
            		Ext.Msg.alert('Ошибка', 'Внутрення ошибка'); 
        		}
        	})
        	
		}
	}
	
})

hashPanel.show();

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
	            		var data= Ext.JSON.decode(response.responseText)
	            		var status = data.success;
	            		var message = data.message;
	            		
	            		Ext.getCmp('oldPassword').setValue('');
	            		Ext.getCmp('newPassword').setValue('');
	            		Ext.getCmp('newRepeatPassword').setValue('');
	            		
	            		Ext.Msg.alert("Статус", message);
	        		},
	        		failure: function(response) {
	            		Ext.Msg.alert('Ошибка', 'Внутрення ошибка'); 
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
})

});

</script>

</body>
</html>