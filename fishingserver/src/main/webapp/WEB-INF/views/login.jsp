<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%> 

<html>
<head>
<title>Авторизация</title>

<link rel="shortcut icon" href="<c:url value="/resources/images/icon.png"/>">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/ext4/resources/css/ext-all-neptune.css"/>">
<script type="text/javascript" src="<c:url value="/resources/ext4/ext-all.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/ext4/locale/ext-lang-ru.js"/>"></script>
</head>

<body>

<script type="text/javascript">

function submitForm() {
	var userField = Ext.getCmp('j_username');
	var passField = Ext.getCmp('j_password');
	var submitBtt = Ext.getCmp('submitBtt');
	
	userField.disable();
	passField.disable();
	submitBtt.disable();
	
	Ext.Ajax.request({
   		url: 'j_spring_security_check',
	    method: 'POST', 
    	params: {
        	'username'				 : userField.getValue(),
        	'password'				 : passField.getValue(),
        	"${_csrf.parameterName}" : "${_csrf.token}"
    	},
    	success: function(response){
    		location.href = 'app'
		},
		failure: function(form, action) {
			Ext.Msg.alert('Ошибка авторизации', 'Ошибка соединения с сервером', function(btn, text){
				if (btn == 'ok') {
                	userField.enable();
					passField.enable();
					submitBtt.enable();
				} else {
                	return false;
				}
   			});  
		}
	});
}

Ext.onReady( 
function showWindow(){
	
	var win = Ext.create('Ext.window.Window', {
		title: 'Авторизация',
		closable: false,
		resizable: false,
		draggable: false,
		bodyStyle: {
			'padding': '15px'
		},
		items: [{
				xtype: 'textfield',
				name: 'j_username',
				id: 'j_username',
				labelStyle: 'width:115px;',
				fieldLabel: 'Имя пользователя',
				listeners: {
            		specialkey: function(field, e){
                		if (e.getKey() == e.ENTER) {
                    		submitForm();
                		}
            		}
        		}
        	},{
				xtype: 'textfield',
				name: 'j_password',
				id: 'j_password',
				labelStyle: 'width:115px;',
            	inputType: 'password',
				fieldLabel: 'Пароль',
				listeners: {
            		specialkey: function(field, e){
                		if (e.getKey() == e.ENTER) {
                    		submitForm();
                		}
            		}
        		}
        	}
		],
		buttons: [{
				xtype: 'button',
				text: 'Вход',
				id: 'submitBtt',
				handler: function (btn, evt) { 
					submitForm();
				}
			}
		]
	}).show();  
			
	var viewport = Ext.create('Ext.container.Viewport', {
		style : 'background-image:url(resources/images/background.jpg);background-repeat: no-repeat;filter:"progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=\'scale\')";-moz-background-size:100% 100%;background-size:100% 100%;'
	});
	
});	
</script>   


</body>
</html>