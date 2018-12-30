
// $(document).ready(function() {
//     var max_fields      = 10; //maximum input boxes allowed
//     var wrapper         = $(".input_fields_wrap"); //Fields wrapper
//     var add_button      = $(".add_field_button"); //Add button ID
//     var x = 1; //initlal text box count
//     $(add_button).click(function(e){ //on add input button click
//         e.preventDefault();
//         if(x < max_fields){ //max input box allowed
//             x++; //text box increment
//             $(wrapper).append('<div class = "input-margin"><div class="col-sm-8 col-sm-offset-3"><select name="location'+x+'" class="form-control"><option>Hà Nội</option><option>Hà Giang</option><option>Cao Bằng</option><option>Bắc Kạn</option><option>Tuyên Quang</option><option>Lào Cai</option><option>Điện Biên</option><option>Lai Châu</option><option>Sơn La</option><option>Yên Bái</option><option>Hòa Bình</option><option>Thái Nguyên</option><option>Lạng Sơn</option><option>Quảng Ninh</option><option>Bắc Giang</option><option>Phú Thọ</option><option>Vĩnh Phúc</option><option>Bắc Ninh</option><option>Hải Dương</option><option>Hải Phòng</option><option>Hưng Yên</option><option>Thái Bình</option><option>Hà Nam</option><option>Nam Định</option><option>Ninh Bình</option><option>Thanh Hóa</option><option>Nghệ An</option><option>Hà Tĩnh</option><option>Quảng Bình</option><option>Quảng Trị</option><option>Thừa Thiên Huế</option><option>Đà Nẵng</option><option>Quảng Nam</option><option>Quảng Ngãi</option><option>Bình Định</option><option>Phú Yên</option><option>Khánh Hòa</option><option>Ninh Thuận</option><option>Bình Thuận</option><option>Kon Tum</option><option>Gia Lai</option><option>Đắk Lắk</option><option>Đắk Nông</option><option>Lâm Đồng</option><option>Bình Phước</option><option>Tây Ninh</option><option>Bình Dương</option><option>Đồng Nai</option><option>Bà Rịa - Vũng Tàu</option><option>Hồ Chí Minh</option><option>Long An</option><option>Tiền Giang</option><option>Bến Tre</option><option>Trà Vinh</option><option>Vĩnh Long</option><option>Đồng Tháp</option><option>An Giang</option><option>Kiên Giang</option><option>Cần Thơ</option><option>Hậu Giang</option><option>Sóc Trăng</option><option>Bạc Liêu</option><option>Cà Mau</option></select></div><a href="#" class="remove_field btn btn-link"><span class="glyphicon glyphicon-minus"></span></a></div>'); //add input box <a href="#" class="remove_field ">X</a>
//             $("#count-location").val(x);

//         }
//     });
    
