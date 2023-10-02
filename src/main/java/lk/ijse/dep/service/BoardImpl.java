package lk.ijse.dep.service;

import lk.ijse.dep.controller.BoardController;

public class BoardImpl implements Board {
    private Piece[][] pieces = new Piece[6][5];
    private BoardUI boardUI;
    public BoardImpl(BoardController boardController) {
        boardUI = boardController;
        for(int i =0;i < NUM_OF_COLS;i++ ){
            for(int j=0; j<NUM_OF_ROWS; j++ ){
                pieces[i][j] = Piece.EMPTY;
            }
        }
    }
    public BoardImpl(BoardUI boardUI) {
        this.boardUI = boardUI;
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
        for(int x=0;x<NUM_OF_ROWS;x++){
            if(pieces[col][x] == Piece.EMPTY){
                pieces[col][x] = move;
                break;
            }
        }
    }
    @Override
    public Winner findWinner() {  // check
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
}
