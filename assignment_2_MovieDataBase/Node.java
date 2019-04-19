public class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T obj) { // constructor
        this.item = obj;
        this.next = null;
    }
    
    public Node(T obj, Node<T> next) {
    	this.item = obj;
    	this.next = next;
    }

    public final T getItem() {
    	return item;
    }
    
    public final void setItem(T item) {
    	this.item = item;
    }
    
    public final void setNext(Node<T> next) {
    	this.next = next;
    }
    
    public Node<T> getNext() {
    	return this.next;
    }

    public final void insertNext(T obj) {
        Node<T> inserted_node = new Node<T>(obj); // create a node to insert

        if (this.next==null) { // when inserting next to the last node
            inserted_node.setNext(null); // this is going to be the new last node
        } else { inserted_node.setNext(this.next); }// else, the inserted node is going to take over current node's next

		this.setNext(inserted_node);
    }


    public final void removeNext() {
        if (this.next==null){
            return;
        } // removeNext() is available only when this.next exists
		this.setNext(this.next.getNext());
    }
}