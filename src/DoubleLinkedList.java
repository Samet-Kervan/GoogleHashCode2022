
public class DoubleLinkedList<K> {
	private DoubleLinkedListNode<K> head, rear;
	private int size;
	public DoubleLinkedList() {
		//Initial empty list
		this.head = null;
		this.size = 0;
	}
	public int getSize() {
		//Returns the number of elements currently on the list
		return size;
	}
	public void add(K content) {
		//Adds the given value to the list
		DoubleLinkedListNode<K> node = new DoubleLinkedListNode<K>(content);
		if (head == null) {
			//There is no entries in the list currently
			//So this is the first node on the list
			head = node;
			rear = node;
			size++;
		}
		else {
			//Adds the node to the end of the list and sets it as the new end
			rear.setNext(node);
			node.setBefore(rear);
			rear = node;
			size++;
		}
	}
	public K remove(int index){
		//Removes the node on the given index from the list and returns it's content
		DoubleLinkedListNode<K> node = get(index);
		if (node == null) {
			return null;
		}
		remove(node);
		return node.getContent();
	}
	public void remove(DoubleLinkedListNode<K> node){
		//Removes the given node from the list
		DoubleLinkedListNode<K> before = node.getBefore();
		DoubleLinkedListNode<K> next = node.getNext();
		if (next != null) {
			next.setBefore(before);
		}
		else {
			rear = before;
		}
		if (before != null) {
			before.setNext(next);
		}
		else {
			head = next;
		}
		if (size > 0) {
			size--;
		}
	}
	public DoubleLinkedListNode<K> getNext(DoubleLinkedListNode<K> node){
		//Returns the next node of the given node
		return node.getNext();
	}
	public DoubleLinkedListNode<K> getFirst(){
		//Returns the first element of the list
		return head;
	}
	public DoubleLinkedListNode<K> getLast(){
		//Returns the last element of the list
		return rear;
	}
	public DoubleLinkedListNode<K> get(int index){
		//Returns the element at the given index.
		//Index starts at zero.
		if (index >= size) {
			return null;
		}
		int count = 1;
		//Optimal way would be to start searching starting from the rear if the index is bigger than the
		//half of the size. That way searching will be faster.
		DoubleLinkedListNode<K> node = head;
		while(count <= index) {
			node = node.getNext();
			count++;
		}
		return node;
	}
}
