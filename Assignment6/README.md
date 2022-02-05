# ser321-fall2021-B-gtsimpso
SER-321:  Assignment 6

### Activity: Distributed System gRPC
For Task 1, the purpose of this program was to implement additional services to the server (Node.java) and allow the client (EchoClient.java) to select the service desired.  The gRPC protocol is used for the server/client communication.  The services included are echo, joke, story, calc, and golf.  The echo service allows the client to send a message to the server and have the server echo the client message.  The joke services allows the client to set a joke and get a number of jokes.  The story service permits the client to add additional lines to a story and read the current version of the story.  The calc service allows the user to input two values and have the server perform addition, subtraction, multiplication, or division.  In Task 2, the golf service was created to allow the client to record a golf score using name, course name, date, and golf score.  Also, this service provides a log list of the all the golf scores recorded.

In Task 3, an additional client (Client.java) and server (NodeService.java) was created to use the registry server compared to the server with the service.  This was to simulate a real world example of a client not knowning the services server address and port. With that being said if a server with a service/s is not registered to the registry server then the client not be able to access that service.  Again, in order for the client to gain access to a service in this task, the client must contact the registry server and search for the service and if the service exists the registry server sends a message back to the client with the services connection address.  At this point the client can access the services ip address and port number and make requests.   

###gRPC Communication Protocol:
###Calc Service
services
- add (CalcRequest) returns (CalcResponse)
- subtract (CalcRequest) returns (CalcResponse)
- multiply (CalcRequest) returns (CalcResponse)
- divide (CalcRequest) returns (CalcResponse)

messages
- CalcRequest => repeated double num = 1 	
- CalcResponse => bool isSuccess = 1 | double solution = 2 | string error = 3

###Echo Service
services
- parrot (ClientRequest) returns (ServerResponse)

messages
- ClientRequest => string message = 1
- ServerResponse => string message = 1

###Golf Service
services
- recordScore (RecordScoreReq) returns (RecordScoreRes)
- getAllScores (NoMessage) returns (AllScoresRes)

messages
- NoMessage => [no requirement]
- RecordScoreReq => string name = 1 | string courseName = 2 | string date = 3 | int32 score = 4
- RecordScoreRes => bool isSuccess = 1 | string message = 3 | string error = 5
- AllScoresRes => bool isSuccess = 1 | repeated CourseScoreEntry scores = 2 | string error = 3
- CourseScoreEntry => string name = 1 | string courseName = 2 | int32 score = 3 | string date = 4

###Joke Service
services
- getJoke (JokeReq) returns (JokeRes)
- setJoke (JokeSetReq) returns (JokeSetRes)

messages
- JokeReq => int32 number = 1
- JokeRes => repeated string joke = 1
- JokeSetReq => string joke = 1
- JokeSetRes => bool ok = 1 | string message = 2

###Story Service
services
- read (Empty) returns (ReadResponse)
- write (WriteRequest) returns (WriteResponse)

messages
- Empty => [no requirement]
- ReadResponse => bool isSuccess = 1 | string sentence = 2 | string error = 3
- WriteRequest => string new_sentence = 2
- WriteResponse => bool isSuccess = 1 | string story = 2 | string error = 3

### GRPC Services and Registry
- The following folder contains a Registry.jar which includes a Registering service where Nodes can register to allow clients to find them and use their implemented GRPC services.
- Some more detailed explanations will follow and please also check the build.gradle file
- Before starting do a "gradle generateProto".

### gradle runRegistryServer
- Will run the Registry node on localhost (arguments are possible see gradle). This node will run and allows nodes to register themselves.
- The Server allows Protobuf, JSON and gRPC. We will only be using gRPC

### gradle runNode
- Will run a node with an Echo and Joke service. The node registers itself on the Registry. You can change the host and port the node runs on and this will register accordingly with the Registry

### gradle runClientJava
- Will run a client which will call the services from the node, it talks to the node directly not through the registry. At the end the client does some calls to the Registry to pull the services, this will be needed later.

### gradle runDiscovery
- Will create a couple of threads with each running a node with services in JSON and Protobuf. This is just an example and not needed for assignment 6.

### gradle testProtobufRegistration
- Registers the protobuf nodes from runDiscovery and do some calls.

### gradle testJSONRegistration
- Registers the json nodes from runDiscovery and do some calls.

## How to run the program
Please use the following commands in the sequence that listed below.  It's important to note that console=plain does not need to be included because it's included within the gradle.properties file
### Task 1 & 2: Run in Terminal with default values
```
For Node: run "gradle runNode" 
Defaults include registryHost="localhost" | grpcPort=9002 | serviceHost="localhost" | servicePort=8000 | nodeName="test"
```
```   
For EchoClient: run "gradle runClientJava"
Defaults include serviceHost="localhost" | servicePort=8000 auto=1
``` 

