let palette;
let drawing;
let drawStartX = 30, drawStartY=30;
let pxSize = 10;
let currCol;
let isDrawing = false;
let drawStrElem;

// P5js' setup() function will occur once before the first draw() call occurs
function setup() {
    // Set the div #p5CanvasDiv as the parent to the canvas created by p5js
    cnv = createCanvas(700, 800);
    cnv.parent('p5CanvasDiv');
    textSize(16);
    textFont('consolas');

    // Get palette hex values in an array
    palette = document.getElementById("plt").value;
    palette = palette.substring(1, palette.length-1).split(", ");
    // console.log(palette);

    drawStrElem = document.getElementById("drawingStr");
    let initialDrawing = drawStrElem.value;

    // Initialize the 64x64 array
    drawing = [];
    for(let i = 0; i < 64; i++){
        drawing.push([]);
        for(let j = 0; j < 64; j++){
            // drawing[i].push(random(palette));
            // drawing[i].push("#000000");
            drawing[i].push(palette[parseInt(initialDrawing.charAt(j*64 + i),16)])
        }
    }

    currentCol = 0;
}

// P5js' draw() function will iterate 60 times per second.
function draw() {
    background(255);

    noStroke();
    fill(palette[0]);

    text('◀ ▶ Press L/R arrow keys to switch colors.', drawStartX, height - 50);
    text('🖱   Left click to draw, Right Click to Erase.', drawStartX, height - 30);
    text('🔄  Refresh to reset.', drawStartX, height - 10);

    stroke(0);
    noFill();
    square(drawStartX, drawStartY, 640);

    // drawCheckerBoard(drawStartX, drawStartY, 640, 640);     // Draw the white & gray checkerboard pattern

    for(let i = 0; i < drawing.length; i++){
        for(let j = 0; j < drawing[i].length; j++){
            noStroke();
            fill(drawing[i][j]);
            if(drawing[i][j] == "#000000") {
                noFill();   // Transparent pixels
            }
            square(drawStartX + (i*pxSize), drawStartY + (j*pxSize), pxSize);
        }
    }
    drawPalette();
    handleMouse();
    setDrawingString(); // set the drawingStr element value
}

// Clear the canvas by filling all pixels with transparent
function clearCanvas(){
    for(let i = 0; i < 64; i++){
        for(let j = 0; j < 64; j++){
            // drawing[i].push(random(palette));
            drawing[i][j] = "#000000";
        }
    }
}

// Draw the white and gray checkerboard for the transparent effect in background
function drawCheckerBoard(sX, sY, w, h){
    noStroke();
    let fillC = 255;
    let tiny = ~~(pxSize/2)
    for(let i = sX; i < sX + w; i += tiny){
        for(let j = sY; j < sY + h ; j += tiny){
            if(fillC == 202)
                fillC = 255;
            else
                fillC = 202;
            fill(fillC);
            square(i, j, tiny);
        }
        if(fillC == 202)
            fillC = 255;
        else
            fillC = 202;
    }
}

// Function that draws the palette below the drawing area and handles the clicks
function drawPalette(){
    let swatchSize = 50;
    let paddingSize = 3.63;
    let startY = 680;
    for(let i = 0; i < 12; i++){
        noStroke();
        fill(palette[i]);
        let startX = drawStartX + i*swatchSize + i * paddingSize;
        if(i == 11) {
            drawCheckerBoard(startX, startY, swatchSize, swatchSize);
        } else {
            square(startX, startY, swatchSize);
        }

        if( !isDrawing &&
            mouseX > startX && mouseX < startX + swatchSize &&
            mouseY > startY && mouseY < startY + swatchSize) {

            fill('rgba(255,255,255,0.4)');
            square(startX, startY, swatchSize);

            if(mouseIsPressed)
                currentCol = i;
        }
    }
    stroke('gray');
    strokeWeight(3);
    noFill();
    square(drawStartX + currentCol * swatchSize + currentCol * paddingSize, startY, swatchSize);
    strokeWeight(1);
}

// Returns a vector with mouse position in drawing area
function getMouseCanvasVec(){
    let mX = pxSize*~~(mouseX/pxSize);
    let mY = pxSize*~~(mouseY/pxSize);

    if (mX < drawStartX || mX > drawStartX + (63 * pxSize) ||
        mY < drawStartY || mY > drawStartY + (63 * pxSize)){
        return null;
    } else {
        return createVector(mX,mY);
    }
}

// Function that handles mouse clicks and movements
function handleMouse(){
    let mouseCell = getMouseCanvasVec();

    if(mouseCell) {
        let currentR = parseInt(palette[currentCol].substring(1,3),16);
        let currentG = parseInt(palette[currentCol].substring(3,5),16);
        let currentB = parseInt(palette[currentCol].substring(5,7),16);

        // Preview of what the cell is gonna look like
        fill("rgba("+ currentR + "," + currentG + "," + currentB + ", 0.7)");
        square(mouseCell.x, mouseCell.y, pxSize);

        if(mouseIsPressed){
            isDrawing = true;
            let x = ~~((mouseCell.x-drawStartX)/pxSize);
            let y = ~~((mouseCell.y-drawStartY)/pxSize);
            // console.log(x + ", " + y);
            if (mouseButton == LEFT) {
                drawing[x][y] = palette[currentCol];
            } else {
                document.oncontextmenu = function() { return false; }   // Prevents right-click menu
                drawing[x][y] = "#000000";
            }
        } else {
            isDrawing = false;
            document.oncontextmenu = function() { return true; }    // Re-enable right-click menu
        }
    }
}

// Handle shortcuts by keyboard key presses
// Keycodes found using: http://keycode.info/
function keyReleased(){
    if(keyCode == 82){ // 'R'
        clearCanvas();
    } else if(keyCode == RIGHT_ARROW){
        nextColor();
    } else if(keyCode == LEFT_ARROW) {
        prevColor();
    }
}

function mouseReleased(){
    isDrawing = false;
}

function nextColor(){
    currentCol = (currentCol + 1) % (palette.length);
}

function prevColor(){
    currentCol = (currentCol - 1) % (palette.length);
    if(currentCol < 0)
        currentCol = palette.length-1;
}

// Function to get the drawing String and assign it to the value of the hidden input element
// with the id "drawingStr". This value can then be used for form submission
function setDrawingString(){
    let drawStr = "";
    for(let i = 0; i < drawing.length; i++){
        for(let j = 0; j < drawing.length; j++){
            drawStr += palette.indexOf(drawing[j][i]).toString(16);
        }
        // drawStr += "\n";
    }
    drawStrElem.value = drawStr;
}
