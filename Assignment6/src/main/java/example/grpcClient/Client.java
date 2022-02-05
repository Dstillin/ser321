package example.grpcClient;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import service.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("DuplicatedCode")
public class Client {
	private final int ADD_FUNCTION = 1;
	private final int SUBTRACT_FUNCTION = 2;
	private final int MULTIPLY_FUNCTION = 3;
	private final int DIVIDE_FUNCTION = 4;
	private final RegistryGrpc.RegistryBlockingStub registryBlockingStub;

	/**
	 * Construct client for accessing server using the existing channel.
	 */
	public Client(Channel regChannel) {
		registryBlockingStub = RegistryGrpc.newBlockingStub(regChannel);
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 4) {
			System.out.println("Expected arguments: <host(String)> <port(int)> <regHost(string)> <regPort(int)>");
			System.exit(1);
		}

		int port = 9099;
		int regPort = 9003;
		String host = args[0];
		String regHost = args[2];
		try {
			port = Integer.parseInt(args[1]);
			regPort = Integer.parseInt(args[3]);
		} catch (NumberFormatException nfe) {
			System.out.println("[Port] must be an integer");
			System.exit(2);
		}

		// Create a communication channel to the server, known as a Channel. Channels are thread-safe
		// and reusable. It is common to create channels at the beginning of your application and reuse
		// them until the application shuts down.
		// Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid needing certificates.
		String target = host + ":" + port;
		ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
		String regTarget = regHost + ":" + regPort;
		ManagedChannel regChannel = ManagedChannelBuilder.forTarget(regTarget).usePlaintext().build();
		try {

			Client client = new Client(regChannel);
			// Get thread's services
			List<String> serviceList = client.getServices();
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
			boolean quit = false;
			while (!quit) {
				// Menu
				System.out.println("\n*************************");
				System.out.println("         Menu");
				System.out.println("*************************");
				System.out.println("Select a service listed below.");
				if (serviceList != null) {
					for (int i = 0; i < serviceList.size(); i++) {
						if (!serviceList.get(i).contains("Registry/")) {
							System.out.println((i + 1) + ":" + serviceList.get(i));
						}
					}
					System.out.println("0: Quit");
				} else {
					System.out.println("Error getting service list");
					quit = true;
					break;
				}

				String selectionTarget;
				ManagedChannel selectionChannel;
				String line = stdin.readLine();
				try {
					int selection = Integer.parseInt(line);
					if (selection >= 0 && selection < 12) {
						switch (selection) {
							case 0:
								quit = true;
								break;
							case 1:
								selectionTarget = client.findServer(serviceList.get(1));
								selectionChannel = ManagedChannelBuilder.forTarget(selectionTarget).usePlaintext().build();
								System.out.println("Enter the message you want the server to echo: ");
								String messageToEcho = stdin.readLine();
								client.askServerToParrot(messageToEcho, selectionChannel);
								break;
							case 2:
								selectionTarget = client.findServer(serviceList.get(2));
								selectionChannel = ManagedChannelBuilder.forTarget(selectionTarget).usePlaintext().build();
								System.out.println("Enter the joke you'd like to set");
								String newJoke = stdin.readLine();
								client.setJoke(newJoke, selectionChannel);
								break;
							case 3:
								selectionTarget = client.findServer(serviceList.get(3));
								selectionChannel = ManagedChannelBuilder.forTarget(selectionTarget).usePlaintext().build();
								System.out.println("How many jokes would you like?");
								String num = stdin.readLine();
								try {
									int jokeCount = Integer.parseInt(num);
									client.askForJokes(jokeCount, selectionChannel);
								} catch (NumberFormatException nfe) {
									System.out.println("Error: not a valid number");
								}
								break;
							case 4:
								selectionTarget = client.findServer(serviceList.get(4));
								selectionChannel = ManagedChannelBuilder.forTarget(selectionTarget).usePlaintext().build();
								client.readStory(selectionChannel);
								break;
							case 5:
								selectionTarget = client.findServer(serviceList.get(5));
								selectionChannel = ManagedChannelBuilder.forTarget(selectionTarget).usePlaintext().build();
								System.out.println("Enter a new line to the story: ");
								String newStoryLine = stdin.readLine();
								client.writeToStory(newStoryLine, selectionChannel);
								break;
							case 6:
								selectionTarget = client.findServer(serviceList.get(7));
								selectionChannel = ManagedChannelBuilder.forTarget(selectionTarget).usePlaintext().build();
								System.out.println("Enter the first value you'd like to find the difference of");
								String diffValue1 = stdin.readLine();
								System.out.println("Enter the second value you'd like to find the difference of");
								String diffValue2 = stdin.readLine();
								try {
									client.calculate(Integer.parseInt(diffValue1), Integer.parseInt(diffValue2), client.SUBTRACT_FUNCTION, selectionChannel);
								} catch (NumberFormatException NumberFormatException) {
									System.out.println("Error: not a valid number");
								}
								break;

							case 7:
								selectionTarget = client.findServer(serviceList.get(9));
								selectionChannel = ManagedChannelBuilder.forTarget(selectionTarget).usePlaintext().build();
								System.out.println("Enter the numerator");
								String numerator = stdin.readLine();
								System.out.println("Enter the denominator");
								String denominator = stdin.readLine();
								try {
									client.calculate(Integer.parseInt(numerator), Integer.parseInt(denominator), client.DIVIDE_FUNCTION, selectionChannel);
								} catch (NumberFormatException NumberFormatException) {
									System.out.println("Error: not a valid number");
								}
								break;
							case 8:
								selectionTarget = client.findServer(serviceList.get(6));
								selectionChannel = ManagedChannelBuilder.forTarget(selectionTarget).usePlaintext().build();
								System.out.println("Enter the first value you'd like to add");
								String addValue1 = stdin.readLine();
								System.out.println("Enter the second value you'd like to add");
								String addValue2 = stdin.readLine();
								try {
									client.calculate(Integer.parseInt(addValue1), Integer.parseInt(addValue2), client.ADD_FUNCTION, selectionChannel);
								} catch (NumberFormatException NumberFormatException) {
									System.out.println("Error: not a valid number");
								}
								break;
							case 9:
								selectionTarget = client.findServer(serviceList.get(8));
								selectionChannel = ManagedChannelBuilder.forTarget(selectionTarget).usePlaintext().build();
								System.out.println("Enter the first value you'd like to multiple");
								String multiplyValue1 = stdin.readLine();
								System.out.println("Enter the second value you'd like to multiple");
								String multiplyValue2 = stdin.readLine();
								try {
									client.calculate(Integer.parseInt(multiplyValue1), Integer.parseInt(multiplyValue2), client.MULTIPLY_FUNCTION, selectionChannel);
								} catch (NumberFormatException NumberFormatException) {
									System.out.println("Error: not a valid number");
								}
								break;
							case 10:
								selectionTarget = client.findServer(serviceList.get(10));
								selectionChannel = ManagedChannelBuilder.forTarget(selectionTarget).usePlaintext().build();
								System.out.println("******Course Scores******");
								client.getAllScores(selectionChannel);
								break;
							case 11:
								selectionTarget = client.findServer(serviceList.get(11));
								selectionChannel = ManagedChannelBuilder.forTarget(selectionTarget).usePlaintext().build();
								System.out.println("******Record Your Score******");
								System.out.println("Enter your name: ");
								String golfName = stdin.readLine();
								System.out.println("Enter the course name: ");
								String courseName = stdin.readLine();
								System.out.println("Enter date played: ");
								String golfDate = stdin.readLine();
								System.out.println("Enter the score: ");
								String golfScore = stdin.readLine();
								try {
									client.setGolfScore(golfName, courseName, golfDate, Integer.parseInt(golfScore), selectionChannel);
								} catch (NumberFormatException nfe) {
									System.out.println("Error with score entry.  Please try again.");
								}
								break;
							default:
								System.out.println("Error with selection");
						}
					}
				} catch (NumberFormatException e) {
					System.out.println("Enter an integer 0-11");
				}
			}
		} finally {
			channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
			regChannel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
		}
	}

	public List<String> getServices() {
		GetServicesReq request = GetServicesReq.newBuilder().build();
		ServicesListRes response;
		List<String> services = new ArrayList<>();
		try {
			response = registryBlockingStub.getServices(request);
			int numOfServices = response.getServicesCount();
			for (int i = 0; i < numOfServices; i++) {
				services.add(response.getServices(i));
			}
			System.out.println(response);
		} catch (Exception e) {
			System.err.println("RPC failed: " + e);
			return null;
		}
		return services;
	}

	public String findServer(String name) {
		FindServerReq request = FindServerReq.newBuilder().setServiceName(name).build();
		SingleServerRes response;
		String target;
		try {
			response = registryBlockingStub.findServer(request);
			target = response.getConnection().getUri() + ":" + response.getConnection().getPort();
			System.out.println(response);
		} catch (Exception e) {
			System.err.println("RPC failed: " + e);
			target = null;
		}
		return target;
	}

	public void askServerToParrot(String message, Channel channel) {
		EchoGrpc.EchoBlockingStub echoStub = EchoGrpc.newBlockingStub(channel);
		ClientRequest request = ClientRequest.newBuilder().setMessage(message).build();
		ServerResponse response;
		try {
			response = echoStub.parrot(request);
		} catch (Exception e) {
			System.err.println("RPC failed: " + e.getMessage());
			return;
		}
		System.out.println("Received from server: " + response.getMessage());
	}

	public void setJoke(String joke, Channel channel) {
		JokeGrpc.JokeBlockingStub jokeStub = JokeGrpc.newBlockingStub(channel);
		JokeSetReq request = JokeSetReq.newBuilder().setJoke(joke).build();
		JokeSetRes response;

		try {
			response = jokeStub.setJoke(request);
			if (response.getOk()) {
				System.out.println("New Joke was set");
			} else {
				System.out.println("New joke was not set");
			}
		} catch (Exception e) {
			System.err.println("RPC failed: " + e);
		}
	}

	public void askForJokes(int num, Channel channel) {
		JokeGrpc.JokeBlockingStub jokeStub = JokeGrpc.newBlockingStub(channel);
		JokeReq request = JokeReq.newBuilder().setNumber(num).build();
		JokeRes response;

		try {
			response = jokeStub.getJoke(request);
		} catch (Exception e) {
			System.err.println("RPC failed: " + e);
			return;
		}
		System.out.println("Your jokes: ");
		for (String joke : response.getJokeList()) {
			System.out.println("--- " + joke);
		}
	}

	public void readStory(Channel channel) {
		StoryGrpc.StoryBlockingStub storyStub = StoryGrpc.newBlockingStub(channel);
		Empty request = Empty.newBuilder().build();
		ReadResponse response;
		response = storyStub.read(request);
		System.out.println(response.getSentence());
	}

	public void writeToStory(String newLine, Channel channel) {
		StoryGrpc.StoryBlockingStub storyStub = StoryGrpc.newBlockingStub(channel);
		WriteRequest request = WriteRequest.newBuilder().setNewSentence(newLine).build();
		WriteResponse response;
		response = storyStub.write(request);
		System.out.println(response.getStory());
	}

	public void calculate(double value1, double value2, int function, Channel channel) {
		CalcGrpc.CalcBlockingStub calcStub = CalcGrpc.newBlockingStub(channel);
		CalcRequest request = CalcRequest.newBuilder().addNum(value1).addNum(value2).build();
		CalcResponse response;
		switch (function) {
			case ADD_FUNCTION:
				response = calcStub.add(request);
				break;
			case SUBTRACT_FUNCTION:
				response = calcStub.subtract(request);
				break;
			case MULTIPLY_FUNCTION:
				response = calcStub.multiply(request);
				break;
			case DIVIDE_FUNCTION:
				response = calcStub.divide(request);
				break;
			default:
				response = null;
				System.out.println("Invalid Selection: Enter a selection between 1-4");
		}

		if (response != null) {
			if (response.getIsSuccess()) {
				System.out.println(response.getSolution());
			} else {
				System.out.println(response.getError());
			}
		}
	}

	public void getAllScores(Channel channel) {
		GolfGrpc.GolfBlockingStub golfStub = GolfGrpc.newBlockingStub(channel);
		NoMessage.Builder request = NoMessage.newBuilder();
		AllScoresRes response;
		response = golfStub.getAllScores(request.build());
		if (response.getIsSuccess()) {
			List<CourseScoreEntry> scores = response.getScoresList();
			for (CourseScoreEntry entry : scores) {
				System.out.println("***Score***");
				System.out.println("Course = " + entry.getCourseName());
				System.out.println("Name = " + entry.getName());
				System.out.println("Date = " + entry.getDate());
				System.out.println("Score = " + entry.getScore());
				System.out.println("************");
			}
		} else {
			System.out.println(response.getError());
		}
	}

	public void setGolfScore(String name, String courseName, String date, int score, Channel channel) {
		GolfGrpc.GolfBlockingStub golfStub = GolfGrpc.newBlockingStub(channel);
		RecordScoreReq.Builder request = RecordScoreReq.newBuilder().setName(name).setCourseName(courseName).setDate(date).setScore(score);
		RecordScoreRes response;
		try {
			response = golfStub.recordScore(request.build());
		} catch (Exception e) {
			System.err.println("RPC failed: " + e.getMessage());
			return;
		}

		if (response.getIsSuccess()) {
			System.out.println(response.getMessage());
		} else {
			System.out.println(response.getError());
		}

	}

	public void findServers(String name) {
		FindServersReq request = FindServersReq.newBuilder().setServiceName(name).build();
		ServerListRes response;
		try {
			response = registryBlockingStub.findServers(request);
			System.out.println(response.toString());
		} catch (Exception e) {
			System.err.println("RPC failed: " + e);
		}
	}
}
