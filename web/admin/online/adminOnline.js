
$(document).ready(function(){

CreateTableFromJSON();

});




  function CreateTableFromJSON() {
      var sId=readCookie('LivingWaterSession');

 $.ajax({
  			type: 'POST',
  			url: "/admin/online",
  			data: JSON.stringify({sId:sId, command:"getListOfMembers"}),
  			success: function(data){
  				console.log(data);

  				var list=data;


  				 // EXTRACT VALUE FOR HTML HEADER.
                        var col = [];
                        for (var i = 0; i < list.length; i++) {
                            for (var key in list[i]) {
                                if (col.indexOf(key) === -1) {
                                    col.push(key);
                                }
                            }
                        }

                        // CREATE DYNAMIC TABLE.
                        var table = document.createElement("table");
                        table.id="customers";
                                table.className="table table-striped custab";
                        // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.

                        var tr = table.insertRow(-1);                   // TABLE ROW.

                        for (var i = 0; i < col.length; i++) {
                            var th = document.createElement("th");      // TABLE HEADER.
                            th.innerHTML = col[i];
                            if(col[i]=="name") th.innerHTML = "Конкурсант";
                            if(col[i]=="songName") th.innerHTML = "Название песни";
                            if(col[i]=="id") th.innerHTML = "ID";
                            if(col[i]=="songNumber") th.innerHTML = "Номер песни";
                            if(col[i]=="category") th.innerHTML = "Категория";

                            tr.appendChild(th);
                        }
                       var th = document.createElement("th");
                        var but=document.createElement("button");
                      //  but.style="width: 100px; height: 30px";
                        th.innerHTML="Отправить судьям";
                        tr.appendChild(th);

                        // ADD JSON DATA TO THE TABLE AS ROWS.
                        for (var i = 0; i < list.length; i++) {

                            tr = table.insertRow(-1);

                            for (var j = 0; j < col.length; j++) {
                                var tabCell = tr.insertCell(-1);
                                tabCell.innerHTML = list[i][col[j]];
                            }
                             var tabCell= tr.insertCell(-1);
                             var but=document.createElement("button");
                             but.style="width: 100%; height: 30px";
                             but.innerText="Отправить";
                             tabCell.appendChild(but);


                        }

                        // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.




                        var divContainer = document.getElementById("showData");
                        divContainer.innerHTML = "";
                        divContainer.appendChild(table);
		},
  			error: function(data){
  			console.log("Error with getCountOfJuries");
  			}

  		  });
 }







