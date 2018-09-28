window.onload = function() {
    // interval slider
    setInterval(runSlider, 10000);
    // set on click event listener for every element in page
    window.onclick = setOnClickListener;

    setInterval(function() {
        var carouselList = document.querySelector('.list');
        curLeft = parseInt(carouselList.style.left) || 0;
        carouselList.style.left = `${(curLeft >= -1150)? curLeft-210: 0}px`;
    }, 5000);
    
    // set listener for radio group

    $("input:radio[name='org_ind']").change(function () {
        if ($("input[name='org_ind']:checked").val() == 'operator') {
            $(".form-right #operator-infos").removeClass("hidden");
            $(".form-right #guide-infos").addClass("hidden");
            $("#nameViet")[0].required = true;
            $("#taxCode")[0].required = true;
            $("#businessRegistrationCode")[0].required = true;
//            $("#representative")ơ]
            $(".radio")[0].required = false;
            return;
        }
        if ($("input[name='org_ind']:checked").val() == 'guide') {
            $(".form-right #guide-infos").removeClass("hidden");
            $(".form-right #operator-infos").addClass("hidden");
            $("#nameViet")[0].required = false;
            $(".radio")[0].required = true;
            $("#businessRegistrationCode")[0].required = false;
            $("#taxCode")[0].required = false;
            return;
            
        }
    });
    // css selector
    // re-password:  .signup-group:not(.hidden) > input[type='password']
    // password: .form-left > input[type='password']
}

function setOnClickListener(event) {
    var target = event.target;
    switch (target.id) {
        case 'txt_sign_in':
        case 'join_us': 
            openFormRegister('default');
            break;
        case 'txt_sign_up':
            openFormRegister('signup')
            break;
        case 'close_form':
            closeFormRegister();
            break;
        default:
            // just default
    }
    switch (target.className) {
        case 'prev':
            handleCarousel('prev');
            break;
        case 'next':
            handleCarousel('next');
            break;
        default:
            // just default
    }
}

function runSlider() {
    var currentActive = document.querySelector('.story.slide-active');
    var nextActive = currentActive.nextElementSibling || currentActive.parentNode.firstElementChild;
    currentActive.classList.remove('slide-active');
    nextActive.classList.add('slide-active');
}

function openFormRegister(mode) {
    if (mode === 'signup') {
        document.querySelector('.signup-group').classList.remove('hidden');
        document.querySelector('.social-sign-in').classList.add('hidden');
        document.querySelector('.line-sep').classList.add('hidden');
        document.querySelector('.btn-submit').value = 'Sign up';
        $("#form1").attr("action","/signup");
        //remove css for signup
        $('.form-register').css('background-image', 'url("")');
        $('.form-register').css('height', '590px');
        $('.form-register').css('width', '925px');
        $(".form-right").removeClass("hidden");
        $(".radio_org")[0].required = true;
        $("#re-password")[0].required = true;
        $("#email")[0].required = true;
        $("#phonenumber")[0].required = true;
//        $(".messError").text("");
//        document.querySelector('.messError').classList.add('hidden');
//        $(".form-right").addClass("hidden");
    } else {
        document.querySelector('.signup-group').classList.add('hidden');
        document.querySelector('.social-sign-in').classList.remove('hidden');
        document.querySelector('.btn-submit').value = 'Sign in';
        document.querySelector('.line-sep').classList.remove('hidden');
        //add css for signin
        $('.form-register').css('background-image', 'url("../image/bus.png")');
        $('.form-register').css('height', '500px');
        $('.form-register').css('width', '700px');
//        document.querySelector('.messError').classList.remove('hidden');
        $("#form1").attr("action","/signin");
        $(".radio_org")[0].required = false;
        $("#re-password")[0].required = false;
        $("#email")[0].required = false;
        $("#phonenumber")[0].required = false;
        $(".form-right").addClass("hidden");
       
//        $(".messError").text("");
        // check password onchange
    }
    var formContainer = document.querySelector('.form-container');
    formContainer.classList.remove('hidden');
}

function closeFormRegister() {
    var formContainer = document.querySelector('.form-container');
    formContainer.classList.add('hidden');
}

