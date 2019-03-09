
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




$(document).ready(function() {
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
	  
	  function pushToServer(){
		  var sId=getCookie('LivingWaterSession');
		  console.log("pushToServer");
		  
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
				memberId:document.getElementById("number").innerHTML.substring(2),
				category:document.getElementById("category").innerHTML.substring(11)}),
			success: function(data){
		console.log(data);
		if(data.status=="200"){
				reset();
		}
		else{
			alert(data.message);
		}
		
	
		}
		  });

	  }
