     $(document).ready(function(){
    var sId=getCookie('LivingWaterSession');
	console.log("getMembersForLeftList");
     $.ajax({
     			type: 'GET',
     			url: "/admin/gets",
     			data: {sId:sId,command:"getListOfMembersOnlyNames"},
     			success: function(data){

     		console.log(data);

  for(var i=0; i<data.name.length; i++){
     console.log(data.name[i]);

    var member=document.createElement("li");
    member.innerHTML=data.name[i];
    member.id=data.id[i];
    member.onclick = function() {

getNextNumberForTurn();
  //  alert (this.id+" "+this.innerHTML)

    };
    document.getElementById("myList1").appendChild(member);
     }
        yyy();
     }

	  });
     });

function getNextNumberForTurn(){
 var sId=getCookie('LivingWaterSession');
 $.ajax({
     			type: 'GET',
     			url: "/admin/gets",
     			data: {sId:sId,command:"getNextNumberForTurn"},
     			success: function(data){
     			confirm(this.innerHTML+" будет зарегистрирован под номером "+data.freeTurnNumber);
//console.log(data);
//console.log(data.freeTurnNumber);


// return data.freeTurnNumber;
     }

	  });
}