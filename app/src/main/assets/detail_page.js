var title;
var author;
var time;
var body;
var images;
var imageCount = 0;

function load_day() {
    document.bgColor="#FF0000";
    var font = document.getElementById("font");
    if (!font) {
        return;
    }
    font.style.color="white";
}

function load_night() {
    document.bgColor="#1f1f1f";
    var font = document.getElementById("font");
    if (!font) {
        return;
    }
    font.style.color="black";
}

function fill(detailBody) {
     var myBody = document.getElementById('body');
     myBody.innerHTML = detailBody
}

function changeFontSize(body) {
    var myBody = document.getElementById('article_body');
    myBody.style.fontSize="22px"
    myBody.innerHTML = body
}

function showSuperBigSize() {
	var myBody = document.getElementById('article_body');
    myBody.style.fontSize="26px";
}

function showBigSize() {
	var myBody = document.getElementById('article_body');
    myBody.style.fontSize="22px";
}

function showMidSize() {
	var myBody = document.getElementById('article_body');
    myBody.style.fontSize="18px";
}

function showSmallSize() {
	var myBody = document.getElementById('article_body');
    myBody.style.fontSize="16px";
}