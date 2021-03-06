package chessengine;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Scott
 */

public class BoardGUI extends Application {
    private final int DIMENSION = 8;
    private final int SQUARE_WIDTH = 100;
    private final int SQUARE_HEIGHT = 100;
    private final int PIECE_WIDTH = 70;
    private final int PIECE_HEIGHT = 70;
    private final double STROKE_WIDTH = 10;
    
    //final Paint MOVE_HIGHLIGHT = Color.DARKCYAN;
    private final Paint MOVE_HIGHLIGHT_COL = Color.DARKTURQUOISE;
    private final Paint DATT_HIGHLIGHT_COL = Color.RED;
    private final Paint INATT_HIGHLIGHT_COL = Color.YELLOW;
    private final Paint DEF_HIGHLIGHT_COL = Color.GREENYELLOW;
    
    private final Paint[] SQ_COLORSET_1 = {Color.BEIGE, Color.DARKGREEN};
    private final Paint[] SQ_COLORSET_2 = {Color.WHITE, Color.DARKGREEN};   
    
    private final Font COORDINATE_SIZE = new Font(20);
    private final String DIVIDOR = "------------------------------------------------------------------------";
    
    private String[] fileLex = {"A", "B", "C", "D", "E", "F", "G", "H"};
    private String[] rankLex = {"1", "2", "3", "4", "5", "6", "7", "8"};
    
    private ArrayList<Square> squares;
    private ArrayList<Piece> whitePieces;
    private ArrayList<Piece> blackPieces;
    private ArrayList<PieceImageView> whitePieceImages = new ArrayList<>();
    private ArrayList<PieceImageView> blackPieceImages = new ArrayList<>();;
    
    private Square[][] squaresFromBoard;
    private Square target;
    
    private HashMap<Piece, Square> piecesToSquaresMap;
    private HashMap<Square, Piece> squaresToPiecesMap;
    
    private Board board;
    private Group root;
    private Scene scene;
    
    @Override
    public void init(){
        board = new Board();
        
        piecesToSquaresMap = board.getPiecesToSquaresMap();
        squaresToPiecesMap = board.getSquaresToPiecesMap();
        
        whitePieces = board.getWhitePieces();
        blackPieces = board.getBlackPieces();
        
        // place images for white an black pieces on board
        placePieceImages(whitePieces, whitePieceImages, piecesToSquaresMap);
        placePieceImages(blackPieces, blackPieceImages, piecesToSquaresMap);
        
        // give each imageView object event handlers
        // calculate initial movement squares for each piece
        int count = 0;
        while (count < whitePieceImages.size()){
            givePieceEvents(whitePieceImages.get(count), board);
            givePieceEvents(blackPieceImages.get(count), board);
            
            board.calcMovementSquares(whitePieces.get(count));
            board.calcMovementSquares(blackPieces.get(count));
            count++;
        }
        
        squaresFromBoard = board.getSquares();
        squares = new ArrayList<>();
        convertFrom2DArrayToArrayList(squares, squaresFromBoard);
        
        root = new Group();
        scene = new Scene(root, DIMENSION * SQUARE_WIDTH, 
                                    DIMENSION * SQUARE_HEIGHT);
        
        giveSquaresColor(squares, SQ_COLORSET_1);
        root.getChildren().addAll(squares);
        addRankAndFileTexts(root);
        
        for (Square square : squares) {
            giveSquareEventHandling(square);
        }
        
        root.getChildren().addAll(whitePieceImages);
        root.getChildren().addAll(blackPieceImages);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        Stage pickSideStage = new Stage();
        Group g = new Group();
        Scene pickSideScene = new Scene(g, 300, 300);
        Button pickWhite = new Button("Pick White!");
        Button pickBlack = new Button("Pick Black!");
        
        g.getChildren().add(pickWhite);
        g.getChildren().add(pickBlack);
        
//        pickSideStage.setScene(pickSideScene);
//        pickSideStage.show();
        
        
        // From here play out the game.
        System.out.println("after show");
    }
    
    private void placePieceImages(ArrayList<Piece> pieces,
            ArrayList<PieceImageView> pieceImages,
            HashMap<Piece, Square> map){
        
        PieceImageView pv;        
        for (Piece p : pieces) {
            target = map.get(p);
            pv = new PieceImageView(p, PIECE_HEIGHT, PIECE_WIDTH, 
                target.getColumn() * SQUARE_WIDTH + SQUARE_WIDTH / 5, 
                target.getRow() * SQUARE_HEIGHT + SQUARE_WIDTH / 10);
            
//            System.out.println(p.toString() + "\t| row: " +
//                    target.getRow() + "\t| column: " +
//                    target.getColumn());
            pieceImages.add(pv); 
        }
    }
    
