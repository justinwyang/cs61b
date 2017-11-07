package qirkat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Observable;
import java.util.Observer;

import static qirkat.PieceColor.*;
import static qirkat.Move.*;
import static qirkat.GameException.*;

/** A Qirkat board.   The squares are labeled by column (a char value between
 *  'a' and 'e') and row (a char value between '1' and '5'.
 *
 *  For some purposes, it is useful to refer to squares using a single
 *  integer, which we call its "linearized index".  This is simply the
 *  number of the square in row-major order (with row 0 being the bottom row)
 *  counting from 0).
 *
 *  Moves on this board are denoted by Moves.
 *  @author Justin Yang
 */
class Board extends Observable {

    /** A new, cleared board at the start of the game. */
    Board() {
        clear();
    }

    /** A copy of B. */
    Board(Board b) {
        internalCopy(b);
    }

    /** Return a constant view of me (allows any access method, but no
     *  method that modifies it). */
    Board constantView() {
        return this.new ConstantBoard();
    }

    /** Clear me to my starting state, with pieces in their initial
     *  positions. */
    void clear() {
        _whoseMove = WHITE;
        _gameOver = false;

        // FIXME
        _board = new PieceColor[Move.SIDE * Move.SIDE];
        _moves = new MoveList();
        setPieces(INIT_BOARD, WHITE);

        setChanged();
        notifyObservers();
    }

    /** Copy B into me. */
    void copy(Board b) {
        internalCopy(b);
    }

    /** Copy B into me. */
    private void internalCopy(Board b) {
        // FIXME
        this._gameOver = b._gameOver;
        this._whoseMove = b._whoseMove;
        _board = new PieceColor[Move.SIDE * Move.SIDE];
        for (char c = 'a'; c <= 'e'; c++) {
            for (char r = '1'; r <= '5'; r++) {
                set(c, r, b.get(c, r));
            }
        }
        this._moves = new MoveList();
        for (Move mov: b._moves) {
            this._moves.add(mov);
        }
    }

    /** Set my contents as defined by STR.  STR consists of 25 characters,
     *  each of which is b, w, or -, optionally interspersed with whitespace.
     *  These give the contents of the Board in row-major order, starting
     *  with the bottom row (row 1) and left column (column a). All squares
     *  are initialized to allow horizontal movement in either direction.
     *  NEXTMOVE indicates whose move it is.
     */
    void setPieces(String str, PieceColor nextMove) {
        if (nextMove == EMPTY || nextMove == null) {
            throw new IllegalArgumentException("bad player color");
        }
        str = str.replaceAll("\\s", "");
        if (!str.matches("[bw-]{25}")) {
            throw new IllegalArgumentException("bad board description");
        }

        // FIXME

        for (int k = 0; k < str.length(); k += 1) {
            switch (str.charAt(k)) {
            case '-':
                set(k, EMPTY);
                break;
            case 'b': case 'B':
                set(k, BLACK);
                break;
            case 'w': case 'W':
                set(k, WHITE);
                break;
            default:
                break;
            }
        }

        // FIXME
        this._whoseMove = nextMove;
        this._gameOver = !isMove();

        setChanged();
        notifyObservers();
    }

    /** Return true iff the game is over: i.e., if the current player has
     *  no moves. */
    boolean gameOver() {
        return _gameOver;
    }

    /** Return the current contents of square C R, where 'a' <= C <= 'e',
     *  and '1' <= R <= '5'.  */
    PieceColor get(char c, char r) {
        assert validSquare(c, r);
        return get(index(c, r));
    }

    /** Return the current contents of the square at linearized index K. */
    PieceColor get(int k) {
        assert validSquare(k);
        return _board[k];
    }

    /** Set get(C, R) to V, where 'a' <= C <= 'e', and
     *  '1' <= R <= '5'. */
    private void set(char c, char r, PieceColor v) {
        assert validSquare(c, r);
        set(index(c, r), v);
    }

    /** Set get(K) to V, where K is the linearized index of a square. */
    private void set(int k, PieceColor v) {
        assert validSquare(k);
        _board[k] = v;
    }

    /** Return true iff MOV is legal on the current board. */
    boolean legalMove(Move mov) {
        return mov.validSquare(mov.fromIndex())
                && mov.validSquare(mov.toIndex());
    }

    /** Return a list of all legal moves from the current position. */
    ArrayList<Move> getMoves() {
        ArrayList<Move> result = new ArrayList<>();
        getMoves(result);
        return result;
    }

