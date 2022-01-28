package two_phase_commit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * File: TransactionCoordinator.java
 * Author: Terry Grant Simpson
 * Description: Responsible for connecting to participants and managing all clients..
 * Date: 1.27.2022
 */
class TransactionCoordinator {

	private static final List<Socket> clientSocketList = new ArrayList<>();
	private static final List<String> list = new ArrayList<>();

	public static void main(String[] args) {
		int port = -1;
		int leftPort = -1;
		int rightPort = -1;
		// Port arguments from CLI
		if (args.length == 5) {
			try {
				port = Integer.parseInt(args[0]);
				leftPort = Integer.parseInt(args[1]);
				rightPort = Integer.parseInt(args[2]);
			} catch (NumberFormatException NumberFormatException) {
				System.out.println("Valid Options: <-Pport=[value]> <-Pleftport=[value]> <-Prightport=[value]> <-Plefthost=[string]> <-Prighthost=[string]>");
				System.out.println("Defaults: <-Pport=8000> <-Pleftport=8001> <-Prightport=8002> <-Plefthost=localhost> <-Prighthost=localhost>");
				System.exit(1);
			}
		}

		// Host arguments from CLI
		String leftHost = args[3];
		String rightHost = args[4];

		// Connect to participants
		Socket node1Socket = connectNode(leftPort, leftHost, 1);
		Socket node2Socket = connectNode(rightPort, rightHost, 2);

		Socket clientSocket = null;
		try {
			System.out.println("SERVER STARTED: Accepting Requests...");
			ServerSocket server = new ServerSocket(port);
			// Accept clients
			while (!server.isClosed()) {
				clientSocket = server.accept();
				clientSocketList.add(clientSocket);
				ClientHandler handler = new ClientHandler(clientSocket, list, node1Socket, node2Socket);
				handler.run();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Closing client socket.");
			try {
				if (clientSocket != null) {
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println("Client Socket already closed");
			}
		}

	}

	private static Socket connectNode(int leftPort, String leftHost, int nodeID) {
		Socket nodeSocket = null;
		try {
			nodeSocket = new Socket(leftHost, leftPort);
			if (nodeSocket.isConnected()) {
				System.out.println("TC connected to Node" + nodeID);
			}
		}catch(IOException e) {
			System.out.println("Unable to establish connection with node " + nodeID);
		}
		return nodeSocket;
	}
}


























