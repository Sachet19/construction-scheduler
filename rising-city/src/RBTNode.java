// This class describes the structure of a node of the Red-Black Tree

public class RBTNode {

        public static final RBTNode nil = new RBTNode(-9999, RBT.COLOR.BLACK); //Structure of node at time of initialization. An alternative to null value 

        public int id; // A unique number assigned to each building
        public int timeNeeded; // Total time needed for the construction of the building to be completed
        
        public RBTNode leftChild = nil; //Left child of the node
        public RBTNode rightChild = nil; // Right child of the node
        public RBTNode parent = nil; // Parent of the node
        
        public RBT.COLOR color; // Color of the node
        
        public HeapNode heapPointer; // Pointer to the corresponding node in the Min-Heap data structure

        public RBTNode(int id){
            this.id = id;
        }

        public RBTNode(int id, RBT.COLOR color){
            this.id = id;
            this.color = color;
        }

    }