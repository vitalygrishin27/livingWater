$(document).ready(function() {
          $("#slider").slider({
              animate: true,
              value:1,
              min: 1,
              max: 5,
              step: 1,
              slide: function(event, ui) {
                  update(1,ui.value); //changed
              }
          });

          $("#slider2").slider({
              animate: true,
              value:1,
              min: 1,
              max: 5,
              step: 1,
              slide: function(event, ui) {
                  update(2,ui.value); //changed
              }
          });
		  
		   $("#slider3").slider({
              animate: true,
              value:1,
              min: 1,
              max: 5,
              step: 1,
              slide: function(event, ui) {
                  update(3,ui.value); //changed
              }
          });

          //Added, set initial value.
          $("#vocal").val(1);
          $("#repertoire").val(1);
          $("#artistic").val(1);
          $("#vocal-label").text(1);
          $("#repertoire-label").text(1);
          $("#artistic-label").text(1);
          
          update();
      });

      //changed. now with parameter
      function update(slider,val) {
        //changed. Now, directly take value from ui.value. if not set (initial, will use current value.)
        var $vocal = slider == 1?val:$("#vocal").val();
        var $repertoire = slider == 2?val:$("#repertoire").val();
		var $artistic = slider == 3?val:$("#artistic").val();

        /* commented
        $amount = $( "#slider" ).slider( "value" );
        $duration = $( "#slider2" ).slider( "value" );
         */

         $total = Number($vocal) + Number($repertoire)+Number($artistic);
         $( "#vocal" ).val($vocal);
         $( "#vocal-label" ).text($vocal);
         $( "#repertoire" ).val($repertoire);
         $( "#repertoire-label" ).text($repertoire);
         $( "#artistic" ).val($artistic);
         $( "#artistic-label" ).text($artistic);
         
		 $( "#total" ).val($total);
         $( "#total-label" ).text($total);

         $('#slider a').html('<label><span class="glyphicon glyphicon-chevron-left"></span> '+$vocal+' <span class="glyphicon glyphicon-chevron-right"></span></label>');
         $('#slider2 a').html('<label><span class="glyphicon glyphicon-chevron-left"></span> '+$repertoire+' <span class="glyphicon glyphicon-chevron-right"></span></label>');
         $('#slider3 a').html('<label><span class="glyphicon glyphicon-chevron-left"></span> '+$artistic+' <span class="glyphicon glyphicon-chevron-right"></span></label>');
      }
