/**************************************************
 ∗ @file: BST.java
 ∗ @description: Binary Search Tree Class
 ∗ @author: Ben Martin
 ∗ @date: Tuesday, October 26, 2025
 **************************************************/
import java.util.Iterator;
import java.util.Stack;
public class BST<E extends Comparable<E>> implements Iterable<E>
{

    // Inner class that performs an in-order traversal
    private class BSTIterator implements Iterator<E>
    {
        private Stack<BSTNode<E>> stack = new Stack<>();//define the stack to iterate

        public BSTIterator(BSTNode<E> root)
        {
            pushLeft(root);
        }

        private void pushLeft(BSTNode<E> node) //from node to node.left. to node.left.left... push these on the stack
        {
            while (node != null)
            {
                stack.push(node);
                node = node.left();
            }
        }

        @Override
        public boolean hasNext() //determine if the stack has another element
        {
            return !stack.isEmpty();
        }

        @Override
        public E next() //determine the value of the next element
        {
            BSTNode<E> current = stack.pop();
            E value = current.value();
            if (current.right() != null)
            {
                pushLeft(current.right());
            }
            return value;
        }
    }
    // BST fields
    private BSTNode<E> root; //root node of the BST;
    private int size; //total number of nodes in the BST

    //constructors
    public BST(){root = null; size =0;}
    public BST(BSTNode r)
    {
        if (r.isBST())
        {
            root = r;
            size = r.treeSize();
        }
    }
    //define iterator
    @Override
    public Iterator<E> iterator() {
        return new BSTIterator(root);
    }

    //location methods
    private E searchHelp(BSTNode<E> rt, E key)//keeps recursion internal returns null if not found otherwise returns the value
    {
        if (rt == null) return null;
        int cmp = key.compareTo(rt.value());
        if (cmp < 0)
            return  searchHelp(rt.left(), key);
        else if (cmp > 0)
            return  searchHelp(rt.right(), key);
        else
            return rt.value(); // found
    }

    public boolean search(E key) //determines whether an element is in the tree or not
    {
        return searchHelp(root, key) != null;
    }


    //insertion methods
    private BSTNode<E> insertHelp(BSTNode<E> rt, E key)//gets the node
    {
        if (rt == null) return new BSTNode<E>(key);
        if (rt.value().compareTo(key)>0) //recurse into left subtree
        {
            rt.setLeft(insertHelp(rt.left(),key));

        }
        else //recurse into right subtree
        {
            rt.setRight(insertHelp(rt.right(),key));

        }
        return rt;
    }
    public void insert(E val)
    {
        root = insertHelp(root,val);
        size++;
    }
    public void insertChain (E[] chain)
    {
        for (E x: chain)
        {
            insert(x);
        }
    }

    //deletion methods
    private BSTNode<E> removeHelp(BSTNode<E> rt, E val)
    {
        if (rt == null) return null;
        if (rt.value().compareTo(val) > 0)
        {
            rt.setLeft(removeHelp(rt.left(),val));
        }
        else if(rt.value().compareTo(val) < 0)
        {
            rt.setRight(removeHelp(rt.right(),val));
        }
        else //found it, now remove it
        {
            if (rt.left()== null) return rt.right();
            else if (rt.right()==null) return rt.left();
            else //in the case of 2 children
            {
                BSTNode<E> temp = rt.right().getMin();
                rt.setVal(temp.value());
                rt.setRight(rt.right().deleteMin());

            }
        }
        return rt;
    }
    public boolean remove(E key)
    {
        E temp = (E) searchHelp(root,key);
        if (temp != null)
        {
            root = removeHelp(root,key);
            size--;
            return true;
        }
        return false;
    }
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (E element : this)
        {
            sb.append(element).append(" ");
        }
        return sb.toString().trim();
    }
}

