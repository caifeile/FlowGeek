function showImagePreview(url) {
	var idx = 0;
	for(var i=0;i<allImgUrls.length;i++){
		if(allImgUrls[i]==url){
			idx= i;
			break;
		}
	}
	var jsonData = "{\"url\":\""+ url +"\",\"index\":"+idx+",\"urls\":\""+allImgUrls+"\"}";
	window.location = "ima-api:action=showImage&data="+jsonData;
}

function getAllImgSrc(htmlstr) {
    var reg=/<img.+?src=('|")?([^'"]+)('|")?(?:\s+|>)/gim;
    var arr = [];
    while(tem=reg.exec(htmlstr)){
        arr.push(tem[2]);
    }
    return arr;
}
