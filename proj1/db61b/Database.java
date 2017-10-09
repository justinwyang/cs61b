// This is a SUGGESTED skeleton for a class that contains the Tables your
// program manipulates.  You can throw this away if you want, but it is a good
// idea to try to understand it first.  Our solution changes about 6
// lines in this skeleton.

// Comments that start with "//" are intended to be removed from your
// solutions.
package db61b;

// FILL IN (WITH IMPORTS)?

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

    private ArrayList<Table> _tables;
    private ArrayList<String> _names;
    private int _length;
}
