/**
 * Created by michael on 29/09/16.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Task3 {
    // stdin
    private BufferedReader _in;
    // stdout
    private BufferedWriter _out;

    public static void main(String[] args) throws Exception {
        new Task3();
    }

    private Task3() throws Exception {
        // Initialises the input and output streams respectively
        _in = new BufferedReader(new InputStreamReader(System.in));
        _out = new BufferedWriter(new OutputStreamWriter(System.out));

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
    }

    private void solve() {
    }

    private void print() throws Exception {
    }
}