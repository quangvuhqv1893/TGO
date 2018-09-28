$(".cancelTourRunning").click(function(){
	var r = confirm("bạn muốn hủy tour?");
	if(r==false){
		return;
	}
	var tourid = parseInt($(this).parent().prev().text());
	var param = $(this);
	console.log(tourid);
	$.ajax({
		url: '/tour/delete/'+tourid,
//		data : {tourid: tourid},
		type : "POST",
		success: function(data){
			console.log(data);
			if(data=="redirect:/operator"){
				alert("tour đã bị hủy!");
				window.location.href = "/operator/runningTour";
			}else if(data=="403"){
				window.location.href = "/403";
			}
			else{
				alert("có lỗi xảy ra, tour chưa hủy!!!");
			}
		}
	})
});