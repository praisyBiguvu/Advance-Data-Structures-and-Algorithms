// PRAISY BIGUVU

//Alchemy
/* 

Create IDs for each vertices
Create a adjacency list for the graph path //representation of graph
Dijkstra algorithm 

*/


import java.util.*;

public class cmsc401 {
    private static final Scanner scanner = new Scanner(System.in);

    // Please use these methods to take inputs and write outputs.
    private static Integer readInt() {
        return scanner.nextInt();
    }

    private static String readString() {
        return scanner.next();
    }

    private static Integer[] readIntegerArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = readInt();
        }
        return array;
    }

    private static void printInt(int a) {
        System.out.println(a);
    }

    private static void printString(String s) {
        System.out.println(s);
    }



    public static void main(String[] args) {

//        // reading an Integer
//        Integer a = readInt();
//        // writing int output
//        printInt(a);
//
//        // reading a String
//        String s = readString();
//        // writing string output
//        printString(s);
//
//        // reading an Integer Array (you should provide the size)
//        Integer[] listOfIntegers = readIntegerArray(5);

        // --------------------------------------- CREATING THE GRAPH -------------------------------------------//

        int end;

        int available_id=1;
        int n= readInt();

        /* create the id for each vertices and initialize them with a big number */
        List<Integer> vertex_init = new ArrayList<Integer>(); //create id for each vertices
        int j=0;
        for (j=0; j<100; j++){
            vertex_init.add(j, 100000000);
        }
        
        /* representation of the graph is stored using adjacency list of minheaps */
        MinHeap [] vertex_graph= new MinHeap[n*2];

        /*for every line of input create IDs for vertices and add the paths to graph
        
        so 47(silver) is always the 0 regardless of when it gets introduced in the input. */
        vertex_init.set(47, 0);
        vertex_graph[vertex_init.get(47)]= new MinHeap(n*2);

        /*for every line of input create IDs for vertices and add the paths to graph*/
        int i=0;
        while(i<n){
            int s= readInt();
            int e=readInt();
            int cost=readInt();

            //create ID and initialize a minheap list in the vertix_graph 
            if(vertex_init.get(s)==100000000){
                vertex_init.set(s, available_id);
                available_id++;

                vertex_graph[vertex_init.get(s)]= new MinHeap(n*2);

            }
            if(vertex_init.get(e)==100000000){
                vertex_init.set(e, available_id);
                available_id++;
                vertex_graph[vertex_init.get(e)]= new MinHeap(n*2);

            }
            
            /* add the path to the vertex_graph[certain index] minheap list */
            int index_s= vertex_init.get(s);
            int index_e=vertex_init.get(e);
            vertex_graph[index_s].insert(index_e, cost);
            i++;
        }

        
        end=vertex_init.get(79); // the id that representes the 79 (Gold)

        
       // --------------------------------------- DIJKSTRA ALGORITHM -------------------------------------- //

        int size=available_id;

        int [] shortest_path= new int[size]; //to keep track of optimal path to each node from Silver-47(0 Node)
        for (int l=0; l<size; l++){
            shortest_path[l]=1000000; //initialize with big number
        }        

        shortest_path[0]=0; //cost from silver to silver is 0. 

        HashMap<Integer, Integer> visited_indexes = new HashMap<Integer, Integer>(); //stores the visited vertices. 
        int selected_vertex=0;
        int prev_cost= shortest_path[selected_vertex];

        while(visited_indexes.size()<size){
            MinHeap temp= new MinHeap(vertex_graph[selected_vertex].currentSize);
            temp= vertex_graph[selected_vertex];

            /* Relaxation -- updates the shortest_path routes if the new cost is less than the exisitng cost */
            while(temp.currentSize>0){                
                HeapItem current= temp.extractMin();
                int endPoint= current.getId();
                int cost_1= current.getPriority() + prev_cost;

                if(cost_1< shortest_path[endPoint]){
                    shortest_path[endPoint]=cost_1;
                }               
            }
           
            visited_indexes.put(selected_vertex, selected_vertex); 

            /* Selection of the next vertix we must visit */
            int shortt=1000000000;
            for (int f= 1; f<shortest_path.length; f++){
                if(shortt>shortest_path[f] && !(visited_indexes.containsKey(f))){
                    shortt= shortest_path[f];
                    selected_vertex=f;
                    prev_cost= shortest_path[selected_vertex];
                }
            }
        }
        
        System.out.println(shortest_path[end]);

        }      

       
   

    private static class MinHeap {
        private final int[] idToPositionMap;
        private final HeapItem[] myMinHeapArray;
        private int currentSize;
        private MinHeap(int size) {
            this.idToPositionMap = new int[size];
            this.myMinHeapArray = new HeapItem[size];
            this.currentSize = 0;
        }
        public boolean isEmpty() {
            return currentSize <= 0;
        }
        public void insert(int id, int priority) {
            myMinHeapArray[currentSize] = new HeapItem(id, priority);
            idToPositionMap[id] = currentSize++;
            decreaseKey(id, priority);
        }

        public void decreaseKey(int id, int newPriority) {
            int i = idToPositionMap[id];
            myMinHeapArray[i].setPriority(newPriority);
            int parentI = parent(i);
            while (i > 0 && myMinHeapArray[parentI].getPriority() > 
myMinHeapArray[i].getPriority()) {
                swap(i, parentI);
                i = parentI;
                parentI = parent(i);
            }
        }
        public HeapItem extractMin() {
            HeapItem minItem = myMinHeapArray[0];
            myMinHeapArray[0] = myMinHeapArray[--currentSize];
            minHeapify(0); // to restore heap property for the recently updated root element
            return minItem;
        }
        private int parent(int childPosition) {
            return (int) Math.floor((childPosition - 1) / 2.0);
        }
        private int left(int parentPosition) {
            // For 1-indexed array implementation, the formula would be (2 * parentPosition).
            // But, here +1 is needed as we're using 0-indexed array system.
            return 2 * parentPosition + 1;
        }
        private int right(int parentPosition) {
            // For 1-indexed array implementation, the formula would be (2 * parentPosition + 1).
            // But, here +2 is needed as we're using 0-indexed array system.
            return 2 * parentPosition + 2;
        }
        private void minHeapify(int i) {
            int left = left(i);
            int right = right(i);
            // We're to identify min. item among the i'th element & its 2 children (left & right).
            // Firstly, we're looking at left:
            int minPos = (left <= currentSize && myMinHeapArray[left].getPriority()< myMinHeapArray[i].getPriority()) ? left : i;
            // Then, we're looking at right:
            if (right <= currentSize && myMinHeapArray[right].getPriority() < myMinHeapArray[minPos].getPriority())
                minPos = right;
            // If any of the left or right child has lower priority than the i'th 
//element, we're to swap those two &
            // then we should min-heapify the lower subtree.
            if (minPos != i) {
                swap(i, minPos);
                minHeapify(minPos);
            }
        }
        private void swap(int pos1, int pos2) {
            HeapItem node1 = myMinHeapArray[pos1];
            HeapItem node2 = myMinHeapArray[pos2];
            myMinHeapArray[pos1] = node2;
            myMinHeapArray[pos2] = node1;
            idToPositionMap[node1.getId()] = pos2;
            idToPositionMap[node2.getId()] = pos1;
        }
    }

    private static class HeapItem {

        private int id, priority;
        
        public HeapItem(int id, int priority) {
            this.id = id;
            this.priority = priority;
        }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public int getPriority() {
            return priority;
        }
        public void setPriority(int priority) {
            this.priority = priority;
        }
        @Override
        public String toString() {
            return "HeapItem { id= " + id + ", priority=" + priority + '}';
        }
    }

}
