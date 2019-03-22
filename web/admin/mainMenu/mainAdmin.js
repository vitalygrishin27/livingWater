function createXLSFilesStatement(){
    var sId=getCookie('LivingWaterSession');
	console.log("createXLSFilesStatement");


$.ajax({
  			 type: 'POST',
  			url: "/admin/statement",
  			data: JSON.stringify({ sId:sId}),

  			success: function(data){
  		console.log(data);
  		alert(data.message);

  		},
  		error: function(){
  		    alert("Произошла ошибка на сервере.")
        	console.log('Request succeeded with JSON response ERROR', data);
  		}
  		  });

}