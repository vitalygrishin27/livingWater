     $(document).ready(function(){
        updateList();
     });

function setTurnNumberForMember(turnNumber,idMember){
 var sId=getCookie('LivingWaterSession');
console.log("setTurnNumberForMember");

    //   alert(this.id);

            $.ajax({
			    type: 'POST',
			    url: "/admin/registration",
			    data: JSON.stringify({sId:sId, command:"setTurnNumberForMember",idMember:idMember,turnNumber:turnNumber}),
                success: function(data){
		                console.log(data);
		             //   document.getElementById("message").innerHTML=data.message;
		                if(data.status=="200"){
				            console.log ("Успешно установлено. "+data.message);
				            updateList();

		                }
		                else{
			                alert(data.message);
		                }

		        }
		     });
}

function updateList(){
 var sId=getCookie('LivingWaterSession');
	console.log("getMembersForLeftList");
    document.getElementById("myList1").innerHTML="";
     $.ajax({
     			type: 'GET',
     			url: "/admin/gets",
     			data: {sId:sId,command:"getListOfMembersOnlyNames"},
     			success: function(data){

     		console.log(data);

  for(var i=0; i<data.name.length; i++){
     console.log(data.name[i]);

    var member=document.createElement("li");
    member.innerHTML=data.turnNumber[i]+". "+data.name[i];
    member.id=data.id[i];
    if(data.turnNumber[i]==0){
       member.className = "blue";
    }else{
     member.className = "green";
    }


    member.onclick = function() {
           memberId=this.id;

            var result=prompt("Введите номер жеребъевки");
            if(result!=null && result!=""){
                if(result>=0 && result<1000){
              // alert ("OK >0 <1000");
                setTurnNumberForMember(result,memberId);
                }else{
                console.log ("Не правильный ввод");
                }
            }
    };
    document.getElementById("myList1").appendChild(member);

     }

     }

	  });}