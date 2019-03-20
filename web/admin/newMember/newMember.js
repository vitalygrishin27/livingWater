
$(document).ready(function(){

$.ajax({
			 type: 'POST',
			url: "/admin/newMember",
			data: JSON.stringify({ sId:sId,command:"getCategory"}),
			success: function(data){


		console.log(data);
	//	var s =document.getElementById("category");
//var s =  document.getElementById('category').options;
var s =  document.getElementById('category');
var s2 =  document.getElementById('ensemble_category');

//s[s.length]= new Option(data[k],'teo',true);


for(var i=0; i<data.category.length; i++){
console.log(data.category[i]);
var opt=document.createElement('option');
opt.value=data.category[i];
opt.innerHTML=data.category[i];
s.appendChild(opt);
var opt2=document.createElement('option');
opt2.value=data.category[i];
opt2.innerHTML=data.category[i];
s2.appendChild(opt2);
//s2.appendChild(opt);


//s[s.length]= new Option(data[i],data[i],true);
}









}

		  });




});

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
var office=document.getElementById('office').value ;
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
                                  						office:office,
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
var ensembleOffice=document.getElementById('ensemble_office').value ;
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
			                                            ensembleOffice:ensembleOffice,
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
