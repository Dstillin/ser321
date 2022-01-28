package two_phase_commit;

import helpers.JsonUtils;
import helpers.NetworkUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * File: Client.java
 * Author: Terry Grant Simpson
 * Description: This class prompts and guides a client through adding a new tip or displaying all tips.
 * Date: 1.27.2022
 */
public class Client {

	public static void main(String[] args) {
		int port = -1;
		if (args.length == 2) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException NumberFormatException) {
				System.out.println("Valid Options: <-Pport=[value]> <-Phost=[string]>");
				System.out.println("Defaults: <-Pport=8000> <-Phost=localhost>");
				System.exit(1);
			}
		}

		String host = args[1];
		Socket socket = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		try {
			socket = new Socket(host, port);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();

			boolean quit = false;
			while (!quit) {
				// Menu
				System.out.println("*************************");
				System.out.println("         Menu");
				System.out.println("*************************");
				System.out.println("Make a selection from 0-2.");
				System.out.println("1: Display all tips.");
				System.out.println("2: Enter a new tip.");
				System.out.println("0: Quit");
				String line = stdin.readLine();
				JSONObject request = null;
				try {
					int selection = Integer.parseInt(line);
					if (selection >= 0 && selection < 3) {
						switch (selection) {
							case 0:
								request = quit();
								quit = true;
								break;
							case 1:
								request = display();
								break;
							case 2:
								System.out.println("Enter your tip.");
								line = stdin.readLine();
								request = add(line);
								break;
							default:
								System.out.println("Error with selection");
						}
					}
				} catch (NumberFormatException e) {
					System.out.println("Enter an integer 0-2");
				}

				// Send Request and read response
				if (request != null) {
					NetworkUtils.send(out, JsonUtils.toByteArray(request));
					JSONObject response = JsonUtils.fromByteArray(NetworkUtils.receive(in));
					String responseType = response.getString("message");
					if (responseType.contains("quit")) {
						System.out.println(response.getString("message"));
					} else if (responseType.contains("display")) {
						String list = response.getString("data");
						System.out.println(list);
					} else if (responseType.contains("add")) {
						System.out.println(response.getString("data"));
					} else if (responseType.contains("error")) {
						System.out.println(response.getString("data"));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
				stdin.close();
			} catch (IOException e) {
				System.out.println("Resources Closed.");
			}
		}
	}

	public static JSONObject quit() {
		JSONObject request = new JSONObject();
		request.put("message", "quit");
		return request;
	}

	public static JSONObject display() {
		JSONObject request = new JSONObject();
		request.put("message", "display");
		return request;
	}

	public static JSONObject add(String newTip) {
		JSONObject request = new JSONObject();
		request.put("message", "add");
		request.put("data", newTip);
		return request;
	}
}