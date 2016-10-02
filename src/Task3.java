/**
 * Created by mkem114/6273632 on 29/09/16.
 */

//TODO Check that cycle at the bottom doesn't break
//Cycle at top
//Cycle in middle
//One cycle
//Triangle
//Upside down triangle
//Hour glass
//Turn off debugging before submission
// Have to collapse to the right such that we don't get weird effects


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

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
    // nodes
    private HashMap<Integer, NodeSet> _nodeSets;
    // is a dag
    private boolean _isDAG;

    public static void debug(String string) {
        if (_debug) {
            System.out.println(string);
        }
    }

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
        _nodeSets = new HashMap<>();
        _isDAG = true;

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
        int numArcs = Integer.parseInt(_in.readLine()); // Determines how many arcs there are to read in
        for (int arc = 0; arc++ < numArcs; ) { // Reads every arc
            String rawArc = _in.readLine();
            String[] splitArc = rawArc.split(" ");

            int fromNodeName = Integer.parseInt(splitArc[0]); // from node
            int toNodeName = Integer.parseInt(splitArc[1]); // to node

            NodeSet fromNode = _nodeSets.get(fromNodeName);
            NodeSet toNode = _nodeSets.get(toNodeName);
            if (fromNode == null) { // if from node doesn't exist yet then creates it
                fromNode = new NodeSet(fromNodeName);
                _nodeSets.put(fromNodeName, fromNode);
            }
            if (toNode == null) { // if to node doesn't exist yet then creates it
                toNode = new NodeSet(toNodeName);
                _nodeSets.put(toNodeName, toNode);
            }

            if (toNodeName != fromNodeName) { // Stops reflexive arcs
                // Creates the arc between the two node
                fromNode.goesTo(toNode);
                toNode.comesFrom(fromNode);
            }
        }
    }

    /**
     * Solves the loaded graph
     */
    private void solve() {
        for (NodeSet n : _nodeSets.values()) {
            n.collapseCycles(new HashMap<>(), new ArrayList<>());
        }

        for (NodeSet n : _nodeSets.values()) {
            if (n.notCollapsed()) {
                n.solve();
            }
        }
    }

    /**
     * Prints the z-sort solution to stout
     *
     * @throws Exception All exceptions are thrown for debugging as this is not intended for production
     */
    private void print() throws Exception {
        if (_debug) {
            for (NodeSet n : _nodeSets.values()) {
                _out.write(n.toString());
            }
        }
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
    class NodeSet implements Comparable<NodeSet> {
        private int _name;
        private int _strata;
        private boolean _collapsed;
        private ArrayList<NodeSet> _to; // List of nodes the node goes to
        private ArrayList<NodeSet> _from; // List of nodes the node comes from
        private ArrayList<Integer> _container; // List of nodes collapsed into this one

        /**
         * Creates a new node with a name and assumes the 0th strata to begin with
         *
         * @param name   NodeSet's name
         */
        NodeSet(int name) {
            _name = name;
            _strata = 0;
            _collapsed = false;
            // Initialises the to, from and collapsed lists
            _to = new ArrayList<>();
            _from = new ArrayList<>();
            _container = new ArrayList<>();
        }

        /**
         * Collapses a list of node sets into one
         *
         */
        void collapse(NodeSet n) {
            if (n != this) {
                for (NodeSet nextTo : n._to) {
                    if (nextTo != this) {
                        _to.add(nextTo);
                    }
                }

                for (NodeSet nextFrom : n._from) {
                    nextFrom.cutTo(n);
                }

                _container.add(n._name);
                _container.addAll(n._container);

                n._collapsed = true;
                _isDAG = false;
            }
        }

        void collapseCycles(HashMap<Integer, Void> _seen, ArrayList<NodeSet> _order) {
            if (notCollapsed()) {
                if (_seen.containsKey(this._name)) {
                    boolean start = false;
                    for (NodeSet n : _order) {
                        if (n == this) {
                            start = true;
                        } else if (start) {
                            collapse(n);
                        }
                    }
                } else {
                    _seen.put(this._name, null);
                    _order.add(this);
                    for (int i = 0; i < _to.size(); i++) {
                        NodeSet nextSet = _to.get(i);
                        nextSet.collapseCycles(_seen, _order);
                    }
                }
            }
        }

        /**
         *
         */
        void solve() {

        }

        /**
         *
         * @return
         */
        boolean notCollapsed() {
            return !_collapsed;
        }

        /**
         * Informs the node that it is pointing to another (specified) node
         *
         * @param to Pointed node
         */
        void goesTo(NodeSet to) {
            // Adds the node to the to list
            _to.add(to);
        }

        /**
         * Informs the node that it is pointing to another (specified) node
         *
         * @param from Pointed node
         */
        void comesFrom(NodeSet from) {
            // Adds the node to the to list
            _from.add(from);
        }

        /**
         * Informs the NodeSet that it is no longer pointing to a node
         * @param to
         */
        void cutTo(NodeSet to) {
            // Removes the node from the to list
            _to.remove(to);
        }

        /**
         * Evaluates what strata number the node is on
         *
         * @return Nth strata
         */
        int strataOn() {
            return _strata;
        }

        /**
         * Produces a string to uniquely identify a node and its properties.
         * With debug settings on the arcs to and fro are also printed.
         *
         * @return String of identifying info
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
                    _str.append(n._name + "\n");
                }
                _str.append("goes to nodes: \n");
                for (NodeSet n : _to) {
                    _str.append(n._name + "\n");
                }
                _str.append("collapsed into this: \n");
                for (Integer i : _container) {
                    _str.append(i + "\n");
                }
                _str.append("is collapsed: " + _collapsed + "\n");
            }

            // Returns the print
            return _str.toString();
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
                return Integer.compare(_name, n._name);
            }
        }
    }
}