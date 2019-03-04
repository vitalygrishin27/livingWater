$(document).ready(function() {
    // var sId=readCookie('LivingWaterSession');
     getInfoAboutJury();
    online();
	//hiddenDivIfEmpty()



})


function online(){
	setInterval(()=> getInfoAboutJury(), 10000)
	}


function getInfoAboutJury(){
	var sId=getCookie('LivingWaterSession');
  console.log("function - getCountOfJuries");

  $.ajax({
  			type: 'POST',
  			url: "/admin/online",
  			data: JSON.stringify({sId:sId, command:"getCountOfJuries"}),
  			success: function(data){
  				console.log(data);
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

  				if(document.getElementById("jury_1_name").innerHTML=="undefined"){
  				document.getElementById("jury_1_div").style.display="none";
  				}
  				else{
  				document.getElementById("jury_1_div").style.display="block";
  				}

  				if(document.getElementById("jury_2_name").innerHTML=="undefined"){
                 document.getElementById("jury_2_div").style.display="none";
                 }
                 else{
                 document.getElementById("jury_2_div").style.display="block";
                 }

                 if(document.getElementById("jury_3_name").innerHTML=="undefined"){
                   document.getElementById("jury_3_div").style.display="none";
                   }
                   else{
                   document.getElementById("jury_3_div").style.display="block";
                    }

  				if(document.getElementById("jury_4_name").innerHTML=="undefined"){
                  document.getElementById("jury_4_div").style.display="none";
                  }
                  else{
                   document.getElementById("jury_4_div").style.display="block";
                   }

  				if(document.getElementById("jury_5_name").innerHTML=="undefined"){
                  document.getElementById("jury_5_div").style.display="none";
                  }
                  else{
                   document.getElementById("jury_5_div").style.display="block";
                   }

                if(document.getElementById("jury_6_name").innerHTML=="undefined"){
                  document.getElementById("jury_6_div").style.display="none";
                  }
                  else{
                   document.getElementById("jury_6_div").style.display="block";
                   }

                if(document.getElementById("jury_7_name").innerHTML=="undefined"){
                  document.getElementById("jury_7_div").style.display="none";
                  }
                  else{
                   document.getElementById("jury_7_div").style.display="block";
                   }

                if(document.getElementById("jury_8_name").innerHTML=="undefined"){
                  document.getElementById("jury_8_div").style.display="none";
                  }
                  else{
                   document.getElementById("jury_8_div").style.display="block";
                   }


  				},
  			error: function(data){
  			console.log("Error with getCountOfJuries");
  			}

  		  });
 }


  
