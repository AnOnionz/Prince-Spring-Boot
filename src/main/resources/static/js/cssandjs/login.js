	const inputs = document.querySelectorAll(".input");


	function addcl(){
		let parent = this.parentNode.parentNode;
		parent.classList.add("focus");
	}

	function remcl(){
		let parent = this.parentNode.parentNode;
		if(this.value == ""){
			parent.classList.remove("focus");
		}
	}

	inputs.forEach(input => {
	input.addEventListener("focus", addcl);
	input.addEventListener("blur", remcl);
	});
	function checkPass(){
		var pass  = document.getElementById("password").value;
		var rpass  = document.getElementById("rpassword").value;
		if(pass != rpass){
			document.getElementById("submit").disabled = true;
			$('.missmatch').html("mật khẩu không giống nhau!");
		}else{
			$('.missmatch').html("");
       document.getElementById("submit").disabled = false;
		}
	}
	var password = document.getElementById("password"),
	confirm_password = document.getElementById("confirm_password");

	function validatePassword(){
		if(password.value != confirm_password.value) {
			confirm_password.setCustomValidity("mật khẩu không trùng khớp");
		} else {
			confirm_password.setCustomValidity('');
		}
	}

	password.onchange = validatePassword;
	confirm_password.onkeyup = validatePassword;
	
	
