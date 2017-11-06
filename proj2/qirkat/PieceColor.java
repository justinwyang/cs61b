package qirkat;

import static qirkat.GameException.error;

/** Describes the classes of Piece on a Qirkat board.
 *  @author P. N. Hilfinger
 */
enum PieceColor {

    /** EMPTY: no piece.
     *  WHITE, BLACK: piece colors. */
    EMPTY,
    WHITE {
        @Override
        PieceColor opposite() {
            return BLACK;
        }

        @Override
        boolean isPiece() {
            return true;
        }
    },
    BLACK {
        @Override
        PieceColor opposite() {
            return WHITE;
        }

        @Override
        boolean isPiece() {
            return true;
        }
    };

    /** Return the piece color of my opponent, if defined. */
    PieceColor opposite() {
        throw new UnsupportedOperationException();
    }

    /** Return true iff I denote a piece rather than an empty square. */
    boolean isPiece() {
        return false;
    }

    /** Parses a string into a PieceValue.
     *
     * @param s the string to be parsed
     * @return the PieceValue outcome
     */
    static PieceColor parse(String s) {
        if (s.toLowerCase().equals("empty")) {
            return EMPTY;
        }
        if (s.toLowerCase().equals("white")) {
            return WHITE;
        }
        if (s.toLowerCase().equals("black")) {
            return BLACK;
        }
        throw error("Piece Color %s not valid", s);
    }

    /** Return the standard one-character denotation of this piece ('b', 'w',
     *  or '-'). */
    String shortName() {
        return this == BLACK ? "b" : this == WHITE ? "w" : "-";
    }

    @Override
    public String toString() {
        return capitalize(super.toString().toLowerCase());
    }

    /** Return WORD with first letter capitalized. */
    static String capitalize(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

}
