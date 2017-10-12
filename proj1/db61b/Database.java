package db61b;

import java.util.ArrayList;

/** A collection of Tables, indexed by name.
 *  @author Justin Yang */
class Database {
    /** An empty database. */
    public Database() {
        _tables = new ArrayList<>();
        _names = new ArrayList<>();
        _length = 0;
    }

    /** Return the Table whose name is NAME stored in this database, or null
     *  if there is no such table. */
    public Table get(String name) {
        for (int i = 0; i < _length; i++) {
            if (_names.get(i).equals(name)) {
                return _tables.get(i);
            }
        }
        return null;
    }

    /** Set or replace the table named NAME in THIS to TABLE.  TABLE and
     *  NAME must not be null, and NAME must be a valid name for a table. */
    public void put(String name, Table table) {
        if (name == null || table == null) {
            throw new IllegalArgumentException("null argument");
        }
        for (int i = 0; i < _length; i++) {
            if (_names.get(i).equals(name)) {
                _tables.set(i, table);
                return;
            }
        }
        _names.add(name);
        _tables.add(table);
        _length++;
    }

    /** Stores the list of tables. */
    private ArrayList<Table> _tables;
    /** Stores the names of the tables. */
    private ArrayList<String> _names;
    /** Stores the length of the table and name arrays. */
    private int _length;
}
