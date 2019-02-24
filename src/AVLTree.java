/*
Name: Dallin Drollinger     # Note: I am not the author #
A#: A01984170

Description: Implements an AVL tree.
        Note that all "matching" is based on the compareTo method.
        @author Mark Allen Weiss
 */

// AvlTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( element )       --> Insert element
// void remove( element )       --> Remove element (unimplemented)
// boolean contains( element )  --> Return true if element is present
// boolean remove( element )    --> Return true if element was present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

public class AVLTree<AnyType extends Comparable<? super AnyType>> {

    //AVLTree only allows imbalance of 1 in tree
    private static final int ALLOWED_IMBALANCE = 1;

    /************************************************
     * Private class for tree nodes. Following standard
     * AVL node structure, there is the element, left child,
     * right child, and height.
     ***********************************************/
    private static class AvlNode<AnyType> {
        // Constructors
        AvlNode( AnyType theElement ) {
            this( theElement, null, null );
        }

        AvlNode( AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt ) {
            element  = theElement;
            left     = lt;
            right    = rt;
            height   = 0;
        }

        AnyType           element;      // The data in the node
        AvlNode<AnyType>  left;         // Left child
        AvlNode<AnyType>  right;        // Right child
        int               height;       // Height
    }

    //The tree root
    private AvlNode<AnyType> root;

    /************************************************
     * Construct the tree.
     ***********************************************/
    public AVLTree( ) {
        root = null;
    }

    /************************************************
     * Return the height of node t, or -1, if null.
     * @param node the node for the height check
     ***********************************************/
    private int height( AvlNode<AnyType> node ) {
        if (node==null) return -1;
        return node.height;
    }

    /************************************************
     * Insert into the tree; duplicates are allowed.
     * @param element the item to insert.
     ***********************************************/
    public void insert( AnyType element ) {
        root = insert( element, root );
    }

    /************************************************
     * Internal method to insert into a subtree.  Duplicates are allowed
     * @param element the item to insert.
     * @param node the node that roots the subtree.
     * @return the new root of the subtree.
     ***********************************************/
    private AvlNode<AnyType> insert( AnyType element, AvlNode<AnyType> node ) {
        if( node == null )
            return new AvlNode<>( element, null, null );

        int compareResult = element.compareTo( node.element );

        if( compareResult < 0 )
            node.left = insert( element, node.left );
        else
            node.right = insert( element, node.right );

        return balance( node );
    }

    /************************************************
     * Remove from the tree. Nothing is done if element is not found.
     * @param element the item to remove.
     ***********************************************/
    public void remove( AnyType element ) {
        root = remove( element, root );
    }

    /************************************************
     * Internal method to remove from a subtree.
     * @param element the item to remove.
     * @param node the node that roots the subtree.
     * @return the new root of the subtree.
     ***********************************************/
    private AvlNode<AnyType> remove( AnyType element, AvlNode<AnyType> node ) {
        if( node == null )
            return node;   // Item not found; do nothing

        int compareResult = element.compareTo( node.element );

        if( compareResult < 0 )
            node.left = remove( element, node.left );
        else if( compareResult > 0 )
            node.right = remove( element, node.right );
        else if( node.left != null && node.right != null ) { // Two children
            node.element = findMin( node.right ).element;
            node.right = remove( node.element, node.right );
        }
        else
            node = ( node.left != null ) ? node.left : node.right;
        return balance( node );
    }

    /************************************************
     * Balances node with ALLOWED_IMBALANCE
     * @param node the node to start the balance
     * @return the new root of the tree
     ***********************************************/
    private AvlNode<AnyType> balance( AvlNode<AnyType> node ) {
        if( node == null )
            return node;

        if( height( node.left ) - height( node.right ) > ALLOWED_IMBALANCE )
            if( height( node.left.left ) >= height( node.left.right ) )
                node = rightRotation( node );
            else
                node = doubleRightRotation( node );
        else
        if( height( node.right ) - height( node.left ) > ALLOWED_IMBALANCE )
            if( height( node.right.right ) >= height( node.right.left ) )
                node = leftRotation( node );
            else
                node = doubleLeftRotation( node );

        node.height = Math.max( height( node.left ), height( node.right ) ) + 1;
        return node;
    }

    /************************************************
     * Prints error message if tree is imbalanced
     ***********************************************/
    public void checkBalance( ) {
        checkBalance( root );
    }

