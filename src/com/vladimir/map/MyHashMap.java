package com.vladimir.map;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

public class MyHashMap<K, V> implements Map<K, V> {
    private int size = 0;
    private float load = 0.75f;
    private Node<K, V>[] store = new Node[10];

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (Node<K, V> node = store[getHash((K) key)]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i < store.length; i++) {
            for (Node<K, V> node = store[i]; node != null; node = node.next) {
                if (node.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        for (Node<K, V> node = store[getHash((K) key)]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        resize();
        Node<K, V> parent = null;
        for (Node<K, V> node = store[getHash(key)]; node != null; node = node.next) {
            if (node.key.equals(key)) {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
            parent = node;
        }
        if (parent != null) {
            parent.next = new Node<>(key, value);
        } else {
            store[getHash(key)] = new Node<>(key, value);
        }
        size++;
        return null;
    }

    @Override
    public V remove(Object key) {
        Node<K, V> parent = null;
        for (Node<K, V> node = store[getHash((K) key)]; node != null; node = node.next) {
            if ((node.key == null && key == null) || node.key.equals(key)) {
                V oldValue = node.value;
                if (parent != null) {
                    parent.next = node.next;
                } else {
                    store[getHash((K) key)] = node.next;
                }
                size--;
                return oldValue;
            }
            parent = node;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m != null) {
            for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
                if (entry != null) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    @Override
    public void clear() {
        size = 0;
        store = new Node[10];
    }

    @Override
    public Set<K> keySet() {
        return new KeySet();
    }

    @Override
    public Collection<V> values() {
        return new ValuesCollection();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    private void resize() {
        if ((float) size / (float) store.length > load) {
            Node<K, V>[] oldStore = store;
            store = new Node[store.length * 2];
            for (int i = 0; i < oldStore.length; i++) {
                for (Node<K, V> node = oldStore[i]; node != null; node = node.next) {
                    Node<K, V> parent = null;
                    for (Node<K, V> newNode = store[getHash(node.key)]; newNode != null; newNode = newNode.next) {
                        parent = newNode;
                    }
                    if (parent != null) {
                        parent.next = new Node<>(node.key, node.value);
                    } else {
                        store[getHash(node.key)] = new Node<>(node.key, node.value);
                    }
                }
            }
        }
    }

    private int getHash(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        if (hash > store.length) {
            int storeRange = String.valueOf(store.length).length();
            int range = (int) Math.pow(10, storeRange);
            hash = hash % range;
        }
        while (hash >= store.length) {
            hash /= 1.3;
        }
        return hash;
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) &&
                    Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    private abstract class AbstractInternalSet<T> implements Set<T> {
        private Node root;

        public AbstractInternalSet() {
            Node parent = null;
            for (int i = 0; i < MyHashMap.this.store.length; i++) {
                for (MyHashMap.Node node = store[i]; node != null; node = node.next) {
                    if (parent == null) {
                        parent = new Node(node);
                        root = parent;
                    } else {
                        parent.next = new Node(node);
                        parent = parent.next;
                    }
                }
            }
        }

        @Override
        public int size() {
            return MyHashMap.this.size();
        }

        @Override
        public boolean isEmpty() {
            return MyHashMap.this.isEmpty();
        }

        @Override
        public abstract boolean contains(Object o);

        @Override
        public abstract Iterator<T> iterator();

        @Override
        public Object[] toArray() {
            Object[] array = new Object[MyHashMap.this.size];
            Iterator itr = iterator();
            for (int i = 0; i < MyHashMap.this.size; i++) {
                array[i] = itr.next();
            }
            return array;
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            if (a.length < MyHashMap.this.size) {
                a = (T1[]) toArray();
                return a;
            }
            Iterator itr = iterator();
            for (int i = 0; i < MyHashMap.this.size; i++) {
                a[i] = (T1) itr.next();
            }
            if (a.length > MyHashMap.this.size) {
                a[MyHashMap.this.size] = null;
            }
            return a;
        }

        @Override
        public boolean add(T t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public abstract boolean remove(Object o);

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
            throw new UnsupportedOperationException();
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
            MyHashMap.this.clear();
        }

        class Node {
            private Node next;
            private MyHashMap.Node mapNode;

            public Node(MyHashMap.Node mapNode) {
                this.mapNode = mapNode;
            }
        }
    }

    private class EntrySet extends AbstractInternalSet<MyHashMap.Entry<K, V>> {

        @Override
        public boolean contains(Object o) {
            for (Node node = super.root; node != null; node = node.next) {
                if (node.mapNode.equals(o)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Iterator<MyHashMap.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean remove(Object o) {
            Node parent = null;
            for (Node node = super.root; node != null; node = node.next) {
                if (node.mapNode.equals(o)) {
                    MyHashMap.this.remove(node.mapNode.key);
                    if (parent == null) {
                        super.root = node.next;
                    } else {
                        parent.next = node.next;
                    }
                    return true;
                }
                parent = node;
            }
            return false;
        }

        private class EntryIterator implements Iterator<MyHashMap.Entry<K, V>> {
            private Node returned;

            @Override
            public boolean hasNext() {
                return MyHashMap.this.size != 0 && (returned == null || returned.next != null);
            }

            @Override
            public MyHashMap.Entry<K, V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (returned == null) {
                    returned = EntrySet.super.root;
                } else {
                    returned = returned.next;
                }
                return returned.mapNode;
            }
        }
    }

    private class KeySet extends AbstractInternalSet<K> {
        @Override
        public boolean contains(Object o) {
            for (Node node = super.root; node != null; node = node.next) {
                if (node.mapNode.key.equals(o)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public boolean remove(Object o) {
            Node parent = null;
            for (Node node = super.root; node != null; node = node.next) {
                if (node.mapNode.key.equals(o)) {
                    MyHashMap.this.remove(node.mapNode.key);
                    if (parent == null) {
                        super.root = node.next;
                    } else {
                        parent.next = node.next;
                    }
                    return true;
                }
                parent = node;
            }
            return false;
        }

        private class KeyIterator implements Iterator<K> {
            private Node returned;

            @Override
            public boolean hasNext() {
                return MyHashMap.this.size != 0 && (returned == null || returned.next != null);
            }

            @Override
            public K next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (returned == null) {
                    returned = KeySet.super.root;
                } else {
                    returned = returned.next;
                }
                return (K) returned.mapNode.key;
            }
        }
    }

    private class ValuesCollection extends AbstractInternalSet<V> {
        @Override
        public boolean contains(Object o) {
            for (Node node = super.root; node != null; node = node.next) {
                if (node.mapNode.value.equals(o)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Iterator<V> iterator() {
            return new ValuesIterator();
        }

        @Override
        public boolean remove(Object o) {
            Node parent = null;
            for (Node node = super.root; node != null; node = node.next) {
                if (node.mapNode.value.equals(o)) {
                    MyHashMap.this.remove(node.mapNode.key);
                    if (parent == null) {
                        super.root = node.next;
                    } else {
                        parent.next = node.next;
                    }
                    return true;
                }
                parent = node;
            }
            return false;
        }

        private class ValuesIterator implements Iterator<V> {
            private Node returned;

            @Override
            public boolean hasNext() {
                return MyHashMap.this.size != 0 && (returned == null || returned.next != null);
            }

            @Override
            public V next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (returned == null) {
                    returned = ValuesCollection.super.root;
                } else {
                    returned = returned.next;
                }
                return (V) returned.mapNode.value;
            }
        }
    }
}
