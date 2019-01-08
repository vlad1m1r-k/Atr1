package com.vladimir.list;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GlistTest {
    private Glist<Integer> glist;

    @Before
    public void init() {
        glist = new Glist<>();
    }

    @Test
    public void size() {
        assertEquals(0, glist.size());
        glist.add(1);
        glist.add(2);
        assertEquals(2, glist.size());
    }

    @Test
    public void isEmpty() {
        assertTrue(glist.isEmpty());
        glist.add(1);
        assertFalse(glist.isEmpty());
    }

    @Test
    public void contains() {
        Integer value = null;
        glist.add(1);
        try {
            glist.contains(value);
            fail("Exception expected.");
        } catch (NullPointerException npe) {
            assertEquals(NullPointerException.class, npe.getClass());
        }
        assertFalse(glist.contains(2));
        assertTrue(glist.contains(1));
    }

    @Test
    public void iterator() {
        Iterator itr = glist.iterator();
        assertFalse(itr.hasNext());
        try {
            itr.next();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
        }
        glist.add(1);
        itr = glist.iterator();
        assertTrue(itr.hasNext());
        try {
            itr.remove();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
        assertEquals(1, itr.next());
        glist.add(2);
        glist.add(3);
        itr.remove();
        assertArrayEquals(new Integer[]{2, 3}, glist.toArray());
        try {
            itr.remove();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
    }

    @Test
    public void toArray() {
        assertArrayEquals(new Integer[]{}, glist.toArray());
        glist.add(1);
        glist.add(2);
        glist.add(4);
        assertArrayEquals(new Integer[]{1, 2, 4}, glist.toArray());
    }

    @Test
    public void toArray1() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        String[] arr = null;
        try {
            glist.toArray(arr);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        arr = new String[3];
        try {
            glist.toArray(arr);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(ArrayStoreException.class, e.getClass());
        }
        Integer[] arr2 = new Integer[1];
        assertArrayEquals(new Integer[]{1, 2, 4}, glist.toArray(arr2));
        arr2 = new Integer[3];
        assertArrayEquals(new Integer[]{1, 2, 4}, glist.toArray(arr2));
        arr2 = new Integer[]{1, 1, 1, 1, 1};
        assertArrayEquals(new Integer[]{1, 2, 4, null, 1}, glist.toArray(arr2));
    }

    @Test
    public void add() {
        try {
            glist.add(null);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        glist.add(-1);
        assertTrue(glist.add(1));
        glist.add(0);
        assertArrayEquals(new Integer[]{-1, 1, 0}, glist.toArray());
    }

    @Test
    public void remove() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(3);
        glist.add(4);
        try {
            glist.remove(null);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        assertFalse(glist.remove("1"));
        assertTrue(glist.remove(new Integer(2)));
        assertFalse(glist.remove(new Integer(2)));
        glist.remove(new Integer(1));
        assertArrayEquals(new Integer[]{4, 3, 4}, glist.toArray());
        glist.remove(new Integer(4));
        assertArrayEquals(new Integer[]{3, 4}, glist.toArray());
        glist.remove(new Integer(4));
        assertArrayEquals(new Integer[]{3}, glist.toArray());
    }

    @Test
    public void indexOf() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(3);
        glist.add(4);
        glist.add(7);
        try {
            glist.indexOf(null);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        assertEquals(-1, glist.indexOf("4"));
        assertEquals(-1, glist.indexOf(5));
        assertEquals(0, glist.indexOf(1));
        assertEquals(0, glist.indexOf(1));
        assertEquals(2, glist.indexOf(4));
        assertEquals(5, glist.indexOf(7));

    }

    @Test
    public void containsAll() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(3);
        glist.add(4);
        glist.add(7);
        List<Object> list = null;
        try {
            glist.containsAll(list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        try {
            list = new ArrayList<>(Arrays.asList(1, 2, null));
            glist.containsAll(list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        list = new ArrayList<>(Arrays.asList(1, 2, 3));
        assertTrue(glist.containsAll(list));
        list = new ArrayList<>(Arrays.asList(1, 2, 5));
        assertFalse(glist.containsAll(list));
        list = new ArrayList<>(Arrays.asList(1, 2, "3"));
        assertFalse(glist.containsAll(list));
    }

    @Test
    public void addAll() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        List<Integer> list = null;
        try {
            glist.addAll(list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        try {
            list = new ArrayList<>(Arrays.asList(null));
            glist.addAll(list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        list = new ArrayList<>();
        assertFalse(glist.addAll(list));
        list = new ArrayList<>(Arrays.asList(1, 2));
        assertTrue(glist.addAll(list));
        assertArrayEquals(new Integer[]{1, 2, 4, 1, 2}, glist.toArray());
    }

    @Test
    public void addAll1() {
        List<Integer> list = null;
        try {
            glist.addAll(0, list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        try {
            list = new ArrayList<>(Arrays.asList(1, 2, null, 4));
            glist.addAll(0, list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        list = new ArrayList<>();
        try {
            glist.addAll(-1, list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        try {
            glist.addAll(1, list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        glist.add(1);
        glist.add(2);
        assertFalse(glist.addAll(0, list));
        assertArrayEquals(new Integer[]{1, 2}, glist.toArray());
        list.add(1);
        assertTrue(glist.addAll(0, list));
        assertArrayEquals(new Integer[]{1, 1, 2}, glist.toArray());
        glist.addAll(2, list);
        assertArrayEquals(new Integer[]{1, 1, 1, 2}, glist.toArray());
    }

    @Test
    public void removeAll() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(4);
        glist.add(2);
        glist.add(3);
        List<Object> list = null;
        try {
            glist.removeAll(list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        try {
            list = new ArrayList<>(Arrays.asList(null));
            glist.removeAll(list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        list = new ArrayList<>(Arrays.asList(5, "3"));
        assertFalse(glist.removeAll(list));
        list.add(2);
        assertTrue(glist.removeAll(list));
        assertArrayEquals(new Integer[]{1, 4, 4, 3}, glist.toArray());
    }

    @Test
    public void retainAll() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(4);
        glist.add(2);
        glist.add(3);
        List<Object> list = null;
        try {
            glist.retainAll(list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        try {
            list = new ArrayList<>(Arrays.asList(null));
            glist.retainAll(list);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        list = new ArrayList<>(Arrays.asList(1, "3", 2, 4, 3));
        assertFalse(glist.retainAll(list));
        list = new ArrayList<>(Arrays.asList("3", 2, 3));
        assertTrue(glist.retainAll(list));
        assertArrayEquals(new Integer[]{2, 2, 3}, glist.toArray());
    }

    @Test
    public void clear() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(4);
        glist.add(2);
        glist.add(3);
        glist.clear();
        assertEquals(0, glist.size());
        assertTrue(glist.isEmpty());
    }

    @Test
    public void get() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(4);
        glist.add(2);
        glist.add(3);
        try {
            glist.get(-1);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        try {
            glist.get(glist.size());
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        assertEquals(new Integer(1), glist.get(0));
        assertEquals(new Integer(3), glist.get(5));
        assertEquals(new Integer(2), glist.get(4));
    }

    @Test
    public void set() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(4);
        glist.add(2);
        glist.add(3);
        try {
            glist.set(0, null);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        try {
            glist.set(-1, 1);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        try {
            glist.set(glist.size(), 1);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        assertEquals(new Integer(1), glist.set(0, 9));
        assertEquals(new Integer(3), glist.set(glist.size() - 1, 9));
        assertEquals(new Integer(4), glist.set(2, 9));
        assertArrayEquals(new Integer[]{9, 2, 9, 4, 2, 9}, glist.toArray());
    }

    @Test
    public void add1() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(4);
        glist.add(2);
        try {
            glist.add(0, null);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        try {
            glist.add(-1, 1);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        glist.add(0, 5);
        glist.add(4, 5);
        glist.add(6, 5);
        assertArrayEquals(new Integer[]{5, 1, 2, 4, 5, 4, 5, 2}, glist.toArray());
        assertEquals(8, glist.size());
    }

    @Test
    public void remove1() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(4);
        glist.add(2);
        try {
            glist.remove(-1);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        try {
            glist.remove(glist.size());
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        assertEquals(new Integer(1), glist.remove(0));
        assertEquals(new Integer(4), glist.remove(1));
        assertEquals(new Integer(2), glist.remove(glist.size() - 1));
        assertEquals(2, glist.size());
        assertArrayEquals(new Integer[]{2, 4}, glist.toArray());
    }

    @Test
    public void lastIndexOf() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(4);
        glist.add(2);
        try {
            glist.lastIndexOf(null);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        assertEquals(0, glist.lastIndexOf(1));
        assertEquals(3, glist.lastIndexOf(4));
        assertEquals(4, glist.lastIndexOf(2));
        assertEquals(-1, glist.lastIndexOf(3));
    }

    @Test
    public void listIterator() {
        ListIterator<Integer> itr = glist.listIterator();
        assertEquals(0, itr.nextIndex());
        assertEquals(-1, itr.previousIndex());
        assertFalse(itr.hasNext());
        assertFalse(itr.hasPrevious());
        try {
            itr.next();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
        }
        try {
            itr.previous();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
        }
        try {
            itr.set(1);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
        itr.add(2);
        itr.add(3);
        itr.add(4);
        assertFalse(itr.hasNext());
        assertTrue(itr.hasPrevious());
        assertEquals(new Integer(4), itr.previous());
        assertEquals(1, itr.previousIndex());
        itr.set(1);
        assertEquals(2, itr.nextIndex());
        assertEquals(1, itr.previousIndex());
        assertEquals(new Integer(3), itr.previous());
        assertEquals(new Integer(2), itr.previous());
        itr.remove();
        try {
            itr.remove();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
    }

    @Test
    public void listIterator1() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(2);
        ListIterator<Integer> itr = glist.listIterator(2);
        assertEquals(new Integer(4), itr.next());
        assertEquals(new Integer(4), itr.previous());
        assertEquals(new Integer(2), itr.previous());
    }

    @Test
    public void subList() {
        glist.add(1);
        glist.add(2);
        glist.add(4);
        glist.add(2);
        glist.add(3);
        glist.add(0);
        glist.add(1);
        try {
            glist.subList(-2, 2);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        try {
            glist.subList(0, glist.size() + 1);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        try {
            glist.subList(5, 2);
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
        assertEquals(5, glist.subList(2, glist.size()).size());
        assertEquals(0, glist.subList(2, 2).size());
        assertFalse(glist.subList(0, 3).isEmpty());
        assertTrue(glist.subList(5, 5).isEmpty());
        assertTrue(glist.subList(0, 2).contains(2));
        assertFalse(glist.subList(0, 2).contains(4));
        Iterator<Integer> itr = glist.subList(0, 3).iterator();
        assertEquals(new Integer(1), itr.next());
        itr.next();
        itr.next();
        assertFalse(itr.hasNext());
        assertArrayEquals(new Integer[]{4, 2, 3, 0, 1}, glist.subList(2, 7).toArray());
        List<Integer> sublist = glist.subList(0, 2);
        sublist.add(5);
        assertArrayEquals(new Integer[]{1, 2, 5}, sublist.toArray());
        assertArrayEquals(new Integer[]{1, 2, 5, 4}, glist.subList(0, 4).toArray());
        assertTrue(sublist.remove(new Integer(5)));
        assertFalse(sublist.remove(new Integer(0)));
        assertArrayEquals(new Integer[]{1, 2, 4, 2}, glist.subList(0, 4).toArray());
        assertArrayEquals(new Integer[]{1, 2}, sublist.toArray());
        Integer[] arr = new Integer[2];
        assertArrayEquals(new Integer[]{1, 2}, sublist.toArray(arr));
        arr = new Integer[3];
        assertArrayEquals(new Integer[]{1, 2, null}, sublist.toArray(arr));
        assertTrue(sublist.containsAll(Arrays.asList(1, 2)));
        assertFalse(sublist.containsAll(Arrays.asList(1, 2, 3)));
        assertTrue(glist.subList(2, 5).addAll(Arrays.asList(9, 9)));
        assertArrayEquals(new Integer[]{1, 2, 4, 2, 3, 9, 9, 0, 1}, glist.toArray());
        sublist = glist.subList(2, 6);
        assertTrue(sublist.removeAll(Arrays.asList(2, 9)));
        assertFalse(sublist.removeAll(Arrays.asList(2, 1, 0)));
        assertArrayEquals(new Integer[]{1, 2, 4, 3, 9, 0, 1}, glist.toArray());
        sublist = glist.subList(1, 6);
        assertTrue(sublist.retainAll(Arrays.asList(2, 0)));
        assertArrayEquals(new Integer[]{1, 2, 0, 1}, glist.toArray());
        assertFalse(sublist.retainAll(Arrays.asList(1, 2, 0, 1)));
        glist.subList(1, 4).subList(0, 2).subList(1, 2).clear();
        assertArrayEquals(new Integer[]{1, 2, 1}, glist.toArray());
        assertEquals(new Integer(2), glist.subList(1, 2).get(0));
        assertEquals(new Integer(2), glist.subList(1, 2).set(0, 0));
        assertEquals(new Integer(0), glist.subList(1, 2).get(0));
        glist.subList(1, 2).add(2);
        glist.subList(1, 2).add(3);
        assertArrayEquals(new Integer[]{1, 0, 3, 2, 1}, glist.toArray());
        sublist = glist.subList(1, 4);
        assertEquals(new Integer(0), sublist.remove(0));
        assertArrayEquals(new Integer[]{3, 2}, sublist.toArray());
        assertEquals(1, sublist.indexOf(2));
        sublist.add(1);
        ListIterator<Integer> litr = sublist.listIterator();
        assertTrue(litr.hasNext());
        assertFalse(litr.hasPrevious());
        try {
            litr.previous();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
        }
        assertEquals(new Integer(3), litr.next());
        litr.next();
        assertEquals(new Integer(1), litr.next());
        assertFalse(litr.hasNext());
        assertTrue(litr.hasPrevious());
        try {
            litr.next();
            fail("Exception expected.");
        } catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
        }
        assertEquals(new Integer(1), litr.previous());
        litr.previous();
        assertEquals(1, litr.nextIndex());
        assertEquals(0, litr.previousIndex());
        litr.remove();
        assertArrayEquals(new Integer[]{3, 1}, sublist.toArray());
        litr.next();
        litr.set(5);
        assertArrayEquals(new Integer[]{3, 5}, sublist.toArray());
        litr.add(6);
        assertArrayEquals(new Integer[]{3, 5, 6}, sublist.toArray());
        assertArrayEquals(new Integer[]{1, 3, 5, 6, 1}, glist.toArray());
    }

    @Test
    public void resize() {
        for (int i = 0; i < 101; i++) {
            glist.add(i);
        }
        for (int i = 100; i >= 0; i--) {
            assertEquals(new Integer(i), glist.get(i));
        }
    }
}