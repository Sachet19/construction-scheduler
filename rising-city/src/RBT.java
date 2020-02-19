// This class contains the implementation of the Red-Black tree data structure

import java.util.LinkedList;
import java.util.List;

public class RBT {

    public RBTNode nil; // Dummy node for initialization purposes
    public RBTNode root; //Root Node

    //Local Enumeration to hold values of the possible colors of the node
    public enum COLOR{ 
        RED, BLACK
    }

    //Constructor initializes the Red-Black tree
    public RBT(){ 
        nil = RBTNode.nil;
        root = nil;
        root.leftChild = nil;
        root.rightChild = nil;
    }

    //Finding a building in the Red-Black tree based on the building no
    public RBTNode find(int key){ 
        return find(root, key);
    }

    //Internal function used for finding the requested building. Called by the external find function
    private RBTNode find(RBTNode root, int key){ 
        if (root == nil){
            return null;
        }
        else if (root.id == key){
            	return root;
        	}
	        else if (key < root.id){
	            	return find(root.leftChild, key);
		        }
		        else{
		            return find(root.rightChild, key);
		        }
    }

    //Function to find all buildings whose building number falls in the range of the specified building numbers
    public List<RBTNode> findInRange(int key1, int key2){ 
        List<RBTNode> list = new LinkedList<RBTNode>();
        rangeFind(root, list, key1, key2);
        return list;
    }
    
    //Internal function used for finding all buildings in a specified range of building numbers. Called by the external range find function
    private void rangeFind(RBTNode root, List<RBTNode> list, int key1, int key2) { 
        if (root == nil) {
            return;
        }
        if (key1 < root.id) {
        	rangeFind(root.leftChild, list, key1, key2);
        }

        if (key1 <= root.id && key2 >= root.id) {
            list.add(root);
        }

        if (key2 > root.id) {
        	rangeFind(root.rightChild, list, key1, key2);
        }
    }

    
    //Function to add a new building into the red-black tree based on the building number
    public void insert(int key){ 
        RBTNode node = new RBTNode(key);
        insertNode(node);
    }

    //Internal function used for inserting a new building node into the red-black tree
    public void insertNode(RBTNode node){
        node.color = COLOR.RED;
        if (root == nil || root.id == node.id){
            root = node;
            root.color = COLOR.BLACK;
            root.parent = nil;
            return;
        }
        else
        {
        	internalInsert(root, node);
        	correctRBT(node);
        }
    }

    //Internal function used for inserting a new building. Called by the external insert function
    private void internalInsert(RBTNode root, RBTNode node){
        if (node.id < root.id){
            if (root.leftChild == nil){
                root.leftChild = node;
                node.parent = root;
            }
            else {
            	internalInsert(root.leftChild, node);
            }
        }
        else{
            if (root.rightChild == nil){
                root.rightChild = node;
                node.parent = root;
            }
            else {
            	internalInsert(root.rightChild, node);
            }
        }
    }

    //Method to correct the red-black tree after a new node has been inserted. It performs the necessary rotations and color changes required to ensure the maintenance of the properties of the Red-Black tree
    private void correctRBT(RBTNode node){
        RBTNode parentNode = nil;
        RBTNode grandParentNode = nil;
        
        
        //If the node is the root node, set the color to black
        if (node.id == root.id){
            node.color = COLOR.BLACK;
            return;
        }
        
        
        //Make color changes based on node color, placement, parent color etc. 
        while (node.color != COLOR.BLACK && node.parent.color == COLOR.RED){
        	
        	parentNode = node.parent;
        	grandParentNode = parentNode.parent;

        	// If node is a right child
        	if (parentNode == grandParentNode.rightChild){
                RBTNode grandParentLeftChild = grandParentNode.leftChild;
                if (grandParentLeftChild == nil || grandParentLeftChild.color == COLOR.BLACK){
                	
                    if (parentNode.leftChild == node){
                    	parentNode = rotateRight(parentNode);
                        node = parentNode.rightChild;
                    }
                    rotateLeft(grandParentNode);
                    
                    COLOR temp = parentNode.color;
                    parentNode.color = grandParentNode.color;
                    grandParentNode.color = temp;
                    
                    node = parentNode;
                	
                }
                else {
                    parentNode.color = COLOR.BLACK;
                    grandParentLeftChild.color = COLOR.BLACK;
                    node = grandParentNode;
                }
            }            	// If node is a left child        
            else if (parentNode == grandParentNode.leftChild){
                RBTNode grandParentRightChild = grandParentNode.rightChild;
                if (grandParentRightChild == nil || grandParentRightChild.color == COLOR.BLACK){

                    if (parentNode.rightChild == node){
                    	parentNode = rotateLeft(parentNode);
                        node = parentNode.leftChild;
                    }
                    rotateRight(grandParentNode);
                    
                    COLOR temp = parentNode.color;
                    parentNode.color = grandParentNode.color;
                    grandParentNode.color = temp;
                    
                    node = parentNode;
                	
                }
                else {                   
                    grandParentNode.color = COLOR.RED;
                    parentNode.color = COLOR.BLACK;
                    grandParentRightChild.color = COLOR.BLACK;
                    node = grandParentNode;
                }

            }
        }
        root.color = COLOR.BLACK;
    }
    
    
    //This function performs a right rotation around the input node in the red-black tree
    private RBTNode rotateRight(RBTNode node){

        RBTNode left = node.leftChild;
        RBTNode leftRight = left.rightChild;

        node.leftChild = leftRight;
        if(leftRight != nil){
        	leftRight.parent = node;
        }

        left.parent = node.parent;

        if (node.parent == nil){
            root = left;
        }
        else if (node == node.parent.rightChild){
        	node.parent.rightChild = left;

        }
        else {
        	node.parent.leftChild = left;

        }
        left.rightChild = node;
        node.parent = left;
        return left;
    }

