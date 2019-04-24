
$(document).ready(function(){
        CreateTableFromJSON();
        setInterval(()=> getMarksValueOfMemberThatEvaluate(), 10000);
});

function getMarksValueOfMemberThatEvaluate (){
    var sId=getCookie('LivingWaterSession');
     $.ajax({
			type: 'GET',
			url: "/admin/gets",
			data: {sId:sId,command:"getMarksValueOfMemberThatEvaluate"},
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


function getCurrentMemberDataThatEvaluate(){
    var sId=getCookie('LivingWaterSession');
    $.ajax({
			type: 'GET',
			url: "/admin/gets",
			data: {sId:sId, command:"getCurrentMemberDataThatEvaluate"},
            success: function(data){
		    console.log(data);
            //перебор всех строк где idMember совпадает
            var tab=document.getElementById("members");
            for (var i=1; i<tab.rows.length; i++){
                if(tab.rows[i].cells[0].innerHTML==data.memberId){
                     if(tab.rows[i].cells[3].innerHTML==data.songNumber){
                        //выделение синим цветом
                        for(var j=0; j<tab.rows[0].cells.length; j++){
                                   tab.rows[i].cells[j].style.backgroundColor = "blue";
                                   tab.rows[i].cells[j].style.color = "white";
                         }
                    }
                 }
            }
            }
		    });
}


function sendPost(ths) {
    var sId=getCookie('LivingWaterSession');
    var tr = ths.parentNode.parentNode;
    songNumber = tr.getElementsByTagName("td")[4].innerHTML;
    memberId = tr.getElementsByTagName("td")[1].innerHTML;
    //Обесцветить неактуальную синию строку
    deleteBlueColor();
    for(var i=0;i<tr.parentNode.rows[0].cells.length;i++){
        tr.getElementsByTagName("td")[i].style.backgroundColor = "blue";
         tr.getElementsByTagName("td")[i].style.color = "white";
    }

    $.ajax({
			type: 'POST',
			url: "/admin/online",
			data: JSON.stringify({ sId:sId,
                                   command:"setMemberForEvaluation",
                                   songNumber:songNumber,
                                   memberId:memberId}),
		    success: function(data){
		    console.log(data);
        //    alert(data.message);
            }
     });
}


function CreateTableFromJSON() {
    var sId=readCookie('LivingWaterSession');

    $.ajax({
  			type: 'GET',
  			url: "/admin/gets",
  			data: {sId:sId, command:"getListOfMembersFull"},
  		//	data: {sId:sId, command:"getListOfMembersOnlyMarkers"},
  			success: function(data){
  				console.log(data);
                var list=data;
                // EXTRACT VALUE FOR HTML HEADER.
                var col = [];
                for (var i = 0; i < 1; i++) {
                      col.push("turnNumber");
                      col.push("id");
                      col.push("name");
                      col.push("category");
                      col.push("songNumber");
                      col.push("songName");
                        for (var key in list[i]) {
                            if (col.indexOf(key) === -1) {
                                    console.log(key);
                                    if(key!="id" && key!="name" && key!="category" && key!="songNumber" && key!="songName"){
                                            col.push(key);
                                    }
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
                       if(col[i]=="turnNumber") th.innerHTML = "Номер";
                       if(col[i]=="name") th.innerHTML = "Конкурсант";
                       if(col[i]=="songName") th.innerHTML = "Название песни";
                       if(col[i]=="id") th.innerHTML = "ID";
                       if(col[i]=="songNumber") th.innerHTML = "Номер песни";
                       if(col[i]=="category") th.innerHTML = "Категория";
                        tr.appendChild(th);
                }
                var th = document.createElement("th");
                var but=document.createElement("button");
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
                 var divContainer = document.getElementById("showData");
                 divContainer.innerHTML = "";
                 divContainer.appendChild(table);
                 //выделить синим строку, если на сервере есть currentMemberForEvaluate
                 getCurrentMemberDataThatEvaluate();
                 //cкрыть анимацию загрузки
                 document.getElementById('loader').style.display='none';;
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
    var countOfJuryFields=0;
    for(var r=1; r<tab.rows.length; r++){
        for (var i=6; i<tab.rows[0].cells.length-1; i++){
                countOfJuryFields++;
            if(tab.rows[r].cells[i].innerHTML=="0"){
                countOfZeros++;
            }
        }
        if(countOfZeros==0){
            if(tab.rows[r].cells[0].style.backgroundColor != "blue"){
                for(var j=0; j<tab.rows[0].cells.length; j++){
                tab.rows[r].cells[j].style.backgroundColor = "green";
                tab.rows[r].cells[j].style.color = "white";
                }
            }
        }
        if(countOfZeros<countOfJuryFields && countOfZeros>0){
              if(tab.rows[r].cells[0].style.backgroundColor != "blue"){
                 for(var j=0; j<tab.rows[0].cells.length; j++){
                 tab.rows[r].cells[j].style.backgroundColor = "#8B0000";
                 tab.rows[r].cells[j].style.color = "white";
                 }
             }
        }
        countOfZeros=0;
        countOfJuryFields=0;

    }
}

function deleteBlueColor(){
    var tab=document.getElementById('members');
    for (var i=1; i<tab.rows.length; i++){
        if(tab.rows[i].cells[0].style.backgroundColor == "blue"){
            for(var j=0; j<tab.rows[0].cells.length; j++){
                tab.rows[i].cells[j].style.backgroundColor = "#f2f2f2";
                tab.rows[i].cells[j].style.color = "black";
             }
             updateColorOfRowWhenLoadPage();
        }
    }

}

