package two_phase_commit;

import helpers.JsonUtils;
import helpers.NetworkUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * File: Node.java
 * Author: Terry Grant Simpson
 * Description: Responsible for participating in the Two-Phase Commit by approving/denying transactions..
 * Date: 1.27.2022
 */
class Node {

	public static void main(String[] args) {
		int port = -1;
		int id = -1;
		if (args.length == 2) {
			try {
				port = Integer.parseInt(args[0]);
				id = Integer.parseInt(args[1]);
			} catch (NumberFormatException NumberFormatException) {
				System.out.println("Valid Options: <-Pport=[value]> <-Pid=[value]>");
				System.out.println("Node 1 Defaults: <-Pport=8001> <-Pid=1>");
				System.out.println("Node 2 Defaults: <-Pport=8002> <-Pid=2>");
				System.exit(1);
			}
		}

		Socket tcSocket = null;
		try {
			ServerSocket nodeSocket = new ServerSocket(port);
			System.out.println("Node " + id + " Started: Waiting for TC...");
			tcSocket = nodeSocket.accept();
			System.out.println("Node " + id + " established connection to TC...");
			InputStream in = tcSocket.getInputStream();
			OutputStream out = tcSocket.getOutputStream();

			while (tcSocket.isConnected()) {
				JSONObject response = null;
				JSONObject message = JsonUtils.fromByteArray(NetworkUtils.receive(in));
				if (message.getString("message").contains("PREPARE")) {
					System.out.println("FROM SERVER: " + message.getString("message"));
					response = ready();
					System.out.println("Node" + id + " TO SERVER: READY");
				} else if (message.getString("message").contains("COMMIT")) {
					System.out.println("FROM SERVER: " + message.getString("message"));
					response = commit();
					System.out.println("Node" + id + " TO SERVER: COMMITTED");
				} else if (message.getString("message").contains("ABORT")) {
					System.out.println("FROM SERVER: " + message.getString("message"));
					response = abort();
					System.out.println("Node" + id + " TO SERVER: ABORTED");
				}

				if (response != null) {
					NetworkUtils.send(out, JsonUtils.toByteArray(response));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Closing client socket.");
			try {
				if (tcSocket != null) {
					tcSocket.close();
				}
			} catch (IOException e) {
				System.out.println("Socket closed");
			}
		}
	}

	public static JSONObject ready() {
		JSONObject response = new JSONObject();
		response.put("message", "READY");
		return response;
	}

	public static JSONObject commit() {
		JSONObject response = new JSONObject();
		response.put("message", "COMMITTED");
		return response;
	}

	public static JSONObject abort() {
		JSONObject response = new JSONObject();
		response.put("message", "ABORTED");
		return response;
	}

	public static JSONObject notReady() {
		JSONObject response = new JSONObject();
		response.put("message", "NOT_READY");
		return response;
	}


}

























