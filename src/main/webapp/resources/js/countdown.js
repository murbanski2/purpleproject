/* 
 * Handle the real time update of countdown to end of auction
 */

var myTimer;
var e;
var startValue;

function countDown() {
    if(startValue > 0) {
        startValue--;
        e.innerHTML = formatValue(startValue);
    } else {
        // if -1 or 0 the auction has ended
        e.innerHTML = formatValue(startValue);
    }
}

function formatValue(timeValue) {
  var fmt,days,hrs,mins,secs;
  var DAY_SEC = 60 * 60 * 24;
  var HR_SEC = 60 * 60;
  var MIN_SEC = 60;

  if(timeValue <= 0) {
    stopCountDown();
    return "Ended";
  }

  //  get days to go
  days = Math.floor(timeValue / DAY_SEC);
  timeValue -= (days * DAY_SEC);

  // get hours to go
  hrs = Math.floor(timeValue / HR_SEC);
  timeValue -= (hrs * HR_SEC);

  // get minutes to go
  mins = Math.floor(timeValue / MIN_SEC);
  timeValue -= (mins * MIN_SEC);

  // get seconds to go
  secs = timeValue;

  // now prepare format and return
  fmt = "Time left: " + days + "d " + hrs + "h " + mins + "m " + secs + "s";
  return fmt;
}

function startCountDown() {
   myTimer = window.setInterval("countDown()",1000);
}

function stopCountDown() {
   window.clearInterval(myTimer);
}
