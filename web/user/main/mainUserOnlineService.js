$(document).ready(function() {
 //  skm_LockScreen('ОШИБКА. Оценка уже была выставлена ранее.\n Ожидание следующего участника.');
    getInfo();
    online();

});



	function online(){
	setInterval(()=> getInfo(), 10000)
	}


function getInfo(){
	var sId=getCookie('LivingWaterSession');
  console.log("Online test with"+sId);
  console.log(document.cookie);
  //alert (document.cookie);

  $.ajax({
			type: 'POST',
			url: "/user/online",
			data: JSON.stringify({sId:sId, command:"online"}),
			success: function(data){
				console.log(data);


				if(document.getElementById("songId").innerHTML!=data.songId){
					skm_UnLockScreen();
				}


				document.getElementById("messageAboutOnline").innerHTML=data.message;
			    document.getElementById("messageAboutOnline").style.backgroundColor="green";
			    document.getElementById("connect").style.display="none";
                document.getElementById("memberId").innerHTML="№ "+data.memberId;
                document.getElementById("member").innerHTML=data.memberName;
                document.getElementById("office").innerHTML=data.office;
                document.getElementById("songId").innerHTML=data.songId;
                document.getElementById("songName").innerHTML=data.songName;
                document.getElementById("category").innerHTML="КАТЕГОРИЯ: "+data.category;

			/*	if(data.messageBox="ONLINE"){
					document.getElementById("messageAboutOnline").style.backgroundColor="green";
					document.getElementById("connect").style.display="none";
					document.getElementById("messageAboutOnline").innerHTML="ONLINE";
						}
				if(data.messageBox="Login out"){
					document.getElementById("messageAboutOnline").innerHTML="OffLine. Server reboot.";
					document.getElementById("messageAboutOnline").style.backgroundColor="red";
					document.getElementById("connect").style.display="block";
						}
				*/
				if(document.getElementById("member").innerHTML=="undefined"){
                					 skm_LockScreen('Ожидание сервера.');
                				}
                if(document.getElementById("butToFullScreen").style.display!="none"){
                skm_UnLockScreen();
}

				},
				error: function(){
					//alert("fewfwef");
					document.getElementById("messageAboutOnline").innerHTML="OFFLINE";
					document.getElementById("messageAboutOnline").style.backgroundColor="red";
					document.getElementById("connect").style.display="block";
				}
		  });

}


	/*
	console.log('Request succeeded with JSON response', data);
	})
	.catch(function(){
		document.getElementById("messageAboutOnline").innerHTML="OFF-line. Error with server.";
		document.getElementById("messageAboutOnline").style.backgroundColor="red";
		document.getElementById("connect").style.display="block";
	console.log('Request succeeded with JSON response', data);
	});

  }*/
