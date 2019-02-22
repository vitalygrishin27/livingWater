$(document).ready(function() {
    getCountOfJuries()
	//hiddenDivIfEmpty()

})


function getCountOfJuries(){		
	var sId=getCookie('LivingWaterSession');
  console.log("function - getCountOfJyries");
  
  
fetch('/admin/online', {
    method: 'POST',

    body: JSON.stringify({sId:sId, command:"getCountOfJuries"})


  })
  .then(response => response.json())
  .then(function (data) {
   


 //  document.getElementById("messageAboutOnline").innerHTML=data.message;
	console.log(data.message);
	console.log(data);
	
//	for(var i=1; i<=data.countOfJuries; i++){
	//console.log(data.jury_+{i}+lastName);
	//fillPageWithNameOfJuries(i,data.jury_&{i}&_lastName);
	
	//alert(jury_3_name.value);
	//if(data.jury_3_name.value==null){
//		alert("kugiug");
//	}
	
	document.getElementById("jury_1_name").innerHTML=data.jury_1_name;
	document.getElementById("jury_2_name").innerHTML=data.jury_2_name;
	document.getElementById("jury_3_name").innerHTML=data.jury_3_name;
	document.getElementById("jury_4_name").innerHTML=data.jury_4_name;
	document.getElementById("jury_5_name").innerHTML=data.jury_5_name;
	document.getElementById("jury_6_name").innerHTML=data.jury_6_name;
	document.getElementById("jury_7_name").innerHTML=data.jury_7_name;
	document.getElementById("jury_8_name").innerHTML=data.jury_8_name;

	document.getElementById("jury_1_office").innerHTML=data.jury_1_office;
	document.getElementById("jury_2_office").innerHTML=data.jury_2_office;
	document.getElementById("jury_3_office").innerHTML=data.jury_3_office;
	document.getElementById("jury_4_office").innerHTML=data.jury_4_office;
	document.getElementById("jury_5_office").innerHTML=data.jury_5_office;
	document.getElementById("jury_6_office").innerHTML=data.jury_6_office;
	document.getElementById("jury_7_office").innerHTML=data.jury_7_office;
	document.getElementById("jury_8_office").innerHTML=data.jury_8_office;
	
	//}
	var result=data.jury_2_office;
	
	
	
	}).then(function(result){
		alert("iugiuh");
	})
	
	
	
	
	.catch(function(){
		document.getElementById("messageAboutOnline").innerHTML="Внимание. Статус - OFFline.";
	console.log('Request succeeded with JSON response', data);
	});
 
  }
  function hiddenDivIfEmpty(){
	  alert("end");
  }
  
 // function fillPageWithNameOfJuries(i,name,office){
//	  alert(i,name,office);
 // }
  
