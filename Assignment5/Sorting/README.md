# ser321-fall2021-B-gtsimpso
SER-321:  Assignment 5

## Activity 1: Task 1                                                                                       
1. Explain the main structure of this code and the advantages and disadvantages of the setup of the distributed algorithm.

   * Structure: The distributed system structure resembles a tree structure.  The Branch class acts as the parent node while the two instances of the Sorter class act as the leaf nodes.  In this structure the parent node (Branch class) takes on the responsibility of distributing the work to the leaf nodes (Sorter class).  It's important to note that each parent node must contain two leaf nodes.  The leafs nodes can be a combination of either a Sort or Branch class. 
   * Advantages: A major advantage that this structure has is its scalability.  This means that larger data sets can be handled while preserving node resources. 
   * Disadvantages: Reliability is a disadvantage of using a tree structure.  Given that networks are unreliable, redundancy and node failure must be handled appropriately.
   
2. Run the code with different arrays to sort (different sizes, numbers etc.) and include code to measure the time (you can just enter start and end times). In your Readme describe your experiments and your analyses of them. E.g. why is the result as it is? Does the distribution help? Why, why not? See this as setting up your own experiment and give me a good description and evaluation.

###Experiment:
To understand the distributed system's performance, I ran ten test cases using the "MergeSort" class.  Each test randomly generates integer values within the range of zero to 1000.  The array size for each test would double in size from the previous set.  The initial array contains 500 integers, while the last and most extensive collection would include 128000.  Each test case includes a timer that records the total time to complete the sort.  The purpose of this experiment was to gain some insight into the overall system's performance as the array size increases.  I created the "LocalSorter" class to simulate the array processing locally.  The "LocalSorter" is a merge sorter that utilizes the same range and size of data.

####MergeSort class Results:
Size = 250       |    Test 0 completion time = 0.2583581
Size = 500       |    Test 1 completion time = 0.3914726
Size = 1000     |    Test 2 completion time = 0.7663992
Size = 2000     |    Test 3 completion time = 1.4060292
Size = 4000     |    Test 4 completion time = 2.7449845
Size = 8000     |    Test 5 completion time = 5.4346051
Size = 16000   |    Test 6 completion time = 10.7798445
Size = 32000   |    Test 7 completion time = 21.5123471
Size = 64000   |    Test 8 completion time = 42.8922081
Size = 128000 |    Test 9 completion time = 86.3626807  

####LocalSort class Results:
Size = 250	    |		Test 0 completion time = 0.0016109
Size = 500	    |		Test 1 completion time = 0.0010338
Size = 1000	    |		Test 2 completion time = 0.0032949
Size = 2000    	|		Test 3 completion time = 0.012339
Size = 4000    	|		Test 4 completion time = 0.0527894
Size = 8000	    |		Test 5 completion time = 0.0752446
Size = 16000   	|		Test 6 completion time = 0.2630321
Size = 32000   	|		Test 7 completion time = 1.0750476
Size = 64000   	|		Test 8 completion time = 4.6733537
Size = 128000	|		Test 9 completion time = 13.3065717

###Analysis:
The results provide us insight into how to scale larger array sizes. After analyzing the results, I believe that it's safe to say that as the array size doubles, so will the completion time. Waiting for a minute and thirty seconds isn't too long to sort 128000 numbers; however, these metrics might not fit user requirements. Depending on the constraints, one could say that after an array size of X, the system would need a new branch and associated nodes to meet the non-functional restrictions. If the goal of this system were to improve performance, it would hinder the performance rather than improve it. The LocalSort class results show that using the distributed system would not be optimal if performance was the goal. However, the distributed system could help with node resources with larger data sets. With that said, the distributed system could help with node resources with large data sets. On the other hand, the distributed system lacks performance due to constant communication between nodes.

3. Experiment with the "tree" setup, what happens with more or less nodes when sorting the same array and different arrays? When does the distribution make things better? Does it ever make things faster? As in the previous step experiment and describe your experiment and your results in detail.

###Experiment:
In this experiment, I decided to set up two configurations using a combination of the branch and sorter classes. One system configuration included two branch classes and three instances of the sorter class. The other configuration contained three branches and four sorters. Both structures utilized the same method to generate the arrays as in the previous experiment. I also used the same approach to determine the processing time for each test case per configuration.

####2 Branch 3 Sorter setup results:
Test 0 completion time = 0.7413174
Test 1 completion time = 1.0368642
Test 2 completion time = 1.8747408
Test 3 completion time = 3.1242697
Test 4 completion time = 5.821306
Test 5 completion time = 11.3708122
Test 6 completion time = 22.226633
Test 7 completion time = 44.5613409
Test 8 completion time = 90.1487529
Test 9 completion time = 185.1660509

####3 Branch 4 Sorter setup results:
Test 0 completion time = 1.1583454
Test 1 completion time = 1.4239591
Test 2 completion time = 2.4555887
Test 3 completion time = 4.1963236
Test 4 completion time = 7.9178064
Test 5 completion time = 16.7949588
Test 6 completion time = 31.4570298
Test 7 completion time = 79.8377261
Test 8 completion time = 131.807053

###Analysis:
During The experiment, I noticed that the processing time for each test case increased when I added more nodes to the system. The processing time increased as I said more parent nodes. The results show that the processing time grew for each test case when the system got larger. I did not witness any added benefit from the distributed system.

5. Explain the traﬃc that you see on Wireshark. How much traﬃc is generated with this setup and do you see a way to reduce it?
Between the three ports about 100 tcp packets are generated for one value to be processed.  One possible solution to reduce the traffic is to use another node to contain the results and send to the client.

## Activity 1: Task 2
