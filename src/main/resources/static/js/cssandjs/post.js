window.onload = function() {
	document.getElementById("1").style.display = "none";
	var display2 = document.querySelector('#time'), timer2 = new CountDownTimer(
			15);

	timer2.onTick(format(display2)).start();

	function format(display) {
		return function(minutes, seconds) {
			minutes = minutes < 10 ? "0" + minutes : minutes;
			seconds = seconds < 10 ? "0" + seconds : seconds;
			display.textContent = minutes + ':' + seconds;
		};
	}
};
function CountDownTimer(duration, granularity) {
	this.duration = duration;
	this.granularity = granularity || 1000;
	this.tickFtns = [];
	this.running = false;
}
CountDownTimer.prototype.start = function() {
	if (this.running) {
		return;
	}
	this.running = true;
	var start = Date.now(), that = this, diff, obj;
	(function timer() {
		diff = that.duration - (((Date.now() - start) / 1000) | 0);
		if (diff > 0) {
			setTimeout(timer, that.granularity);
		} else {
			diff = 0;
			document.getElementById("2").style.display = "none";
			document.getElementById("1").style.display = "grid";
			that.running = false;

		}
		obj = CountDownTimer.parse(diff);
		that.tickFtns.forEach(function(ftn) {
			ftn.call(this, obj.minutes, obj.seconds);
		}, that);
	}());
};
CountDownTimer.prototype.onTick = function(ftn) {
	if (typeof ftn === 'function') {
		this.tickFtns.push(ftn);
	}
	return this;
};
CountDownTimer.prototype.expired = function() {
	return !this.running;
};
CountDownTimer.parse = function(seconds) {
	return {
		'minutes' : (seconds / 60) | 0,
		'seconds' : (seconds % 60) | 0
	};
};