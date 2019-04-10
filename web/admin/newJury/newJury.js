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


  $(document).ready(function(){
 //  document.getElementById("updateSolo").style.display="none";
 //     document.getElementById("deleteSolo").style.display="none";
 //      document.getElementById("updateEnsemble").style.display="none";
 //      document.getElementById("deleteEnsemble").style.display="none";



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