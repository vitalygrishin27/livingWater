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

      var button=document.createElement("button");
         button.type="button";
         button.className="btn btn-success btn-cons";
         button.innerHTML=data.name[i];
         button.id=data.id[i];
         document.getElementById("con").appendChild(button);



//     var opt=document.createElement('li');
 //    opt.value=data.name[i];
  //   opt.className="list-group-item";
  //   opt.innerHTML=data.name[i];
   //  leftList.appendChild(opt);

     }

     }

	  });


     });
