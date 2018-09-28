$(".cancelAccepteTour").click(function(){
	var r = confirm("bạn muốn hủy?");
	if(r==false){
		return;
	}
	var tourxrefid = parseInt($(this).parent().prev().text());
	var param = $(this);
	console.log(tourxrefid);
	$.ajax({
		url: '/tour/cancelGuide',
		data : {tourxrefid: tourxrefid},
		type: "GET",
		datatype : "JSON",
		success: function(data){
			console.log(data);
			if(data=="success"){
				alert("đã hủy!");
				window.location.href = "/guide/showAcceptedTour";
			}else if(data=="403"){
				window.location.href = "/403";
			}else{
				alert("có lỗi xảy ra!!");
			}
		}
	})
});