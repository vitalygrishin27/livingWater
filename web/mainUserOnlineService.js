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
	console.log('Request succeeded with JSON response', data);
	})
	.catch(function(){
		document.getElementById("messageAboutOnline").innerHTML="Внимание. Статус - OFFline.";
	console.log('Request succeeded with JSON response', data);
	});
 
  }
