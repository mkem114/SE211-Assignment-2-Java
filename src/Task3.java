/**
 * Created by mkem114/6273632 on 29/09/16.
 */

//TODO Check that cycle at the bottom doesn't break
//Cycle at top
//Cycle in midle
//One cycle
//Triangle
//Upside down triangle
//Hour glass


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * <h1>Task3</h1>
 * The ultimate solution to Gerald's infamous secondary auto-marker assignment for SE211 S2 2016
 * <p>
 * This class takes a graph from stdin, z-sorts it and then prints the result to stdout. See assignment specs for
 * specifics
 * </p>
 */
public class Task3 {
    /**
     * Debug-mode printing switch with "true" being on
     */
    public static final boolean _debug = true;
    // stdin
    private BufferedReader _in;
    // stdout
    private BufferedWriter _out;

    /**
     * Starts the program after being called from command line
     *
     * @param args Ignored command line arguments
     * @throws Exception All exceptions are thrown for debugging as this is not intended for production
     */
    public static void main(String[] args) throws Exception {
        // Creates new graph solver
        new Task3();
    }

    /**
     * The main function in which initialisation and general program control flow occurs
     *
     * @throws Exception All exceptions are thrown for debugging as this is not intended for production
     */
    private Task3() throws Exception {
        // Initialises the input and output streams respectively
        _in = new BufferedReader(new InputStreamReader(System.in));
        _out = new BufferedWriter(new OutputStreamWriter(System.out));
        // Initialises the data structure

        // Loads the graph
        load();

        // Solves the graph
        solve();

        // Prints the graph
        print();

        // Flushes the output stream
        _out.flush();
    }

    /**
     * Loads the graph from stdin
     *
     * @throws Exception All exceptions are thrown for debugging as this is not intended for production
     */
    private void load() throws Exception {
    }

    /**
     * Solves the loaded graph
     */
    private void solve() {
    }

    /**
     * Prints the z-sort solution to stout
     *
     * @throws Exception All exceptions are thrown for debugging as this is not intended for production
     */
    private void print() throws Exception {
    }

    /**
     * <h1>NodeComparer</h1>
     * <p>
     * Represents something to compare Nodes
     * </p>
     * <p>
     * Only dependency is NodeSet class
     * </p>
     */
    private class NodeComparer implements Comparator<NodeSet> {
        /**
         * Same as o1.compareTo(o2) thus see NodeSet.compareTo(NodeSet n)
         *
         * @param o1 First NodeSet
         * @param o2 Second NodeSet
         * @return Order of nodes
         */
        @Override
        public int compare(NodeSet o1, NodeSet o2) {
            return o1.compareTo(o2);
        }
    }

    /**
     * <h1>NodeSet</h1>
     * <p>
     * Represents a node in a graph (e.g DAG, binary tree, cycle)
     * </p>
     * <p>
     * Responsible for storing and determining information about itself including comparision to other nodes
     * </p>
     * <p>
     * No dependencies other than built in java libraries
     * </p>
     */
    public class NodeSet implements Comparable<NodeSet> {
        private int _name;
        private int _strata;
        private boolean _base; // Whether anything connects to this node
        private LinkedList<NodeSet> _to; // List of nodes the node goes to
        private LinkedList<NodeSet> _collapsed; // List of nodes collapsed into this one

        /**
         * Creates a new node with a name and assumes the 0th strata to begin with
         *
         * @param name   NodeSet's name
         * @param strata NodeSet's strata
         */
        public NodeSet(int name) {
            _name = name;
            _strata = 0;
            _base = true;
            // Initialises the to and collapsed lists
            _to = new LinkedList<>();
            _collapsed = new LinkedList<>();
        }

        /**
         * Determines whether or not the NodeSet consists of a single element
         * @return Consists of one element
         */
        public boolean isSingular() {
            // If there is no collapsed NodeSet then the set can only have one element
            return _collapsed.size() == 0;
        }

        /**
         * Collapses a list of node sets into one
         *
         */
        public void collapse(NodeSet n) {
            for (NodeSet n2 : n._to) {
                if ((n2 != this) && !(_to.contains(n2))) {
                    _to.add(n2);
                }
            }
        }

        /**
         * Evaluates which node comes first primarily by the strata of each and secondly by their names
         * zero means that no difference is found
         * negative means that the specified node is greater
         * positive means that the specified nod is lesser
         *
         * @param n Specified node to compare
         * @return Order of nodes
         */
        public int compareTo(NodeSet n) {
            // Checks the strata first
            if (n.strataOn() > _strata) {
                return -1;
            } else if (n.strataOn() < _strata) {
                return 1;
            } else {
                // Checks the name secondly
                return Integer.compare(_name, n.name());
            }
        }

        /**
         * Determines the name of the node
         *
         * @return NodeSet's name
         */
        public int name() {
            return _name;
        }

        /**
         * Informs the node that it is pointing to another (specified) node
         *
         * @param to Pointed node
         */
        public void goesTo(NodeSet to) {
            // Adds the node to the to list
            _to.add(to);
        }

        /**
         * Informs the NodeSet that it is no longer pointing to a node
         * @param to
         */
        public void cutTo(NodeSet to) {
            // Removes the node from the to list
            _to.remove(to);
        }

        /**
         * Informs the node that it is pointed to by another (specified) node
         *
         * @param from Pointing node
         */
        public void comesFrom(NodeSet from) {
            // Adds the node to the from list
            _from.add(from);
        }

        /**
         * Informs the NodeSet that it is no longer pointed to from a nod
         * @param from
         */
        public void cutFrom(NodeSet from) {
            // Removes the node from the from list
            _from.remove(from);
        }

        /**
         * Evaluates what strata number the node is on
         *
         * @return Nth strata
         */
        public int strataOn() {
            return _strata;
        }

        /**
         * Evaluates whether on not the node is on the 0th strata to its knowledge
         *
         * @return Whether or not its on strata 0
         */
        public boolean base() {
            // This node is on 0th strata iff it's not pointed to from another node
            return _from.size() == 0;
        }

        /**
         * Produces a string to uniquely identify a node and its properties.
         * With debug settings on the arcs to and fro are also printed.
         *
         * @returns String of identifying info
         */
        @Override
        public String toString() {
            // Whether or not to print the arcs
            boolean printArcs = _debug;
            // String compiler
            StringBuilder _str = new StringBuilder();
            // Insert the name and strata of the node
            _str.append("\n" + _name + " is on: " + strataOn() + " and referenced by: " + hashCode() + "\n");

            // Prints the arcs
            if (printArcs) {
                _str.append("comes from nodes: \n");
                for (NodeSet n : _from) {
                    _str.append(n.name() + "\n");
                }
                _str.append("\ngoes to nodes: \n");
                for (NodeSet n : _to) {
                    _str.append(n.name() + "\n");
                }
            }

            // Returns the print
            return _str.toString();
        }
    }
}