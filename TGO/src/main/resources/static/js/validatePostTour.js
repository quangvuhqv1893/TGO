function postour(){
	var tourname = document.querySelector('#tourname').value.trim();
	
	console.log(tourname);
	if(tourname==""){
		alert('Tên tour nhập không hợp lệ!');
		return false;
	}
	//validate amount
	var amount = document.querySelector('#amount').value.trim();
	if (amount<=0) {
		alert("số lượng hướng dẫn viên không được nhỏ hơn 0!");
		return false;
	}
	//validate startdate
	var startDate = document.querySelector('#startDate').value;
	if(startDate==""||Date.parse(startDate)<Date.parse(new Date())){
		alert("Ngày bắt đầu không thể nhỏ hơn ngày hiện tại!!");
		return false;
	}
	//validate endDate
	var endDate = document.querySelector('#endDate').value;
	if(endDate==""||Date.parse(endDate)<Date.parse(startDate)){
		alert("Ngày kết thúc không thể nhỏ hơn ngày bắt đầu!!");
		return false;
	}
	var tourprice = document.querySelector('#tourprice').value.trim();
	if(tourprice<10000){
		alert('giá tour không được dưới 10.000VND');
		return false;
	}
	$.ajax({
		url: '/operator/posttour',
		data: $('form#formPostTour').serialize(),
		type: 'GET',
		datatype : "JSON",
		success: function(data){
			console.log(data);
			if(data == "success"){
				alert("post tour thành công");
				window.location.href = "/operator";
				return;
			}else if(data=="403"){
				window.location.href = "/403";
				}
			else{
				alert("post tour thất bại!");
				window.location.href = "/operator";
			}
		}
	})
}