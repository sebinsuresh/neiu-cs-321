let palette = $("#palette").val();
palette = palette.substring(1, palette.length-1).split(", ");
document.addEventListener('DOMContentLoaded', initialize, false);

function initialize() {
    drawCanvases();

    // From https://stackoverflow.com/a/27923937
    $(window).resize(function() {
        clearTimeout(window.resizedFinished);
        window.resizedFinished = setTimeout(function(){
            drawCanvases();
        }, 300);
    });
}

function drawCanvases(){
    let canvasdivs = $(".canvasdiv");

    let cnvWidth = canvasdivs[0].clientWidth;
    cnvWidth = 64*Math.floor(cnvWidth/64);
    let pixelSize = cnvWidth/64;

    for(let i = 0; i < canvasdivs.length; i++){
        setCanv(i, cnvWidth, pixelSize);
    }
}

function setCanv(index, cnvWidth, pixelSize){
    let cnv = $("#cnv_"+index)[0];
    cnv.width = cnvWidth;
    cnv.height = cnvWidth;

    let c = cnv.getContext("2d");
    let str = $("#str_"+index).val();

    for(let i = 0; i < 64; i++){
        for(let j = 0; j < 64; j++){
            let colorIndex = str.charAt(j*64 + i);
            c.fillStyle = palette[parseInt(colorIndex,16)];
            if(colorIndex == 'b'){
                c.fillStyle="#FFF";
            }
            c.fillRect(i*pixelSize, j*pixelSize, pixelSize, pixelSize);
        }
    }
}