package com.vladimir.list;

import java.util.*;

public class Glist<T> implements List<T> {
    private Object[] store = new Object[25];
    private int size = 0;
    private int addSize = 25;

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
        if (o == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < size; i++) {
            if (store[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new GlistIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(store, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a == null) {
            throw new NullPointerException();
        }
        if (a.length < size) {
            a = (T1[]) Arrays.copyOf(store, size);
            return a;
        }
        for (int i = 0; i < size; i++) {
            a[i] = (T1) store[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean add(T o) {
        resize();
        if (o == null) {
            throw new NullPointerException();
        }
        store[size++] = o;
        return true;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < size; i++) {
            if (store[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        int index = indexOf(o);
        if (index == -1) {
            return false;
        }
        System.arraycopy(store, index + 1, store, index, size - index);
        size--;
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        Object[] collection = c.toArray();
        boolean contains = true;
        for (Object object : collection) {
            if (object == null) {
                throw new NullPointerException();
            }
            if (!contains(object)) {
                contains = false;
            }
        }
        return contains;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        Object[] collection = c.toArray();
        boolean added = false;
        for (Object object : collection) {
            added = add((T) object);
        }
        return added;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        for (Object object : c) {
            if (object == null) {
                throw new NullPointerException();
            }
        }
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        Object[] collection = c.toArray();
        resize(size + collection.length);
        System.arraycopy(store, index, store, index + collection.length, size - index);
        System.arraycopy(collection, 0, store, index, collection.length);
        size += collection.length;
        return collection.length != 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
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
    public boolean retainAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException();
        }
        boolean changed = false;
        boolean modified = false;
        do {
            modified = false;
            for (int i = 0; i < size; i++) {
                if (!c.contains(store[i])) {
                    changed = true;
                    modified = true;
                    remove(store[i]);
                    break;
                }
            }
        } while (modified);
        return changed;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (T) store[index];
    }

    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (element == null) {
            throw new NullPointerException();
        }
        T old = (T) store[index];
        store[index] = element;
        return old;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (element == null) {
            throw new NullPointerException();
        }
        resize();
        System.arraycopy(store, index, store, index + 1, size - index);
        store[index] = element;
        size++;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T old = (T) store[index];
        System.arraycopy(store, index + 1, store, index, size - index);
        size--;
        return old;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        for (int i = size - 1; i >= 0; i--) {
            if (store[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new GlistListIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new GlistListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }
        return new Sublist(this, 0, fromIndex, toIndex);
    }

    private void resize() {
        if (size > store.length - 5) {
            Object[] temp;
            temp = Arrays.copyOf(store, store.length + addSize);
            store = temp;
        }
    }

    private void resize(int toSize) {
        if (toSize > store.length) {
            Object[] temp;
            temp = Arrays.copyOf(store, toSize + addSize);
            store = temp;
        }
    }

    private class GlistIterator implements Iterator<T> {
        private int index = 0;
        private int returnedInd = -1;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            if (index >= size) {
                throw new NoSuchElementException();
            }
            returnedInd = index;
            return (T) store[index++];
        }

        @Override
        public void remove() {
            if (returnedInd < 0) {
                throw new IllegalStateException();
            }
            Glist.this.remove(returnedInd);
            index = returnedInd;
            returnedInd = -1;
        }
    }

    private class GlistListIterator extends GlistIterator implements ListIterator<T> {

        public GlistListIterator(int index) {
            super();
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException();
            }
            super.index = index;
        }

        @Override
        public boolean hasPrevious() {
            return super.index > 0;
        }

        @Override
        public T previous() {
            if (super.index == 0) {
                throw new NoSuchElementException();
            }
            super.returnedInd = --super.index;
            return (T) store[super.returnedInd];
        }

        @Override
        public int nextIndex() {
            return super.index;
        }

        @Override
        public int previousIndex() {
            return super.index - 1;
        }

        @Override
        public void set(T t) {
            if (super.returnedInd < 0) {
                throw new IllegalStateException();
            }
            Glist.this.set(super.returnedInd, t);
        }

        @Override
        public void add(T t) {
            Glist.this.add(super.index, t);
            super.index++;
            super.returnedInd = -1;
        }
    }

    private class Sublist implements List<T> {
        private List<T> parrent;
        private int parrentOffset;
        private int globalOffset;
        private int size;

        public Sublist(List parrent, int globalOffset, int fromIndex, int toIndex) {
            this.parrent = parrent;
            this.parrentOffset = fromIndex;
            this.globalOffset = globalOffset + fromIndex;
            this.size = toIndex - fromIndex;
        }

        @Override
        public int size() {
            return this.size;
        }

        @Override
        public boolean isEmpty() {
            return this.size == 0;
        }

        @Override
        public boolean contains(Object o) {
            if (o == null) {
                throw new NullPointerException();
            }
            for (int i = globalOffset; i < globalOffset + this.size; i++) {
                if (store[i].equals(o)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Iterator<T> iterator() {
            return listIterator();
        }

        @Override
        public Object[] toArray() {
            return Arrays.copyOfRange(store, globalOffset, globalOffset + this.size);
        }

        @Override
        public boolean add(T o) {
            parrent.add(parrentOffset + this.size, o);
            this.size++;
            return true;
        }

        @Override
        public boolean remove(Object o) {
            if (o == null) {
                throw new NullPointerException();
            }
            for (int i = globalOffset; i < globalOffset + this.size; i++) {
                if (store[i].equals(o)) {
                    parrent.remove(parrentOffset + (i - globalOffset));
                    this.size--;
                    return true;
                }
            }
            return false;
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            if (a == null) {
                throw new NullPointerException();
            }
            if (a.length < this.size) {
                a = (T1[]) Arrays.copyOfRange(store, globalOffset, globalOffset + this.size);
                return a;
            }
            for (int i = 0; i < this.size; i++) {
                a[i] = (T1) store[globalOffset + i];
            }
            if (a.length > this.size) {
                a[this.size] = null;
            }
            return a;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            if (c == null) {
                throw new NullPointerException();
            }
            Object[] collection = c.toArray();
            boolean contains = true;
            for (Object object : collection) {
                if (object == null) {
                    throw new NullPointerException();
                }
                if (!this.contains(object)) {
                    contains = false;
                }
            }
            return contains;
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            if (c == null) {
                throw new NullPointerException();
            }
            Object[] collection = c.toArray();
            boolean added = false;
            for (Object object : collection) {
                added = this.add((T) object);
            }
            return added;
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> c) {
            if (index < 0 || index > this.size) {
                throw new IndexOutOfBoundsException();
            }
            boolean result = parrent.addAll(parrentOffset + index, c);
            if (result) {
                this.size += c.size();
            }
            return result;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            if (c == null) {
                throw new NullPointerException();
            }
            boolean changed = false;
            Object[] collection = c.toArray();
            for (Object object : collection) {
                while (this.contains(object)) {
                    if (this.remove(object)) {
                        changed = true;
                    }
                }
            }
            return changed;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            if (c == null) {
                throw new NullPointerException();
            }
            boolean changed = false;
            boolean modified = false;
            do {
                modified = false;
                for (int i = globalOffset; i < globalOffset + this.size; i++) {
                    if (!c.contains(store[i])) {
                        changed = true;
                        modified = true;
                        this.remove(store[i]);
                        break;
                    }
                }
            } while (modified);
            return changed;
        }

        @Override
        public void clear() {
            while (this.size != 0) {
                parrent.remove(parrentOffset + this.size - 1);
                this.size--;
            }
        }

        @Override
        public T get(int index) {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            return (T) store[globalOffset + index];
        }

        @Override
        public T set(int index, T element) {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            if (element == null) {
                throw new NullPointerException();
            }
            T old = (T) store[globalOffset + index];
            store[globalOffset + index] = element;
            return old;
        }

        @Override
        public void add(int index, T element) {
            if (index < 0 || index > this.size) {
                throw new IndexOutOfBoundsException();
            }
            if (element == null) {
                throw new NullPointerException();
            }
            parrent.add(parrentOffset + index, element);
            this.size++;
        }

        @Override
        public T remove(int index) {
            T old = parrent.remove(parrentOffset + index);
            this.size--;
            return old;
        }

        @Override
        public int indexOf(Object o) {
            if (o == null) {
                throw new NullPointerException();
            }
            for (int i = globalOffset; i < globalOffset + this.size; i++) {
                if (store[i].equals(o)) {
                    return i - globalOffset;
                }
            }
            return -1;
        }

        @Override
        public int lastIndexOf(Object o) {
            if (o == null) {
                throw new NullPointerException();
            }
            for (int i = globalOffset + this.size - 1; i >= globalOffset; i--) {
                if (store[i].equals(o)) {
                    return i - globalOffset;
                }
            }
            return -1;
        }

        @Override
        public ListIterator<T> listIterator() {
            return new SublistIterator(0);
        }

        @Override
        public ListIterator<T> listIterator(int index) {
            return new SublistIterator(index);
        }

        @Override
        public List<T> subList(int fromIndex, int toIndex) {
            if (fromIndex < 0 || toIndex > this.size || fromIndex > toIndex) {
                throw new IndexOutOfBoundsException();
            }
            return new Sublist(this, globalOffset, fromIndex, toIndex);
        }

        private class SublistIterator implements ListIterator<T> {
            private int index = 0;
            private int returnedInd = -1;

            public SublistIterator(int index) {
                if (index < 0 || index > Sublist.this.size) {
                    throw new IndexOutOfBoundsException();
                }
                this.index = index;
            }

            @Override
            public boolean hasNext() {
                return index < Sublist.this.size;
            }

            @Override
            public T next() {
                if (index >= Sublist.this.size) {
                    throw new NoSuchElementException();
                }
                returnedInd = index;
                return (T) store[globalOffset + index++];
            }

            @Override
            public boolean hasPrevious() {
                return index > 0;
            }

            @Override
            public T previous() {
                if (index == 0) {
                    throw new NoSuchElementException();
                }
                returnedInd = --index;
                return (T) store[globalOffset + returnedInd];
            }

            @Override
            public int nextIndex() {
                return index;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() {
                if (returnedInd < 0) {
                    throw new IllegalStateException();
                }
                Sublist.this.remove(returnedInd);
                index = returnedInd;
                returnedInd = -1;
            }

            @Override
            public void set(T t) {
                if (returnedInd < 0) {
                    throw new IllegalStateException();
                }
                Sublist.this.set(returnedInd, t);
            }

            @Override
            public void add(T t) {
                Sublist.this.add(index, t);
                index++;
                returnedInd = -1;
            }
        }
    }
}
