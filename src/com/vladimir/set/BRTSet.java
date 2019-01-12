package com.vladimir.set;

import com.sun.istack.internal.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class BRTSet<T extends Comparable> implements Set<T> {
    private int size = 0;
    private Node<T> root;

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (size > 0) {
            return getNode((T) o, root) != null;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new BRTIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Iterator itr = iterator();
        for (int i = 0; i < size; i++) {
            array[i] = itr.next();
        }
        return array;
    }

    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        if (a.length < size) {
            a = (T1[]) toArray();
            return a;
        }
        Iterator itr = iterator();
        for (int i = 0; i < size; i++) {
            a[i] = (T1) itr.next();
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean add(T t) {
        if (t == null) {
            return false;
        }
        if (size == 0) {
            root = new Node<>(t, NodeColors.BLACK, null);
            size++;
            return true;
        }
        if (!contains(t)) {
            add(t, root);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        Node<T> node;
        if (size == 0 || (node = getNode((T) o, root)) == null) {
            return false;
        }
        if (node.left != null && node.right != null) {
            Node<T> replacement = getDeepLeft(node.right);
            if (replacement == null) {
                replacement = node.right;
            }
            node.data = replacement.data;
            node = replacement;
        }
        Node<T> child = node.left == null ? node.right : node.left;
        if (node.parent != null) {
            if (node == node.parent.left) {
                node.parent.left = child;
            } else {
                node.parent.right = child;
            }
        }
        if (child != null) {
            child.parent = node.parent;
        }
        if (node.color == NodeColors.BLACK) {
            if (child != null && child.color == NodeColors.RED) {
                child.color = NodeColors.BLACK;
            } else {
                deleteCorrectionCase1(child, node.parent);
            }
        }
        size--;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null || c.size() == 0) {
            return false;
        }
        Object[] collection = c.toArray();
        boolean contains = true;
        for (Object object : collection) {
            if (!contains(object)) {
                contains = false;
            }
        }
        return contains;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c == null) {
            return false;
        }
        Object[] collection = c.toArray();
        boolean added = false;
        for (Object object : collection) {
            if (add((T) object)) {
                added = true;
            }
        }
        return added;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            return false;
        }
        boolean changed = false;
        boolean modified;
        do {
            modified = false;
            Iterator<T> itr = iterator();
            while (itr.hasNext()) {
                T data = itr.next();
                if (!c.contains(data)) {
                    changed = true;
                    modified = true;
                    remove(data);
                    break;
                }
            }
        } while (modified);
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            return false;
        }
        boolean changed = false;
        Object[] collection = c.toArray();
        for (Object object : collection) {
            while (contains(object)) {
                if (remove(object)) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    @Override
    public void clear() {
        size = 0;
        root = null;
    }

    private void add(T data, Node<T> node) {
        if (node.data.compareTo(data) > 0) {
            if (node.left == null) {
                node.left = new Node<>(data, NodeColors.RED, node);
                insertCorrectionCase1(node.left);
                size++;
            } else {
                add(data, node.left);
            }
        } else {
            if (node.right == null) {
                node.right = new Node<>(data, NodeColors.RED, node);
                insertCorrectionCase1(node.right);
                size++;
            } else {
                add(data, node.right);
            }
        }
    }

    private Node<T> getGrandparent(Node<T> node) {
        if (node.parent != null) {
            return node.parent.parent;
        }
        return null;
    }

    private Node<T> getUncle(Node<T> node) {
        Node<T> grandNode = getGrandparent(node);
        if (grandNode == null) {
            return null;
        }
        if (node.parent == grandNode.left) {
            return grandNode.right;
        }
        return grandNode.left;
    }

    private Node<T> getBrother(Node<T> node, Node<T> parent) {
        if (node == parent.left) {
            return parent.right;
        }
        return parent.left;
    }

    private void rotateLeft(Node<T> node) {
        Node<T> pivot = node.right;
        pivot.parent = node.parent;
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.left = pivot;
            } else {
                node.parent.right = pivot;
            }
        } else {
            root = pivot;
        }
        node.right = pivot.left;
        if (pivot.left != null) {
            pivot.left.parent = node;
        }
        node.parent = pivot;
        pivot.left = node;
    }

    private void rotateRight(Node<T> node) {
        Node<T> pivot = node.left;
        pivot.parent = node.parent;
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.left = pivot;
            } else {
                node.parent.right = pivot;
            }
        } else {
            root = pivot;
        }
        node.left = pivot.right;
        if (pivot.right != null) {
            pivot.right.parent = node;
        }
        node.parent = pivot;
        pivot.right = node;
    }

    private void insertCorrectionCase1(Node<T> node) {
        if (node.parent == null) {
            node.color = NodeColors.BLACK;
            root = node;
        } else {
            insertCorrectionCase2(node);
        }
    }

    private void insertCorrectionCase2(Node<T> node) {
        if (node.parent.color == NodeColors.BLACK) {
            return;
        }
        insertCorrectionCase3(node);
    }

    private void insertCorrectionCase3(Node<T> node) {
        Node<T> uncle = getUncle(node);
        Node<T> grand = getGrandparent(node);
        if (uncle != null && uncle.color == NodeColors.RED) {
            node.parent.color = NodeColors.BLACK;
            uncle.color = NodeColors.BLACK;
            grand.color = NodeColors.RED;
            insertCorrectionCase1(grand);
        } else {
            insertCorrectionCase4(node);
        }
    }

    private void insertCorrectionCase4(Node<T> node) {
        Node<T> grand = getGrandparent(node);
        if (node == node.parent.right && node.parent == grand.left) {
            rotateLeft(node.parent);
            node = node.left;
        } else {
            if (node == node.parent.left && node.parent == grand.right) {
                rotateRight(node.parent);
                node = node.right;
            }
        }
        insertCorrectionCase5(node);
    }

    private void insertCorrectionCase5(Node<T> node) {
        Node<T> grand = getGrandparent(node);
        node.parent.color = NodeColors.BLACK;
        grand.color = NodeColors.RED;
        if (node == node.parent.left && node.parent == grand.left) {
            rotateRight(grand);
        } else {
            rotateLeft(grand);
        }
    }

    private void deleteCorrectionCase1(Node<T> node, Node<T> parent) {
        if (parent != null) {
            deleteCorrectionCase2(node, parent);
        } else {
            root = node;
        }
    }

    private void deleteCorrectionCase2(Node<T> node, Node<T> parent) {
        Node<T> brother = getBrother(node, parent);
        if (brother.color == NodeColors.RED) {
            parent.color = NodeColors.RED;
            brother.color = NodeColors.BLACK;
            if (node == parent.left) {
                rotateLeft(parent);
            } else {
                rotateRight(parent);
            }
        }
        deleteCorrectionCase3(node, parent);
    }

    private void deleteCorrectionCase3(Node<T> node, Node<T> parent) {
        Node<T> brother = getBrother(node, parent);
        if (parent.color == NodeColors.BLACK && brother.color == NodeColors.BLACK && (brother.left == null || brother.left.color == NodeColors.BLACK) && (brother.right == null || brother.right.color == NodeColors.BLACK)) {
            brother.color = NodeColors.RED;
            deleteCorrectionCase1(parent, parent.parent);
        } else {
            deleteCorrectionCase4(node, parent);
        }
    }

    private void deleteCorrectionCase4(Node<T> node, Node<T> parent) {
        Node<T> brother = getBrother(node, parent);
        if (parent.color == NodeColors.RED && brother.color == NodeColors.BLACK && (brother.left == null || brother.left.color == NodeColors.BLACK) && (brother.right == null || brother.right.color == NodeColors.BLACK)) {
            brother.color = NodeColors.RED;
            parent.color = NodeColors.BLACK;
        } else {
            deleteCorrectionCase5(node, parent);
        }
    }

    private void deleteCorrectionCase5(Node<T> node, Node<T> parent) {
        Node<T> brother = getBrother(node, parent);
        if (brother.color == NodeColors.BLACK) {
            if (node == parent.left && (brother.right == null || brother.right.color == NodeColors.BLACK) && (brother.left != null && brother.left.color == NodeColors.RED)) {
                brother.color = NodeColors.RED;
                brother.left.color = NodeColors.BLACK;
                rotateRight(brother);
            } else {
                if (node == parent.right && (brother.left == null || brother.left.color == NodeColors.BLACK) && (brother.right != null && brother.right.color == NodeColors.RED)) {
                    brother.color = NodeColors.RED;
                    brother.right.color = NodeColors.BLACK;
                    rotateLeft(brother);
                }
            }
        }
        deleteCorrectionCase6(node, parent);
    }

    private void deleteCorrectionCase6(Node<T> node, Node<T> parent) {
        Node<T> brother = getBrother(node, parent);
        brother.color = parent.color;
        parent.color = NodeColors.BLACK;
        if (node == parent.left) {
            brother.right.color = NodeColors.BLACK;
            rotateLeft(parent);
        } else {
            brother.left.color = NodeColors.BLACK;
            rotateRight(parent);
        }
    }

    private Node<T> getNode(T data, Node<T> node) {
        if (data == null) {
            return null;
        }
        if (node.data.compareTo(data) == 0) {
            return node;
        }
        if (node.left != null && node.data.compareTo(data) > 0) {
            return getNode(data, node.left);
        }
        if (node.right != null && node.data.compareTo(data) < 0) {
            return getNode(data, node.right);
        }
        return null;
    }

    private Node<T> getDeepLeft(Node<T> node) {
        if (node.left == null) {
            return null;
        }
        Node<T> currentNode = node;
        while (currentNode.left != null) {
            currentNode = currentNode.left;
        }
        return currentNode;
    }

    private enum NodeColors {
        RED, BLACK
    }

    private class Node<T> {
        private T data;
        private Node<T> right;
        private Node<T> left;
        private NodeColors color;
        private Node<T> parent;

        Node(T data, NodeColors color, Node<T> parent) {
            this.data = data;
            this.color = color;
            this.parent = parent;
        }
    }

    private class BRTIterator implements Iterator<T> {
        private Node<T> returned;

        @Override
        public boolean hasNext() {
            if (BRTSet.this.size == 0) {
                return false;
            }
            return getNext(returned) != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node<T> node;
            node = getNext(returned);
            returned = node;
            return node.data;
        }

        private Node<T> getNext(Node<T> node) {
            if (node == null) {
                node = BRTSet.this.getDeepLeft(BRTSet.this.root);
                if (node == null) {
                    return BRTSet.this.root;
                }
                return node;
            }
            Node<T> next;
            if (node.right != null) {
                if ((next = BRTSet.this.getDeepLeft(node.right)) != null) {
                    return next;
                }
                return node.right;
            }
            next = node.parent;
            while (next != null) {
                if (node.data.compareTo(next.data) < 0) {
                    return next;
                }
                next = next.parent;
            }
            return null;
        }
    }
}
