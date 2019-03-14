
var sId=getCookie('LivingWaterSession');

function registerSolo(){
     
 var sId=getCookie('LivingWaterSession');
		  console.log("pushToServerNewMember");

var firstname=document.getElementById('firstname').value ;
var secondname=document.getElementById('secondname').value ;
var lastname=document.getElementById('lastname').value ;
var country=document.getElementById('country').value ;
var district=document.getElementById('district').value ;
var region=document.getElementById('region').value ;
var town=document.getElementById('town').value ;

if(document.getElementById('genderMale').checked){
	var gender="Male";
}
else{
	var gender="Female";
}

var passport=document.getElementById('passport').value ;
var INN=document.getElementById('INN').value ;
var boss=document.getElementById('boss').value ;
var phone=document.getElementById('phone').value ;

var category=document.getElementById('category').value;
var firstSong=document.getElementById('firstSong').value ;
var secondSong=document.getElementById('secondSong').value ;



	$.ajax({
			 type: 'POST',
			url: "/admin/newMember",
			data: JSON.stringify({                      lastname:lastname,
                                  						firstname:firstname,
                                  						sId:sId,
                                  						secondname:secondname,
                                  						command:"registerSolo",
                                  						country:country,
                                  						district:district,
                                  						region:region,
                                  						town:town,
                                  						genger:gender,
                                  						passport:passport,
                                  						INN:INN,
                                  						boss:boss,
                                  						phone:phone,
                                  						category:category,
                                  						firstSong:firstSong,
                                  						secondSong:secondSong}),
			success: function(data){
		console.log(data);
		document.getElementById("message").innerHTML=data.message;
		if(data.status=="200"){
				reset();
		}
		else{
			alert(data.message);
		}


		}
		  });



}

