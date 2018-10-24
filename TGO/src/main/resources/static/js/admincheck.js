$(".runningTour").click(function(){
	var r = confirm("bạn muốn duyệt tài khoản này?");
	if(r==false){
		return;
	}
	var userid = parseInt($(this).parent().prev().text());
	var param = $(this);
	console.log(userid);
	$.ajax({
		url: "/tour/acceptApproveUser",
		data : {userid: userid,role: 2},
		type: "POST",
		success: function(data){
			console.log(data);
			if(data=="success"){
				alert("Kiểm duyệt tài khoản thành công!");
				window.location.href = "/admin/userApproval";
			}
			else if(data=="403"){
				window.location.href = "/403";
			}
			else{
				alert("Kiểm duyệt tài khoản thất bại!");
//				return false;
			}
		}
	})
});


$(".cancelTourRunning").click(function(){
	var r = confirm("bạn muốn hủy tài khoản này?");
	if(r==false){
		return;
	}
	var userid = parseInt($(this).parent().prev().text());
	var param = $(this);
	console.log(userid);
	$.ajax({
		url: '/admin/cancelApproveUser',
		data : {userid: userid,role: 2},
		type : "POST",
		success: function(data){
			console.log(data);
			if(data=="success"){
				alert("tài khoản đã bị hủy!");
				window.location.href = "/admin/userApproval";
			}else if(data=="403"){
				window.location.href = "403";
			}
			else{
				alert("có lỗi xảy ra, tài khoản chưa hủy!!!");
			}
		}
	})
});


