     $(document).ready(function(){
    var sId=getCookie('LivingWaterSession');
	console.log("getMembersForLeftList");
     $.ajax({
     			 type: 'POST',
     			url: "/admin/edit",
     			data: JSON.stringify({ sId:sId,command:"getListOfMembers"}),
     			success: function(data){


     		console.log(data);
     	//	var s =document.getElementById("category");
     //var s =  document.getElementById('category').options;
     var lestList =  document.getElementById('leftList');
     var rightList =  document.getElementById('rightList');

     //s[s.length]= new Option(data[k],'teo',true);


     for(var i=0; i<data.name.length; i++){
     console.log(data.name[i]);
     var opt=document.createElement('li');
     opt.value=data.name[i];
     opt.className="list-group-item";
     opt.innerHTML=data.name[i];
     leftList.appendChild(opt);
   //  var opt2=document.createElement('option');
   //  opt2.value=data.category[i];
  //   opt2.innerHTML=data.category[i];
  //   s2.appendChild(opt2);
     //s2.appendChild(opt);


     //s[s.length]= new Option(data[i],data[i],true);
     }









     }

     		  });




     });























        $(function () {

            $('body').on('click', '.list-group .list-group-item', function () {
                $(this).toggleClass('active');
            });
            $('.list-arrows button').click(function () {
                var $button = $(this), actives = '';
                if ($button.hasClass('move-left')) {

                    actives = $('.list-right ul li.active');
                    actives.clone().appendTo('.list-left ul');
                    actives.remove();


                       alert(actives.class);
                } else if ($button.hasClass('move-right')) {

                    actives = $('.list-left ul li.active');
                    actives.clone().appendTo('.list-right ul');
                    actives.remove();
                }
            });
            $('.dual-list .selector').click(function () {
                var $checkBox = $(this);
                if (!$checkBox.hasClass('selected')) {
                    $checkBox.addClass('selected').closest('.well').find('ul li:not(.active)').addClass('active');
                    $checkBox.children('i').removeClass('glyphicon-unchecked').addClass('glyphicon-check');
                } else {
                    $checkBox.removeClass('selected').closest('.well').find('ul li.active').removeClass('active');
                    $checkBox.children('i').removeClass('glyphicon-check').addClass('glyphicon-unchecked');

                }
            });
            $('[name="SearchDualList"]').keyup(function (e) {
                var code = e.keyCode || e.which;
                if (code == '9') return;
                if (code == '27') $(this).val(null);
                var $rows = $(this).closest('.dual-list').find('.list-group li');
                var val = $.trim($(this).val()).replace(/ +/g, ' ').toLowerCase();
                $rows.show().filter(function () {
                    var text = $(this).text().replace(/\s+/g, ' ').toLowerCase();
                   return !~text.indexOf(val);
                }).hide();
            });

        });