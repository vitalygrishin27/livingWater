$('#combobox').on('change', function() {

if($(this).children(":selected").attr("id")!=-1){
 document.getElementById("registerSolo").style.display="none";
 document.getElementById("registerEnsemble").style.display="none";


    $.ajax({
			 type: 'GET',
			url: "/admin/gets",
			data: { sId:sId,command:"getMemberInformation", idMember:$(this).children(":selected").attr("id")},
			success: function(data){

    console.log(data);

document.getElementById('idMemberForUpdateOrDelete').innerHTML=data.id;

if(data.countOfMembers==1){
document.getElementById("home-tab").click();




document.getElementById('firstname').value=data.firstName;
document.getElementById('secondname').value=data.secondName ;
document.getElementById('lastname').value=data.lastName;
document.getElementById('birth').value=data.birth;
document.getElementById('country').value=data.country;
document.getElementById('district').value=data.district;
document.getElementById('region').value=data.region;
document.getElementById('city').value=data.city;

if(data.gender=="M"){
$('#genderMale').prop('checked', true);
$('#genderFemale').prop('checked', false);
//$('.myCheckbox').prop('checked', false);
}else{
$('#genderMale').prop('checked', false);
$('#genderFemale').prop('checked', true);
}


document.getElementById('passport').value=data.passport;
document.getElementById('office').value=data.office;
document.getElementById('INN').value=data.INN;
document.getElementById('boss').value=data.boss;
document.getElementById('phone').value=data.phone;
document.getElementById('category').value=data.category;
document.getElementById('firstSong').value=data.firstSong;
document.getElementById('secondSong').value=data.secondSong;

document.getElementById("updateEnsemble").style.display="none";
document.getElementById("deleteEnsemble").style.display="none";
document.getElementById("updateSolo").style.display="block";
document.getElementById("deleteSolo").style.display="block";


}

if(data.countOfMembers>1){
document.getElementById("profile-tab").click();

document.getElementById('ensemble_name').value=data.ensembleName;
document.getElementById('ensemble_office').value=data.office;
document.getElementById('ensemble_country').value=data.country;
document.getElementById('ensemble_district').value=data.district;
document.getElementById('ensemble_region').value=data.region;
document.getElementById('ensemble_city').value=data.city;
document.getElementById('ensemble_phone').value=data.phone;
document.getElementById('ensemble_countOfMembers').value=data.countOfMembers;
document.getElementById('ensemble_boss').value=data.boss;
document.getElementById('ensemble_category').value=data.category;
document.getElementById('ensemble_firstSong').value=data.firstSong;
document.getElementById('ensemble_secondSong').value=data.secondSong;

document.getElementById("updateSolo").style.display="none";
document.getElementById("deleteSolo").style.display="none";
document.getElementById("updateEnsemble").style.display="block";
document.getElementById("deleteEnsemble").style.display="block";

}

}
		  });

}else{
 document.getElementById("updateSolo").style.display="none";
 document.getElementById("updateEnsemble").style.display="none";
 document.getElementById("deleteEnsemble").style.display="none";
 document.getElementById("deleteSolo").style.display="none";
 document.getElementById("registerSolo").style.display="block";
 document.getElementById("registerEnsemble").style.display="block";

document.getElementById('idMemberForUpdateOrDelete').innerHTML=-1;

}



});



$(document).ready(function(){
 document.getElementById("updateSolo").style.display="none";
    document.getElementById("deleteSolo").style.display="none";
     document.getElementById("updateEnsemble").style.display="none";
     document.getElementById("deleteEnsemble").style.display="none";


$.ajax({
			 type: 'GET',
			url: "/admin/gets",
			data: {sId:sId,command:"getCategory"},
			success: function(data){


		console.log(data);
var s =  document.getElementById('category');
var s2 =  document.getElementById('ensemble_category');

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
}

}
		  });

updateListMember();


});



 var sId=getCookie('LivingWaterSession');



function updateListMember(){
$("#combobox").empty();
var emptyOption=document.createElement("option");
emptyOption.value="";
emptyOption.innerHTML="";
emptyOption.id=-1;
document.getElementById("combobox").appendChild(emptyOption);

$.ajax({
			 type: 'GET',
			url: "/admin/gets",
			data: {sId:sId,command:"getListOfMembersOnlyNames"},
			success: function(data){
     		console.log(data);

  for(var i=0; i<data.name.length; i++){
     console.log(data.name[i]);

var member=document.createElement("option");
member.value=data.name[i];
member.innerHTML=data.name[i];
member.id=data.id[i];
document.getElementById("combobox").appendChild(member);
     }

}
		  });
}



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
		if(data.status=="201"){
				clearForm();
                updateListMember();
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
		if(data.status=="201"){
				clearForm();
                updateListMember();
		}
		else{
			alert(data.message);
		}


		}
		  });

}


function clearForm(){
$('#category').get(0).selectedIndex = 0;
$('#ensemble_category').get(0).selectedIndex = 0;
$('#combobox').get(0).selectedIndex = 0;

 document.getElementById("updateSolo").style.display="none";
 document.getElementById("updateEnsemble").style.display="none";
 document.getElementById("deleteEnsemble").style.display="none";
 document.getElementById("deleteSolo").style.display="none";
 document.getElementById("registerSolo").style.display="block";
 document.getElementById("registerEnsemble").style.display="block";

document.getElementById('idMemberForUpdateOrDelete').innerHTML=-1;

var elements = document.getElementsByTagName("input");
for (var i = 0, element; element = elements[i++];) {
    if (element.type === "text" ||  element.type ==="date")
        element.value = "";
}

}


function deleteMember(){
    var sId=getCookie('LivingWaterSession');
	console.log("deleteMember");
            $.ajax({
			    type: 'POST',
			    url: "/admin/edit",
			    data: JSON.stringify({sId:sId, command:"delete",idMember:document.getElementById('idMemberForUpdateOrDelete').innerHTML}),
                success: function(data){
		                console.log(data);
		                document.getElementById("message").innerHTML=data.message;
		                if(data.status=="200"){
				            clearForm();
				            updateListMember();
		                }
		                else{
			                alert(data.message);
		                }

		        }
		     });
}

function updateSolo(){
    var sId=getCookie('LivingWaterSession');
    console.log("updateMemberSolo");
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
			    url: "/admin/edit",
			    data: JSON.stringify({sId:sId,
			                            command:"updateSolo",
			                            idMember:document.getElementById('idMemberForUpdateOrDelete').innerHTML,
                                        lastname:lastname,
                                        firstname:firstname,
                                        secondname:secondname,
                                        birth:birth,
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
                                        secondSong:secondSong
                                        }),
                success: function(data){
		                console.log(data);
		                document.getElementById("message").innerHTML=data.message;
		                if(data.status=="200"){
				            clearForm();
				            updateListMember();
		                }
		                else{
			                alert(data.message);
		                }

		        }
		     });
}

function updateEnsemble(){
 var sId=getCookie('LivingWaterSession');
    console.log("updateMemberEnsemble");


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
    			    url: "/admin/edit",
    			    data: JSON.stringify({sId:sId,
    			                            command:"updateEnsemble",
    			                            idMember:document.getElementById('idMemberForUpdateOrDelete').innerHTML,
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
                                            secondSong:secondSong
                                            }),
                    success: function(data){
    		                console.log(data);
    		                document.getElementById("message").innerHTML=data.message;
    		                if(data.status=="200"){
    				            clearForm();
    				            updateListMember();
    		                }
    		                else{
    			                alert(data.message);
    		                }

    		        }
    		     });

}