
import java.util.LinkedList;
public class PriorityQueue<K> {
	//A generic priority queue class. Min priority, if an elements priority is bigger it will be in end.
	private LinkedList<PriorityQueue<K>.Element<K>> queue;
	//Start of nested class
	private class Element<K>{
		private K content;
		private float priority;
		public Element(K content, float priority) {
			this.content = content;
			this.priority = priority;
		}
		K getContent() {
			return content;
		}
		void setContent(K content) {
			this.content = content;
		}
		float getPriority() {
			return priority;
		}
		void setPriority(float priority) {
			this.priority = priority;
		}
	}
	//End of nested class
	public PriorityQueue() {
		queue = new LinkedList<Element<K>>();
	}
	public int size() {
		//Returns the number of elements in the queue
		return queue.size();
	}
	public void add(K k, float priority) {
		//Adds the given object to the list
		Element<K> element = new Element<K>(k, priority);
		if (queue.isEmpty()) {
			queue.add(element);
		}
		else {
			boolean flag = false;
			for (int i = 0; i < queue.size(); i++) {
				//Tries to find a appropriate position for the given object based on its priority 
				if (queue.get(i).getPriority() < element.getPriority()) {
					queue.add(i,element);
					flag = true;
					break;
				}
			}
			if (!flag) {
				//if the given objects priority is bigger than the entire queue 
				queue.add(element);
			}
		}
	}
	public K get(int index) {
		//Returns the contents of the given index. Does not removes.
		if (queue.isEmpty()) {
			return null;
		}
		if (index >= queue.size()) {
			return null;
		}
		return queue.get(index).getContent();
	}
	public K getFirst() {
		//Removes and returns the content of the first index
		if (queue.isEmpty()) {
			return null;
		}
		return queue.remove(0).getContent();
	}
	public K remove(int index) {
		//Removes and returns the content of the given index
		if (queue.isEmpty()) {
			return null;
		}
		if (index >= queue.size()) {
			return null;
		}
		return queue.remove(index).getContent();
	}
	public float getPriority(int index) {
		//Returns the priority of the element in the given index
		if (queue.isEmpty()) {
			return -1;
		}
		if (index >= queue.size()) {
			return -1;
		}
		return queue.get(index).getPriority();
	}
	public float getPriorityFirst() {
		//Returns the priority of the first element in the queue
		if (queue.isEmpty()) {
			return Integer.MAX_VALUE;
		}
		return queue.get(0).getPriority();
	}
	public void updatePriority(K content, int newPriority) {
		//Updates the nodes priority with the given content
		if (!queue.isEmpty()) {
			for (int i = 0; i < queue.size(); i++) {
				Element<K> element  = queue.get(i);
				if (element.getContent() == content) {
					//Found the node
					element = queue.remove(i);//Removing the element from queue to add again
					add(content, newPriority);//Adding a new node to the list with the content and the updated priority
					break;
				}
			}
		}
	}
}
