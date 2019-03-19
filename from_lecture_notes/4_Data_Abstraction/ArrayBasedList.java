// ADT List Implementation with Array

public class ArrayBasedList {

    public interface ListInterface{ // Interface design
        public boolean isEmpty();
        public int size();

        public void add(int index, Object item)
            throws ListIndexOutOfBoundsException, ListException;

        public Object get(int index)
            throws ListIndexOutOfBoundsException;

        public void remove(int index)
            throws ListIndexOutOfBoundsException;

        public void removeAll();
    }


    public class ListArrayBased implements ListInterface{
        private final int MAX_LIST = 50;
        private Object items[];
        private int numItems; // # of items in the list

        public ListArrayBased(){ // default constructor
            items = new Object[MAX_LIST+1];
            numItems = 0;
        }

        public boolean isEmpty(){
            return (numItems==0);
        }

        public int size(){
            return numItems;
        }

        public void removeAll(){
            items = new Object[MAX_LIST+1]; // cut off the reference link and let the garbage collector work
            numItems = 0;
        }

        public void add(int index, Object item)
            throws ListIndexOutOfBoundsException {
                if (numItems>MAX_LIST){
                    // Exception Handling
                    System.out.println("Error: The list is full.");
                }
                if (index >= 1 && index <= numItems+1) {
                    for (int i = numItems; i >= index; i--){
                        items[i+1] = items[i]; // shift right and spare the seat for the new element
                    }
                    items[index] = item; // insert the item in the spared seat
                    numItems++; // one more item added
                } else {
                    // Exception Handling
                    System.out.println("Error: Please try again.");
                }
        }

        public Object get(int index)
            throws ListIndexOutOfBoundsException{
                if (index >= 1 && index <= numItems){
                    return items[index];
                } else { // index out of range
                    // Exception Handling
                    System.out.println("Error: Index ouf of range. Please try again.");
                }
        }

        public void remove(int index)
            throws ListIndexOutOfBoundsException{
                if (index >=1 && index <= numItems){
                    for (int i = index+1; i<= size(); i++){
                        items[i-1] = items[i]; // shift left
                    }
                    numItems--; // one deleted
                } else { // index out of range
                    // Exception Handling
                    System.out.println("Error: Index ouf of range. Please try again.");
                }
        }

    } // end class ListArrayBased

}
