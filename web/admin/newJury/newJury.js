function registerNewJury(){		
	var sId=getCookie('LivingWaterSession');
	console.log("addNewJury");
  
  
fetch('/admin/jury', {
    method: 'POST',

    body: JSON.stringify({sId:sId, 
						command:"addNewJury",
						firstName: document.getElementById("firstName").value,
						secondName: document.getElementById("secondName").value,
						lastName: document.getElementById("lastName").value,
						office: document.getElementById("office").value,
						login: document.getElementById("login").value,
						password: document.getElementById("password").value,
						
	})


  })
  .then(response => response.json())
  .then(function (data) {
    document.getElementById("message").innerHTML=data.message;
	console.log('Request succeeded with JSON response', data);
	})
	.catch(function(){
		document.getElementById("message").innerHTML="Ошибка регистрации. Обратитесь к администратору.";
	console.log('Request succeeded with JSON response ERROR', data);
	});
 
  }