    /************************************************
     * internal method for checking the tree balance
     * @param node the node to start the balance
     * @return the height of node
     ***********************************************/
    private int checkBalance( AvlNode<AnyType> node ) {
        if( node == null )
            return -1;
        else {
            int hl = checkBalance( node.left );
            int hr = checkBalance( node.right );
            if( Math.abs( height( node.left ) - height( node.right ) ) > 1 ||
                    height( node.left ) != hl || height( node.right ) != hr )
                System.out.println( "\n\n***********************OOPS!!" );
        }

        return height( node );
    }

    /************************************************
     * Find an item in the tree.
     * @param element the item to search for.
     * @return true if element is found.
     ***********************************************/
    public boolean contains( AnyType element ) {
        return contains( element, root );
    }

    /************************************************
     * Internal method to find an item in a subtree.
     * @param element is item to search for.
     * @param node the node that roots the tree.
     * @return true if element is found in subtree.
     ***********************************************/
    private boolean contains( AnyType element, AvlNode<AnyType> node ) {
        while( node != null )
        {
            int compareResult = element.compareTo( node.element );

            if( compareResult < 0 )
                node = node.left;
            else if( compareResult > 0 )
                node = node.right;
            else
                return true;    // Match
        }

        return false;   // No match
    }

    /************************************************
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     ***********************************************/
    public AnyType findMin( ) {
        if( isEmpty( ) )
            throw new RuntimeException( );
        return findMin( root ).element;
    }

    /************************************************
     * Internal method to find the smallest item in a subtree.
     * @param node the node that roots the tree.
     * @return node containing the smallest item.
     ***********************************************/
    private AvlNode<AnyType> findMin( AvlNode<AnyType> node ) {
        if( node == null )
            return node;

        while( node.left != null )
            node = node.left;
        return node;
    }

    /************************************************
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     ***********************************************/
    public AnyType findMax( ) {
        if( isEmpty( ) )
            throw new RuntimeException( );
        return findMax( root ).element;
    }

    /************************************************
     * Internal method to find the largest item in a subtree.
     * @param node the node that roots the tree.
     * @return node containing the largest item.
     ***********************************************/
    private AvlNode<AnyType> findMax( AvlNode<AnyType> node ) {
        if( node == null )
            return node;

        while( node.right != null )
            node = node.right;
        return node;
    }

    /************************************************
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     ***********************************************/
    public boolean isEmpty( ) {
        return root == null;
    }

    /************************************************
     * Make the tree logically empty.
     ***********************************************/
    public void makeEmpty( ) {
        root = null;
    }

    /************************************************
     * Deletes the leftmost node in the AVLTree
     ***********************************************/
    public  void  deleteMin( ){
        root =  deleteMin(root);
    }

    /************************************************
     * Internal Method to delete smallest node(left-most)
     * @param node subtree root to delete min from
     * @return new root
     ***********************************************/
    private AvlNode<AnyType> deleteMin( AvlNode<AnyType> node ) {
        if( node == null )
            return null;
        else if ( node.left == null )
            return node.right;

        node.left = deleteMin(node.left);

        return balance ( node );
    }

    /************************************************
     * Print the tree contents in sorted order.
     ***********************************************/
    public void printTree( String label) {
        System.out.println(label);
        if( isEmpty( ) )
            System.out.println( "Empty tree" );
        else
            printTree( root,"" );
    }

    /************************************************
     * Internal method to print a subtree in sorted order.
     * @param node the node that roots the tree.
     ***********************************************/
    private void printTree( AvlNode<AnyType> node, String indent ) {
        if( node != null ) {
            printTree( node.right, indent+"   " );
            System.out.println( indent+ node.element + "("+ node.height  +")" );
            printTree( node.left, indent+"   " );
        }
    }

    /************************************************
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     ***********************************************/
    private AvlNode<AnyType> rightRotation(AvlNode<AnyType> t ) {
        AvlNode<AnyType> theLeft = t.left;
        t.left = theLeft.right;
        theLeft.right = t;
        t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
        theLeft.height = Math.max( height( theLeft.left ), t.height ) + 1;
        return theLeft;
    }

    /************************************************
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     ***********************************************/
    private AvlNode<AnyType> leftRotation(AvlNode<AnyType> t ) {
        AvlNode<AnyType> theRight = t.right;
        t.right = theRight.left;
        theRight.left = t;
        t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
        theRight.height = Math.max( height( theRight.right ), t.height ) + 1;
        return theRight;
    }

    /************************************************
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     ***********************************************/
    private AvlNode<AnyType> doubleRightRotation( AvlNode<AnyType> t ) {
        t.left = leftRotation( t.left );
        return rightRotation( t );

    }

    /************************************************
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     ***********************************************/
    private AvlNode<AnyType> doubleLeftRotation(AvlNode<AnyType> t ) {
        t.right = rightRotation( t.right );
        return leftRotation( t );
    }
}
