let posts = document.getElementsByClassName("feedpost");
let palette = document.getElementById("plt").value;
palette = palette.substring(1, palette.length-1).split(", ");

document.addEventListener('DOMContentLoaded', initialize, false);

function initialize(){
    loadPosts();
    $(window).resize(function() {
        loadPosts();
    })
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
    let cnv = post.children[0];
    cnv.width = ~~(posts[0].clientWidth) - 2*(parseInt($(posts[0]).css('padding-left').substring(-2)));
    cnv.height = cnv.width;
    return cnv;
}


function drawDoodle(cnv, drawStr){
    let c = cnv.getContext("2d");
    let w = ~~(cnv.width/64);
    for(let i = 0; i < 64; i++){
        for(let j = 0; j < 64; j++){
            let colorIndex = drawStr.charAt(j*64 + i);
            c.fillStyle=palette[parseInt(colorIndex,16)];
            c.strokeStyle='rgba(0,0,0,0)';
            if(colorIndex == 'b'){
                c.fillStyle="#FFF";
                c.strokeStyle="#FFF";
            }
            c.fillRect(i*w, j*w, w, w);
        }
    }
}
