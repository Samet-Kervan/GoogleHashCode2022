
public class PriorityQueue<K> {
	//A generic priority queue class. 
	private PriorityQueueNode<K> head, rear;//Storing both the head and rear for simpler executions
	private int size;//Number of elements currently on the list
	//Sorting is decided by ascending boolean.
	//True => ascending False => descending
	private boolean ascending;
	public PriorityQueue(boolean ascending) {
		head = null;
		rear = null;
		size = 0;
		this.ascending = ascending;
	}
	public PriorityQueueNode<K> getFirst() {
		return head;
	}
	public K getFirstContent() {
		//Returns the contents of the first node
		return head.getContent();
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public boolean isAscending() {
		return ascending;
	}
	public void add(K content, double priority) {
		//Creates a node with the given content and priority and adds it to the list
		PriorityQueueNode<K> node = new PriorityQueueNode<K>(content, priority);
		add(node);
	}
	public void add(PriorityQueueNode<K> node) {
		//Adds the given node to the list
		if (head == null) {
			//There is no element currently on the list
			head = node;
			rear = node;
			size++;
		}
		else {
			PriorityQueueNode<K> currentNode = head;
			while(currentNode != null) {
				if (currentNode.getPriority() < node.getPriority()) {
					if (currentNode == head) {
						head = node;
					}
					else {
						currentNode.getBefore().setNext(node);
						node.setBefore(currentNode.getBefore());
					}
					currentNode.setBefore(node);
					node.setNext(currentNode);
					break;
				}
				currentNode = currentNode.getNext();
			}
			if (currentNode == null) {
				rear.setNext(node);
				node.setBefore(rear);
				rear = node;
			}
			size++;
		}
	}
	public PriorityQueueNode<K> get(int index){
		if (index < size) {
			PriorityQueueNode<K> node = head;
			for (int i = 0; i < index; i++) {
				node = node.getNext();
			}
			return node;
		}
		return null;
	}
	public PriorityQueueNode<K> remove(int index){
		//Removes and returns the node in the given index from the list
		if (index < size) {
			//Checking if the index is valid
			PriorityQueueNode<K> node = head;
			for (int i = 0; i < index; i++) {
				//Searches and finds the node
				node = node.getNext();
			}
			remove(node);
			//Nodes before and next values are reset before returning
			node.setBefore(null);
			node.setNext(null);
			return node;
		}
		return null;
	}
	public void remove(PriorityQueueNode<K> node){
		//Removes the given node from the list
		PriorityQueueNode<K> before = node.getBefore();
		PriorityQueueNode<K> next = node.getNext();
		try {
			before.setNext(next);
		} catch (NullPointerException e) {
			head = next;
		}
		try {
			next.setBefore(before);
		} catch (NullPointerException e) {
			rear = before;
		}
		size--;
	}
	public PriorityQueueNode<K> removeFirst(){
		//Removes and returns the first node from the list
		return remove(0);
	}
	public void updatePriority(int index, double priority) {
		//Finds the node in the given index and updates its priority
		PriorityQueueNode<K> node = get(index);
		if (node != null) {
			updatePriority(node, priority);
		}
	}
	public void updatePriority(PriorityQueueNode<K> node, double priority) {
		//Updates the priority of the given node on the list
		remove(node);//Removes the node from the list
		add(node.getContent(), priority);//Using add function. Adds a new node with the old nodes content and new priority
	}
}
