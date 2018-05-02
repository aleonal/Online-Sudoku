package code.Network;

import code.Sudoku.HistoryNode;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * A simple chat server implemented using TCP/IP sockets. A client can
 * connect to this server and send messages to other clients. The chat
 * server receives messages from clients and broadcast them to all the
 * connected clients. A message is an arbitrary text and is also
 * printed on stdout. The default port number is 8008.
 *
 * <pre>
 *  Usage: java SudokuServer
 * </pre>
 *
 * @author Yoonsik Cheon
 */
class SudokuServer {

    /** Default port number on which this server to be run. */
    private int PORT_NUMBER;
    private ClientHandler service;
    private JTextArea logT;
    private HistoryNode data;
    private ServerSocket s;
    Socket incoming;

    /** Create a new server. */
    SudokuServer(JTextArea logT, HistoryNode data, int port) {
        this.logT = logT;
        this.PORT_NUMBER = port;
        this.data = data;
        start();
    }

    /** Start the server. */
    void start() {
        logT.append("\nSudoku Server on " + PORT_NUMBER + ".");
        try {
            s = new ServerSocket(PORT_NUMBER);
            for (;;) {
                incoming = s.accept();
                service = new ClientHandler(incoming, data, logT);
                service.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logT.append("\nError: "+e);
        }
    }

    void close() {
        try {
            s.close();
            incoming.close();
            service.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(HistoryNode hist) {
        this.data = hist;
        service.update(hist);
    }
}
