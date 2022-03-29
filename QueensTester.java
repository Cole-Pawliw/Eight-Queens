public class QueensTester {
    public static void main(String[] args) throws CloneNotSupportedException {
        EightQueens chessBoard = new EightQueens();

        chessBoard.setQueen(1, 1);
        chessBoard.setQueen(3, 2);
        chessBoard.setQueen(5, 3);
        chessBoard.emptySquare(3, 2);
        chessBoard.emptySquare(0, 0);

        EightQueens copy = (EightQueens)chessBoard.clone();

        boolean works = copy.setQueens(6);
        char[][] state1 = chessBoard.getBoard();
        char[][] state2 = copy.getBoard();

        System.out.println(works);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(state1[i][j]);
            }
            System.out.println();
        }
        System.out.println();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(state2[i][j]);
            }
            System.out.println();
        }
    }
}
