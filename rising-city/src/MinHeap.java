// This class contains the implementation of the Min Heap data structure

public class MinHeap {

	public int heapSize = 0; //Size of the heap

	public HeapNode[] heapArray; // Array which stores the actual nodes

	public MinHeap() {
		heapArray = new HeapNode[1];
	}

	//Function to check if the heap is empty
	public boolean isEmpty() {

		if (heapSize == 0)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}

	//Function to insert an existing node into the min heap
	public void insert(HeapNode heapNode) {
		insertNode(heapNode);
	}

	//Internal insert function containing the insertion logic. Called by the two external insert functions
	private void insertNode(HeapNode heapNode) {
		if (heapSize > 0 && heapSize == heapArray.length) {
			expandHeap();
		}
		int i = heapSize;
		heapSize++;
		heapArray[i] = heapNode;

		while (i != 0 && getParentNode(i).key >= heapArray[i].key) {

			if (getParentNode(i).key == heapArray[i].key) {

				if (getParentNode(i).rbNode.id > heapArray[i].rbNode.id) {
										
					int parentIndex = (i - 1) / 2;
					swap(i, parentIndex);
					i = parentIndex;
				}
				else
				{
					break;
				}

			} else {
				int parentIndex = (i - 1) / 2;
				swap(i, parentIndex);
				i = parentIndex;
			}
		}

	}

	//Function which returns the parent of a specified node
	private HeapNode getParentNode(int index) {
		int parentIndex = (index - 1) / 2;
		return heapArray[parentIndex];
	}

	//Function which swaps two nodes in the min heap
	private void swap(int index1, int index2) {
		HeapNode temp = heapArray[index1];
		heapArray[index1] = heapArray[index2];
		heapArray[index2] = temp;
	}

	//Function which doubles the size of the heap when it becomes full
	private void expandHeap() {
		HeapNode[] tempHeap = new HeapNode[heapArray.length * 2];
		System.arraycopy(heapArray, 0, tempHeap, 0, heapArray.length);
		heapArray = tempHeap;
	}

	//Function to ensure that the min heap properties are maintained after modifications are made to the heap
	private void heapify(int index) {
		int left = index * 2 + 1;
		int right = index * 2 + 2;

		int smallest = index;

		if (left < heapSize && heapArray[left].key <= heapArray[index].key) {
			
			if(heapArray[left].key == heapArray[index].key)
			{
				if(heapArray[left].rbNode.id < heapArray[index].rbNode.id)
					smallest = left;
			}
			else 
				smallest = left;

		}
		if (right < heapSize && heapArray[right].key <= heapArray[smallest].key) {
			
			
			if(heapArray[right].key == heapArray[smallest].key)
			{
				if(heapArray[right].rbNode.id < heapArray[smallest].rbNode.id)
					smallest = right;
			}
			else 
				smallest = right;
		}
		if (smallest != index) {
			swap(index, smallest);
			heapify(smallest);
		}
	}

	//Function to extract the minimum key value node of the min he
	public HeapNode extractMin() {

		if (heapSize == 1) {
			HeapNode min = heapArray[0];
			heapSize--;
			heapArray[0] = null;
			return min;
		} else {
			HeapNode min = heapArray[0];
			heapArray[0] = heapArray[heapSize - 1];
			heapArray[heapSize - 1] = null;
			heapSize--;
			heapify(0);

			return min;
		}
	}

}
