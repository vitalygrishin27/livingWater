
function registerNewJury(){
	 document.getElementById("registerJury").style.display="none";
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
  		document.getElementById("registerJury").style.display="block";
  		if(data.status=="201"){
  				clearForm();
  				updateListJury();
  		}
  		else{
  			alert(data.message);
  		}


  		},
  		error: function(){
  		document.getElementById("message").innerHTML="Ошибка регистрации. Обратитесь к администратору.";
        	console.log('Request succeeded with JSON response ERROR', data);
        	 document.getElementById("registerJury").style.display="block";
  		}
  		  });

  }


  $(document).ready(function(){
 document.getElementById("registerJury").style.display="block";
  document.getElementById("deleteJury").style.display="none";
  document.getElementById("updateJury").style.display="none";
 //document.getElementById("clear").style.display="none";



  updateListJury();


  });


  function updateListJury(){
  var sId=getCookie('LivingWaterSession');
  $("#combobox").empty();
  var emptyOption=document.createElement("option");
  emptyOption.value="";
  emptyOption.innerHTML="";
  emptyOption.id=-1;
  document.getElementById("combobox").appendChild(emptyOption);

  $.ajax({
  			 type: 'GET',
  			url: "/admin/gets",
  			data: {sId:sId,command:"getListOfJuriesOnlyNames"},
  			success: function(data){
       		console.log(data);

    for(var i=0; i<data.name.length; i++){
       console.log(data.name[i]);

  var jury=document.createElement("option");
  jury.value=data.name[i];
  jury.innerHTML=data.name[i];
  jury.id=data.id[i];
  document.getElementById("combobox").appendChild(jury);
       }

  }
  		  });
  }


  $('#combobox').on('change', function() {
  var sId=getCookie('LivingWaterSession');
  if($(this).children(":selected").attr("id")!=-1){
 // document.getElementById("login").setAttribute('disabled', 'disabled');

  //document.getElementById('mytext').setAttribute('disabled', 'disabled');
   document.getElementById("registerJury").style.display="none";
      document.getElementById("updateJury").style.display="block";
        document.getElementById("deleteJury").style.display="block";

      $.ajax({
  			 type: 'GET',
  			url: "/admin/gets",
  			data: {sId:sId,command:"getJuryInformation", idJury:$(this).children(":selected").attr("id")},
  			success: function(data){

      console.log(data);

  document.getElementById('idJuryForUpdateOrDelete').innerHTML=data.userName;

  document.getElementById('firstName').value=data.firstName;
  document.getElementById('secondName').value=data.secondName ;
  document.getElementById('lastName').value=data.lastName;
  document.getElementById('office').value=data.office;
  document.getElementById('login').value=data.userName;
  document.getElementById('password').value=data.password;


  }
  		  });

  }else{
   document.getElementById("updateJury").style.display="none";
     document.getElementById("deleteJury").style.display="none";
     document.getElementById("registerJury").style.display="block";

  document.getElementById('idJuryForUpdateOrDelete').innerHTML=-1;

  }



  });



function clearForm(){
console.log("clearForm");
$('#combobox').get(0).selectedIndex = 0;

    document.getElementById("updateJury").style.display="none";
     document.getElementById("deleteJury").style.display="none";
     document.getElementById("registerJury").style.display="block";

document.getElementById('idJuryForUpdateOrDelete').innerHTML=-1;

var elements = document.getElementsByTagName("input");
for (var i = 0, element; element = elements[i++];) {
    if (element.type === "text" ||  element.type ==="password")
        element.value = "";
}

}



function delJury(){
    var sId=getCookie('LivingWaterSession');

	if(confirm("Вы уверены в удалении?")){
	console.log("deleteJury");
            $.ajax({
			    type: 'POST',
			    url: "/admin/newJury",
			    data: JSON.stringify({sId:sId, command:"deleteJury",idJury:document.getElementById('idJuryForUpdateOrDelete').innerHTML}),
                success: function(data){
		                console.log(data);
		                document.getElementById("message").innerHTML=data.message;
		                if(data.status=="200"){
				            clearForm();
				            updateListJury();
		                }
		                else{
			                alert(data.message);
		                }

		        }
		     });
		     }

}

function updJury(){
	var sId=getCookie('LivingWaterSession');
	console.log("updateJury");

    $.ajax({
  			type: 'POST',
  			url: "/admin/newJury",
  			data: JSON.stringify({  sId:sId,
                                  	command:"updateJury",
                                  	idOldJury:document.getElementById('idJuryForUpdateOrDelete').innerHTML,
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
  				clearForm();
  				updateListJury();
  		}
  		else{
  			alert(data.message);
  		}


  		},
  		error: function(){
  		document.getElementById("message").innerHTML="Ошибка обновления. Обратитесь к администратору.";
        	console.log('Request succeeded with JSON response ERROR', data);

  		}
  		  });

clearForm();
updateListJury();
  }