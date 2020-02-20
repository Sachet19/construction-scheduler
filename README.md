# Construction Scheduler

Wayne Enterprises is developing a new city. They are constructing many buildings and plan
to use software to keep track of all buildings under construction in this new city. A building
record has the following fields:
buildingNum: unique integer identifier for each building.
executed_time: total number of days spent so far on this building.
total_time: the total number of days needed to complete the construction of the building.

## Requirements

1. Print (buildingNum) prints the triplet buildingNume,executed_time,total_time.
2. Print (buildingNum1, buildingNum2) prints all triplets bn, executed_tims, total_time for
which buildingNum1 <= bn <= buildingNum2.
3. Insert (buildingNum,total_time) where buildingNum is different from existing building
numbers and executed_time = 0.
Wayne Construction works on one building at a time. When it is time to select a building to
work on, the building with the lowest executed_time (ties are broken by selecting the building
with the lowest buildingNum) is selected. The selected building is worked on until complete or
for 5 days, whichever happens first. If the building completes during this period its number and
day of completion is output and it is removed from the data structures. Otherwise, the building’s
executed_time is updated. In both cases, Wayne Construction selects the next building to work
on using the selection rule. When no building remains, the completion date of the new city is
output.

Input test data will be given in the following format:
Insert(buildingNum,total_time)
PrintJob(buildingNum)
PrintJob(buildingNum1,buildingNum2)


## Implementation

Following data structures are implemented as part of this project to build the scheduler:
1. Red-Black Tree: Each node in the red black tree represents a building. Each building has
a unique building number associated with it and this building number serves as the key
value for each node in the tree. Since values are unique, the complexity of searching for
any building is O(log n) . Each node has pointers to maintain a relationship with its
children and parent nodes and also stores information about how much total time is
required in order to complete the construction of a building.
2. Min Heap: Each node in the min heap represents a building. The time already spent on
constructing a particular building is stored in the node corresponding to the building in
the min heap. This serves as the key value for each node in the min heap. Since the
structure is a min heap, it also efficient extraction of the building with the least time spet
on it.

When a new building is to be inserted, it is added to both the red-black tree and the min heap. A
pointer variable is kept in each data structure to allow a quick reference to the corresponding
node of the same building in the other data structure.
A global timer is maintained to ensure sequential and timely execution of input commands. The
timer value is updated as operations are executed.
To select a building for construction, we use the extract min functionality of the min heap to
identify the building which has had the least amount of time spent on it. In case there are
multiple buildings with the same time, a secondary check is made for the building number and
the building with the lowest building number is selected. Once the building has finished
construction, or 5 units of time have passed, whichever comes first, a new building is selected for
construction. The commands from the input file are processed as the global time matches the
time of the corresponding command.
