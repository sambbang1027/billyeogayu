// 작성자 : 서샘이


document.addEventListener("DOMContentLoaded", function () {
  getUser ();
});


function getUser () {
	$.ajax({
	  url: "/api/profile",
	  type: "GET",
	  success: function(res) {
	//console.log('header ', res);
	  const adminName = res.data.name;
		$(".user-text-box").text(adminName);
   
   	  },
	  error: function(xhr) {
	    if(xhr.status === 401){
	      alert("로그인이 필요합니다.");
	      location.href = "/login";
	    } else {
	      showAlert("회원정보를 가져올 수 없습니다", () =>{
			 	console.error('점검 신청 실패', error); location.reload();})
	  	}
	  }
	});
}

$(function() {
  $(document).on("click", ".logout-img-box", function() {
    console.log("클릭됨!"); // 테스트
	$.ajax({
	   url: "/logout",
	   type: "GET",
	   success: function(res) {
	     console.log("로그아웃 성공:", res);
	     // 로그아웃 성공 후 로그인 페이지로 이동
	     location.href = "/login";
	   },
	   error: function(xhr) {
	     if(xhr.status === 401){
	       location.href = "/login";
	     } else {
	       console.error("로그아웃 실패", xhr);
	     }
	   }
	 })
  });
});

