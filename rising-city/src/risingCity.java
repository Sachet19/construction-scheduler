// The primary class which gets executed to schedule the construction of the various buildings. It reads the specified input command file and generates the required outpur file

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class risingCity {

	private RBT tree = new RBT(); //Object containing the red-black tree
	
	private MinHeap heap = new MinHeap(); //Object containing the min heap
	
	private FileWriter fileWriter; //Object used to write into the outpur file
	
	private HeapNode underConstructionBuilding = null; //Object holding the node representing the building currently under construction

	private int globalTime = 0; //Variable that maintains the global time
	private int timeForConstructionPeriodEnd = 0; //Variable to keep track of each construction period
	private int timeForCurrentBuildingCompletion = 0; // Variable that keeps track of the time needed to complete the construction of the under construction building

	private static String FILE_INPUT;
	private static final String FILE_OUTPUT = "output_file.txt";

	public static void main(String[] args) {

		FILE_INPUT = args[0]; // Read name of input file from command line arguments
		risingCity constructCity = new risingCity();
		constructCity.begin();

	}

	private void begin() {
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;

		try {

			String sCurrentLine;
			String[] params;

			String filepath = new File(FILE_INPUT).getAbsolutePath();  //Reading the input file
			fileReader = new FileReader(new File(filepath));
			bufferedReader = new BufferedReader(fileReader); 

			fileWriter = new FileWriter(FILE_OUTPUT);

			while ((sCurrentLine = bufferedReader.readLine()) != null) {

				Pattern pattern = Pattern.compile("(^\\d+): ([a-zA-Z]+)\\((.+)\\)"); // Regular expression to decode the commands in each line of the input file
				Matcher matcher = pattern.matcher(sCurrentLine);

				if (matcher.find()) {
					params = matcher.group(3).split(",");
					int cmdExecTime = Integer.parseInt(matcher.group(1));

					while (cmdExecTime != globalTime) {
						updateConstruction();
						incrementTime();
					}

					switch (matcher.group(2)) {
					case "Insert": {  // Processing an insert command
						
						int buildingNo = Integer.parseInt(params[0]);
						int totalConstructionTime = Integer.parseInt(params[1]);
						
						insertNewBuilding(buildingNo, totalConstructionTime);
						break;
					}
					case "PrintBuilding": { // Processing the two variations of the print command
						
						if(params.length == 1)
						{
							//Single building print
							int buildingNo = Integer.parseInt(params[0]);
							printSingleBuilding(buildingNo);
						}
						else
						{
							//Range print
							int buildingNo1 = Integer.parseInt(params[0]);
							int buildingNo2 = Integer.parseInt(params[1]);
							printBuildingsInRange(buildingNo1, buildingNo2);
						}
						
						break;
					}
					}
				}
				updateConstruction();
				incrementTime();
			}
			completeConstruction();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.close();
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//Function to insert a new building into the construction schedule by inserting it into both the min heap and the red black tree. It also establishes the link between the two nodesg 
	private void insertNewBuilding(int buildingNo, int totalConstructionTime) {

		RBTNode rbNode = new RBTNode(buildingNo);
		rbNode.timeNeeded = totalConstructionTime;
		HeapNode heapNode = new HeapNode(0);
		rbNode.heapPointer = heapNode;
		heapNode.rbNode = rbNode;
		tree.insertNode(rbNode);
		heap.insert(heapNode);
	}

	//Function to print the details of a single building based on the building number
	private void printSingleBuilding(int buildingNo) throws IOException {

		RBTNode rbNode = tree.find(buildingNo);
		if (rbNode == null) {
			printNonExistingBuilding();
		} else {
			printBuildingInformation(rbNode);
		}
	} 
		
	//Function to print the buildings falling in the range of the specified building numbers	
	private void printBuildingsInRange(int buildingNo1, int buildingNo2) throws IOException {
		
		List<RBTNode> list = tree.findInRange(buildingNo1, buildingNo2);

		if (!list.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (RBTNode node : list) {
				sb.append("(" + node.id + "," + node.heapPointer.key + "," + node.timeNeeded + "),");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("\n");
			fileWriter.write(sb.toString());
		} else {
			fileWriter.write("(0,0,0)\n");
		}
		
	}

	//Function to print output when requested building does not exist
	private void printNonExistingBuilding() throws IOException {
		fileWriter.write("(0,0,0)\n");
	}

	//Function to specify the format of the the print output and write the data to the outpur file
	private void printBuildingInformation(RBTNode node) throws IOException {
		if (node.id == underConstructionBuilding.rbNode.id) {
			fileWriter.write("(" + node.id + "," + underConstructionBuilding.key + "," + node.timeNeeded + ")\n");
		} else {
			fileWriter.write("(" + node.id + "," + node.heapPointer.key + "," + node.timeNeeded + ")\n");
		}
	}

	//Function which selects a building for construction based on time spent on the building and the building number. Buildings that finish completion are removed from the data structures 
	private void updateConstruction() throws IOException {
		if (underConstructionBuilding == null) {
			if (heap.isEmpty()) {
				return;
			} else {
				underConstructionBuilding = heap.extractMin();
				timeForConstructionPeriodEnd = globalTime + 5;
				timeForCurrentBuildingCompletion = globalTime + underConstructionBuilding.rbNode.timeNeeded - underConstructionBuilding.key;
			}
		} else {
			if (timeForCurrentBuildingCompletion <= timeForConstructionPeriodEnd) {
				if (globalTime == timeForCurrentBuildingCompletion) {
					printCompletedBuilding(underConstructionBuilding.rbNode.id);
					tree.deleteNode(underConstructionBuilding.rbNode);
					underConstructionBuilding = null;
					timeForConstructionPeriodEnd = 0;
					timeForCurrentBuildingCompletion = 0;
					updateConstruction();
				}
			} else {
				if (globalTime == timeForConstructionPeriodEnd) {
					heap.insert(underConstructionBuilding);
					underConstructionBuilding = null;
					timeForConstructionPeriodEnd = 0;
					timeForCurrentBuildingCompletion = 0;
					updateConstruction();

				}
			}
		}
	}

	//Function that prints the building number and the time of completion of a completed building
	private void printCompletedBuilding(int buildingNo) throws IOException {
		RBTNode rbNode = tree.find(buildingNo);
		if (rbNode == null) {
			fileWriter.write("(0,0,0)\n");

		} else {
			fileWriter.write("(" + rbNode.id + "," + globalTime + ")\n");

		}
	}

	//Function which advances the global timer
	private void incrementTime() {
		globalTime++;
		if (underConstructionBuilding != null) {
			underConstructionBuilding.key++;
		}
	}

	//Function to complete all unfinished buildings after all commands from the input file have been processed
	private void completeConstruction() throws IOException {
		while (underConstructionBuilding != null) {
			updateConstruction();
			incrementTime();
		}
	}

}
