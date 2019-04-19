import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<T> implements ListInterface<T> {
	// dummy head
	Node<T> head;
	int numItems;

	public MyLinkedList() {
		head = new Node<T>(null);
	}

    /**
     * {@code Iterable<T>}를 구현하여 iterator() 메소드를 제공하는 클래스의 인스턴스는
     * 다음과 같은 자바 for-each 문법의 혜택을 볼 수 있다.
     * 
     * <pre>
     *  for (T item: iterable) {
     *  	item.someMethod();
     *  }
     * </pre>
     * 
     * @see PrintCmd#apply(MovieDB)
     * @see SearchCmd#apply(MovieDB)
     * @see Iterable#iterator()
     */

    // 내가 만든 메소드 -- 첫번째 요소를 추가하기 위한거
    public void setFirst(Node<T> item){
    	head.setNext(item);
	}

	// 내가 만든 메소드
	public Node<T> first_node() {
		return head.getNext();
	}

    public final Iterator<T> iterator() {
    	return new MyLinkedListIterator<T>(this);
    }

	@Override
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	public T first() {
		return head.getNext().getItem();
	}

	@Override
	public void add(T item) { // stay sorted before/after adding --> maybe not available..
//		Node<T> item_place = head;
//		while (item.compare(item_place.getNext()) < 0) {
//			item_place = item_place.getNext();
//		}
//		item_place.insertNext(item);
		Node<T> last = head;
		while (last.getNext() != null) {
			last = last.getNext();
		}
		last.insertNext(item);
		numItems += 1;
	}

	@Override
	public void delete(T item) { //--> add할때 sorted되어있다면 remove해도 sorted된 상태

    	if (head.getNext()==null) return;

		Node<T> before_target_node = head;


		while (before_target_node.getNext()!=null) {
			if (before_target_node.getNext().getItem().equals(item)){
				before_target_node.removeNext();
				numItems -= 1;
				if (numItems==0) removeAll();
				return;
			}
			before_target_node = before_target_node.getNext();
		}
		return; // 아이템을 찾지못하고 루프가 다 돌았다면, 그냥 return해라
	}

	@Override
	public void removeAll() {
		head.setNext(null);
	}

} // end of MyLinkedList class

class MyLinkedListIterator<T> implements Iterator<T> {

	private MyLinkedList<T> list;
	private Node<T> curr;
	private Node<T> prev;

	public MyLinkedListIterator(MyLinkedList<T> list) {
		this.list = list;
		this.curr = list.head;
		this.prev = null;
	}

	// 내가 써준거
	public Node<T> getCurr(){
		return curr;
	}

	@Override
	public boolean hasNext() {
		return curr.getNext() != null;
	}

	@Override
	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		prev = curr;
		curr = curr.getNext();

		return curr.getItem();
	}

	@Override
	public void remove() {
		if (prev == null)
			throw new IllegalStateException("next() should be called first");
		if (curr == null)
			throw new NoSuchElementException();
		prev.removeNext();
		list.numItems -= 1;
		curr = prev;
		prev = null;
	}
} //  Done defining class MyLinkedListIterator<T>