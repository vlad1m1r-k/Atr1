package com.vladimir.set;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class SunSetTest {
    private SunSet<Integer> set;

    @Before
    public void init() {
        set = new SunSet<>();
    }

    @Test
    public void sizeZero() {
        assertEquals(0, set.size());
    }

    @Test
    public void sizeNonZero() {
        set.add(1);
        set.add(2);
        set.add(3);
        assertEquals(3, set.size());
    }

    @Test
    public void isEmpty() {
        assertTrue(set.isEmpty());
    }

    @Test
    public void isNotEmpty() {
        set.add(1);
        set.add(2);
        assertFalse(set.isEmpty());
    }

    @Test
    public void contains() {
        set.add(1);
        set.add(2);
        set.add(3);
        assertTrue(set.contains(new Integer(3)));
    }

    @Test
    public void notContains() {
        assertFalse(set.contains(new Integer(3)));
    }

    @Test
    public void notContains2() {
        set.add(1);
        set.add(2);
        set.add(3);
        assertFalse(set.contains(new Integer(4)));
    }

    @Test
    public void iteratorNotHasNext() {
        Iterator<Integer> itr = set.iterator();
        assertFalse(itr.hasNext());
    }

    @Test
    public void iteratorNotHasNext2() {
        set.add(1);
        Iterator<Integer> itr = set.iterator();
        itr.next();
        assertFalse(itr.hasNext());
    }

    @Test
    public void iteratorHasNext() {
        set.add(1);
        set.add(2);
        set.add(3);
        Iterator<Integer> itr = set.iterator();
        assertTrue(itr.hasNext());
        itr.next();
        itr.next();
        assertTrue(itr.hasNext());
    }

    @Test
    public void toArray() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6}, set.toArray());
    }

    @Test
    public void toArray2() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        Integer[] arr = new Integer[5];
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6}, set.toArray(arr));
        arr = new Integer[6];
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6}, set.toArray(arr));
        arr = new Integer[]{9, 8, 6, 5, 3, 2, 1, 0};
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, null, 0}, set.toArray(arr));
    }

    @Test
    public void add() {
        assertTrue(set.add(1));
    }

    @Test
    public void notAdd() {
        set.add(1);
        assertFalse(set.add(null));
        assertFalse(set.add(1));
        assertArrayEquals(new Integer[]{1}, set.toArray());
    }

    @Test
    public void notRemove() {
        assertFalse(set.remove(1));
        set.add(2);
        assertFalse(set.remove(1));
        assertArrayEquals(new Integer[]{2}, set.toArray());
    }

    @Test
    public void remove() {
        set.add(2);
        assertTrue(set.remove(2));
        assertTrue(set.isEmpty());
    }

    @Test
    public void removeNoBranch() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        set.add(7);
        set.remove(2);
        assertArrayEquals(new Integer[]{1, 3, 4, 5, 6, 7}, set.toArray());
        set.remove(7);
        assertArrayEquals(new Integer[]{1, 3, 4, 5, 6}, set.toArray());
    }

    @Test
    public void removeOneBranch() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        set.add(7);
        set.remove(4);
        assertArrayEquals(new Integer[]{1, 2, 3, 5, 6, 7}, set.toArray());
    }

    @Test
    public void removeTwoBranch() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        set.add(7);
        set.add(8);
        set.add(0);
        set.remove(1);
        assertArrayEquals(new Integer[]{0, 2, 3, 4, 5, 6, 7, 8}, set.toArray());
        set.remove(6);
        assertArrayEquals(new Integer[]{0, 2, 3, 4, 5, 7, 8}, set.toArray());
    }

    @Test
    public void removeRoot() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        set.add(7);
        set.remove(3);
        assertArrayEquals(new Integer[]{1, 2, 4, 5, 6, 7}, set.toArray());
    }

    @Test
    public void notContainsAll() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        set.add(7);
        List<Object> list = null;
        assertFalse(set.containsAll(list));
        list = new ArrayList<>(Arrays.asList());
        assertFalse(set.containsAll(list));
        list = new ArrayList<>(Arrays.asList(1, 2, null));
        assertFalse(set.containsAll(list));
    }

    @Test
    public void containsAll() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        set.add(7);
        assertTrue(set.containsAll(new ArrayList<>(Arrays.asList(1, 2, 3))));
    }

    @Test
    public void notAddAll() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        set.add(7);
        assertFalse(set.addAll(new ArrayList<>(Arrays.asList(1, 2, null))));
    }

    @Test
    public void addAll() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        assertTrue(set.addAll(new ArrayList<>(Arrays.asList(1, 2, null, 8, 9))));
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 6, 8, 9}, set.toArray());
    }

    @Test
    public void notRetainAll() {
        set.add(3);
        set.add(1);
        set.add(2);
        assertFalse(set.retainAll(new ArrayList<>(Arrays.asList(1, 2, null, 8, 3))));
        assertArrayEquals(new Integer[]{1, 2, 3}, set.toArray());
    }

    @Test
    public void retainAll() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        set.add(7);
        assertTrue(set.retainAll(new ArrayList<>(Arrays.asList(1, 2, null, 8, 3))));
        assertArrayEquals(new Integer[]{1, 2, 3}, set.toArray());
    }

    @Test
    public void notRemoveAll() {
        set.add(3);
        set.add(1);
        set.add(2);
        assertFalse(set.removeAll(new ArrayList<>(Arrays.asList(4, 5, null, 8, 0))));
        assertArrayEquals(new Integer[]{1, 2, 3}, set.toArray());
    }

    @Test
    public void removeAll() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        set.add(7);
        assertTrue(set.removeAll(new ArrayList<>(Arrays.asList(4, 5, null, 1, 2))));
        assertArrayEquals(new Integer[]{3, 6, 7}, set.toArray());
    }
}