//<script type="text/babel">
var sId=getCookie('LivingWaterSession');

function registerSolo(){
     
//var sId=readCookie('sessionId');

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

//document.getElementById('user').value ="";
//document.getElementById('pass').value="";
//alert (passport);

fetch('/admin/registration/member', {
	
		headers:{
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
	

   method: "POST",

	
	
//	body: JSON.stringify({a: 1, b: 'tetsts ytyryr'})
	
   body: JSON.stringify({lastname:lastname, 
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
						secondSong:secondSong
						})


  })
  .then(response => response.json())
  .then(function (data) {
    document.getElementById("message").innerHTML=data.message;
	console.log('Request succeeded with JSON response', data);
	
	
	
		if(data.status=="ADMIN"){
	//	writeCookie('sessionId', data.sId,3)
		document.location.href="/admin/registration/member";
		}
		if(data.status=="USER"){
	//	writeCookie('sessionId', data.sId,3)
		document.location.href="/user";
		}
		if(data.status=="300"){
	//	writeCookie('sessionId', data.sId,3)
		//document.location.href="/user";
//alert(data.message);	
// document.getElementById("message").innerHTML=
	}
	
	
//	document.getElementById("message").innerHTML=data.message;
	
	
	//document.location.href="/admin";
	
	});
 

}

//</script>