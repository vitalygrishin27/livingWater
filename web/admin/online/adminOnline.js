




$(document).ready(function(){

CreateTableFromJSON();
setInterval(()=> getMarksValueOfMemberThatEvaluate(), 5000);

});
// При загрузке документа нарисовать синим строку, если currentMember на сервере существует


function getMarksValueOfMemberThatEvaluate (){
var sId=getCookie('LivingWaterSession');
//
$.ajax({
			type: 'POST',
			url: "/admin/online",
			data: JSON.stringify({    sId:sId,
                                  	  command:"getMarksValueOfMemberThatEvaluate"}),
		success: function(data){
		console.log(data);
		var tab=document.getElementById("members");
        //Перебор всех ключей JSON
        for(var k in data){

                if(k!="songNumber" && k!="memberId"){
                    //Какой идекс колонки нужного члена жюри
               // document.getElementById('table1').rows[0].cells.length

                for(var j=0;j<tab.rows[0].cells.length;j++){
                if(tab.rows[0].cells[j].innerHTML==k){
                    //Поиск нужной строки по MemberId
                    for(var i=0; i<tab.rows.length;i++){
                         if(tab.rows[i].cells[0].innerHTML==data.memberId && tab.rows[i].cells[3].innerHTML==data.songNumber){
                         //Проверка нужной песни

                            //  console.log(data[k]);
                              tab.rows[i].cells[j].innerHTML=data[k];
                              updateColorOfRow(i);

                         }

                    }

                }
                }
  }


        }


		}
		  });
updateColorOfRowWhenLoadPage();
}






function sendPost(ths) {
var sId=getCookie('LivingWaterSession');
    var tr = ths.parentNode.parentNode;

         songNumber = tr.getElementsByTagName("td")[3].innerHTML;
         memberId = tr.getElementsByTagName("td")[0].innerHTML;

//alert(tr.parentNode.rows[0].cells.length);
    for(var i=0;i<tr.parentNode.rows[0].cells.length;i++){
        tr.getElementsByTagName("td")[i].style.backgroundColor = "blue";
         tr.getElementsByTagName("td")[i].style.color = "white";
    }




$.ajax({
			 type: 'POST',
			url: "/admin/online",
			data: JSON.stringify({                      sId:sId,
                                  						command:"setMemberForEvaluation",
                                  						songNumber:songNumber,
                                  						memberId:memberId}),
		success: function(data){
		console.log(data);

	//	document.getElementById("message").innerHTML=data.message;

			alert(data.message);



		}
		  });







      //   name = tr.getElementsByTagName("td")[1].innerHTML.replace("<b>", "").replace("</b>", ""),

     //    review = tr.getElementsByTagName("td")[3].childNodes[0].value;

 //   alert(code+"\n"+name+"\n"+review);

}




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
                    //    for (var i = 0; i < list.length; i++) {
                        for (var i = 0; i < 1; i++) {
                            col.push("id");
                            col.push("name");
                            col.push("category");
                            col.push("songNumber");
                            col.push("songName");

                            for (var key in list[i]) {
                               // console.log(col.indexOf(key));
                                if (col.indexOf(key) === -1) {
                                    console.log(key);
                                   if(key!="id" && key!="name" && key!="category" && key!="songNumber" && key!="songName"){
                                   col.push(key);
                                   }

                                 //   col.push("yt");
                                }
                            }
                           console.log(col)
                        }

                        // CREATE DYNAMIC TABLE.
                        var table = document.createElement("table");
                        table.id="members";
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
                             but.id="but"+i;
                            but.setAttribute('onclick', 'sendPost(this);')

                             tabCell.appendChild(but);


                        }

                        // FINALLY ADD THE NEWLY CREATED TABLE WITH JSON DATA TO A CONTAINER.

  /*  $(function() {
          jQuery.each($("table tr"), function() {
              $(this).children(":eq(1)").after($(this).children(":eq(0)"));
          });
      });
*/

   //узнать по какой строке кликнули
 /*   $(document).ready(function(){

          $('table tr').click(function(){

            alert($('td.cellIndex', this).html());
//var cell = e.target || window.event.srcElement;
 // alert( cell.cellIndex + ' : ' + cell.parentNode.rowIndex );
          });

        });


/*
var tbl = document.getElementById("customers");
var cls = $tbl(tbl 'table tr');

function alertRowCell(e){
  var cell = e.target || window.event.srcElement;
  alert( cell.cellIndex + ' : ' + cell.parentNode.rowIndex );
}

for ( var i = 0; i < cls.length; i++ ) {
  if ( cls[i].addEventListener ) {
    cls[i].addEventListener("click", alertRowCell, false);
  } else if ( cls[i].attachEvent ) {
    cls[i].attachEvent("onclick", alertRowCell);
  }
}

*/




                        var divContainer = document.getElementById("showData");
                        divContainer.innerHTML = "";
                        divContainer.appendChild(table);
		},
  			error: function(data){
  			console.log("Error with getCountOfJuries");
  			}

  		  });
 }


function updateColorOfRow(rowIndex){
var tab=document.getElementById('members');
var countOfZeros=0;
for (var i=1; i<tab.rows[0].cells.length; i++){
    if(tab.rows[rowIndex].cells[i].innerHTML=="0"){
    countOfZeros++;
    }

}
//alert(countOfZeros);

if(countOfZeros==0){
    for(var j=0; j<tab.rows[0].cells.length; j++){
            tab.rows[rowIndex].cells[j].style.backgroundColor = "green";
            tab.rows[rowIndex].cells[j].style.color = "white";

    }


}


}

function updateColorOfRowWhenLoadPage(){
var tab=document.getElementById('members');
var countOfZeros=0;
for(var r=1; r<tab.rows.length; r++){
for (var i=1; i<tab.rows[0].cells.length; i++){
    if(tab.rows[r].cells[i].innerHTML=="0"){
    countOfZeros++;
    }

}

if(countOfZeros==0){
    for(var j=0; j<tab.rows[0].cells.length; j++){
            tab.rows[r].cells[j].style.backgroundColor = "green";
            tab.rows[r].cells[j].style.color = "white";

    }


}

}
//alert(countOfZeros);




}




