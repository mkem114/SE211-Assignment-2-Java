/**
 * Created by mkem114/6273632 on 29/09/16.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.LinkedList;

public class Task3 {
    // debug switch
    public static final boolean _debug = true;
    // stdin
    private BufferedReader _in;
    // stdout
    private BufferedWriter _out;

    public static void main(String[] args) throws Exception {
        // Creates new graph solver
        new Task3();
    }

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

    private void load() throws Exception {
    }

    private void solve() {
    }

    private void print() throws Exception {
    }

    private class NodeComparer implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.compareTo(o2);
        }
    }

    /**
     * <h1>Node</h1>
     * <p>
     *     Represents a node in a graph (e.g DAG, binary tree, cycle)
     * </p>
     * <p>
     *     Responsible for storing and determining information about itself
     * </p>
     * <p>
     *     No dependencies other than built in java libraries
     * </p>
     */
    class Node implements Comparable<Node> {
        private int _name;
        private int _strata;
        private LinkedList<Node> _from;
        private LinkedList<Node> _to;

        /**
         * Creates a new node with the given name and assumes 0th strata to begin with
         * @param name Node's name
         */
        Node(int name) {
            this(name, 0);
        }

        /**
         * Creates a new node with a name and strata
         * @param name Node's name
         * @param strata Node's strata
         */
        Node(int name, int strata) {
            _name = name;
            _strata = strata;
            _from = new LinkedList<>();
            _to = new LinkedList<>();
        }

        /**
         * Evaluates which node comes first primarily by the strata of each and secondly by their names
         * zero means that no difference is found
         * negative means that the specified node is greater
         * positive means that the specified nod is lesser
         * @param n Specified node to compare
         * @return Order of nodes
         */
        public int compareTo(Node n) {
            if (n.strataOn() > _strata) {
                return -1;
            } else if (n.strataOn() < _strata) {
                return 1;
            } else {
                return Integer.compare(_name, n.name());
            }
        }

        /**
         * Determines the name of the node
         * @return Node's name
         */
        public int name() {
            return _name;
        }

        /**
         * Informs the node that it is pointing to another (specified) node
         * @param to Pointed node
         */
        public void goesTo(Node to) {
            _to.add(to);
        }

        /**
         * Informs the node that it is pointed to by another (specified) node
         * @param from Pointing node
         */
        public void comesFrom(Node from) {
            _from.add(from);
        }

        /**
         * Evaluates what strata number the node is on
         * @return Nth strata
         */
        public int strataOn() {
            return _strata;
        }

        /**
         * Evaluates whether on not the node is on the 0th strata to its knowledge
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