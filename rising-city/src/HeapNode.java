//This class describes the structure of a node of the Min Tree 

public class HeapNode {

        public int key; // Key value of each node. It is the time spent constructing the building
        
        public RBTNode rbNode; // Pointer to the corresponding node in the Red-Black tree data structure

        public HeapNode(int k){
            this.key = k;
        }

    }
