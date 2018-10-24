$(".sendRequest").click(function(){
	var r = confirm("bạn muốn gửi yêu cầu");
	if(r==false){
		return;
	}
	var tourid = parseInt($(this).parent().prev().text());
	var param = $(this);
	console.log(tourid);
	
	$.ajax({
		url:'/guide/guideSendrequest',
		data : {tourid: tourid},
		type : "GET",
		datatype : "JSON",
		success: function(data){
			console.log(data);
			if(data=="success"){
			alert(" Gửi yêu cầu thành công");
//			param.text("đã gửi yêu cầu");
//			param.attr("disabled","disabled");
			$("#tour"+tourid).remove();
			$("#collapse"+tourid).remove();
		}else if (data=="403") {
			window.location.href = "/403";
		}else{
			alert("Gửi yêu cầu thất bại");
		}
		}
	})
});