     $(document).ready(function(){
    var sId=getCookie('LivingWaterSession');
	console.log("getMembersForLeftList");
     $.ajax({
     			type: 'POST',
     			url: "/admin/edit",
     			data: JSON.stringify({ sId:sId,command:"getListOfMembers"}),
     			success: function(data){

     		console.log(data);

  for(var i=0; i<data.name.length; i++){
     console.log(data.name[i]);

var member=document.createElement("li");
member.innerHTML=data.name[i];
member.id=data.id[i];
document.getElementById("myList1").appendChild(member);
     }
yyy();
     }

	  });
     });