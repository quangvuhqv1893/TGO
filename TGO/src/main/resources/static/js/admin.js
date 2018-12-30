$(".runningTour").click(function(){
	var r = confirm("duyệt tour này?");
	if(r==false){
		return false;
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
				alert("kiểm duyệt tour thất bại!");
//				return false;
			}
		}
	})
});


$(".cancelTourRunning").click(function(){
	var r = confirm("bạn muốn hủy tour này?");
	if(r==false){
		return false;
	}
	var tourid = parseInt($(this).parent().prev().text());
	if (isNaN(tourid)) {
		tourid = document.querySelector('input[name="tourid"]').value.trim();
	}
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
				location.reload();
			}else if(data=="403"){
				window.location.href = "403";
			}
			else{
				alert("có lỗi xảy ra, tour chưa hủy!!!");
			}
		}
	})
});

$(".updateTour").click(function(){
	var tourname = document.querySelector('input[name="tourname"]').value.trim();
	
	console.log('tourname '+tourname);
	if(tourname==""){
		alert('Tên tour nhập không hợp lệ!');
		return false;
	}
	//validate amount
	var amount = document.querySelector('input[name="amount"]').value.trim();
	if (amount<=0) {
		alert("số lượng hướng dẫn viên không được nhỏ hơn 0!");
		return false;
	}
	var currentamount = document.querySelector('input[name="currentamount"]').value.trim();
	if (amount<currentamount) {
		alert("số lượng hướng dẫn viên không được nhỏ hơn số lượng hướng dẫn viên hiện tại");
		return false;
	}
	//validate startdate
	var startDate = document.querySelector('input[name="date"]').value;
	if(startDate==""||Date.parse(startDate)<Date.parse(new Date())){
		alert("Ngày bắt đầu không thể nhỏ hơn ngày hiện tại!!");
		return false;
	}
	//validate endDate
	var endDate = document.querySelector('input[name="endDate"]').value;
	if(endDate==""||Date.parse(endDate)<Date.parse(startDate)){
		alert("Ngày kết thúc không thể nhỏ hơn ngày bắt đầu!!");
		return false;
	}
	var tourprice = document.querySelector('input[name="tourPrice"]').value.trim();
	if(tourprice<10000){
		alert('giá tour không được dưới 10.000VND');
		return false;
	}
	var r = confirm("bạn chắc chắn muốn cập nhật tour này?");
	if(!r){
		return false;
	}
	var tourid = document.querySelector('input[name="tourid"]').value.trim();
		$.ajax({
		url: '/tour/updateTour',
		data : $('form#formTourInfo').serialize(),
		type : "POST",
		async: false,
		datatype : "JSON",
		success: function(data1){
			console.log(data1);
			if(data1==="success"){
				alert("tour đã được cập nhật thông tin!");
				window.location.href = "/tour/detailTour/?tourId="+ tourid;
				return false;
				// location.reload();
			}else if(data1==="403"){
				window.location.href = "403";
				return false;
			}
			else{
				alert("có lỗi xảy ra, tour chưa được cập nhật!!!");
				return false;
			}
		}
	})
});
// $(".detailTour").click(function(){
// 	var tourid = parseInt($(this).parent().prev().text());
// 	var param = $(this);
// 	console.log(tourid);
// 	$.ajax({
// 		url: '/tour/detailTour',
// 		data : {tourid: tourid},
// 		type : "POST",
// 		success: function(data){
// 			console.log(data);
// 			if(data=="success"){
// 				alert("tour đã được cập nhật thông tin!");
// 				window.location.href = "/admin";
// 			}else if(data=="403"){
// 				window.location.href = "403";
// 			}
// 			else{
// 				alert("có lỗi xảy ra, tour chưa được cập nhật!!!");
// 			}
// 		}
// 	})
// });

function cancelTour(tourid){
	var result = confirm('Bạn muốn hủy tour này?');
	if(result){
		$.ajax({
			url: '/admin/delete/?tourid='+tourid,
			// data : {tourid: id},
			type : "POST",
			success: function(data){
				console.log(data);
				if(data=="redirect:/admin"){
					alert("tour đã bị hủy!");
					window.location.href = "/admin";// dùng js remove 
				}else if(data=="403"){
					window.location.href = "403";
				}
				else{
					alert("có lỗi xảy ra, tour chưa hủy!!!");
				}
			}
		})
	}else{
		return false;
	}
}