    private void addRankAndFileTexts(Group root){
        Text text;
        for (int i = 0; i < DIMENSION; ++i){
            for (int j = DIMENSION-1; j >= 0; --j){
                text = new Text (
                        ((i * SQUARE_WIDTH)),
                        ((Math.abs(j-(DIMENSION-1)) * SQUARE_HEIGHT) + SQUARE_HEIGHT),
                        (fileLex[i] + rankLex[j]));
                
                text.setFont(COORDINATE_SIZE);
                root.getChildren().add(text);
            }
        }
    }
    
    private void giveSquaresColor(ArrayList<Square> squares, Paint[] colorSet){
        for (int i = 0; i < DIMENSION; ++i){
            for (int j = 0; j < DIMENSION; ++j){
                
                // Acts as a normal '2D' array access
                target = squares.get((i * DIMENSION) + j);
                
                // Sets color
                if (i % 2 == 0){
                    if (j % 2 == 0){
                        target.setFill(colorSet[0]);
                    }
                    else{
                        target.setFill(colorSet[1]);
                    }
                }
                else{
                    if (j % 2 != 0){
                        target.setFill(colorSet[0]);
                    }
                    else{
                        target.setFill(colorSet[1]);
                    }
                }
            } // end j for
        } // end i for
    }
    
