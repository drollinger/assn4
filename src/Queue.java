/*
Name: Dallin Drollinger
A#: A01984170

Description: Queue.java is our class used only for Board objects and can enqueue or dequeue them.
 */
public class Queue {
    //our queue pointers pointing to the root node and the head node
    private Node root = null;
    private Node head = null;

    //the class that defines what a Node in our Queue is
    public class Node {
        public Board item;
        public Node next;
        public Node(Board item) {
            this.item = item;
        }
    }

    //the default enqueue with no previous moves
    public void enqueue(Board item) {
        enqueue(item, " ");
    }

    //our enqueue with listOfMoves being tracked
    public void enqueue(Board item, String listOfMoves) {
        if (root == null) {
            root = new Node(item);
            head = root;
        } else {
            Node temp = new Node(item);
            temp.next = root;
            root = temp;
        }
        root.item.listOfMoves = listOfMoves;
    }

    //our dequeue which returns the entire node
    public Node dequeue() {
        Node temp = root;
        if(temp == null) {
            return null;
        }
        else {
            if (temp.next == null) {
                head = null;
                root = null;
                return temp;
            }
            else {
                while (temp.next != head) {
                    temp = temp.next;
                }
                head = temp;
                return temp.next;
            }
        }
    }
}