    /** Add all legal moves from the current position to MOVES. */
    void getMoves(ArrayList<Move> moves) {
        if (gameOver()) {
            return;
        }
        if (jumpPossible()) {
            for (int k = 0; k <= MAX_INDEX; k += 1) {
                getJumps(moves, k);
            }
        } else {
            for (int k = 0; k <= MAX_INDEX; k += 1) {
                getMoves(moves, k);
            }
        }
    }

    /** Add all legal non-capturing moves from the position
     *  with linearized index K to MOVES. */
    private void getMoves(ArrayList<Move> moves, int k) {
        // FIXME
        for (Move move : generateMoves(k, 1)) {
            moves.add(move);
        }
    }

    /** Add all legal captures from the position with linearized index K
     *  to MOVES. */
    private void getJumps(ArrayList<Move> moves, int k) {
        // FIXME
        for (Move move : generateMoves(k, 2)) {
            if (checkJump(move, true)) {
                moves.add(move);
            }
        }
    }

    /** Generates a list of possible moves.
     *
     * @param k the index
     * @param len the length of jump
     * @return a list of moves
     */
    private MoveList generateMoves(int k, int len) {
        int[] dc = {1, 0, -1, 0, 1, 1, -1, -1};
        int[] dr = {0, 1, 0, -1, 1, -1, 1, -1};

        char c = Move.col(k), r = Move.row(k);
        int type;
        if ((c - 'a') % 2 == (r - '1') % 2) {
            type = 4;
        } else {
            type = 8;
        }

        MoveList list = new MoveList();

        for (int i = 0; i < type; i++) {
            if (dc[i] == -((whoseMove().equals(WHITE)) ? 1 : -1)) {
                continue;
            }
            char nc = (char) ((int) c + len * dc[i]), nr = (char) ((int) r + len * dr[i]);
            if (validSquare(nc, nr) && get(nc, nr).equals(EMPTY)) {
                list.add(Move.move(c, r, nc, nr));
            }
        }
        return list;
    }

    /** Return true iff MOV is a valid jump sequence on the current board.
     *  MOV must be a jump or null.  If ALLOWPARTIAL, allow jumps that
     *  could be continued and are valid as far as they go.  */
    boolean checkJump(Move mov, boolean allowPartial) {
        // FIXME
        if (!validSquare(mov.fromIndex()) || get(mov.fromIndex()).equals(EMPTY)) {
            return false;
        }
        PieceColor color = get(mov.fromIndex());
        boolean began = false;
        while (mov != null) {
            if (!validSquare(mov.toIndex()) || !get(mov.toIndex()).equals(EMPTY)) {
                return allowPartial && began;
            }
            if (!get(mov.jumpedIndex()).equals(color.opposite())) {
                return false;
            }
            mov = mov.jumpTail();
            began = true;
        }
        return true;
    }

    /** Return true iff a jump is possible for a piece at position C R. */
    boolean jumpPossible(char c, char r) {
        return jumpPossible(index(c, r));
    }

    /** Return true iff a jump is possible for a piece at position with
     *  linearized index K. */
    boolean jumpPossible(int k) {
        // FIXME
        if (!get(k).equals(whoseMove())) {
            return false;
        }
        MoveList moveList = new MoveList();
        getJumps(moveList, k);
        for (Move move : moveList) {
            if (checkJump(move, true)) {
                return true;
            }
        }
        return false;
    }

    /** Return true iff a jump is possible from the current board. */
    boolean jumpPossible() {
        for (int k = 0; k <= MAX_INDEX; k += 1) {
            if (jumpPossible(k)) {
                return true;
            }
        }
        return false;
    }

    /** Return the color of the player who has the next move.  The
     *  value is arbitrary if gameOver(). */
    PieceColor whoseMove() {
        return _whoseMove;
    }

    /** Perform the move C0R0-C1R1, or pass if C0 is '-'.  For moves
     *  other than pass, assumes that legalMove(C0, R0, C1, R1). */
    void makeMove(char c0, char r0, char c1, char r1) {
        makeMove(Move.move(c0, r0, c1, r1, null));
    }

    /** Make the multi-jump C0 R0-C1 R1..., where NEXT is C1R1....
     *  Assumes the result is legal. */
    void makeMove(char c0, char r0, char c1, char r1, Move next) {
        makeMove(Move.move(c0, r0, c1, r1, next));
    }

