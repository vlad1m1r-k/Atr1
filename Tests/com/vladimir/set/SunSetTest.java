package com.vladimir.set;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SunSetTest {
    private SunSet<Integer> set;

    @Before
    public void init() {
        set = new SunSet<>();
    }

    @Test
    public void testSizeZero() {
        assertEquals(0, set.size());
    }

    @Test
    public void testSizeNonZero() {
        set.add(1);
        set.add(2);
        set.add(3);
        assertEquals(3, set.size());
    }

    @Test
    public void testIsEmptyZeroSize() {
        assertTrue(set.isEmpty());
    }

    @Test
    public void testIsEmptyNotZeroSize() {
        set.add(1);
        set.add(2);
        assertFalse(set.isEmpty());
    }

    @Test
    public void testContainsWhenContains() {
        set.add(1);
        set.add(2);
        set.add(3);
        assertTrue(set.contains(new Integer(3)));
    }

    @Test
    public void testContainsZeroSize() {
        assertFalse(set.contains(new Integer(3)));
    }

    @Test
    public void testContainsWhenNotContains() {
        set.add(1);
        set.add(2);
        set.add(3);
        assertFalse(set.contains(new Integer(4)));
    }

    @Test
    public void testIteratorZeroSet() {
        Iterator<Integer> itr = set.iterator();
        assertFalse(itr.hasNext());
    }

    @Test
    public void testIteratorEndOfSet() {
        set.add(1);
        Iterator<Integer> itr = set.iterator();
        itr.next();
        assertFalse(itr.hasNext());
    }

    @Test
    public void testIteratorHasNext() {
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
    public void testIteratorNext() {
        set.add(1);
        set.add(2);
        Iterator<Integer> itr = set.iterator();
        assertEquals(new Integer(1), itr.next());
        assertEquals(new Integer(2), itr.next());
    }

    @Test (expected = NoSuchElementException.class)
    public void testIteratorNextException() {
        set.add(1);
        Iterator<Integer> itr = set.iterator();
        itr.next();
        itr.next();
    }

    @Test
    public void testToArraySuccess() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        set.add(5);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6}, set.toArray());
    }

    @Test
    public void testToArray2Success() {
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
    public void testAddSuccess() {
        assertTrue(set.add(1));
    }

    @Test
    public void testAddNotSuccess() {
        set.add(1);
        assertFalse(set.add(null));
        assertFalse(set.add(1));
        assertArrayEquals(new Integer[]{1}, set.toArray());
    }

    @Test
    public void testRemoveNotSuccess() {
        assertFalse(set.remove(1));
        set.add(2);
        assertFalse(set.remove(1));
        assertArrayEquals(new Integer[]{2}, set.toArray());
    }

    @Test
    public void testRemoveSuccess() {
        set.add(2);
        assertTrue(set.remove(2));
        assertTrue(set.isEmpty());
    }

    @Test
    public void testRemoveNoBranch() {
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
    public void testRemoveOneBranch() {
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
    public void testRemoveTwoBranch() {
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
    public void testRemoveRoot() {
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
    public void testContainsAllNotSuccess() {
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
    public void testContainsAllSuccess() {
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
    public void testAddAllNotSuccess() {
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
    public void testAddAllSuccess() {
        set.add(3);
        set.add(1);
        set.add(6);
        set.add(2);
        set.add(4);
        assertTrue(set.addAll(new ArrayList<>(Arrays.asList(1, 2, null, 8, 9))));
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 6, 8, 9}, set.toArray());
    }

    @Test
    public void testRetainAllNotSuccess() {
        set.add(3);
        set.add(1);
        set.add(2);
        assertFalse(set.retainAll(new ArrayList<>(Arrays.asList(1, 2, null, 8, 3))));
        assertArrayEquals(new Integer[]{1, 2, 3}, set.toArray());
    }

    @Test
    public void testRetainAllSuccess() {
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
    public void testRemoveAllNotSuccess() {
        set.add(3);
        set.add(1);
        set.add(2);
        assertFalse(set.removeAll(new ArrayList<>(Arrays.asList(4, 5, null, 8, 0))));
        assertArrayEquals(new Integer[]{1, 2, 3}, set.toArray());
    }

    @Test
    public void testRemoveAllSuccess() {
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