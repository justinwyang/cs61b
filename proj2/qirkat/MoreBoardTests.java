package qirkat;

import org.junit.Test;
import static org.junit.Assert.*;

public class MoreBoardTests {

    /** the string representation of this is
     * "  b b b b b\n  b b b b b\n  b b - w w\n  w w w w w\n  w w w w w"
     * feel free to modify this to however you want to represent your board. */
    private final char[][] boardRepr = new char[][]{
        {'b', 'b', 'b', 'b', 'b'},
        {'b', 'b', 'b', 'b', 'b'},
        {'b', 'b', '-', 'w', 'w'},
        {'w', 'w', 'w', 'w', 'w'},
        {'w', 'w', 'w', 'w', 'w'}
    };

    private final PieceColor currMove = PieceColor.WHITE;

    /**
     * @return the String representation of the initial state. This will
     * be a string in which we concatenate the values from the bottom of
     * board upwards, so we can pass it into setPieces. Read the comments
     * in Board#setPieces for more information.
     *
     * For our current boardRepr, the String returned by
     * getInitialRepresentation is
     * "  w w w w w\n  w w w w w\n  b b - w w\n  b b b b b\n  b b b b b"
     *
     * We use a StringBuilder to avoid recreating Strings (because Strings
     * are immutable).
     */
    private String getInitialRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int i = boardRepr.length - 1; i >= 0; i--) {
            for (int j = 0; j < boardRepr[0].length; j++) {
                sb.append(boardRepr[i][j] + " ");
            }
            sb.deleteCharAt(sb.length() - 1);
            if (i != 0) {
                sb.append("\n  ");
            }
        }
        return sb.toString();
    }

    private Board getBoard() {
        Board b = new Board();
        b.setPieces(getInitialRepresentation(), currMove);
        return b;
    }

    private void resetToInitialState(Board b) {
        b.setPieces(getInitialRepresentation(), currMove);
    }

    @Test
    public void testSomething() {
        Board b = getBoard();
        assertEquals(b, new Board());
        Board test = new Board();
        b.setPieces("w---b -w-b- --b-- -b-w- b---w", PieceColor.WHITE);

        b.makeMove(Move.parseMove("b2-c2"));
        b.makeMove(Move.parseMove("d2-b2"));
        b.makeMove(Move.parseMove("a1-a2"));
        b.makeMove(Move.parseMove("a5-b5"));
        test.setPieces("----b wb--- --b-- -b-w- -b--w", PieceColor.WHITE);
        assertEquals(b, test);

        b.makeMove(Move.parseMove("a2-c2-c4-a4"));
        test.setPieces("----b ----- ----- w--w- -b--w", PieceColor.BLACK);
        assertEquals(b, test);
    }

    @Test
    public void testGetJumps() {
        Board b0 = new Board();
        b0.setPieces("----- -wb-- ---b- ----- -----", PieceColor.WHITE);
        assertEquals(b0.getMoves().get(0), Move.parseMove("b2-d2-d4"));

        String fourJumps = "----- ----- --wb- -b-bb -----";
        b0.setPieces(fourJumps, PieceColor.WHITE);
        assertEquals(b0.getMoves().size(), 4);
    }
}