//     $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
//         e.preventDefault(); $(this).parent('div').remove(); x--;
//         $("#count-language").val(x);
//         $(".input-margin1").remove();
//         for(var i=2;i<=x;i++){
//             $(wrapper).append('<div class = "input-margin1"><div class="col-sm-8 col-sm-offset-3"><select name="location'+i+'" class="form-control"><option>Hà Nội</option><option>Hà Giang</option><option>Cao Bằng</option><option>Bắc Kạn</option><option>Tuyên Quang</option><option>Lào Cai</option><option>Điện Biên</option><option>Lai Châu</option><option>Sơn La</option><option>Yên Bái</option><option>Hòa Bình</option><option>Thái Nguyên</option><option>Lạng Sơn</option><option>Quảng Ninh</option><option>Bắc Giang</option><option>Phú Thọ</option><option>Vĩnh Phúc</option><option>Bắc Ninh</option><option>Hải Dương</option><option>Hải Phòng</option><option>Hưng Yên</option><option>Thái Bình</option><option>Hà Nam</option><option>Nam Định</option><option>Ninh Bình</option><option>Thanh Hóa</option><option>Nghệ An</option><option>Hà Tĩnh</option><option>Quảng Bình</option><option>Quảng Trị</option><option>Thừa Thiên Huế</option><option>Đà Nẵng</option><option>Quảng Nam</option><option>Quảng Ngãi</option><option>Bình Định</option><option>Phú Yên</option><option>Khánh Hòa</option><option>Ninh Thuận</option><option>Bình Thuận</option><option>Kon Tum</option><option>Gia Lai</option><option>Đắk Lắk</option><option>Đắk Nông</option><option>Lâm Đồng</option><option>Bình Phước</option><option>Tây Ninh</option><option>Bình Dương</option><option>Đồng Nai</option><option>Bà Rịa - Vũng Tàu</option><option>Hồ Chí Minh</option><option>Long An</option><option>Tiền Giang</option><option>Bến Tre</option><option>Trà Vinh</option><option>Vĩnh Long</option><option>Đồng Tháp</option><option>An Giang</option><option>Kiên Giang</option><option>Cần Thơ</option><option>Hậu Giang</option><option>Sóc Trăng</option><option>Bạc Liêu</option><option>Cà Mau</option></select></div><a href="#" class="remove_field btn btn-link"><span class="glyphicon glyphicon-minus"></span></a></div>');
//         }
//     })
// });
$(document).ready(function() {
    var max_fields      = 10; //maximum input boxes allowed
    var wrapper         = $(".input_fields_wrap"); //Fields wrapper
    var add_button      = $(".add_field_button"); //Add button ID
    
    var x = 1; //initlal text box count
    $(add_button).click(function(e){ //on add input button click
        e.preventDefault();
        if(x < max_fields){ //max input box allowed
            x++; //text box increment
            $(wrapper).append('<div class = "input-margin"><div class="col-sm-8 col-sm-offset-3"><select name="location'+x+'" class="form-control location"><option>Hà Nội</option><option>Hà Giang</option><option>Cao Bằng</option><option>Bắc Kạn</option><option>Tuyên Quang</option><option>Lào Cai</option><option>Điện Biên</option><option>Lai Châu</option><option>Sơn La</option><option>Yên Bái</option><option>Hòa Bình</option><option>Thái Nguyên</option><option>Lạng Sơn</option><option>Quảng Ninh</option><option>Bắc Giang</option><option>Phú Thọ</option><option>Vĩnh Phúc</option><option>Bắc Ninh</option><option>Hải Dương</option><option>Hải Phòng</option><option>Hưng Yên</option><option>Thái Bình</option><option>Hà Nam</option><option>Nam Định</option><option>Ninh Bình</option><option>Thanh Hóa</option><option>Nghệ An</option><option>Hà Tĩnh</option><option>Quảng Bình</option><option>Quảng Trị</option><option>Thừa Thiên Huế</option><option>Đà Nẵng</option><option>Quảng Nam</option><option>Quảng Ngãi</option><option>Bình Định</option><option>Phú Yên</option><option>Khánh Hòa</option><option>Ninh Thuận</option><option>Bình Thuận</option><option>Kon Tum</option><option>Gia Lai</option><option>Đắk Lắk</option><option>Đắk Nông</option><option>Lâm Đồng</option><option>Bình Phước</option><option>Tây Ninh</option><option>Bình Dương</option><option>Đồng Nai</option><option>Bà Rịa - Vũng Tàu</option><option>Hồ Chí Minh</option><option>Long An</option><option>Tiền Giang</option><option>Bến Tre</option><option>Trà Vinh</option><option>Vĩnh Long</option><option>Đồng Tháp</option><option>An Giang</option><option>Kiên Giang</option><option>Cần Thơ</option><option>Hậu Giang</option><option>Sóc Trăng</option><option>Bạc Liêu</option><option>Cà Mau</option></select></div><a href="#" class="remove_field btn btn-link"><span class="glyphicon glyphicon-minus"></span></a></div>'); //add input box <a href="#" class="remove_field ">X</a>
//            $(wrapper).append('<div class = "input-margin"><div class="col-sm-8 col-sm-offset-3"><select name="location'+x+'" class="form-control location"><option th:each="location:${listLocation}" th:text="${location.locationName}"></option></select></div><a href="#" class="remove_field btn btn-link"><span class="glyphicon glyphicon-minus"></span></a></div>');
            $("#count-location").val(x);
            

        }
    });
    
    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
        e.preventDefault(); $(this).parent('div').remove(); x--;
        $("#count-location").val(x);
        var z               = $(".location");
        for (var i = 0; i<=z.length;i++) {
            $(z[i]).attr("name", "location"+(i+2));
        }
        
    })
});
$(document).ready(function() {
    var max_fields      = 10; //maximum input boxes allowed
    var wrapper         = $(".input_fields_wrap1"); //Fields wrapper
    var add_button      = $(".add_field_button1"); //Add button ID
    
    var x = 0; //initlal text box count
    $(add_button).click(function(e){ //on add input button click
        e.preventDefault();
        if(x < max_fields){ //max input box allowed
            x++; //text box increment
            $(wrapper).append('<div class = "input-margin"><div class="col-sm-8 col-sm-offset-3"><select name="language'+x+'" class="form-control language"><option>Akan</option><option>Amharic</option><option>Amharic</option><option>Assamese</option><option>Awadhi</option><option>Azerbaijani</option><option>Balochi</option><option>Belarusian</option><option>Bhojpuri</option><option>Burmese</option><option>Cebuano (Visayan)</option><option>Chewa</option><option>Chhattisgarhi</option><option>Chittagonian</option><option>Czech</option><option>Deccan</option><option>Dhundhari</option><option>Dutch</option><option>Eastern Min</option><option>English</option><option>French</option><option>Fula</option><option>Gan Chinese</option><option>German</option><option>Greek</option><option>Gujarati</option><option>Haitian Creole</option><option>Hakka</option><option>Haryanvi</option><option>Hausa</option><option>Hiligaynon</option><option>Hindi</option><option>Hmong</option><option>Hungarian</option><option>Igbo</option><option>Ilocano</option><option>Italian</option><option>Japanese</option><option>Javanese</option><option>Jin</option><option>Kannada</option><option>Kazakh</option><option>Khmer</option><option>Kinyarwanda</option><option>Kirundi</option><option>Konkani</option><option>Korean</option><option>Kurdish</option><option>Madurese</option><option>Magahi</option><option>Maithili</option><option>Malagasy</option><option>Malay/Indonesian</option><option>Malayalam</option><option>Mandarin</option><option>Marathi</option><option>Marwari</option><option>Mossi</option><option>Nepali</option><option>Northern Min</option><option>Odia (Oriya)</option><option>Oromo</option><option>Pashto</option><option>Persian</option><option>Polish</option><option>Portuguese</option><option>Punjabi</option><option>Quechua</option><option>Romanian</option><option>Russian</option><option>Saraiki</option><option>Serbo-Croatian</option><option>Shona</option><option>Sindhi</option><option>Sinhalese</option><option>Somali</option><option>Southern Min</option><option>Spanish</option><option>Sundanese</option><option>Swedish</option><option>Sylheti</option><option>Tagalog</option><option>Tamil</option><option>Telugu</option><option>Thai</option><option>Turkish</option><option>Turkmen</option><option>Ukrainian</option><option>Urdu</option><option>Uyghur</option><option>Uzbek</option><option>Vietnamese</option><option>Wu (inc. Shanghainese)</option><option>Xhosa</option><option>Xiang (Hunnanese)</option><option>Yoruba</option><option>Yue (Cantonese)</option><option>Zhuang</option><option>Zulu</option></select></div><a href="#" class="remove_field1 btn btn-link"><span class="glyphicon glyphicon-minus"></span></a></div>'); //add input box <a href="#" class="remove_field ">X</a>
            $("#count-language").val(x);

        }
    });
    
    $(wrapper).on("click",".remove_field1", function(e){ //user click on remove text
        e.preventDefault(); $(this).parent('div').remove(); x--;
        $("#count-language").val(x);
        var z               = $(".language");
        for (var i = 0; i<=z.length;i++) {
            $(z[i]).attr("name", "language"+(i+2));
        }
        
    })
});
$(document).ready(function() {
    var change_button = $("#changelocation");
    
    $(change_button).click(function(e){
        if ($("#count-location").val()==0) {
            var z               = $(".location");
            $("#count-location").val(z.length +1);
    } else {
        $("#count-location").val(0);
    }
    })
});
$(document).ready(function() {
    var change_button = $("#changelanguage");
    
    $(change_button).click(function(e){
        if ($("#count-language").val()==0) {
            var z               = $(".language");
            $("#count-language").val(z.length +1);
    } else {
        $("#count-language").val(0);
    }
    })
});
