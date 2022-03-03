
public class DoubleLinkedListNode<K> {
	//Class for the nodes of double linked list class
	//It is not an inner class to make traversing the list faster.
	//Normally to get the element after n/2 th element from a list at least half of the list must be traversed
	//But with this class if a node is accessible it's next node can be obtained on o(1) time
	//Making a much faster traverse of the list from start to finish
	private K content;
	private DoubleLinkedListNode<K> next, before;
	public DoubleLinkedListNode(K content){
		this.content = content;
	}
	public K getContent() {
		return content;
	}
	public void setContent(K content) {
		this.content = content;
	}
	public DoubleLinkedListNode<K> getNext() {
		return next;
	}
	public void setNext(DoubleLinkedListNode<K> next) {
		this.next = next;
	}
	public DoubleLinkedListNode<K> getBefore() {
		return before;
	}
	public void setBefore(DoubleLinkedListNode<K> before) {
		this.before = before;
	}
}
