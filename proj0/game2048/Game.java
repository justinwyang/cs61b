package game2048;

import static game2048.Side.*;

/** The input/output and GUI controller for play of a game of 2048.
 *  @author Justin Yang
 */
public class Game {

    /** Controller for a game represented by MODEL, using SOURCE as the
     *  the source of key inputs and random Tiles. */
    public Game(Model model, InputSource source) {
        _model = model;
        _source = source;
        _playing = true;
    }

    /** Return true iff we have not received a Quit command. */
    boolean playing() {
        return _playing;
    }

    /** Clear the board and play one game, until receiving a quit or
     *  new-game request.  Update the viewer with each added tile or
     *  change in the board from tilting. */
    void playGame() {
        _model.clear();
        _model.addTile(getValidNewTile());
        while (_playing) {
            if (!_model.gameOver()) {
                _model.addTile(getValidNewTile());
                _model.notifyObservers();
            }

            boolean moved;
            moved = false;
            while (!moved) {
                String cmnd = _source.getKey();
                switch (cmnd) {
                case "Quit":
                    _playing = false;
                    return;
                case "New Game":
                    return;
                case "Up": case "Down": case "Left": case "Right": case "↑":
                case "↓": case "←": case "→":
                    if (!_model.gameOver() && _model.tilt(keyToSide(cmnd))) {
                        _model.notifyObservers();
                        moved = true;
                    }
                    break;
                default:
                    break;
                }

            }
        }
    }

    /** Return the side indicated by KEY ("Up", "Down", "Left",
     *  or "Right"). */
    private Side keyToSide(String key) {
        switch (key) {
        case "Up": case "↑":
            return NORTH;
        case "Down": case "↓":
            return SOUTH;
        case "Left": case "←":
            return WEST;
        case "Right": case "→":
            return EAST;
        default:
            throw new IllegalArgumentException("unknown key designation");
        }
    }

    /** Return a valid tile, using our source's tile input until finding
     *  one that fits on the current board. Assumes there is at least one
     *  empty square on the board. */
    private Tile getValidNewTile() {
        Tile get;
        do {
            get = _source.getNewTile(_model.size());
        } while (_model.tile(get.col(), get.row()) != null);
        return get;
    }

    /** Strings representing the four arrow keys. */
    private static final String[] ARROW_KEYS = {
        "Up", "Down", "Left", "Right"
    };

    /** The playing board. */
    private Model _model;

    /** Input source from standard input. */
    private InputSource _source;

    /** True while user is still willing to play. */
    private boolean _playing;

}
