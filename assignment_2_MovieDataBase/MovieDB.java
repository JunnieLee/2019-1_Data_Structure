import java.util.Iterator;
import java.util.NoSuchElementException;

// 알파벳 순서 고려해서 저장해두는거는
// MovieDB에 존재하는 GenreList에 새로운 Genre를 추가하는 AddGenre 메소드하고
// Genre node 각각이 가지고 있는 MovieList에 새로운 영화요소를 추가할때 사용하는 add 메소드
// 그때는 정렬된 상태가 유지되도록, 대소비교 연산을 진행한 뒤 적절한 자리에 들어간다!
// (정렬된 모습을 유지하도록 삽입되면, 삭제되더라도 동일하다. 따라서 delete에서 따로 정렬관련 처리를 해줄 필요는 없다)


/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {

	private MyLinkedList<Genre> GenreList;
	private int genre_num = 0;

	public MovieDB() {
		GenreList = new MyLinkedList<Genre>();
    }

	private void AddGenre(Genre new_genre){
		// Genre has to be sorted before/after addition
		Node<Genre> new_node = new Node<Genre>(new_genre);

		if (genre_num==0){
			GenreList.setFirst(new_node);
			genre_num++;
			return;
		}

		// (3-1.) 특수케이스 --> 맨 앞에 삽입해야 할 경우
		if (new_genre.compareTo(GenreList.first()) < 0){
			new_node.setNext(GenreList.first_node());
			GenreList.setFirst(new_node);
			genre_num++;
			return;
		}

		// 리스트가 비어있지 않다면
		Node<Genre> target_node = GenreList.head;

		while (target_node.getNext()!=null){

			if (new_genre.equals(target_node.getNext().getItem())){return;}  // (2. - 중복된 요소를 add하라고 한 경우)

			if (new_genre.compareTo(target_node.getNext().getItem()) < 0){ //이러면 target_node.getNext() 앞에 얘가 삽입되어야 하는 모습인거 같은데
				new_node.setNext(target_node.getNext());
				target_node.setNext(new_node);
				genre_num++;
				return;
			}
			target_node = target_node.getNext();
		}
		// 한바퀴 다 돌때까지 앞에 삽입해야 하는 애가 없었으면, 얘가 제일 큰거니까 맨 뒤에 넣어줘야겠징
		// 루프 끝나고 나선 target_node가 맨 뒤 노드니까 target_node뒤에!!
		target_node.setNext(new_node);
		genre_num++;
		return;
	}


	// Insert the item to the MovieDB
    public void insert(MovieDBItem item) {
		if (GenreList.isEmpty()){ // (1) initial insert
			AddGenre(new Genre(item.getGenre(), item.getTitle()));
			return;
		}
		// (2)
		for ( Genre existing_genre : GenreList){
			if (existing_genre.getItem().equals(item.getGenre())){ // 장르 이름이 같다면
				existing_genre.addMovieElement(item.getTitle());
				return;
			}
		} // 루프를 다 돌때까지 일치하는 장르이름이 나오지 않았다면
		AddGenre(new Genre(item.getGenre(), item.getTitle()));
    }


    public void delete(MovieDBItem item) { // Delete the item from the MovieDB while staying "sorted"
		for ( Genre existing_genre : GenreList){
			if (existing_genre.getItem().equals(item.getGenre())){ // 장르 이름이 같다면
				existing_genre.deleteMovieElement(item.getTitle());
				return;
			}
		} // 루프를 다 돌때까지 일치하는 장르이름이 나오지 않았다면 그냥 넘어가게 되겠지
    }

    public MyLinkedList<MovieDBItem> search(String term) {
		// 여기서 search term은 오로지 movie title에만 해당함. 따라서 MovieList차원에서 처리해주면 됨.
		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();

		for ( MovieDBItem movie : items()){
			if (movie.getTitle().contains(term)){
				results.add(movie);
			}
		}
        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();

		for (Genre genre : GenreList) {
			if (genre.getMovieList()!=null){
				for (String movie : genre.getMovieList()){
					results.add(new MovieDBItem(genre.getItem(), movie));
				}
			}
		}
    	return results;
    }

} // end of MovieDB class


class Genre extends Node<String> implements Comparable<Genre> { // 여기서는 알파벳 순서 따져줄 필요 없음.

	private MovieList my_movie_list;
	private int moive_num;

	public Genre(String genre_name, String first_movie) {
		super(genre_name);
		my_movie_list = new MovieList(first_movie);
		moive_num++;
	}

	// 내가 만들어준 메소드
	public MovieList getMovieList(){
		return my_movie_list;
	}


	// 내가 만들어준 메소드
	public void addMovieElement(String item){
		my_movie_list.add(item);
		moive_num++;
	}

	// 내가 만들어준 메소드
	public void deleteMovieElement(String item){
		my_movie_list.delete(item);
		moive_num--;
	}

	@Override
	public int compareTo(Genre o) {
		return getItem().compareTo(o.getItem());
	}

	// 얘 필요없는듯
//	@Override
//	public int hashCode() { // just randomly chose the value of prime and result
//		final int prime = 31;
//		int result = 17;
//		result = prime * result + ((this.getItem() == null) ? 0 : this.getItem().hashCode());
//		return result;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Genre other = (Genre) obj;
		if (this.getItem() == null) {
			if (other.getItem() != null)
				return false;
		} else if (!this.getItem().equals(other.getItem())) return false;

		return true;
	}

}


class MovieList extends MyLinkedList<String> implements ListInterface<String> {
	// 얘는 LinkedList이긴 한데 movie title이 string의 형태로 알파벳 순서에 맞춰 저장되어있다는 점 빼고는 특별한게 없음.

	public MovieList(String first_movie) {
		super();
		add(first_movie);
		numItems++;
	}

	public MovieList() {
		super();
	}

	@Override
	public void add(String item) {

		Node<String> new_node = new Node<String>(item);

		if (isEmpty()){ // (1.) 만약 비어있는 상태라면
			this.setFirst(new_node);
			numItems++;
			return;
		}

		// (3-1.) 특수케이스 --> 맨 앞에 삽입해야 할 경우
		if (item.compareTo(first()) < 0){
			new_node.setNext(first_node());
			setFirst(new_node);
			numItems++;
			return;
		}

		// 리스트가 비어있지 않다면
		Node<String> target_node = head;

		while (target_node.getNext()!=null){

			if (item.equals(target_node.getNext().getItem())){return;}  // (2. - 중복된 요소를 add하라고 한 경우)

			if (item.compareToIgnoreCase(target_node.getNext().getItem()) < 0){ //이러면 target_node.getNext() 앞에 얘가 삽입되어야 하는 모습
				new_node.setNext(target_node.getNext());
				target_node.setNext(new_node);
				numItems++;
				return;
			}

			target_node = target_node.getNext();
		}
		// 한바퀴 다 돌때까지 앞에 삽입해야 하는 애가 없었으면, 얘가 제일 큰거니까 맨 뒤에 넣어줘야겠징
		// 루프 끝나고 나선 target_node가 맨 뒤 노드니까 target_node뒤에!!
		target_node.setNext(new_node);
		numItems++;
		return;
		// 그럼, item은 target_node와 target_node.getNext() 사이에 삽입해줘야 한다.

	}


	@Override
	public boolean isEmpty() { return super.isEmpty(); }
	@Override
	public int size() { return super.size(); }
	@Override
	public void delete (String item) {
		super.delete(item);
	}
	@Override
	public String first() { return super.first(); }
	@Override
	public void removeAll() { super.removeAll(); }
}