package com.vladimir.set;

import com.sun.istack.internal.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class SunSet<T extends Comparable> implements Set<T> {
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
        return new SunSetIterator();
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
            root = new Node<>(t, null);
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
        if (node.left == null && node.right == null) {
            if (node.parent == null) {
                size--;
                return true;
            }
            replace(node, null);
            size--;
            return true;
        }
        if (node.left == null || node.right == null) {
            Node<T> branch = node.left == null ? node.right : node.left;
            if (node.parent == null) {
                size--;
                root = branch;
                root.parent = null;
                return true;
            }
            replace(node, branch);
            size--;
            return true;
        }
        Node<T> replacement = getDeepLeft(node.right);
        if (replacement == null) {
            replacement = node.right;
        }
        if (node.parent == null) {
            if (replacement.right == null) {
                root = replacement;
                size--;
                replacement.parent = null;
                links(node, replacement);
                return true;
            }
            replace(replacement, replacement.right);
            root = replacement;
            size--;
            replacement.parent = null;
            links(node, replacement);
            return true;
        }
        if (replacement.right == null) {
            replace(node, replacement);
            links(node, replacement);
            size--;
            return true;
        }
        replace(replacement, replacement.right);
        replace(node, replacement);
        links(node, replacement);
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

    private void links(Node<T> from, Node<T> to) {
        from.left.parent = to;
        to.left = from.left;
        if (from.right.data.compareTo(to.data) != 0) {
            from.right.parent = to;
            to.right = from.right;
        }
    }

    private void replace(Node<T> node, Node<T> to) {
        if (to != null) {
            to.parent = node.parent;
        }
        if (node.parent.data.compareTo(node.data) > 0) {
            node.parent.left = to;
        } else {
            node.parent.right = to;
        }
    }

    private void add(T data, Node<T> node) {
        if (node.data.compareTo(data) > 0) {
            if (node.left == null) {
                node.left = new Node<>(data, node);
                size++;
            } else {
                add(data, node.left);
            }
        } else {
            if (node.right == null) {
                node.right = new Node<>(data, node);
                size++;
            } else {
                add(data, node.right);
            }
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

    private class Node<T> {
        private T data;
        private Node<T> right;
        private Node<T> left;
        private Node<T> parent;

        Node(T data, Node<T> parent) {
            this.data = data;
            this.parent = parent;
        }
    }

    private class SunSetIterator implements Iterator<T> {
        private Node<T> returned;

        @Override
        public boolean hasNext() {
            if (SunSet.this.size == 0) {
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
                node = SunSet.this.getDeepLeft(SunSet.this.root);
                if (node == null) {
                    return SunSet.this.root;
                }
                return node;
            }
            Node<T> next;
            if (node.right != null) {
                if ((next = SunSet.this.getDeepLeft(node.right)) != null) {
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
