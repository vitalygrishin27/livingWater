//<script type="text/babel">

$(document).ready(function() {
    online();
	
});



	function online(){
	setInterval(()=> getInfo(), 60000)}
	
	
function getInfo(){		
	var sId=getCookie('LivingWaterSession');
  console.log("test");
  
  
fetch('/user/online', {
    method: 'POST',

    body: JSON.stringify({sId:sId, command:"online"})


  })
  .then(response => response.json())
  .then(function (data) {
    document.getElementById("messageAboutOnline").innerHTML=data.message;
	if(data.message="ONLINE"){
		document.getElementById("messageAboutOnline").style.backgroundColor="green";
		document.getElementById("connect").style.display="none";
		
	}
	
	console.log('Request succeeded with JSON response', data);
	})
	.catch(function(){
		document.getElementById("messageAboutOnline").innerHTML="OFF-line.";
		document.getElementById("messageAboutOnline").style.backgroundColor="red";
		document.getElementById("connect").style.display="block";
	console.log('Request succeeded with JSON response', data);
	});
 
  }
