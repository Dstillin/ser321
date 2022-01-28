# ser321-fall2021-B-gtsimpso
SER-321:  Assignment 5

### Activity 2:Two-Phase Commit
The purpose of this program is to highlight a successful transaction using the two-phase commit protocol. This is achieved when the client requests to add a new string to a list managed by the Transaction Coordinator (TC) class.  Requests and responses are in the form of JSON and passed to and from all sources and destinations (communication protocol listed below). Once the request is received by the TC it deploys the two-phase commit protocol to node1 and node2 for approval. Phase one of the two-phase commit is implemented by the TC atomically sending a "PREPARE" request followed by both nodes responding with "READY" response.  Phase two is implemented by the TC atomically sending a "VOTE_TO_COMMIT" request and both nodes responding with a "COMMITTED" response.  Once both phases are completed then the TC can finally add the Clients string to the existing list.  It's important to note that the prior to the TC sending and receiving messages it checks the state of both nodes.  If a node fails the TC aborts the current transaction and sends and appropriate response to the client.


### Communication Protocol
#### Client Requests

	quit = request.put("message", "quit")

	display = request.put("message", "display")

	add(String newTip) = request.put("message", "add")
	                     request.put("data", newTip)
#### Transaction Coordinator Client Responses

	add(String strToAdd) = response.put("message", "add")
                           response.put("data", strToAdd)

	error(String error) = response.put("message", "error")
                          response.put("data", error)

	display(StringList list) = response.put("message", "display")
                               response.put("data", list.displayList())
			       
#### Transaction Coordinator Requests

	prepare = request.put("message", "PREPARE")

	vote_to_commit = request.put("message", "VOTE_TO_COMMIT")

	abort = request.put("message", "ABORT")
#### Node Responses

	ready = response.put("message", "READY")

	not_ready = response.put("message", "NOT_READY")

	committed = response.put("message", "COMMITTED")

	not_committed = response.put("message", "NOT_COMMITTED")

	aborted = response.put("message", "ABORTED")
	
### How to run the program
Please use the following commands in the sequence that listed below.  It's important to note that console=plain does not need to be included because it's included within the gradle.properties file
#### Run in Terminal with default values
```
For Node1: run "gradle node1" 
Defaults include port=8001 id=1
```
```   
For Node2: run "gradle node2"
Defaults include port=8002 id=2
``` 
```   
For TC: run "gradle TC"
Defaults include port=8000 leftport=8001 rightport=8002 lefthost=localhost righthost=localhost
``` 
```   
For Client: run "gradle Client"
Defaults include port=8000 host=localhost
``` 

#### Run in Terminal with Custom Values
```
For Node1: run "gradle node1 -Pport=<value> -Pid=<value>" 
Notes: id is used to help identify messages found in the console view. The data is trivial.
```
```   
For Node2: run "gradle node2 -Pport=<value> -Pid=<value>" 
Notes: id is used to help identify messages found in the console view. The data is trivial.
``` 
```   
For TC: run "gradle TC -Pport=<value> -Pleftport=<value> -Prightport=<value> -Plefthost=<string> -Prighthost=<string>"
Notes: Ensure the the left and right values correspond to the ports entered in the gradle node1/2 ports
``` 
```   
For Client: run "gradle Client -Pport=<value> -Phost=<string>"
Notes: Ensure the port and host entered here correspond to the Transaction Coordinator values
``` 



