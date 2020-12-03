let palette;
let drawing; // 2d array of strings of hex colors
let currentColIndex = 0, lastColIndex;
let isDrawing = false;
let drawStrElem;
let swatchSize = 50;
let drawStartX = 5,
    drawStartY = 5;
let lastCanW = 0,
    lastCanH = 0;
let pxSize = 10;
let parentDiv;
let drwRegionW;
let smallerUI = false;

// P5js' setup() function will occur once before the first draw() call occurs
function setup() {
    parentDiv = select("#p5CanvasDiv");
    cnv = createCanvas(lastCanW, lastCanH);
    calcUI(parentDiv);

    cnv.style("touch-action: none;"); // prevent scrolling in mobile

    // Set the div #p5CanvasDiv as the parent to the canvas created by p5js
    cnv.parent(parentDiv);

    textSize(16);
    textFont('consolas, courier new');

    // Get palette hex values in an array
    palette = document.getElementById("plt").value;
    palette = palette.substring(1, palette.length - 1).split(", ");

    for (let i = 0; i < palette.length; i++) {
        palette[i] = Number("0x" + palette[i].substring(1))
    }
    // palette = Int32Array.from(palette);
    palette[11] = 0xFFFFFF;

    initializeDrawingArray();
    // noLoop()
}

function initializeDrawingArray() {
    drawStrElem = document.getElementById("drawingStr");
    let initialDrawing = drawStrElem.value;

    // Initialize the 64x64 array
    drawing = [];
    for (let i = 0; i < 64; i++) {
        drawing.push(new Int8Array(64));
        for (let j = 0; j < 64; j++) {
            drawing[i][j] = parseInt(initialDrawing.charAt(j * 64 + i), 16);
        }
    }
    lastColIndex = drawing[0][0];
}

// P5js' draw() function will iterate 60 times per second.
function draw() {
    calcUI(parentDiv);
    background(255);

    noStroke();
    fill(20,20,30);

    if (!smallerUI) {
        text('◀ ▶ L/R arrow keys to switch colors.', drawStartX, height - 50);
        text('🖱   L/R click to Draw/Erase.', drawStartX, height - 30);
        text('🔄  Refresh page to reset.', drawStartX, height - 10);
    } else {
        text('🔄  Refresh page to reset.', drawStartX, height - 10);
    }

    stroke(0);
    noFill();
    square(drawStartX, drawStartY, drwRegionW);

    // drawCheckerBoard(drawStartX, drawStartY, drwRegionW, drwRegionW);     // Draw the white & gray checkerboard pattern
    fill(150)
    noStroke();
    for (let i = 0; i < drawing.length; i++) {
        for (let j = 0; j < drawing[i].length; j++) {
            let colIndex = drawing[i][j];
            if(colIndex != lastColIndex){
                lastColIndex = colIndex;
                let col = palette[colIndex];
                fill((col & 0xFF0000) >> 16, (col & 0xFF00) >> 8, col & 0xFF);
            }

            if (colIndex != 11) { // Transparent pixels

                square(drawStartX + (i * pxSize), drawStartY + (j * pxSize), pxSize);
            }
        }
    }
    drawPalette();
    handleMouse();

    // text(~~frameRate(), 20, 20); // DEBUG PERFORMANCE
}

