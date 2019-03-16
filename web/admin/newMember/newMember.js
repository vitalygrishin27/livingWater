
var sId=getCookie('LivingWaterSession');

function registerSolo(){
     
 var sId=getCookie('LivingWaterSession');
		  console.log("pushToServerNewMemberSolo");

var firstname=document.getElementById('firstname').value ;
var secondname=document.getElementById('secondname').value ;
var lastname=document.getElementById('lastname').value ;
var birth=document.getElementById('birth').value ;

var country=document.getElementById('country').value ;
var district=document.getElementById('district').value ;
var region=document.getElementById('region').value ;
var city=document.getElementById('city').value ;

if(document.getElementById('genderMale').checked){
	var gender="M";
}
else{
	var gender="F";
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
                                  						birth:birth,
                                  						command:"registerSolo",
                                  						country:country,
                                  						district:district,
                                  						region:region,
                                  						city:city,
                                  						gender:gender,
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

function registerEnsemble(){

var sId=getCookie('LivingWaterSession');
		  console.log("pushToServerNewMemberEnsemble");

var ensembleName=document.getElementById('ensemble_name').value ;
var country=document.getElementById('ensemble_country').value ;
var district=document.getElementById('ensemble_district').value ;
var region=document.getElementById('ensemble_region').value ;
var city=document.getElementById('ensemble_city').value ;
var phone=document.getElementById('ensemble_phone').value ;

var countOfMembers=document.getElementById('ensemble_countOfMembers').value ;
var boss=document.getElementById('ensemble_boss').value ;
var category=document.getElementById('ensemble_category').value;
var firstSong=document.getElementById('ensemble_firstSong').value ;
var secondSong=document.getElementById('ensemble_secondSong').value ;



	$.ajax({
			 type: 'POST',
			url: "/admin/newMember",
			data: JSON.stringify({                      sId:sId,
			                                            command:"registerEnsemble",
			                                            ensembleName:ensembleName,
                                                        country:country,
                                  						district:district,
                                  						region:region,
                                  						city:city,
                                  						phone:phone,
                                  						countOfMembers:countOfMembers,
                                  						boss:boss,
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
