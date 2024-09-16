let boardUI = document.getElementById("board");
let lib;
let board;
let Coordinate;
let player1;
let player2;

async function firstLoad() {
    await setup();
}

async function setup() {
    await cheerpjInit();
    lib = await cheerpjRunLibrary("/QangoJ8/app/Qango.jar");
    const Qango6Board = await lib.qango.Qango6Board;
    board = await new Qango6Board();

    const players = await lib.qango.Player;
    player1 = await players.PLAYER1;
    player2 = await players.PLAYER2;

    // Coordinate = await lib.qango.Coordinate;
    // const c1 = await new Coordinate(1, 1);
    // await board.placePlayer(player1, c1);

    await drawBoard();
}

async function occupySquare(row, column) {
    console.log("occupied");
    const c1 = await new Coordinate(row, column);
    await board.placePlayer(player1, c1);
    await drawBoard();
}

async function drawBoard() {
    boardUI.replaceChildren();
    const current = await board.toString();
    console.log(current);
    let row = 1;
    let column = 1;

    current.split("\n").forEach((element, index) => {
        if (index > 0) {
            const newElement = element.substring(2);
            // console.log(index + ": ", newElement);
            newElement.split("\x1B[0m").forEach((square) => {
                const newSquare = new String(square);
                if (newSquare.length > 1) {
                    const currentSquare = document.createElement("div");
                    currentSquare.onclick = () =>
                        occupySquare(row - 1, column - 1);
                    currentSquare.onmouseover = () => {
                        currentSquare.style.borderRadius = "50%";
                    };
                    console.log("colors: ", square.split(";"));
                    const colors = square.split(";");
                    const r = colors[2];
                    const g = colors[3];
                    const b = colors[4].split("m")[0];
                    currentSquare.style.backgroundColor = `rgb(${r}, ${g}, ${b})`;
                    if (colors.length > 5) {
                        console.log("check");
                        const r = colors[6];
                        const g = colors[7];
                        const b = colors[8].split("m")[0];
                        const currentText = document.createElement("div");
                        currentText.style.borderRadius = "50%";
                        currentText.style.backgroundColor = `rgb(${r}, ${g}, ${b})`;
                        currentText.style.height = "50%";
                        currentText.style.width = "50%";
                        currentText.style.margin = "25%";
                        currentSquare.appendChild(currentText);
                    }
                    currentSquare.style.gridRow = row;
                    currentSquare.style.gridColumn = column;

                    boardUI.appendChild(currentSquare);

                    column++;
                }
            });
            row++;
            column = 1;
        }
    });
}
