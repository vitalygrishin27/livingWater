

function logIn(){
//	Cookies.remove('LivingWaterSession');
 writeCookie("LivingWaterSession", "" ,{
    expires: -1
  });

	var sId=readCookie('LivingWaterSession');
	//document.cookie
	var user=document.getElementById('user').value ;
	var pass=document.getElementById('pass').value;
$.ajax({
			type: 'POST',
			url: "/adminLogin",
			data: JSON.stringify({userName:user, password:pass, sId:sId, command:"logIn"}),
			success: function(data){
				console.log(data);
				document.getElementById("message").innerHTML=data.message;
				if(data.status=="200"){
					writeCookie("LivingWaterSession",user);
					document.location.href="/admin";
					}
				}
		  });
		  }