    //This function performs a left rotation around the input node in the red-black tree
    private RBTNode rotateLeft(RBTNode node){
        RBTNode right = node.rightChild;
        RBTNode rightLeft = right.leftChild;

        node.rightChild = rightLeft;
        if(rightLeft != nil){
        	rightLeft.parent = node;
        }

        right.parent = node.parent;

        if (node.parent == nil){
            root = right;
        }
        else if (node == node.parent.rightChild){
        	node.parent.rightChild = right;
        }
        else {
        	node.parent.leftChild = right;
        }
        right.leftChild = node;
        node.parent = right;
        return right;
    }

    // This function deletes a node from the red-black tree
    public boolean deleteNode(RBTNode node){
        return deleteInternal(node.id);
    }

    
    // Internal delete function which contains the actual deletion logic
    public boolean deleteInternal(int buildingNo){
        RBTNode node = find(root,buildingNo);
        if (node == null){
            return Boolean.FALSE;
        }
        RBTNode swapNode;
        RBTNode temp = node;
        COLOR origColor = node.color;
        
        if (node.leftChild != nil && node.rightChild != nil){
        	
        	temp = getMin(node.rightChild);
            origColor = temp.color;
            swapNode = temp.rightChild;
            if (temp.parent == node) {
            	swapNode.parent = temp;
            }
            else {
            	replaceParent(temp, temp.rightChild);
                temp.rightChild = node.rightChild;
                temp.rightChild.parent = temp;
            }
            replaceParent(node, temp);
            temp.leftChild = node.leftChild;
            temp.leftChild.parent = temp;
            temp.color = node.color;
       	
        } else if (node.rightChild == nil){
        	swapNode = node.leftChild;
        	replaceParent(node, node.leftChild);
        } else {
        	swapNode = node.rightChild;
        	replaceParent(node, node.rightChild);
        }
        
                
        if (origColor == COLOR.BLACK) {
            fixRBTPostDelete(swapNode);
        }
        
        return Boolean.TRUE;
    }
    
    // Function to move a node up a level in place of its parent
    private void replaceParent(RBTNode parentNode, RBTNode node){
        if (parentNode.parent == nil){
            root = node;
        }
        else if ( parentNode == parentNode.parent.rightChild){
        	parentNode.parent.rightChild = node;
        }
        else {
        	parentNode.parent.leftChild = node;
        }
        
        node.parent = parentNode.parent;
    }
    
    //Function to find the node with the minimum key value in the red-black tree
    private RBTNode getMin(RBTNode root){
        while (root.leftChild != nil){
            root = root.leftChild;
        }
        return root;
    }

    //Function to correct the structure of the red-black tree in order to ensure that the red-black tree properties are maintained after a node has been deleted from the red-black tree
    private void fixRBTPostDelete(RBTNode node){

        while(node!=root && node.color == COLOR.BLACK){

        	//If deleted node was a right child
            if(node == node.parent.rightChild){
                
                RBTNode parentLeftChild = node.parent.leftChild;

                if(parentLeftChild.color == COLOR.RED){
                	parentLeftChild.color = COLOR.BLACK;
                    node.parent.color = COLOR.RED;
                    rotateRight(node.parent);
                    parentLeftChild = node.parent.leftChild;
                }               

                if(parentLeftChild.rightChild.color != COLOR.RED && parentLeftChild.leftChild.color != COLOR.RED){
                	parentLeftChild.color = COLOR.RED;
                    node = node.parent;
                    continue;
                }
                else if(parentLeftChild.leftChild.color != COLOR.RED){
                	parentLeftChild.rightChild.color = COLOR.BLACK;
                	parentLeftChild.color = COLOR.RED;
                    rotateLeft(parentLeftChild);
                    parentLeftChild = node.parent.leftChild;
                }
                
                if(parentLeftChild.leftChild.color == COLOR.RED){
                	parentLeftChild.color = node.parent.color;
                    node.parent.color = COLOR.BLACK;
                    parentLeftChild.leftChild.color = COLOR.BLACK;
                    rotateRight(node.parent);
                    node = root;
                }  
                          	

            } else //If deleted node was a left child
            	{            	            	
	            	RBTNode parentRightChild = node.parent.rightChild;
	
	                if(parentRightChild.color == COLOR.RED){
	                	parentRightChild.color = COLOR.BLACK;
	                    node.parent.color = COLOR.RED;
	                    rotateLeft(node.parent);
	                    parentRightChild = node.parent.rightChild;
	                }
	                
	                if(parentRightChild.leftChild.color != COLOR.RED && parentRightChild.rightChild.color != COLOR.RED){
	                	parentRightChild.color = COLOR.RED;
	                    node = node.parent;
	                    continue;
	                }
	                else if(parentRightChild.rightChild.color != COLOR.RED){
	                	parentRightChild.leftChild.color = COLOR.BLACK;
	                	parentRightChild.color = COLOR.RED;
	                    rotateRight(parentRightChild);
	                    parentRightChild = node.parent.rightChild;
	                }
	                
	                if(parentRightChild.rightChild.color == COLOR.RED){
	                	parentRightChild.color = node.parent.color;
	                    node.parent.color = COLOR.BLACK;
	                    parentRightChild.rightChild.color = COLOR.BLACK;
	                    rotateLeft(node.parent);
	                    node = root;
	                }
  
            }
        }
        node.color = COLOR.BLACK;
    }

}