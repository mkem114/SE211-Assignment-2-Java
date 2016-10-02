/**
 * Created by michael on 28/09/16.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.String;

public class Task1 {
    // stdin
    private BufferedReader _in;
    // stdout
    private BufferedWriter _out;
    // list of nodes
    private ArrayList<Node> _nodeList;
    // nodes
    private HashMap<String, Node> _nodes;
    // strata
    private LinkedList<LinkedList<Node>> _strata;

    public static void main(String[] args) throws Exception {
        new Task1();
    }

    private Task1() throws Exception {
        // Initialises the input and output streams respectively
        _in = new BufferedReader(new InputStreamReader(System.in));
        _out = new BufferedWriter(new OutputStreamWriter(System.out));
        _nodeList = new ArrayList<>();
        _nodes = new HashMap<>();
        _strata = new LinkedList<>();

        // Loads the DAG
        load();

        // Solves the DAG
        solve();

        // Prints the DAG
        print();

        // Flushes out stream
        _out.flush();
    }

    private void load() throws Exception {
        int numArcs = Integer.parseInt(_in.readLine());
        for (int i = 0; i < numArcs; i++) {
            String rawLine = _in.readLine();
            String[] splitLine = rawLine.split(" ");
            Node from = _nodes.get(splitLine[0]);
            Node to = _nodes.get(splitLine[1]);
            if (from == null) {
                from = new Node(splitLine[0]);
                _nodes.put(splitLine[0], from);
                _nodeList.add(from);
            }
            if (to == null) {
                to = new Node(splitLine[1]);
                _nodes.put(splitLine[1], to);
                _nodeList.add(to);
            }
            from.goesTo(to);
            to.comesFrom(from);
        }
    }

    private void solve() {
        for (Node n : _nodeList) {
            n.start();
        }

        _nodeList.sort(new NodeComparer());

        int _strataOn = 0;
        _strata.add(new LinkedList<>());
        for (Node n : _nodeList) {
            if (n.strataOn() > _strataOn) {
                _strataOn = n.strataOn();
                _strata.add(new LinkedList<>());
            }
            _strata.get(_strata.size()-1).add(n);
        }
    }

    private void print() throws Exception {
        _out.write("DAG\n" + _strata.size() + "\n");

        //prints out all the nodes
        /*_out.write("\n");
        for (NodeSet n : _nodeList) {
            _out.write(n.toString() + "\n");
        }
        _out.write("\n");*/

        for (LinkedList<Node> list : _strata) {
            _out.write(list.size() + "\n");
            for (Node n : list) {
                _out.write(n.nameOf() + "\n");
            }
        }
    }

    private class NodeComparer implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.compareTo(o2);
        }
    }

    private class Node implements Comparable<Node> {
        private String _name;
        private int _strata;
        private LinkedList<Node> _from;
        private LinkedList<Node> _to;

        Node(String name) {
            this(name, 0);
        }

        Node(String name, int strata) {
            _name = name;
            _strata = strata;
            _from = new LinkedList<>();
            _to = new LinkedList<>();
        }

        public int compareTo(Node n) {
            if (n.strataOn() > _strata) {
                return -1;
            } else if (n.strataOn() < _strata) {
                return 1;
            } else {
                return _name.compareTo(n.nameOf());
            }
        }

        String nameOf() {
            return _name;
        }

        void goesTo(Node to) {
            _to.add(to);
        }

        void comesFrom(Node from) {
            _from.add(from);
        }

        int strataOn() {
            return _strata;
        }

        boolean founder() {
            return _from.size() ==  0;
        }

        void start() {
            if (founder()) {
                for (Node n : _to) {
                    n.upStrata(0);
                }
            }
        }

        void upStrata(int strata) {
            if (_strata < strata + 1) {
                _strata = strata + 1;
                for (Node n : _to) {
                    n.upStrata(_strata);
                }
            }
        }

        @Override
        public String toString() {
            return _name + " : " + _strata;
        }
    }
} 