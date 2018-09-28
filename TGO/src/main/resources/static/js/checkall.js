function  CheckListGuideReady(obj){
	console.log("anc: ");
//	alert("attribute");
	var id = $(obj).attr('class');
	console.log('id: '+id);
	var length = $('.gl'+id).length;
	console.log('guideList: '+length);
	if(length==0){
		 this.$("#noticeListGuide"+id).removeClass("hidden");
	}else  $("#noticeListGuide"+id).addClass("hidden");
	
}
function  CheckListGuideRequest(obj){
	var id = $(obj).attr('class');
	console.log('id: '+id);
	var length = $('.rl'+id).length;
	console.log('requestlist: '+length);
	if(length==0){
		this.$("#noticelistGuideRequest"+id).removeClass("hidden");
	}else  $("#noticelistGuideRequest"+id).addClass("hidden");
	
}