    private void giveSquareEventHandling (Square square){        
        square.setOnMouseClicked(new EventHandler <MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
//                System.out.println("clicked square\t| row: " + 
//                        rankLex[square.getRow()] + "\t| column: " +
//                        fileLex[square.getColumn()] + "\t| occupied by: " +
//                        squaresToPiecesMap.get(square));
                
                System.out.println("clicked square\t| "+ 
                        fileLex[square.getColumn()] + 
                        rankLex[Math.abs(DIMENSION - square.getRow() - 1)] + "\t| occupied by: " +
                        squaresToPiecesMap.get(square));
                
                System.out.println(DIVIDOR);
            }
        });
    }
    
    // Converts the 2D array of squares into an array list.
    private void convertFrom2DArrayToArrayList(ArrayList<Square> squares,
                                                Square[][] s){
        for (int i = 0; i < DIMENSION; i++){
            for (int j = 0; j < DIMENSION; j++){
                squares.add(s[i][j]);
            }
        }
    }
    
    private void givePieceEvents(PieceImageView pv, Board b){
        final Delta dragDelta = new Delta();
        final Delta start = new Delta();
        
        pv.setOnMousePressed(new EventHandler<MouseEvent>() {         
            @Override
            public void handle(MouseEvent mouseEvent) {
                start.x = pv.getLayoutX();
                start.y = pv.getLayoutY();
                System.out.println("start x: " + start.x + "\t| start y: " + start.y);
                
                Piece piece = pv.getPiece();
                Square square = piecesToSquaresMap.get(piece);
                
                System.out.println(piece.toString() + "\t| " + 
                        fileLex[square.getColumn()] +
                        rankLex[Math.abs(DIMENSION - square.getRow() - 1)]);             
            
                highlightMoveSquares(piece.getPossibleMoves());
                highlightDirectAttackSquares(piece.getDirectAttackSquares());
                highlightIndirectAttackSquares(piece.getIndirectAttackSquares());
                highlightDefendedSquares(piece.getDefendedSquares());
                
                // record a delta distance for the drag and drop operation.
                dragDelta.x = pv.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = pv.getLayoutY() - mouseEvent.getSceneY();
                pv.setCursor(Cursor.MOVE);
            }
        });
        
        pv.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent mouseEvent) {
                pv.setCursor(Cursor.HAND);

                double xDrop = pv.getLayoutX() + (SQUARE_WIDTH / 2);
                double yDrop = pv.getLayoutY() + (SQUARE_HEIGHT / 2);

                // Squares a piece is moving from and to
                Square sourceSquare;
                Square targetSquare;
                int count = 0;

                // finds the index of the square that the piece
                // was dropped into
                while (count < squares.size() && 
                        !squares.get(count).contains(xDrop, yDrop)){
                    count++;
                }
                
                Piece piece = pv.getPiece();

                // Checks if the piece is brought off the board.
                // If the piece is, put it back to its starting posision.
                if (count >= squares.size()){
                    pv.setLayoutX(start.x);
                    pv.setLayoutY(start.y);
                }
                else{
                
                    targetSquare = squares.get(count);

                    // square in map maps to no piece
                    if (squaresToPiecesMap.get(targetSquare) == null){  
                        
                        System.out.println("target square\t| " +
                                fileLex[targetSquare.getColumn()] +
                                rankLex[Math.abs(DIMENSION - targetSquare.getRow() - 1)]);
                        
                        // if the target square is within the possible moves
                        // of the piece, move it
                        if (piece.getPossibleMoves().contains(targetSquare)){
                            
                            // piece moved is a pawn, make it ineligible to move
                            // forward twice again
                            if (piece.getPieceType().equals(PieceTypeEnum.PAWN)){
                                piece.setHasMoved(true);
                            }
                            
                            // make the square that the piece was occupying empty
                            sourceSquare = piecesToSquaresMap.get(piece);
                            squaresToPiecesMap.replace(sourceSquare, null);

                            System.out.println("source square\t| " +
                                fileLex[sourceSquare.getColumn()] +
                                rankLex[Math.abs(DIMENSION - sourceSquare.getRow() - 1)]);

                            // move image of piece to target sqaure
                            pv.setLayoutX(targetSquare.getX() + (SQUARE_WIDTH / 5));
                            pv.setLayoutY(targetSquare.getY() + (SQUARE_HEIGHT / 10));

                            // make the sqaure that the piece moves to occupied
                            squaresToPiecesMap.replace(targetSquare, piece);

                            // set piece's current square
                            piecesToSquaresMap.replace(piece, targetSquare);
                            
                            removeMoveHighlights(piece.getPossibleMoves());
                            removeIndirectAttackHighlights(piece.getIndirectAttackSquares());
                            removeDirectAttackHighlights(piece.getDirectAttackSquares());
                            removeDefendedHighlights(piece.getDefendedSquares());
                            
                            // calculate possible next moves for pieces
                            for (Piece whitePiece : whitePieces){
                                b.calcMovementSquares(whitePiece);
                            }

                            for (Piece blackPiece : blackPieces){
                                b.calcMovementSquares(blackPiece);
                            }
                        
                        } 
                        else{
                            System.out.println("target square not in possible moves");
                            pv.setLayoutX(start.x);
                            pv.setLayoutY(start.y);
                        }
                    }
                    else{
                        
                        // move the image of the piece back to the 
                        // starting position
                        pv.setLayoutX(start.x);
                        pv.setLayoutY(start.y);
                    }
                }
                
                removeMoveHighlights(piece.getPossibleMoves());
                removeIndirectAttackHighlights(piece.getIndirectAttackSquares());
                removeDirectAttackHighlights(piece.getDirectAttackSquares());
                removeDefendedHighlights(piece.getDefendedSquares());
                
                System.out.println(DIVIDOR);
            }
        });
        
        pv.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override 
            public void handle(MouseEvent mouseEvent) {
                pv.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                pv.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
            }
        });
    }
    
    private void removeMoveHighlights(ArrayList<Square> moves){
        for (Square s : moves){
            s.setStroke(null);
        }
    }
    
    private void removeIndirectAttackHighlights(ArrayList<Square> indirectAttacks){
        for (Square s : indirectAttacks){
            s.setStroke(null);
        }
    }
    
    private void removeDirectAttackHighlights(ArrayList<Square> directAttacks){
        for (Square s : directAttacks){
            s.setStroke(null);
        }
    }
    
    private void removeDefendedHighlights(ArrayList<Square> defended){
        for (Square s : defended){
            s.setStroke(null);
        }
    }
    
    private void highlightMoveSquares(ArrayList<Square> moves){
        for (Square s : moves){
            System.out.println("potential move\t| " +
                    fileLex[s.getColumn()] +
                    rankLex[Math.abs(DIMENSION - s.getRow() - 1)]);
                    
            s.setStrokeWidth(STROKE_WIDTH);
            s.setStrokeType(StrokeType.INSIDE);
            s.setStroke(MOVE_HIGHLIGHT_COL);        
        }
    }
    
    private void highlightIndirectAttackSquares(ArrayList<Square> indirectAttacks){
        for (Square s : indirectAttacks) {
            System.out.println("indirect attack\t| " +
                    fileLex[s.getColumn()] +
                    rankLex[Math.abs(DIMENSION - s.getRow() - 1)]);
            
            s.setStrokeWidth(STROKE_WIDTH);
            s.setStrokeType(StrokeType.INSIDE);
            s.setStroke(INATT_HIGHLIGHT_COL); 
        }
    }
    
    private void highlightDirectAttackSquares(ArrayList<Square> directAttacks){
        for (Square s : directAttacks){
            System.out.println("direct attack\t| " +
                    fileLex[s.getColumn()] +
                    rankLex[Math.abs(DIMENSION - s.getRow() - 1)]);
                    
            s.setStrokeWidth(STROKE_WIDTH);
            s.setStrokeType(StrokeType.INSIDE);
            s.setStroke(DATT_HIGHLIGHT_COL);        
        }
    }
    
    private void highlightDefendedSquares(ArrayList<Square> defended){
        for (Square s : defended){
            System.out.println("defended square\t| " +
                    fileLex[s.getColumn()] +
                    rankLex[Math.abs(DIMENSION - s.getRow() - 1)]);
                    
            s.setStrokeWidth(STROKE_WIDTH);
            s.setStrokeType(StrokeType.INSIDE);
            s.setStroke(DEF_HIGHLIGHT_COL);        
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        
        launch(args);
    }
    
// records relative x and y co-ordinates.
class Delta { double x, y; }
    
}
