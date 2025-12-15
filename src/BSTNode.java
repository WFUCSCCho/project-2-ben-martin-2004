/**************************************************
 ∗ @file: BSTNode.java
 ∗ @description: Binary Search Tree Node Class
 ∗ @author: Ben Martin
 ∗ @date: Tuesday, October 14, 2025
 **************************************************/
public class BSTNode<E extends Comparable<? super E>>
{
    //fields
    private E element;
    private BSTNode<E> left; //left child
    private BSTNode<E> right; //right child

    //constructors
    public BSTNode() {left = null; right = null; element = null;}
    public BSTNode(E val){left = null; right = null; element = val;}
    public BSTNode(E val,BSTNode<E> l, BSTNode<E> r)
    {
        left = l;
        right = r;
        element = val;
    }


    //get data methods
    public E value(){return element;}
    public BSTNode<E> right(){return right;}
    public BSTNode<E> left(){return left;}

    //set methods
    public void setVal(E val){element = val;}
    public void setRight(BSTNode<E> n) {right = n;}
    public void setLeft(BSTNode<E> n){left = n;}

    //learn facts methods
    public int height()
    {
        if (isLeaf())
        {
            return 0;
        }
        else return 1 + left.height() + right.height();
    }
    public boolean isLeaf()
    {
        return (left == null && right == null);
    }
    public int treeSize()
    {
        int size = 1;
        if (left != null) size += left.treeSize();
        if (right != null) size += right.treeSize();
        return size;
    }
    public boolean isBST()
    {
        boolean leftOK = (left == null || left.value().compareTo(this.value()) < 0 && left.isBST());
        boolean rightOK = (right == null || right.value().compareTo(this.value()) > 0 && right.isBST());
        return leftOK && rightOK;
    }
    public BSTNode<E> getMax()
    {
        if (right == null) return this;
        return right.getMax();
    }
    public BSTNode<E> getMin()
    {
        if (left == null) return this;
        return left.getMax();
    }

    //modifiers
    public void clear()
    {
        element = null;
        left = null;
        right = null;
    }

    public BSTNode<E> deleteMin()
    {
        if (left == null)
        {
            // No left child — this is the smallest node
            return right;  // The right subtree replaces this node
        }
        left = left.deleteMin();
        return this;
    }
    public BSTNode<E> deleteMax()
    {
        if (right == null)
        {
            // No right child — this is the largest node
            return left;  // The left subtree replaces this node
        }
        right = right.deleteMax();
        return this;
    }
}