function handleCarousel(move) {
    var carouselList = document.querySelector('.list');
    curLeft = parseInt(carouselList.style.left) || 0;
    if (move === 'prev') {
        carouselList.style.left = `${(curLeft >= -1150)? curLeft-210: curLeft}px`;
    } else {
        carouselList.style.left = `${(curLeft <= -210)? curLeft+210: curLeft}px`;
    }
}
$(document).ready(function(){
	$("#btn_sign_in_up").click(function() {
	if(document.querySelector('.btn-submit').value == 'Sign in' ){
		console.log("Sign in");
		var username  = document.querySelector('#username').value.trim();
		validationRule = /^\S{6,}$/;
		console.log(username);
		if(!validationRule.test(username)){
			alert("username phải lớn hơn 5 ký tự và không được chứa dấu cách!");
			return false;
		}
		var password = document.querySelector('#password').value;
		if(password.length<6){
			alert("password phải lớn hơn 5 kí tự!");
			return false;
		}
		$.ajax({
			url: '/signin',
			data: {username: username, password: password},
			type: 'POST',
			dataType: 'text',
			success: function(respone){
			  console.log(respone);
			  
				if(respone==="notExist"){
					alert("tài khoản không tồn tại!!");
				}
				if(respone==="notApproval"){
					alert("tài khoản chưa được phê duyệt!!");
				}
				if(respone==="fail"){
					alert("tài khoản và mật khẩu không khớp!!");
				}
				if(respone==="success"){
					 $.ajax({
					    url: '/login',
							data: $('form#form1').serialize(),
							type: 'POST',
							dataType: 'text',
					        success: function(result){
					        	console.log($('#error').text());
					            if($('#error').text()!=null && $('#error').text()!=""){
					            	console.log($('#error').text());
					            	alert("tài khoản và mật khẩu không khớp!!!");
					            	window.location.href = "/index";
					            }
					            window.location.href = "/default";
					        }
					    });
				}
				
			}
		});
		
	}else if(document.querySelector('.btn-submit').value == 'Sign up'){
		console.log("Sign up");
		//validate username lenght>=6 and no exist space
		var username  = document.querySelector('#username').value.trim();
		validationRule = /^\S{6,}$/;
		console.log(username);
		if(!validationRule.test(username)){
			alert("username phải lớn hơn 5 ký tự và không được chứa dấu cách!");
			return false;
		}
		//match password and re-password
		var password = document.querySelector('#password').value;
		if(password.length<6){
			alert("password phải lớn hơn 5 kí tự!");
			return false;
		}
		var repassword = document.querySelector('#re-password').value;
		if( password!=repassword){
			alert("password không khớp!")
			return false;
		}
		//validate email
		var email  = document.querySelector('#email').value.trim();
		validationRule1= /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		console.log(email);
		if(!validationRule1.test(email)){
			alert("email chưa đúng định dạng!");
			return false;
		}
		//validate phone
		var phonenumber  = document.querySelector('#phonenumber').value.trim();
		validationRule2= /^(84|0)(1\d{9}|9\d{8})$/;
		console.log(phonenumber);
		if(!validationRule2.test(phonenumber)){
			alert("số điện thoại chưa đúng định dạng!");
			return false;
		}
		//validate address
		var address = document.querySelector("#address").value.trim();
		if(address==""){
			alert("không được để trống địa chỉ!!");
			return false;
		}
		//validate name of user signup
		var fullname = document.querySelector("#fullname").value.trim();
		if(fullname==""){
			alert("không được để trống tên người đăng kí!!");
			return false;
		}
		//validate raido check type
		if ($("input[name='org_ind']:checked").length <= 0) {
			alert("phải chọn loại tài khoản muốn tạo!");
			return false;
		}
		//validate info of operator or guide
		if($("input[name='org_ind']:checked").val()=="operator"){
			//validate name of operator
			if(document.querySelector("#nameViet").value.trim()==""){
				alert(" không được để trống tên công ty");
				return false;
			}
			//validate representative
			if(document.querySelector("#representative").value.trim()==""){
				alert("không được để trống tên người đại diện!");
				return false;
			}
			//validate businessRegistrationCode
			var businessRegistrationCode  = document.querySelector('#businessRegistrationCode').value.trim();
			validationRule3= /^\d{3,5}$/;
			console.log(businessRegistrationCode);
			if(!validationRule3.test(businessRegistrationCode)){
				alert("mã số kinh doanh chưa đúng định dạng!");
				return false;
			}
			//validate taxcode
			var taxCode  = document.querySelector('#taxCode').value.trim();
			validationRule4= /^\d{9}$/;
			console.log(taxCode);
			if(!validationRule4.test(taxCode)){
				alert("mã số thuế chưa đúng định dạng!");
				return false;
			}
		}else{
			//validate birthday
			var birthdate = document.querySelector('#birthdate').value;
			console.log(birthdate);
			if(birthdate==""||Date.parse(birthdate)>Date.parse(new Date())){
				alert("Ngày sinh không được để trống hoặc lớn hơn ngày hiện tại!!");
				return false;
			}
			//validate guide card number ^\d{9}$
			var cardnumber  = document.querySelector('#cardnumber').value.trim();
			validationRule5= /^\d{9}$/;
			console.log(cardnumber);
			if(!validationRule5.test(cardnumber)){
				alert("mã số thẻ chưa đúng định dạng!");
				return false;
			}
			//validate date Expiration of card
			var dateExpiration = document.querySelector('#dateExpiration').value;
			console.log(dateExpiration);
			if(dateExpiration==""||Date.parse(dateExpiration)<Date.parse(new Date())){
				alert("Ngày hết hạn thẻ không được để trống hoặc nhỏ hơn ngày hiện tại!!");
			}
			//validate exp
			var year  = document.querySelector('#year').value.trim();
			var month  = document.querySelector('#month').value.trim();
			var day  = document.querySelector('#day').value.trim();
			validationRule6= /^\d{1,10}$/;
			console.log(year);
			console.log(month);
			console.log(day);
			if(!validationRule6.test(year)||!validationRule6.test(month)||!validationRule6.test(day)){
				alert("ngày, tháng, năm kinh nghiệm phải là những số nguyên!");
				return false;
			}
			//validate gender
			if ($("input[name='gender']:checked").length <= 0) {
				alert("giới tính không được để trống!");
				return false;
			}
		}
		//hứng dữ liệu từ server trả về
		//popup confirm send data to server
		var r = confirm("xác nhận đăng kí?");
		if(r==false){
			return false;
		}
		$.ajax({
			url: '/signup',
			data: $('form#form1').serialize(),
//			data:
			type: 'POST',
			datatype : "JSON",
			success: function(data){
				console.log(data);
				if(data == "success"){
					alert("đăng kí thành công!");
					window.location.href = "/index";
				}
				if(data=="existUser"){
					alert("username đã được sử dụng!");
				}
				if(data=="fail"){
					alert("đăng kí thất bại!");
				}
				
			}
		});
	}	
});
});
