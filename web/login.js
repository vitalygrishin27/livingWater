//<script type="text/babel">
var sId=getCookie('LivingWaterSession');

function logIn(){
     
//var sId=readCookie('sessionId');

var user=document.getElementById('user').value ;
var pass=document.getElementById('pass').value;

document.getElementById('user').value ="";
document.getElementById('pass').value="";

fetch('/login', {
    method: 'POST',

    body: JSON.stringify({userName:user, password:pass, sId:sId, command:"logIn"})


  })
  .then(response => response.json())
  .then(function (data) {
    document.getElementById("message").innerHTML=data.message;
	console.log('Request succeeded with JSON response', data);
	
	
	
		if(data.status=="ADMIN"){
	//	writeCookie('sessionId', data.sId,3)
	//	document.location.href="/admin/registration/member";
		document.location.href="/admin";
		}
			if(data.status=="MANAGER"){
			//	alert("3f2323r32");
	//	writeCookie('sessionId', data.sId,3)
	//	document.location.href="/admin/registration/member";
		document.location.href="/manager";
		}
		if(data.status=="USER"){
	//	writeCookie('sessionId', data.sId,3)
		document.location.href="/user";
		}
	
	
//	document.getElementById("message").innerHTML=data.message;
	
	
	//document.location.href="/admin";
	
	});
 

}

//</script>