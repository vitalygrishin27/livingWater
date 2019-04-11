
function reset(){
	 $("#slider").slider({
              animate: true,
              value:0,
              min: 1,
              max: 5,
              step: 1,
              slide: function(event, ui) {
                  update(1,ui.value); //changed
              }
          });

          $("#slider2").slider({
              animate: true,
              value:0,
              min: 1,
              max: 5,
              step: 1,
              slide: function(event, ui) {
                  update(2,ui.value); //changed
              }
          });
		  
		   $("#slider3").slider({
              animate: true,
              value:0,
              min: 1,
              max: 5,
              step: 1,
              slide: function(event, ui) {
                  update(3,ui.value); //changed
              }
          });
		  
		    $("#slider4").slider({
              animate: true,
              value:0,
              min: 1,
              max: 5,
              step: 1,
              slide: function(event, ui) {
                  update(4,ui.value); //changed
              }
          });
		  
          //Added, set initial value.
          $("#vocal").val(0);
          $("#repertoire").val(0);
          $("#artistic").val(0);
		  $("#individualy").val(0);
          $("#vocal-label").text(0);
          $("#repertoire-label").text(0);
          $("#artistic-label").text(0);
		  $("#individualy").text(0);
          
          update();
}

function setValueFromFirst(val){
//$("#vocal").val(val);
//$("#vocal-label").text(val);
//var $vocal == 1?val:$("#vocal").val();
//var $vocal == 1?val:$("#vocal").val();
}




$(document).ready(function() {
         document.getElementById("push").style.display="none";
         reset();

      });

      //changed. now with parameter
      function update(slider,val) {
        //changed. Now, directly take value from ui.value. if not set (initial, will use current value.)
        var $vocal = slider == 1?val:$("#vocal").val();
        var $repertoire = slider == 2?val:$("#repertoire").val();
		var $artistic = slider == 3?val:$("#artistic").val();
		var $individualy = slider == 4?val:$("#individualy").val();

        /* commented
        $amount = $( "#slider" ).slider( "value" );
        $duration = $( "#slider2" ).slider( "value" );
         */

         $total = Number($vocal) + Number($repertoire)+Number($artistic)+Number($individualy);
         $( "#vocal" ).val($vocal);
         $( "#vocal-label" ).text($vocal);
         $( "#repertoire" ).val($repertoire);
		 $( "#repertoire-label" ).text($repertoire);
         $( "#artistic" ).val($artistic);
         $( "#artistic-label" ).text($artistic);
		 $( "#individualy" ).val($individualy);
         $( "#individualy-label" ).text($individualy);
		 
		 $( "#total" ).val($total);
         $( "#total-label" ).text($total);

         $('#slider a').html('<label><span class="glyphicon glyphicon-chevron-left"></span> '+$vocal+' <span class="glyphicon glyphicon-chevron-right"></span></label>');
         $('#slider2 a').html('<label><span class="glyphicon glyphicon-chevron-left"></span> '+$repertoire+' <span class="glyphicon glyphicon-chevron-right"></span></label>');
         $('#slider3 a').html('<label><span class="glyphicon glyphicon-chevron-left"></span> '+$artistic+' <span class="glyphicon glyphicon-chevron-right"></span></label>');
      $('#slider4 a').html('<label><span class="glyphicon glyphicon-chevron-left"></span> '+$individualy+' <span class="glyphicon glyphicon-chevron-right"></span></label>');
	  }

	  

	function validateForm(){
	   clearStyleForLabelsWithError();
	   var validate=true;
	    if (document.getElementById("vocal-label").innerHTML==0){
                        document.getElementById("greatVocal").style.backgroundColor= "#ff3333";
                        document.getElementById("vocal-label").style.color="#ff3333";
                        validate=false;
	    }
	    if (document.getElementById("repertoire-label").innerHTML==0){
                       document.getElementById("greatRepertoire").style.backgroundColor= "#ff3333";
                       document.getElementById("repertoire-label").style.color="#ff3333";
                       validate=false;
        	    }
        if (document.getElementById("artistic-label").innerHTML==0){
                        document.getElementById("greatArtistic").style.backgroundColor= "#ff3333";
                        document.getElementById("artistic-label").style.color="#ff3333";
                        validate=false;
                	    }
        if (document.getElementById("individualy-label").innerHTML==0){
                        document.getElementById("greatIndividualy").style.backgroundColor= "#ff3333";
                        document.getElementById("individualy-label").style.color="#ff3333";
                        validate=false;
                	    }

        if(validate==true){
         pushToServer();
        }
	}


        function clearStyleForLabelsWithError(){
        document.getElementById("vocal-label").style.color="#ffff00";
        document.getElementById("repertoire-label").style.color="#ffff00";
        document.getElementById("artistic-label").style.color="#ffff00";
        document.getElementById("individualy-label").style.color="#ffff00";

        document.getElementById("greatVocal").style.backgroundColor= "#00ac98";
        document.getElementById("greatRepertoire").style.backgroundColor= "#00ac98";
        document.getElementById("greatArtistic").style.backgroundColor= "#00ac98";
        document.getElementById("greatIndividualy").style.backgroundColor= "#00ac98";
        }





	  function pushToServer(){
		 // validateForm();
		  var sId=getCookie('LivingWaterSession');
		  console.log("pushToServer");


		  //блокировка экрана до следующего участника









	//	var poster=  $.ajax({
		$.ajax({
			 type: 'POST',
			url: "/user",
			data: JSON.stringify({sId:sId, command:"setMark",
				vocal:document.getElementById("vocal-label").innerHTML,
				repertoire:document.getElementById("repertoire-label").innerHTML,
				artistic:document.getElementById("artistic-label").innerHTML,
				individualy:document.getElementById("individualy-label").innerHTML,
				memberName:document.getElementById("member").innerHTML,
				memberId:document.getElementById("memberId").innerHTML.substring(2),
				category:document.getElementById("category").innerHTML.substring(11),
				songId:document.getElementById("songId").innerHTML}),
			success: function(data){
		console.log(data);

        if(data.message=="ОШИБКА. Оценка уже была выставлена ранее."){
				skm_LockScreen('ОШИБКА. Оценка уже была выставлена ранее.\n Ожидание следующего участника.');
            reset();

		}


		if(data.status=="200"){
				skm_LockScreen('Оценка принята.\n Ожидание следующего участника.');
				reset();

		}
	//	else{
	//		alert(data.message);

	//	}


		}
		  });
}

function skm_LockScreen(str)
               {
                  var lock = document.getElementById('skm_LockPane');
                  if (lock)
                     lock.className = 'LockOn';


                  lock.innerHTML = '<div class="h61">'+str+'</div>';
                //  lock.innerHTML = str;
               }

            function skm_UnLockScreen()
                           {
                              var lock = document.getElementById('skm_LockPane');
                              if (lock)
                                 lock.className = 'LockOff';

                        //      lock.innerHTML = str;
                           }


                           function fullScreenOn(){
            document.getElementById("butToFullScreen").style.display="none";
            document.getElementById("bod").webkitRequestFullscreen();
            document.getElementById("push").style.display="block";


                           }