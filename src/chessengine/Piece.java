/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessengine;

import java.util.ArrayList;

/**
 *
 * @author Scott
 */
public class Piece{
    private String color;
    private String[] pieceStringList = {"Pawn", "Knight", "Bishop", "Rook", "Queen", "King"};
    private PieceTypeEnum typeOfPiece;
    private ArrayList<Square> moveSquares = new ArrayList<>();
    private ArrayList<Square> directAttackSquares = new ArrayList<>();
    private ArrayList<Square> indirectAttackSquares = new ArrayList<>();
    private ArrayList<Square> defendedSquares = new ArrayList<>();
    private String pathForPNG;
    private Boolean hasMoved;
    private Boolean pinned;
    
    public Piece(String col, PieceTypeEnum type){
           color = col;
           typeOfPiece = type;
           hasMoved = false;
           pinned = false;
    }
    
    public Piece(String col, PieceTypeEnum type, String path){
           color = col;
           typeOfPiece = type;      
           pathForPNG = path;
           hasMoved = false;
           pinned = false;
    }
    
    public String getColor(){
        return color;
    }
    
    public PieceTypeEnum getPieceType(){
        return typeOfPiece;
    }
    
    public void setHasMoved(Boolean move){
        hasMoved = move;
    }
    
    public Boolean getHasMoved(){
        return hasMoved;
    }
    
    public String getPathForPNG(){
        return pathForPNG;
    }
    
    public ArrayList<Square> getPossibleMoves(){
        return moveSquares;
    }
    
    public void setPossibleMoves(ArrayList<Square> moves){
        moveSquares = moves;
    }
    
    public ArrayList<Square> getDirectAttackSquares(){
        return directAttackSquares;
    }
    
    public void setDirectAttackSquares(ArrayList<Square> directAttack){
        directAttackSquares = directAttack;
    }    
    
    public ArrayList<Square> getIndirectAttackSquares(){
        return indirectAttackSquares;
    }
    
    public void setIndirectAttackSquares(ArrayList<Square> indirectAttack){
        indirectAttackSquares = indirectAttack;
    }
    
    public ArrayList<Square> getDefendedSquares(){
        return defendedSquares;
    }
    
    public void setDefendedSquares(ArrayList<Square> defended){
        defendedSquares = defended;
    }
    
    public void setPinned(boolean p){
        pinned = p;
    }
    
    public boolean getPinned(){
        return pinned;
    }
    
    @Override
    public String toString(){
        return getColor() +  " " + 
                pieceStringList[typeOfPiece.ordinal()];
                
    }
}