### Task 1 & 2: Run in Terminal with custom values
```
For Node: run "gradle runNode -PregistryHost=<String> -PgrpcPort=<int> -PserviceHost=<string> -PservicePort=<int> -PnodeName=<string>" 
Notes: serviceHost, servicePort, nodeName are critical for operations. Task 1 & 2 don't use the registryHost & grpcPort; however, they are still required (leave default values)
```
```   
For EchoClient: run "gradle runClientJava -PserviceHost=<string> -PservicePort=<int> auto=<0 or 1>"
Notes: auto=1: automatically runs program with calls | auto=0: user prompts
``` 

### Task 3: Run in Terminal with default values
```
For runRegistryServer: run "gradle runRegistryServer" 
Defaults include protobufPort=9000 | jsonPort=9001 | grpcPort=9002
```
```
For registerServiceNode: run "gradle registerServiceNode" 
Defaults include registryHost="localhost" | grpcPort=9002 | serviceHost="localhost" | servicePort=8000 | nodeName="test"
```
```
For Client: run "gradle runClient2"
Defaults include serviceHost="localhost" | servicePort=8000 | registryHost="localhost" | grpcPort=9002
``` 

### Task 3: Run in Terminal with custom values
```
For runRegistryServer: run "gradle runRegistryServer -PprotobufPort=<int> -PjsonPort=<int> -PgrpcPort=<int>" 
Notes: ensure port values are correct 
```
```
For registerServiceNode: run "gradle registerServiceNode -PregistryHost=<string> -PgrpcPort=<int> -PserviceHost=<string> -PservicePort=<int> -PnodeName=<string>"
Notes: ensure that host and port values correlate to the corresponding nodes
```
```
For Client: run "gradle runClient2 -PserviceHost=<string> -PservicePort=<int> -PregistryHost=<string> -PgrpcPort=<int>"
Notes: ensure that host and port values correlate to the corresponding nodes 
``` 

## Requirements Met
###Task 1: Starting services locally (50 points)
1. (3 points) Must have: We need to be able to run the service node through "gradle
runNode" which should use default arguments, and the client through "gradle runClientJava" using the correct default values to connect to the started service node!!!!
If this does not work we will not run things.
2. (15 points each service) Implement 2 from the 4 services that are given in the .proto
files. Implement either calc or sort AND either tips or story. Read through
the Protobuf files and Readme for more details on how the services are
supposed to work.
3. (8 points) Your client should let the user decide what they want to do with some
nice terminal input easy to understand, e.g. first showing all the available services,
then asking the user which service they want to use, then asking for the input the
service needs. Good overall client that does not crash.
4. (4 points) Give the option that we can run "gradle runClient -Phost=host -Pport=port
-Pauto=1" which will run all requests on its own with input data you hardcode and
give good output results and also of course shows what was called. This will call the
server directly without using any registry. So basically shows your test cases running
successfully. See video about Task 1 for more details.
5. (5 points) Server and Client should be robust and not crash.
###Task 2: Creating a service (Golf service)
1. Service allows at least two different requests
2. Each request needs at least one input
3. Response returns different data for different requests
4. Response returns a repeated field
5. Data is held persistent on the server
###Task 3.1: Register locally
1. MUST: Create a new version of your Node.java ==> NodeService.java and your
EchoClient.java ==> Client.java. You should be able to call them through "gradle
registerServiceNode" and "gradle runClient2" asking for the same parameters as the
calls that were already in the given Gradle file for the original files. This call will use
the Registry again, so is not the same as runClient from the previous task. These
should use default values so that they connect correctly!
2. The things you commented in Node.java (The registry parts in the server and client
that are mentioned in the Background before Task1) should now be put back into the
code in your NodeService.java code. This should allow your Node to be registered
with its service with the Registry.
3. Test this: Run your Registry, run your node (you need to provide the correct host
and port of course) – you should set this as default values for us. You should see a
println on the Registry side that the service is registered. If you do not, try to figure
out what happened (or did not happen).
4. Now, you should run your Client (also with the parts included which you need to
uncomment now) and see if it will find the registered services correctly.
5. (15 points) If all this works, adapt your client so it does not just call the service on
the node you provide directly as was done in Task 1 but that the client can choose
between all services registered on the Registry (in this case locally it will still just be
your services. For testing purposes you can run a couple server nodes and register
all of them to your local registry. You do not hard code which server to talk to
anymore but use the following workflow:
a. Client contacts Registry to check for available services
b. List all registered services in the terminal and the client can choose one 
c. (You should basically have this already) Based on what the client chooses the
terminal should ask for input, eg. a new sentence, a sorting array or whatever
the request needs
d. The request should be sent to one of the available service nodes with the following workflow: 
d1. client should call the registry again and ask for a Server
providing the chosen service 
d2. Returned server should then be used
d3. Send the request to the server that was returned
d4. Return the response in a good way to the client
e. Make sure that your Client does not crash in case the Server did not respond
or crashed. Make it as robust as possible.



