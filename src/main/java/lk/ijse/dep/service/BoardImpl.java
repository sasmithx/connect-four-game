package lk.ijse.dep.service;

import lk.ijse.dep.controller.BoardController;

import java.util.ArrayList;
import java.util.List;

public class BoardImpl implements Board {
    private Piece[][] pieces;
    private BoardUI boardUI;

    public Piece piece ;
    public int cols;
    public BoardImpl(BoardUI boardUI) {
        this.boardUI = boardUI;
        pieces = new Piece[6][5];
        for(int i =0;i < NUM_OF_COLS;i++ ){
            for(int j=0; j<NUM_OF_ROWS; j++ ){
                pieces[i][j] = Piece.EMPTY;
            }
        }
    }
    @Override
    public BoardUI getBoardUI() {

        return boardUI;
    }
    @Override
    public int findNextAvailableSpot(int col) {
        for(int row=0;row<NUM_OF_ROWS;row++){
            if(pieces[col][row] == Piece.EMPTY){
                return row;
            }
        }
        return -1;
    }
    @Override
    public boolean isLegalMove(int col) {
        int row = findNextAvailableSpot(col);
        if(row >-1){
            return true;
        }
        return false;
    }
    @Override
    public boolean existLegalMoves() {
        for(int col=0;col<NUM_OF_COLS;col++){
            for(int row =0;row<NUM_OF_ROWS;row++){
                if(pieces[col][row] == Piece.EMPTY){
                    return true;
                }
            }

        }
        return false;
    }
    @Override
    public void updateMove(int col, Piece move) {
        this.cols = col;
        this.piece= move;

        for(int x=0;x<NUM_OF_ROWS;x++){
            if(pieces[col][x] == Piece.EMPTY){
                pieces[col][x] = move;
                break;
            }
        }
    }
    @Override
    public Winner findWinner() {
        for(int col=0;col<NUM_OF_COLS;col++){
            for(int row=0;row<NUM_OF_ROWS;row++){
                Piece piece = pieces[col][row];
                if(piece != Piece.EMPTY){
                    if(col + 3< NUM_OF_COLS &&
                        pieces[col+1][row] == piece &&
                        pieces[col+2][row] == piece &&
                        pieces[col+3][row] == piece ){
                        return new Winner(piece,col,row,col+3,row);
                    }
                    if(row + 3< NUM_OF_ROWS &&
                            pieces[col][row+1] == piece &&
                            pieces[col][row+2] == piece &&
                            pieces[col][row+3] == piece ){
                        return new Winner(piece,col,row,col,row+3);
                    }
                }
            }
        }
        return new Winner(Piece.EMPTY,-1,-1,-1,-1);
    }
    /////////// -> MCTS Algorithm <- /////////////

    public BoardImpl(Piece[][] pieces, BoardUI boardUI){
        this.pieces = new Piece[NUM_OF_COLS][NUM_OF_ROWS];

        //copies existing 2D array to newly created array here
        for (int i = 0;i < NUM_OF_COLS;i++){
            for (int j = 0;j < NUM_OF_ROWS;j++){
                this.pieces[i][j] = pieces[i][j];
            }
        }
        this.boardUI = boardUI;
    }
    //return the boardimpl object
    @Override
    public BoardImpl getBoardImpl() {
        return this;
    }

    //checks the all next legal moves while expanding the tree (creating child nodes)
    public List<BoardImpl> getAllLegalNextMoves() {
        Piece nextPiece = piece == Piece.BLUE? Piece.GREEN : Piece.BLUE;

        List<BoardImpl> nextMoves = new ArrayList<>();
        for (int col = 0; col < NUM_OF_COLS; col++) {
            if (findNextAvailableSpot(col) > -1) {
                BoardImpl legalMove = new BoardImpl(this.pieces,this.boardUI);
                legalMove.updateMove(col, nextPiece);
                nextMoves.add(legalMove);
            }
        }
        return nextMoves;
    }
    //randomly select child node just after expanding the parent node
    public BoardImpl getRandomLegalNextMove(){
        final  List<BoardImpl> legalMoves = getAllLegalNextMoves();
        if (legalMoves.isEmpty()) {
            return null;
        }
        final int random;
        random = RANDOM_GENERATOR.nextInt(legalMoves.size());
        return legalMoves.get(random);
    }
    //decide whether there's any empty piece or not
    public boolean getStatus(){
        if (!existLegalMoves()) {
            return false;
        }
        Winner winner = findWinner();
        if (winner.getWinningPiece() != Piece.EMPTY) {
            return false;
        }
        return true;
    }
}

