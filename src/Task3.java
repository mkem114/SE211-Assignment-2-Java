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
//Fix collapsing chain

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
    private HashMap<String, NodeSet> _nodeSets;
    // is a dag
    private boolean _isDAG;
    // strata
    private LinkedList<LinkedList<NodeSet>> _strata;

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
        _isDAG = true;
        _nodeSets = new HashMap<>();
        _strata = new LinkedList<>();

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
            StringTokenizer line = new StringTokenizer(_in.readLine());
            String fromNodeName = line.nextToken(); // from node
            String toNodeName = line.nextToken(); // to node

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
    class NodeSet {
        private String _name;
        private NodeSet _collapsedTo;
        private HashSet<NodeSet> _to; // List of nodes the node goes to
        private HashSet<NodeSet> _from; // List of nodes the node comes from
        private TreeSet<String> _container; // List of nodes collapsed into this one

        /**
         * Creates a new node with a name and assumes the 0th strata to begin with
         *
         * @param name   NodeSet's name
         */
        NodeSet(String name) {
            _name = name;
            // Initialises the to, from and container lists
            _collapsedTo = null;
            _to = new HashSet<>();
            _from = new HashSet<>();
            _container = new TreeSet<>();
        }

        /**
         * Collapses a list of node sets into one
         *
         */
        void collapse(NodeSet n) {
            NodeSet base = collapseTo();
            base._to.addAll(n._to);
            base._from.addAll(n._from);
            base._container.add(n._name);
            base._container.addAll(n._container);
            for (NodeSet newTo : n._to) {
                newTo.cutFrom(n);
                newTo.comesFrom(base);
            }
            for (NodeSet newFrom : n._from) {
                newFrom.cutTo(n);
                newFrom.goesTo(base);
            }
            n._to.clear();
            n._from.clear();
            n._container.clear();
            base._to.remove(base);
            base._from.remove(base);
            base._to.remove(n);
            base._from.remove(n);
            base._container.remove(base._name);
            _nodeSets.remove(n._name);
            n._collapsedTo = base;
        }

        NodeSet collapseTo() {
            if (_collapsedTo == null) {
                return this;
            } else {
                return _collapsedTo.collapseTo();
            }
        }

        void collapseCycles(HashSet<String> _seen, ArrayList<NodeSet> _order) {
            if (_seen.contains(_name)) {
                while(_order.get(_order.size()-1) != this) {
                    _order.get(_order.size()-2).collapse(_order.get(_order.size()-1));
                    _order.remove(_order.size()-1);
                }
            } else {
                _seen.add(_name);
                _order.add(this);
                ArrayList<NodeSet> nextNodeSet = new ArrayList<>(_to);
                for (int i = 0; i++ < _to.size();) {
                    nextNodeSet.get(i).collapseCycles((HashSet<String>) _seen.clone(), (ArrayList<NodeSet>) _order.clone());
                }
            }
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

        void cutFrom(NodeSet from) {
            _from.remove(from);
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
            _str.append("\n" + _name + " is on: " + " and referenced by: " + hashCode() + "\n");

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
                for (String i : _container) {
                    _str.append(i + "\n");
                }
                _str.append("is collapsed: " +  "\n");
            }

            // Returns the print
            return _str.toString();
        }
    }
}