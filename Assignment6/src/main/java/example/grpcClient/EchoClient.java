package example.grpcClient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import service.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EchoClient {
	private final int ADD_FUNCTION = 1;
	private final int SUBTRACT_FUNCTION = 2;
	private final int MULTIPLY_FUNCTION = 3;
	private final int DIVIDE_FUNCTION = 4;
	private final EchoGrpc.EchoBlockingStub blockingStub1;
	private final JokeGrpc.JokeBlockingStub blockingStub2;
	private final StoryGrpc.StoryBlockingStub blockingStub3;
	private final CalcGrpc.CalcBlockingStub blockingStub4;
	private final GolfGrpc.GolfBlockingStub blockingStub5;

	/**
	 * Construct client for accessing server using the existing channel.
	 */
	public EchoClient(ManagedChannel channel) {
		blockingStub1 = EchoGrpc.newBlockingStub(channel);
		blockingStub2 = JokeGrpc.newBlockingStub(channel);
		blockingStub3 = StoryGrpc.newBlockingStub(channel);
		blockingStub4 = CalcGrpc.newBlockingStub(channel);
		blockingStub5 = GolfGrpc.newBlockingStub(channel);
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.out.println("Expected arguments: <host(String)> <port(int)> <auto(int)>");
			System.exit(1);
		}

		int port = 9099;
		int auto = 1;
		String host = args[0];
		try {
			port = Integer.parseInt(args[1]);
			auto = Integer.parseInt(args[2]);
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

		try {
			EchoClient client = new EchoClient(channel);
			if (auto != 1) {
				BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
				boolean quit = false;
				while (!quit) {
					// Menu
					System.out.println("\n*************************");
					System.out.println("         Menu");
					System.out.println("*************************");
					System.out.println("Select a service listed below.");
					System.out.println("1: Echo/parrot");
					System.out.println("2: Joke/setJoke");
					System.out.println("3: Joke/getJoke");
					System.out.println("4: Story/read");
					System.out.println("5: Story/write");
					System.out.println("6: Calc/add");
					System.out.println("7: Calc/subtract");
					System.out.println("8: Calc/multiple");
					System.out.println("9: Calc/divide");
					System.out.println("10: Golf/course_record");
					System.out.println("11: Golf/record_score");
					System.out.println("0: Quit");

					String line = stdin.readLine();
					try {
						int selection = Integer.parseInt(line);
						if (selection >= 0 && selection < 12) {
							switch (selection) {
								case 0:
									quit = true;
									break;
								case 1:
									System.out.println("Enter the message you want the server to echo: ");
									String messageToEcho = stdin.readLine();
									client.askServerToParrot(messageToEcho);
									break;
								case 2:
									System.out.println("Enter the joke you'd like to set");
									String newJoke = stdin.readLine();
									client.setJoke(newJoke);
									break;
								case 3:
									System.out.println("How many jokes would you like?");
									String num = stdin.readLine();
									try {
										int jokeCount = Integer.parseInt(num);
										client.askForJokes(jokeCount);
									} catch (NumberFormatException nfe) {
										System.out.println("Error: not a valid number");
									}
									break;
								case 4:
									client.readStory();
									break;
								case 5:
									System.out.println("Enter a new line to the story: ");
									String newStoryLine = stdin.readLine();
									client.writeToStory(newStoryLine);
									break;
								case 6:
									System.out.println("Enter the first value you'd like to add");
									String addValue1 = stdin.readLine();
									System.out.println("Enter the second value you'd like to add");
									String addValue2 = stdin.readLine();
									try {
										client.calculate(Integer.parseInt(addValue1), Integer.parseInt(addValue2), client.ADD_FUNCTION);
									} catch (NumberFormatException NumberFormatException) {
										System.out.println("Error: not a valid number");
									}
									break;
								case 7:
									System.out.println("Enter the first value you'd like to find the difference of");
									String diffValue1 = stdin.readLine();
									System.out.println("Enter the second value you'd like to find the difference of");
									String diffValue2 = stdin.readLine();
									try {
										client.calculate(Integer.parseInt(diffValue1), Integer.parseInt(diffValue2), client.SUBTRACT_FUNCTION);
									} catch (NumberFormatException NumberFormatException) {
										System.out.println("Error: not a valid number");
									}
									break;
								case 8:
									System.out.println("Enter the first value you'd like to multiple");
									String multValue1 = stdin.readLine();
									System.out.println("Enter the second value you'd like to multiple");
									String multValue2 = stdin.readLine();
									try {
										client.calculate(Integer.parseInt(multValue1), Integer.parseInt(multValue2), client.MULTIPLY_FUNCTION);
									} catch (NumberFormatException NumberFormatException) {
										System.out.println("Error: not a valid number");
									}
									break;
								case 9:
									System.out.println("Enter the numerator");
									String numValue1 = stdin.readLine();
									System.out.println("Enter the denominator");
									String denomValue2 = stdin.readLine();
									try {
										client.calculate(Integer.parseInt(numValue1), Integer.parseInt(denomValue2), client.DIVIDE_FUNCTION);
									} catch (NumberFormatException NumberFormatException) {
										System.out.println("Error: not a valid number");
									}
									break;
								case 10:
									System.out.println("******Course Scores******");
									client.getAllScores();
									break;
								case 11:
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
										client.setGolfScore(golfName, courseName, golfDate, Integer.parseInt(golfScore));
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

			} else {

				System.out.println("******************************************");
				System.out.println("Automated results for Echo/parrot");
				System.out.println("Simon says to Repeat this");
				client.askServerToParrot("Repeat this.");

				System.out.println("******************************************");
				System.out.println("Automated results for Joke/setJoke");
				client.setJoke("THIS IS THE JOKE BEING SET");

				System.out.println("******************************************");
				System.out.println("Automated results for Joke/getJoke");
				System.out.println("Get 4 Jokes");
				client.askForJokes(4);

				System.out.println("******************************************");
				System.out.println("Automated results for Story/read");
				client.readStory();

				System.out.println("******************************************");
				System.out.println("Automated results for Story/write");
				client.writeToStory("I added this line\n");
				client.writeToStory("I added that line\n");
				client.writeToStory("and finally this line again.");

				System.out.println("******************************************");
				System.out.println("Automated results for Calc/add");
				System.out.print(" 15 + 15 = ");
				client.calculate(15, 15, client.ADD_FUNCTION);

				System.out.println("******************************************");
				System.out.println("Automated results for Calc/subtract");
				System.out.print(" 15 - 15 = ");
				client.calculate(15, 15, client.SUBTRACT_FUNCTION);

				System.out.println("******************************************");
				System.out.println("Automated results for Calc/multiply");
				System.out.print("15 x 15 = ");
				client.calculate(15, 15, client.MULTIPLY_FUNCTION);

				System.out.println("******************************************");
				System.out.println("Automated results for Calc/divide");
				System.out.print("4 / 2 = ");
				client.calculate(4, 2, client.DIVIDE_FUNCTION);

				System.out.println("******************************************");
				System.out.println("Automated results for Golf/recordScore");
				client.setGolfScore("David", "Hello Kitty Course", "1.1.2022", 80);
				client.setGolfScore("Kip", "Smack-Doodle-Do", "5.1.1982", 199);
				client.setGolfScore("Daffy", "Pinehurst", "9.1.1923", 65);

				System.out.println("******************************************");
				System.out.println("Automated results for Golf/getAllScores");
				client.getAllScores();
			}

		} finally {
			channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
		}
	}

	public void askServerToParrot(String message) {
		ClientRequest request = ClientRequest.newBuilder().setMessage(message).build();
		ServerResponse response;
		try {
			response = blockingStub1.parrot(request);
		} catch (Exception e) {
			System.err.println("RPC failed: " + e.getMessage());
			return;
		}
		System.out.println("Received from server: " + response.getMessage());
	}

	public void setJoke(String joke) {
		JokeSetReq request = JokeSetReq.newBuilder().setJoke(joke).build();
		JokeSetRes response;

		try {
			response = blockingStub2.setJoke(request);
			if (response.getOk()) {
				System.out.println("New Joke was set");
			} else {
				System.out.println("New joke was not set");
			}
		} catch (Exception e) {
			System.err.println("RPC failed: " + e);
		}
	}

	public void askForJokes(int num) {
		JokeReq request = JokeReq.newBuilder().setNumber(num).build();
		JokeRes response;

		try {
			response = blockingStub2.getJoke(request);
		} catch (Exception e) {
			System.err.println("RPC failed: " + e);
			return;
		}
		System.out.println("Your jokes: ");
		for (String joke : response.getJokeList()) {
			System.out.println("--- " + joke);
		}
	}

	public void readStory() {
		Empty request = Empty.newBuilder().build();
		ReadResponse response;
		response = blockingStub3.read(request);
		System.out.println(response.getSentence());
	}

	public void writeToStory(String newLine) {
		WriteRequest request = WriteRequest.newBuilder().setNewSentence(newLine).build();
		WriteResponse response;
		response = blockingStub3.write(request);
		System.out.println(response.getStory());
	}

	public void calculate(double value1, double value2, int function) {
		CalcRequest request = CalcRequest.newBuilder().addNum(value1).addNum(value2).build();
		CalcResponse response;
		switch (function) {
			case ADD_FUNCTION:
				response = blockingStub4.add(request);
				break;
			case SUBTRACT_FUNCTION:
				response = blockingStub4.subtract(request);
				break;
			case MULTIPLY_FUNCTION:
				response = blockingStub4.multiply(request);
				break;
			case DIVIDE_FUNCTION:
				response = blockingStub4.divide(request);
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

	public void getAllScores() {
		NoMessage.Builder request = NoMessage.newBuilder();
		AllScoresRes response;
		response = blockingStub5.getAllScores(request.build());
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

	public void setGolfScore(String name, String courseName, String date, int score) {
		RecordScoreReq.Builder request = RecordScoreReq.newBuilder().setName(name).setCourseName(courseName).setDate(date).setScore(score);
		RecordScoreRes response;
		try {
			response = blockingStub5.recordScore(request.build());
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

}
