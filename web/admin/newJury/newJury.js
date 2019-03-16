function registerNewJury(){		
	var sId=getCookie('LivingWaterSession');
	console.log("addNewJury");
  

  $.ajax({
  			 type: 'POST',
  			url: "/admin/newJury",
  			data: JSON.stringify({                      sId:sId,
                                  						command:"addNewJury",
                                  						firstName: document.getElementById("firstName").value,
                                  						secondName: document.getElementById("secondName").value,
                                  						lastName: document.getElementById("lastName").value,
                                  						office: document.getElementById("office").value,
                                  						userName: document.getElementById("login").value,
                                  						password: document.getElementById("password").value}),
  			success: function(data){
  		console.log(data);
  		document.getElementById("message").innerHTML=data.message;
  		if(data.status=="200"){
  				reset();
  		}
  		else{
  			alert(data.message);
  		}


  		},
  		error: function(){
  		document.getElementById("message").innerHTML="Ошибка регистрации. Обратитесь к администратору.";
        	console.log('Request succeeded with JSON response ERROR', data);
  		}
  		  });


  }