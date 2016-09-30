/**
 * Created by mkem114/6273632 on 29/09/16.
 */

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
     * @param args Ignored command line arguments
     * @throws Exception All exceptions are thrown for debugging as this is not intended for production
     */
    public static void main(String[] args) throws Exception {
        // Creates new graph solver
        new Task3();
    }

    /**
     * The main function in which initialisation and general program control flow occurs
     * @throws Exception All exceptions are thrown for debugging as this is not intended for production
     */
    private Task3() throws Exception {
        // Initialises the input and output streams respectively
        _in = new BufferedReader(new InputStreamReader(System.in));
        _out = new BufferedWriter(new OutputStreamWriter(System.out));

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
     * Only dependency is Node class
     * </p>
     */
    private class NodeComparer implements Comparator<Node> {
        /**
         * Same as o1.compareTo(o2) thus see Node.compareTo(Node n)
         *
         * @param o1 First Node
         * @param o2 Second Node
         * @return Order of nodes
         */
        @Override
        public int compare(Node o1, Node o2) {
            return o1.compareTo(o2);
        }
    }

    /**
     * <h1>Node</h1>
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
    class Node implements Comparable<Node> {
        private int _name;
        private int _strata;
        private LinkedList<Node> _from; // List of nodes the node comes from
        private LinkedList<Node> _to; // Lists of nodes the node goes to

        /**
         * Creates a new node with the given name and assumes 0th strata to begin with
         *
         * @param name Node's name
         */
        Node(int name) {
            this(name, 0);
        }

        /**
         * Creates a new node with a name and strata
         *
         * @param name   Node's name
         * @param strata Node's strata
         */
        Node(int name, int strata) {
            _name = name;
            _strata = strata;
            // Initialises the to and from lists
            _from = new LinkedList<>();
            _to = new LinkedList<>();
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
        public int compareTo(Node n) {
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
         * @return Node's name
         */
        public int name() {
            return _name;
        }

        /**
         * Informs the node that it is pointing to another (specified) node
         *
         * @param to Pointed node
         */
        public void goesTo(Node to) {
            // Adds the node to the to list
            _to.add(to);
        }

        /**
         * Informs the node that it is pointed to by another (specified) node
         *
         * @param from Pointing node
         */
        public void comesFrom(Node from) {
            // Adds the node to the from list
            _from.add(from);
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
                for (Node n : _from) {
                    _str.append(n.name() + "\n");
                }
                _str.append("\ngoes to nodes: \n");
                for (Node n : _to) {
                    _str.append(n.name() + "\n");
                }
            }

            // Returns the print
            return _str.toString();
        }
    }
}