    /** Make the Move MOV on this Board, assuming it is legal. */
    void makeMove(Move mov) {
        assert legalMove(mov);

        // FIXME
        _moves.add(mov);
        if (mov.isVestigial()) {
            return;
        }
        if (mov.isJump()) {
            if (!checkJump(mov, true)) {
                throw error("That move is illegal.");
            }
            for (; mov != null; mov = mov.jumpTail()) {
                set(mov.col1(), mov.row1(), _whoseMove);
                set(mov.col0(), mov.row0(), EMPTY);
                set((char) ((mov.col0() + mov.col1()) / 2),
                        (char) ((mov.row0() + mov.row1()) / 2), EMPTY);
            }
        } else {
            if (jumpPossible()) {
                throw error("A jump is possible.");
            }
            if (_moves.size() >= 2) {
                Move prev = _moves.get(_moves.size() - 2);
                if ((mov.isLeftMove() && prev.isRightMove())
                        && (mov.isRightMove() && prev.isLeftMove())
                        && prev.fromIndex() == mov.toIndex()) {
                    throw error("That move is illegal.");
                }
            }
            set(mov.col1(), mov.row1(), get(mov.col0(), mov.row0()));
            set(mov.col0(), mov.row0(), EMPTY);
        }
        _whoseMove = _whoseMove.opposite();

        setChanged();
        notifyObservers();
    }

    /** Undo the last move, if any. */
    void undo() {
        // FIXME
        if (_moves.size() != 0) {
            Move mov = _moves.remove(_moves.size() - 1);
            PieceColor mover = _whoseMove.opposite();
            if (mov.isJump()) {
                MoveList list = new MoveList();
                for (; mov != null; mov = mov.jumpTail()) {
                    list.add(mov);
                }
                for (int i = list.size() - 1; i >= 0; i--) {
                    Move curMov = list.get(i);
                    set(curMov.col0(), curMov.row0(), mover);
                    set(curMov.col1(), curMov.row1(), EMPTY);
                    set((char) ((curMov.col0() + curMov.col1()) / 2),
                            (char) ((curMov.row0() + curMov.row1()) / 2),
                            _whoseMove);
                }
            } else {
                set(mov.col0(), mov.row0(), mover);
                set(mov.col1(), mov.row1(), EMPTY);
            }
            _whoseMove = _whoseMove.opposite();
        }

        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /** Return a text depiction of the board.  If LEGEND, supply row and
     *  column numbers around the edges. */
    String toString(boolean legend) {
        Formatter out = new Formatter();
        // FIXME
        String s = "";
        for (char r  = '5'; r >= '1'; r--) {
            s += " ";
            if (legend) {
                s += " " + r;
            }
            for (char c = 'a'; c <= 'e'; c++) {
                s += " " + get(c, r).shortName();
            }
            if (r > '1') {
                s += "\n";
            }
        }
        out.format(s);
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Board)) {
            return false;
        }
        Board b = (Board) o;
        return _whoseMove.equals(b._whoseMove) && _gameOver == b._gameOver
                && Arrays.equals(_board, b._board);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /** Return true iff there is a move for the current player. */
    private boolean isMove() {
        // FIXME
        for (char c = 'a'; c <= 'e'; c++) {
            for (char r = '1'; r <= '5'; r++) {
                MoveList list = new MoveList();
                getMoves(list);
                if (list.size() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Player that is on move. */
    private PieceColor _whoseMove;

    /** Set true when game ends. */
    private boolean _gameOver;

    /** Convenience value giving values of pieces at each ordinal position. */
    static final PieceColor[] PIECE_VALUES = PieceColor.values();

    /** One cannot create arrays of ArrayList<Move>, so we introduce
     *  a specialized private list type for this purpose. */
    private static class MoveList extends ArrayList<Move> {
    }

    /** A read-only view of a Board. */
    private class ConstantBoard extends Board implements Observer {
        /** A constant view of this Board. */
        ConstantBoard() {
            super(Board.this);
            Board.this.addObserver(this);
        }

        @Override
        void copy(Board b) {
            assert false;
        }

        @Override
        void clear() {
            assert false;
        }

        @Override
        void makeMove(Move move) {
            assert false;
        }

        /** Undo the last move. */
        @Override
        void undo() {
            assert false;
        }

        @Override
        public void update(Observable obs, Object arg) {
            super.copy((Board) obs);
            setChanged();
            notifyObservers(arg);
        }
    }

    /** Stores the contents of the board in an array. */
    private PieceColor[] _board;

    /** Stores the current list of moves. */
    private MoveList _moves;

    /** represents the intial configuration of the board. */
    private static final String INIT_BOARD =
            "wwwww wwwww bb-ww bbbbb bbbbb";
}
