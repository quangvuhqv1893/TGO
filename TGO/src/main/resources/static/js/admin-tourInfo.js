$(".cancelGuide").click(function(){
	var r = confirm("bạn muốn loại hdv này?");
	if(r==false){
		return;
	}
	var tourxrefid = parseInt($(this).parent().prev().text());
	var param = $(this);
	console.log('tourxrefid: '+tourxrefid);
	$.ajax({
		url: '/tour/cancelGuide',
		data : {tourxrefid: tourxrefid},
		type: "GET",
		datatype : "JSON",
		success: function(data){
			console.log(data);
			if(data=="success"){
				alert("đã hủy!");
//				param.text("đã hủy");
//				param.attr("disabled","disabled");
//				$("#accept"+tourxrefid).addClass("hidden");
				$(".guide"+tourxrefid).remove();
			}else if(data=="403"){
				window.location.href = "/403";
			}else{
				alert("có lỗi xảy ra!!");
			}
		}
	})
});