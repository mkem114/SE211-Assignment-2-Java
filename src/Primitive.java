import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Created by mkem114 on 3/10/16.
 */
public class Primitive {
    ArrayList<ArrayList<Boolean>> _arcMatrix;
    HashMap<String, Integer> _nodes;
    HashMap<Integer, TreeSet<String>> _labels;
    private BufferedReader _in;
    private BufferedWriter _out;
    private boolean _isDAG;

    public static void main(String[] args) throws Exception {
        new Primitive();
    }

    Primitive() throws  Exception {
        _arcMatrix = new ArrayList<>();
        _nodes = new HashMap<>();
        _labels = new HashMap<>();
        _isDAG = true;
        _in = new BufferedReader(new InputStreamReader(System.in));
        _out = new BufferedWriter(new OutputStreamWriter(System.out));

        load();

        //solve();

        //print();
    }

    void load() throws Exception {
        int numArcs = Integer.parseInt(_in.readLine()); // Determines how many arcs there are to read in
        for (int arc = 0; arc++ < numArcs; ) { // Reads every arc
            StringTokenizer line = new StringTokenizer(_in.readLine());
            String fromNodeName = line.nextToken(); // from node
            String toNodeName = line.nextToken(); // to node

            if (fromNodeName.equals(toNodeName)) {
                if (!_nodes.containsKey(fromNodeName)) {

                }
            } else {

            }


        }
    }
}
