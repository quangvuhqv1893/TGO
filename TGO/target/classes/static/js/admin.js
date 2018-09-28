$(".runningTour").click(function(){
	var r = confirm("duyệt tour này?");
	if(r==false){
		return;
	}
	var tourid = parseInt($(this).parent().prev().text());
	var param = $(this);
	console.log(tourid);
	$.ajax({
		url: "/tour/approveTour",
		data : {tourid: tourid},
		type: "POST",
		success: function(data){
			console.log(data);
			if(data=="success"){
				alert("kiểm duyệt tour thành công!");
				window.location.href = "/admin";
			}
			else if(data=="403"){
				window.location.href = "/403";
			}
			else{
				alert("thực hiện tour thất bại!");
//				return false;
			}
		}
	})
});


$(".cancelTourRunning").click(function(){
	var r = confirm("bạn muốn hủy tour này?");
	if(r==false){
		return;
	}
	var tourid = parseInt($(this).parent().prev().text());
	var param = $(this);
	console.log(tourid);
	$.ajax({
		url: '/tour/cancelApproveTour',
		data : {tourid: tourid},
		type : "POST",
		success: function(data){
			console.log(data);
			if(data=="success"){
				alert("tour đã bị hủy!");
				window.location.href = "/admin";
			}else if(data=="403"){
				window.location.href = "403";
			}
			else{
				alert("có lỗi xảy ra, tour chưa hủy!!!");
			}
		}
	})
});


