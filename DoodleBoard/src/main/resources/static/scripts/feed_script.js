let posts = document.getElementsByClassName("feedpost");
let feedpalette = document.getElementById("plt").value;
feedpalette = feedpalette.substring(1, feedpalette.length-1).split(", ");

document.addEventListener('DOMContentLoaded', initialize, false);

function initialize(){
    loadPosts();
    $(window).resize(function() {
        loadPosts();
    })

    $('#editouter').click(function(ev){
        if(ev.target.classList.contains("editouter")){
            hideEditRegion();
        }
    })

    $('#viewouter').click(function(ev){
        if(ev.target.classList.contains("viewouter")){
            hideViewRegion();
        }
    })


}

function hideEditRegion(){
    $('#editouter').css("opacity","0%");
    $('#editregion').css("margin-top","20%");
    if(typeof noLoop !== 'undefined'){
        noLoop();   // stop p5js draw() loop
    }

    setTimeout(()=>{
        $('#editouter').hide()
    },~~(1000/3));
}

function showEditRegion(){
    $('#editouter').show();
    $('#editouter').css("opacity","100%");
    $('#editregion').css("margin-top","0%");
    setTimeout(()=>{
        loop();  // restart p5js draw() loop
    },~~(1000/3));
}

function hideViewRegion(){
    $('#viewouter').css("opacity","0%");
    $('#viewregion').css("margin-top","20%");
    setTimeout(()=>{
        $('#viewouter').hide()
    },~~(1000/3));
}

function showViewRegion(){
    $('#viewouter').show();
    $('#viewouter').css("opacity","100%");
    $('#viewregion').css("margin-top","0%");
    setDrawingStr();
    let post = document.getElementById("viewregion");
    let cnv = setcnv(post);
    drawDoodle(cnv, $("#drawingStr").val());
}

function setDrawingStr(){
    let currentId = $("#currEditing").val();
    let newDrawStrValue = $("#str_"+currentId).val();
    let oldTitle = $("#title_"+currentId).text();
    $("#dtitle").val(oldTitle);
    noLoop();
    $("#drawingStr").val(newDrawStrValue);
}

function setP5DrawingArray(){
    setDrawingStr();
    // let newDrawStrValue = $("#drawingStr").val();
    noLoop();
    initializeDrawingArray();
    loop();
}

function loadPosts(){
    for(let i = 0; i < posts.length; i++){
        let post = posts[i];
        let drawStr = post.children[1].value;   //hidden input element
        let cnv = setcnv(post) // canvas element
        drawDoodle(cnv, drawStr);
    }
}

function setcnv(post){
    // let cnv = post.children[0];
    let cnv = $(post).children("canvas")[0];
    let availWidth = ~~(posts[0].clientWidth) - 2*(parseInt($(posts[0]).css('padding-left').substring(-2)));
    cnv.width = 64*~~(availWidth/64);
    cnv.height = cnv.width;
    return cnv;
}


function drawDoodle(cnv, drawStr){
    let c = cnv.getContext("2d");
    let w = ~~(cnv.width/64);
    for(let i = 0; i < 64; i++){
        for(let j = 0; j < 64; j++){
            let colorIndex = drawStr.charAt(j*64 + i);
            c.fillStyle = feedpalette[parseInt(colorIndex,16)];
            c.strokeStyle='rgba(0,0,0,0)';
            if(colorIndex == 'b'){
                c.fillStyle="#FFF";
                c.strokeStyle="#FFF";
            }
            c.fillRect(i*w, j*w, w, w);
        }
    }
}
