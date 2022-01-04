# Assignment 4 Activity 1
## Description
The initial Performer code only has one function for adding strings to an array: 

## Protocol

### Requests
request: { "selected": <int: 1=add, 2=remove, 3=display, 4=count, 5=reverse,
0=quit>, "data": <thing to send>}

add: data <string> | add string to list\
remove: data <int> | remove string from list\
display: no data | displays all elements in the list\
count: no data | counts the number of elements in the list\
reverse: data <int> | reverses a string in the list\
quit: no data

### Responses

success response: {"type": <"add",
"remove", "display", "count", "reverse", "quit"> "data": <thing to return> }\

type <String>: echoes original selected from request (e.g. if request was "add", type will be "add")
data <string>: add = new list\ 
remove = removed element\
display = current list\ 
count = num elements\
reverse = new list\

error response: {"type": "error", "message"": <error string> }
Should give good error message if something goes wrong which explains what went wrong

## How to run the program
### Terminal
Task 1, please use the following commands:
```
    For Server, run "gradle runTask1 -Pport=9099 -q --console=plain"
```
```   
    For Client, run "gradle runClient -Phost=localhost -Pport=9099 -q --console=plain"
``` 

Task 2, please use the following commands:
```
    For Server, run "gradle runTask2 -Pport=9099 -q --console=plain"
```
```   
    For Client, run "gradle runClient -Phost=localhost -Pport=9099 -q --console=plain"
```

Task 3, please use the following commands:
```
    For Server, run "gradle runTask3 -Pport=9099 -Pthreadsize=20 -q --console=plain"
```
```   
    For Client, run "gradle runClient -Phost=localhost -Pport=9099 -q --console=plain"
```
##Requirements Met
###Task 1
Functionality to remove an element from the list.\
Functionality to display content of the list.\
Functionality to count the number of elements in the list.\
Functionality to reverse an elements order.
###Task 2
Functionality for unbounded multi-threaded server with no client blocking.\
Shared data is managed via synchronous methods.
###Task 3
Functionality for bounded (n) multi-threaded server.