// Calculate some of the UI related stuff - canvas size, pixel size,
// drawing region, swatchSize, and determine if it's the smaller UI form.
function calcUI(parentDiv) {
    if (parentDiv.size().width != lastCanW || parentDiv.size().height != lastCanH) {
        lastCanW = parentDiv.size().width;
        lastCanH = parentDiv.size().height;
        if (lastCanW < 365) {
            swatchSize = 40;
            let newCanvW = 64 * ~~((lastCanW - 2 * drawStartX) / 64);
            newCanvW += 2 * drawStartX;
            resizeCanvas(newCanvW, newCanvW + 16 + swatchSize + 4 + swatchSize + 4 + (25) + drawStartX);
            smallerUI = true;
        } else if (lastCanW < 650) {
            let newCanvW = 64 * ~~((lastCanW - 2 * drawStartX) / 64);
            newCanvW += 2 * drawStartX;
            resizeCanvas(newCanvW, newCanvW + 16 + swatchSize + 4 + swatchSize + 4 + (25) + drawStartX);
            smallerUI = true;
        } else {
            resizeCanvas(650, 800);
            smallerUI = false;
        }
        drwRegionW = cnv.size().width - 2 * drawStartX; // subtracting the small boundaries
        // drawing region is drwRegionW tall and wide

        pxSize = ~~(drwRegionW / 64); // "~~" will truncate decimals and turn float to an integer
        drwRegionW = 64 * pxSize; // Avoid leftover width

    }
}

// Clear the canvas by filling all pixels with transparent
function clearCanvas() {
    for (let i = 0; i < 64; i++) {
        for (let j = 0; j < 64; j++) {
            drawing[i][j] = 11;
        }
    }
}

// Draw the white and gray checkerboard for the transparent effect in background
function drawCheckerBoard(sX, sY, w, h) {
    noStroke();
    let fillC = 202;
    let tiny = ~~(pxSize / 2);
    for (let i = sY; i < sY + h; i += tiny) {
        let innerC = fillC;
        for (let j = sX; j < sX + w; j += tiny) {
            fill(innerC);
            square(j, i, tiny);
            if (innerC == 202) {
                innerC = 255;
            } else {
                innerC = 202;
            }
        }
        if (fillC == 202) {
            fillC = 255;
        } else {
            fillC = 202;
        }
    }
}

// Function that draws the palette below the drawing area and handles the clicks
function drawPalette() {
    let paddingSize = 4;
    let startY = drawStartY + drwRegionW + 16;
    for (let i = 0; i < 12; i++) {
        noStroke();
        let col = palette[i];
        fill((col & 0xFF0000) >> 16, (col & 0xFF00) >> 8, (col & 0xFF));

        let startX = drawStartX + i * swatchSize + i * paddingSize;
        if (smallerUI && i > 5) {
            startY = drawStartY + drwRegionW + 16 + swatchSize + paddingSize;
            startX = drawStartX + (i - 6) * swatchSize + (i - 6) * paddingSize;
        }

        if (i == 11) {
            drawCheckerBoard(startX, startY, swatchSize, swatchSize);
        } else {
            square(startX, startY, swatchSize);
        }

        if (!isDrawing &&
            mouseX > startX && mouseX < startX + swatchSize &&
            mouseY > startY && mouseY < startY + swatchSize) {

            fill(255, 255, 255, 0.4); // 'rgba(255,255,255,0.4)'
            square(startX, startY, swatchSize);

            if (mouseIsPressed){
                currentColIndex = i;
            }
        }
    }
    stroke(120);
    strokeWeight(3);
    noFill();
    startY = drawStartY + drwRegionW + 16;
    if (!smallerUI || currentColIndex < 6) {
        square(drawStartX + currentColIndex * swatchSize + currentColIndex * paddingSize, startY, swatchSize);
    } else {
        if (currentColIndex > 5) {
            square(drawStartX + (currentColIndex - 6) * swatchSize + (currentColIndex - 6) * paddingSize,
                startY + paddingSize + swatchSize, swatchSize);
        }
    }

    strokeWeight(1);
}

// Returns a vector with mouse position in drawing area
function getMouseCanvasVec(pmouse = false) {
    let origMX = mouseX,
        origMY = mouseY;
    if (pmouse) {
        origMX = pmouseX;
        origMY = pmouseY;
    }
    let mX = drawStartX + pxSize * ~~(origMX / pxSize);
    let mY = drawStartY + pxSize * ~~(origMY / pxSize);

    if (mX < drawStartX || mX > drawStartX + (63 * pxSize) ||
        mY < drawStartY || mY > drawStartY + (63 * pxSize)) {
        return null;
    } else {
        return createVector(mX, mY);
    }
}

