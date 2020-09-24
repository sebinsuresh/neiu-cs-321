let posts = document.getElementsByClassName("post");
let palette = document.getElementById("plt").value;
palette = palette.substring(1, palette.length-1).split(", ");

document.addEventListener('DOMContentLoaded', loadPosts, false);

function loadPosts(){
    for(let i = 0; i < posts.length; i++){
        let post = posts[i];
        let drawStr = post.children[1].value;   //hidden input element
        let cnv = post.children[0]; // canvas element
        cnv.width = 640;
        cnv.height = 640;
        drawDoodle(cnv, drawStr);
    }
}


function drawDoodle(cnv, drawStr){
    let c = cnv.getContext("2d");
    for(let i = 0; i < 64; i++){
        for(let j = 0; j < 64; j++){
            let colorIndex = drawStr.charAt(j*64 + i);
            c.fillStyle=palette[parseInt(colorIndex,16)];
            if(colorIndex == 'b'){
                c.fillStyle="#FFF";
            }
            c.fillRect(i*10, j*10, 10, 10);
        }
    }
}
