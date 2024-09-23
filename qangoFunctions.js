let boardUI = document.getElementById("board");
let communicationHeader = document.getElementById("communication");
let lib;
let board;
let Coordinate;
let player1;
let player2;
let currentPlayer = 1;
let winner = "";
let draw = false;

async function firstLoad() {
    await setup();
}

async function setup() {
    await cheerpjInit();
    lib = await cheerpjRunLibrary("/app/QangoJ8/Qango.jar");
    const Qango6Board = await lib.qango.Qango6Board;
    board = await new Qango6Board();

    const players = await lib.qango.Player;
    player1 = await players.PLAYER1;
    player2 = await players.PLAYER2;

    Coordinate = await lib.qango.Coordinate;

    await drawBoard();
    communicationHeader.innerText = `Welcome to Qango! player${currentPlayer} starts`;
}

async function restart() {
    await board.emptyBoard();
    await drawBoard();
    winner = "";
    draw = false;
    updateCommunication();
}

async function occupySquare(row, column) {
    const c1 = await new Coordinate(
        new Number(row).valueOf(),
        new Number(column).valueOf()
    );

    await board.placePlayer(currentPlayer === 1 ? player1 : player2, c1);
    if (await board.playerWon(currentPlayer === 1 ? player1 : player2, c1)) {
        winner = currentPlayer === 1 ? "player1" : "player2";
    } else {
        const freeLocations = await board.freeLocations();
        if (await freeLocations.isEmpty()) {
            draw = true;
        }
    }
}

async function onClick(event) {
    const id = event.target.id;
    const row = new Number(id[1]);
    const column = new Number(id[3]);
    await occupySquare(row - 1, column - 1);

    const rgbValue = currentPlayer === 1 ? 255 : 0;
    const r = rgbValue;
    const g = rgbValue;
    const b = rgbValue;
    const currentText = document.createElement("div");
    currentText.style.borderRadius = "50%";
    currentText.style.backgroundColor = `rgb(${r}, ${g}, ${b})`;
    currentText.style.height = "50%";
    currentText.style.width = "50%";
    currentText.style.margin = "25%";

    currentPlayer = currentPlayer === 1 ? 2 : 1;
    const currentColor = currentPlayer === 1 ? `white` : `black`;
    document.documentElement.style.setProperty(
        `--squarePreviewColor`,
        `${currentColor}`
    );
    event.target.appendChild(currentText);
    event.target.removeEventListener("click", onClick);
    event.target.className = "";

    updateCommunication();
}

function updateCommunication() {
    if (winner.length > 0) {
        for (let i = 0; i < boardUI.children.length; i++) {
            boardUI.children[i].removeEventListener("click", onClick);
        }
        communicationHeader.innerText = `The winner is: ${winner}`;
    } else if (draw === true) {
        communicationHeader.innerText = "It's a Draw";
    } else {
        communicationHeader.innerText = `It's player${currentPlayer}'s turn`;
    }
}

async function drawBoard() {
    boardUI.replaceChildren();
    const current = await board.toString();
    let row = 1;
    let column = 1;

    current.split("\n").forEach((element, index) => {
        if (index > 0) {
            const newElement = element.substring(2);
            newElement.split("\x1B[0m").forEach((square) => {
                const newSquare = new String(square);
                if (newSquare.length > 1) {
                    const currentSquare = document.createElement("div");
                    currentSquare.className = "boardSquare";
                    const colors = square.split(";");
                    const r = colors[2];
                    const g = colors[3];
                    const b = colors[4].split("m")[0];
                    currentSquare.style.backgroundColor = `rgb(${r}, ${g}, ${b})`;

                    currentSquare.addEventListener("click", onClick);

                    currentSquare.style.gridRow = row;
                    currentSquare.style.gridColumn = column;
                    currentSquare.id = `r${row}c${column}`;

                    boardUI.appendChild(currentSquare);

                    column++;
                }
            });
            row++;
            column = 1;
        }
    });
}