// Function that handles mouse clicks and movements
function handleMouse() {
    let pMouseCell = getMouseCanvasVec(true);
    let mouseCell = getMouseCanvasVec();

    if (mouseCell) {
        let col = palette[currentColIndex];

        // preview of what the cell is gonna look like
        fill((col & 0xFF0000) >> 16, (col & 0xFF00) >> 8, (col & 0xFF), 160);
        if (col == 0)
            fill(255, 200);

        square(mouseCell.x, mouseCell.y, pxSize);

        if (mouseIsPressed || touches.length == 1) {
            isDrawing = true;
            let x = ~~((mouseCell.x - drawStartX) / pxSize);
            let y = ~~((mouseCell.y - drawStartY) / pxSize);

            if (mouseButton == LEFT || touches.length > 0) {
                if (pMouseCell) {
                    drawLine(currentColIndex, pMouseCell.x, pMouseCell.y, mouseCell.x, mouseCell.y);
                } else {
                    drawing[x][y] = palette[currentColIndex];
                }
            } else {
                document.oncontextmenu = function() {
                    return false; // Prevents right-click menu
                }

                if (pMouseCell) {
                    drawLine(11, pMouseCell.x, pMouseCell.y, mouseCell.x, mouseCell.y);
                } else {
                    drawing[x][y] = 11;
                }

            }
        } else {
            isDrawing = false;
            document.oncontextmenu = function() {
                return true;  // Re-enable right-click menu
            }
        }
    }
}

function drawLine(col, x1, y1, x2, y2, w = pxSize) {
    let nDivs, xSep, ySep;
    let absac = abs(x1 - x2),
        absbd = abs(y1 - y2);
    if (absac > absbd) {
        nDivs = absac / w;
        xSep = w;
        ySep = (absbd / nDivs);
    } else {
        nDivs = absbd / w;
        xSep = (absac / nDivs);
        ySep = w;
    }

    if (x1 > x2)
        xSep *= -1
    if (y1 > y2)
        ySep *= -1

    noStroke();

    if (nDivs > 0) {
        for (let i = 0; i <= nDivs; i++) {
            let nx = x1 + (w / 2) + i * xSep,
                ny = y1 + (w / 2) + i * ySep;

            let foundCell = findCell(nx, ny, w);
            if (foundCell[0] < 64 && foundCell[1] < 64)
                drawing[foundCell[0]][foundCell[1]] = col;
            else
                drawing[min(63, foundCell[0])][min(63, foundCell[1])] = col;
        }
    } else {
        drawing[findCell(x1, y1, w)[0]][findCell(x1, y1, w)[1]] = col;
    }
}

function findCell(px, py, w) {
    return [~~((px - drawStartX) / w), ~~((py - drawStartY) / w)];
}

// Handle shortcuts by keyboard key presses
function keyReleased() {
    if (keyCode == RIGHT_ARROW) {
        nextColor();
    } else if (keyCode == LEFT_ARROW) {
        prevColor();
    }
}

function mouseReleased() {
    isDrawing = false;
    setDrawingString(); // set the drawingStr element value
}

function nextColor() {
    currentColIndex = (currentColIndex + 1) % (palette.length);
}

function prevColor() {
    currentColIndex = (currentColIndex - 1) % (palette.length);
    if (currentColIndex < 0)
        currentColIndex = palette.length - 1;
}

// Function to get the drawing String and assign it to the value of the hidden input element
// with the id "drawingStr". This value can then be used for form submission
function setDrawingString() {
    let drawStr = "";
    for (let i = 0; i < drawing.length; i++) {
        for (let j = 0; j < drawing.length; j++) {
            drawStr += drawing[j][i].toString(16);
        }
    }
    drawStrElem.value = drawStr;
}