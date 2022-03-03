
public class PriorityQueueNode<K> {
	//Node class for the priorityqueue class
	//It is not an inner class to make traversing the list faster
	private K content;
	private double priority;
	private PriorityQueueNode<K> next, before;//Nodes which are right next to and right before this node
	public PriorityQueueNode(K content, double priority){
		this.content = content;
		this.priority = priority;
		this.next = null;
		this.before = null;
	}
	public K getContent() {
		return content;
	}
	public void setContent(K content) {
		this.content = content;
	}
	public double getPriority() {
		return priority;
	}
	public void setPriority(double priority) {
		this.priority = priority;
	}
	public PriorityQueueNode<K> getNext() {
		return next;
	}
	public void setNext(PriorityQueueNode<K> next) {
		this.next = next;
	}
	public PriorityQueueNode<K> getBefore() {
		return before;
	}
	public void setBefore(PriorityQueueNode<K> before) {
		this.before = before;
	}
	
}
