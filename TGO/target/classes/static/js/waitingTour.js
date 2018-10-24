 function loadContent(divName,pageURL) {
                $("#" + divName).load(pageURL);
                console.log('loading.................');
            }

$(document).ready(function(){
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
//			data : {tourid: tourid},
			type : "POST",
			success: function(data){
				console.log(data);
				if(data=="redirect:/operator"){
					alert("tour đã bị hủy!");
//					window.location.href = "/operator";// dùng js remove 
					$("#tour"+tourid).remove();
					$("#collapse"+tourid).remove();
				}else if(data=="403"){
					window.location.href = "403";
				}
				else{
					alert("có lỗi xảy ra, tour chưa hủy!!!");
				}
			}
		})
	});

$(".acceptGuide").click(function(){
	var r = confirm("Bạn muốn thêm hướng dẫn viên vào tour?");
	if(r==false){
		return;
	}
	var tourxrefid = parseInt($(this).parent().prev().text());
	var tourid = parseInt($(this).parent().prevAll().eq(1).text());
	var param = $(this);
	console.log('tourxrefid: '+tourxrefid);
	console.log('tourid: '+tourid);
	$.ajax({
		url: '/tour/accept/'+ tourxrefid,
		type: "GET",
		datatype : "JSON",
		success: function(data){
			console.log(data);
			if(data=="success"){
				alert("Thành công!");
//				param.text("đã chấp nhận");
//				param.attr("disabled","disabled");
//				$("#cancel"+tourxrefid).addClass("hidden");
				$("#rowadd"+tourxrefid).remove();
				var amount = $("#currentAmount"+tourid).val();
				console.log("amount: "+amount);
				++amount;
				console.log("amount: "+amount);
				$("#currentAmount"+tourid).val(amount);
				$("#rowcancel"+tourxrefid).add();
				console.log("amount: "+$("#currentAmount"+tourid).val() );
				//add new record in "danh sach hdv"
				loadContent('h'+tourid,'/operator/loadingListGuide?tourid='+tourid)
				//
			}else if(data=="403"){
				window.location.href = "/403";
			}else if(data=="errorAmount"){
				alert("Số lượng hướng dẫn viên đã đủ!!");
			}else{
				alert("có lỗi xảy ra!!");
			}
		}
	})
});


$(".runningTour").click(function(){
	var r = confirm("bạn muốn thực hiện tour này?");
	if(r==false){
		return;
	}
	var tourid = parseInt($(this).parent().prev().text());
	var param = $(this);
	console.log(tourid);
	$.ajax({
		url: "/tour/run/"+tourid,
		type: "POST",
		success: function(data){
			console.log(data);
			if(data=="success"){
				alert("thực hiện tour thành công!");
//				window.location.href = "/operator/waitingTour";
//				$("#tour"+tourid).remove();
				$("#tour"+tourid).remove();
				$("#collapse"+tourid).remove();
			}else if(data=="notExistGuide"){
				alert("Không tìm thấy hdv tham gia tour!");
//				return false;
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
$(".cancelRequestGuide").click(function(){
	var r = confirm("bạn muốn loại hdv này?");
	if(r==false){
		return;
	}
	var tourxrefid = parseInt($(this).parent().prev().text());
	var tourid = parseInt($(this).parent().prevAll().eq(1).text());
	var param = $(this);
	console.log('tourxrefid: '+tourxrefid);
	console.log('tourid: '+tourid);
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
				$("#rowadd"+tourxrefid).remove();
				var amount = $("#currentAmount"+tourid).val();
				console.log("amount: "+amount);
				--amount;
				console.log("amount: "+amount);
				$("#currentAmount"+tourid).val(amount);
				console.log("amount: "+$("#currentAmount"+tourid).val());
//				window.location.href = "/operator/waitingTour";
			}else if(data=="403"){
				window.location.href = "/403";
			}else{
				alert("có lỗi xảy ra!!");
			}
		}
	})
});
});
$(".cancelAccepteTour").click(function(){
	var r = confirm("bạn muốn loại hdv này?");
	if(r==false){
		return;
	}
	var tourxrefid = parseInt($(this).parent().prev().text());
	var tourid = parseInt($(this).parent().prevAll().eq(1).text());
	var param = $(this);
	console.log('tourxrefid: '+tourxrefid);
	console.log('tourid: '+tourid);
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
				$("#rowcancel"+tourxrefid).remove();
				var amount = $("#currentAmount"+tourid).val();
				console.log("amount: "+amount);
				--amount;
				console.log("amount: "+amount);
				$("#currentAmount"+tourid).val(amount);
				console.log("amount: "+$("#currentAmount"+tourid).val());
//				window.location.href = "/operator/waitingTour";
			}else if(data=="403"){
				window.location.href = "/403";
			}else{
				alert("có lỗi xảy ra!!");
			}
		}
	})
});