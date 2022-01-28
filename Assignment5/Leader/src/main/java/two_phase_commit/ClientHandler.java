package two_phase_commit;

import helpers.JsonUtils;
import helpers.NetworkUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

/**
 * File: ClientHandler.java
 * Author: Terry Grant Simpson
 * Description: Handles each client with unique thread.  Responsible for flow the
 * Two-Phase Commit flow by implementing phase 1 and 2.
 * Date: 1.27.2022
 */
public class ClientHandler implements Runnable {
	private final Socket socket;
	private final Socket node1Socket;
	private final Socket node2Socket;
	private final StringList list;

	public ClientHandler(Socket socket, List<String> list, Socket n1Socket, Socket n2Socket) {
		this.socket = socket;
		this.node1Socket = n1Socket;
		this.node2Socket = n2Socket;
		this.list = new StringList(list);
	}

	@Override
	public void run() {
		System.out.println("SERVER: connected to client");
		InputStream in;
		OutputStream out;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
			boolean quit = false;
			while (!quit) {
				// Receive & print request from client
				JSONObject response;
				JSONObject clientRequest = JsonUtils.fromByteArray(NetworkUtils.receive(in));
				String clientMessage = clientRequest.getString("message");
				if (clientMessage.contains("quit")) {
					response = quit();
					quit = true;
				} else if (clientMessage.contains("add")) {

					// Phase 1
					boolean phase1Complete = false;
					if (node1Socket.isConnected() && node2Socket.isConnected()) {
						messageToNodes(node1Socket, node2Socket, "PREPARE");
					}
					if (node1Socket.isConnected() && node2Socket.isConnected()) {
						String nodesReady = messageFromNodes(node1Socket, node2Socket, "READY");
						if (nodesReady.equals("OK")) {
							phase1Complete = true;
						}
					}

					// Phase 2
					boolean phase2Complete = false;
					if (phase1Complete) {
						if (node1Socket.isConnected() && node2Socket.isConnected()) {
							messageToNodes(node1Socket, node2Socket, "VOTE_TO_COMMIT");
						}
						if (node1Socket.isConnected() && node2Socket.isConnected()) {
							String nodesReady = messageFromNodes(node1Socket, node2Socket, "COMMITTED");
							if (nodesReady.equals("OK")) {
								phase2Complete = true;
							}
						}
					}

					// Complete Transaction
					if (phase1Complete && phase2Complete) {
						String newData = clientRequest.getString("data");
						synchronized (list) {list.add(newData);}
						response = add(newData);
					} else {
						response = error("Request Aborted");
					}

				} else if (clientMessage.contains("display")) {
					response = display(list);
				} else {
					response = error("Message was received with error");
				}

				NetworkUtils.send(out, JsonUtils.toByteArray(response));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Closing client socket.");
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				System.out.println("Socket closed");
			}
		}

	}

	public static JSONObject quit() {
		JSONObject response = new JSONObject();
		response.put("message", "Bye");
		return response;
	}

	private synchronized static void messageToNodes(Socket n1Socket, Socket n2Socket, String message) {
		JSONObject request = new JSONObject();
		request.put("message", message);
		try {
			OutputStream node1Out = n1Socket.getOutputStream();
			OutputStream node2Out = n2Socket.getOutputStream();
			NetworkUtils.send(node1Out, JsonUtils.toByteArray(request));
			System.out.println("TC: sent " + message + " request to node 1.");
			NetworkUtils.send(node2Out, JsonUtils.toByteArray(request));
			System.out.println("TC: sent " + message + " request to node 2.");
		} catch (IOException e) {
			System.out.println("Unable to send request to nodes.");
		}
	}

	private synchronized static String messageFromNodes(Socket n1Socket, Socket n2Socket, String message) {
		try {
			InputStream node1In = n1Socket.getInputStream();
			JSONObject node1Response = JsonUtils.fromByteArray(NetworkUtils.receive(node1In));
			System.out.println("FROM Node 1: " + node1Response.getString("message"));
			InputStream node2In = n2Socket.getInputStream();
			JSONObject node2Response = JsonUtils.fromByteArray(NetworkUtils.receive(node2In));
			System.out.println("FROM Node 2: " + node2Response.getString("message"));

			if (node1Response.getString("message").contains(message) && node2Response.getString("message").contains(message)) {
				return "OK";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "ABORT";
	}

	public static JSONObject add(String strToAdd) {
		JSONObject response = new JSONObject();
		response.put("message", "add");
		response.put("data", strToAdd);
		return response;
	}

	public static JSONObject error(String error) {
		JSONObject response = new JSONObject();
		response.put("message", "error");
		response.put("data", error);
		return response;
	}

	public static JSONObject display(StringList list) {
		JSONObject response = new JSONObject();
		response.put("message", "display");
		response.put("data", list.displayList());
		return response;
	}
}
