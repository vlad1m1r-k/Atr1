package com.vladimir.map;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MyHashMapTest {
    private MyHashMap<String, Integer> mhm;

    @Before
    public void init() {
        mhm = new MyHashMap<>();
    }

    @Test
    public void testSizeZero() {
        assertEquals(0, mhm.size());
    }

    @Test
    public void testSizeNonZero() {
        mhm.put("a", 1);
        assertEquals(1, mhm.size());
    }

    @Test
    public void testIsEmptyZeroSize() {
        assertTrue(mhm.isEmpty());
    }

    @Test
    public void testIsEmptyNonZeroSize() {
        mhm.put("a", 1);
        assertFalse(mhm.isEmpty());
    }

    @Test
    public void testContainsKeyContains() {
        mhm.put("a", 1);
        mhm.put("aaa", 1);
        mhm.put("abbaa", 1);
        assertTrue(mhm.containsKey("abbaa"));
        assertTrue(mhm.containsKey("aaa"));
        assertTrue(mhm.containsKey("a"));
    }

    @Test
    public void testContainsKeyNotContains() {
        mhm.put("a", 1);
        mhm.put("aaa", 1);
        mhm.put("abbaa", 1);
        assertFalse(mhm.containsKey("a1aa"));
        assertFalse(mhm.containsKey(null));
    }

    @Test
    public void testContainsValueContains() {
        mhm.put("a", 1);
        mhm.put("aaa", 3);
        mhm.put("abbaa", 7);
        assertTrue(mhm.containsValue(1));
        assertTrue(mhm.containsValue(3));
        assertTrue(mhm.containsValue(7));
    }

    @Test
    public void testContainsValueNotContains() {
        mhm.put("a", 1);
        mhm.put("aaa", 3);
        mhm.put("abbaa", 7);
        assertFalse(mhm.containsValue(66));
    }

    @Test
    public void testGetContainsKey() {
        mhm.put("a", 1);
        mhm.put("aaa", 3);
        mhm.put("abbaa", 7);
        assertEquals(new Integer(1), mhm.get("a"));
        assertEquals(new Integer(3), mhm.get("aaa"));
    }

    @Test
    public void testGetNotContainsKey() {
        mhm.put("a", 1);
        mhm.put("aaa", 3);
        mhm.put("abbaa", 7);
        assertNull(mhm.get("a1"));
        assertNull(mhm.get(null));
    }

    @Test
    public void testPutNewItem() {
        assertNull(mhm.put("a", 1));
        assertNull(mhm.put("b", null));
        assertNull(mhm.put(null, null));
        assertEquals(3, mhm.size());
    }

    @Test
    public void testPutReplaceItem() {
        mhm.put("a", 1);
        mhm.put("aaa", 3);
        mhm.put("abbaa", 7);
        assertEquals(new Integer(1), mhm.put("a", 2));
        assertEquals(new Integer(3), mhm.put("aaa", null));
        assertEquals(3, mhm.size());
    }

    @Test
    public void testRemoveExisting() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        mhm.put("d", 4);
        mhm.put("e", 5);
        mhm.put(null, 0);
        assertEquals(6, mhm.size());
        assertEquals(new Integer(2), mhm.remove("b"));
        assertEquals(new Integer(4), mhm.remove("d"));
        assertEquals(new Integer(0), mhm.remove(null));
        assertEquals(3, mhm.size());
    }

    @Test
    public void testRemoveNotExisting() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        mhm.put("d", 4);
        mhm.put("e", 5);
        assertNull(mhm.remove("r"));
        assertNull(mhm.remove(null));
        assertEquals(5, mhm.size());
    }

    @Test
    public void testPutAllSuccess() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("a", 1);
        hashMap.put("b", 2);
        mhm.putAll(hashMap);
        assertEquals(2, mhm.size());
        assertTrue(mhm.containsKey("a"));
        assertTrue(mhm.containsKey("b"));
        assertTrue(mhm.containsValue(1));
        assertTrue(mhm.containsValue(2));
    }

    @Test
    public void testClearSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        mhm.put("d", 4);
        mhm.put("e", 5);
        mhm.clear();
        assertTrue(mhm.isEmpty());
        assertEquals(0, mhm.size());
    }

    @Test
    public void testResizeSuccess() {
        for (int i = 0; i < 100; i++) {
            mhm.put(String.valueOf(i), i);
        }
        assertEquals(100, mhm.size());
    }

    @Test
    public void testEntrySetSizeZero() {
        assertEquals(0, mhm.entrySet().size());
    }

    @Test
    public void testEntrySetSizeNonZero() {
        mhm.put("a", 1);
        assertEquals(1, mhm.entrySet().size());
    }

    @Test
    public void testEntrySetIsEmptyEmpty() {
        assertTrue(mhm.entrySet().isEmpty());
    }

    @Test
    public void testEntrySetIsEmptyNotEmpty() {
        mhm.put("a", 1);
        assertFalse(mhm.entrySet().isEmpty());
    }

    @Test
    public void testEntrySetContainsContains() {
        mhm.put("a", 1);
        assertTrue(mhm.entrySet().contains(new MyHashMap.Node<>("a", 1)));
    }

    @Test
    public void testEntrySetContainsNotContains() {
        mhm.put("a", 1);
        assertFalse(mhm.entrySet().contains(new MyHashMap.Node<>("a", 2)));
    }

    @Test
    public void testEntrySetIteratorHasNextHas() {
        mhm.put("a", 1);
        assertTrue(mhm.entrySet().iterator().hasNext());
    }

    @Test
    public void testEntrySetIteratorHasNextNotHas() {
        assertFalse(mhm.entrySet().iterator().hasNext());
        mhm.put("a", 1);
        Iterator itr = mhm.entrySet().iterator();
        itr.next();
        assertFalse(itr.hasNext());
    }

    @Test
    public void testEntrySetIteratorNextSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        Iterator itr = mhm.entrySet().iterator();
        assertEquals(new MyHashMap.Node<>("a", 1), itr.next());
        assertEquals(new MyHashMap.Node<>("b", 2), itr.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testEntrySetIteratorNextZeroSizeException() {
        mhm.entrySet().iterator().next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testEntrySetIteratorNextEndOfListException() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        Iterator itr = mhm.entrySet().iterator();
        itr.next();
        itr.next();
        itr.next();
    }

    @Test
    public void testEntrySetToArraySuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        assertArrayEquals(new MyHashMap.Node[]{new MyHashMap.Node<>("a", 1), new MyHashMap.Node<>("b", 2)}, mhm.entrySet().toArray());
    }

    @Test
    public void testEntrySetToArray2Success() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        MyHashMap.Node[] arr = new MyHashMap.Node[2];
        assertArrayEquals(new MyHashMap.Node[]{new MyHashMap.Node<>("a", 1), new MyHashMap.Node<>("b", 2)}, mhm.entrySet().toArray(arr));
        arr = new MyHashMap.Node[0];
        assertArrayEquals(new MyHashMap.Node[]{new MyHashMap.Node<>("a", 1), new MyHashMap.Node<>("b", 2)}, mhm.entrySet().toArray(arr));
        arr = new MyHashMap.Node[]{null, null, null};
        assertArrayEquals(new MyHashMap.Node[]{new MyHashMap.Node<>("a", 1), new MyHashMap.Node<>("b", 2), null}, mhm.entrySet().toArray(arr));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAddException() {
        mhm.entrySet().add(new MyHashMap.Node<>("1", 1));
    }

    @Test
    public void testEntrySetRemoveSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        Set<Map.Entry<String, Integer>> entrySet = mhm.entrySet();
        assertTrue(entrySet.remove(new MyHashMap.Node<>("c", 3)));
        assertTrue(entrySet.remove(new MyHashMap.Node<>("b", 2)));
        assertEquals(1, entrySet.size());
        assertEquals(1, mhm.size());
    }

    @Test
    public void testEntrySetRemoveNotSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        assertFalse(mhm.entrySet().remove(new MyHashMap.Node<>("d", 5)));
        assertFalse(mhm.entrySet().remove(null));
    }

    @Test
    public void testEntrySetContainsAllContains() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertTrue(mhm.entrySet().containsAll(Arrays.asList(new MyHashMap.Node<>("b", 2), new MyHashMap.Node<>("a", 1))));
    }

    @Test
    public void testEntrySetContainsAllNotContains() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        List<Object> list = null;
        assertFalse(mhm.entrySet().containsAll(list));
        list = new ArrayList<>(Arrays.asList());
        assertFalse(mhm.entrySet().containsAll(list));
        list = new ArrayList<>(Arrays.asList(new MyHashMap.Node<>("d", 5), null));
        assertFalse(mhm.entrySet().containsAll(list));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAddAllException() {
        mhm.entrySet().addAll(Arrays.asList(new MyHashMap.Node<>("a", 1)));
    }

    @Test
    public void testEntrySetRetainAllSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertTrue(mhm.entrySet().retainAll(Arrays.asList(new MyHashMap.Node<>("c", 3), null)));
        assertEquals(1, mhm.size());
        assertTrue(mhm.entrySet().contains(new MyHashMap.Node<>("c", 3)));
    }

    @Test
    public void testEntrySetRetainAllNotSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        assertFalse(mhm.entrySet().retainAll(Arrays.asList(new MyHashMap.Node<>("a", 1), new MyHashMap.Node<>("b", 2))));
    }

    @Test
    public void testEntrySetRemoveAllSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertTrue(mhm.entrySet().removeAll(Arrays.asList(new MyHashMap.Node<>("a", 1), new MyHashMap.Node<>("b", 2))));
        assertEquals(1, mhm.size());
    }

    @Test
    public void testEntrySetRemoveAllNoSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertFalse(mhm.entrySet().removeAll(Arrays.asList(new MyHashMap.Node<>("d", 1), new MyHashMap.Node<>("e", 2))));
        assertEquals(3, mhm.size());
    }

    @Test
    public void testEntrySetClearSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        mhm.entrySet().clear();
        assertTrue(mhm.isEmpty());
        assertEquals(0, mhm.size());
    }

    @Test
    public void testKeySetSizeZero() {
        assertEquals(0, mhm.keySet().size());
    }

    @Test
    public void testKeySetSizeNonZero() {
        mhm.put("a", 1);
        assertEquals(1, mhm.keySet().size());
    }

    @Test
    public void testKeySetIsEmptyEmpty() {
        assertTrue(mhm.keySet().isEmpty());
    }

    @Test
    public void testKeySetIsEmptyNotEmpty() {
        mhm.put("a", 1);
        assertFalse(mhm.keySet().isEmpty());
    }

    @Test
    public void testKeySetContainsContains() {
        mhm.put("a", 1);
        assertTrue(mhm.keySet().contains("a"));
    }

    @Test
    public void testKeySetContainsNotContains() {
        mhm.put("a", 1);
        assertFalse(mhm.keySet().contains("b"));
    }

    @Test
    public void testKeySetIteratorHasNextHas() {
        mhm.put("a", 1);
        assertTrue(mhm.keySet().iterator().hasNext());
    }

    @Test
    public void testKeySetIteratorHasNextNotHas() {
        assertFalse(mhm.keySet().iterator().hasNext());
        mhm.put("a", 1);
        Iterator itr = mhm.keySet().iterator();
        itr.next();
        assertFalse(itr.hasNext());
    }

    @Test
    public void testKeySetIteratorNextSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        Iterator itr = mhm.keySet().iterator();
        assertEquals("a", itr.next());
        assertEquals("b", itr.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testKeySetIteratorNextZeroSizeException() {
        mhm.keySet().iterator().next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testKeySetIteratorNextEndOfListException() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        Iterator itr = mhm.keySet().iterator();
        itr.next();
        itr.next();
        itr.next();
    }

    @Test
    public void testKeySetToArraySuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        assertArrayEquals(new String[]{"a", "b"}, mhm.keySet().toArray());
    }

    @Test
    public void testKeySetToArray2Success() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        String[] arr = new String[2];
        assertArrayEquals(new String[]{"a", "b"}, mhm.keySet().toArray(arr));
        arr = new String[0];
        assertArrayEquals(new String[]{"a", "b"}, mhm.keySet().toArray(arr));
        arr = new String[]{null, null, null};
        assertArrayEquals(new String[]{"a", "b", null}, mhm.keySet().toArray(arr));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKeySetAddException() {
        mhm.keySet().add("1");
    }

    @Test
    public void testKeySetRemoveSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        Set<String> keySet = mhm.keySet();
        assertTrue(keySet.remove("c"));
        assertTrue(keySet.remove("b"));
        assertEquals(1, keySet.size());
        assertEquals(1, mhm.size());
    }

    @Test
    public void testKeySetRemoveNotSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        assertFalse(mhm.keySet().remove("d"));
        assertFalse(mhm.keySet().remove(null));
    }

    @Test
    public void testKeySetContainsAllContains() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertTrue(mhm.keySet().containsAll(Arrays.asList("b", "a")));
    }

    @Test
    public void testKeySetContainsAllNotContains() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        List<String> list = null;
        assertFalse(mhm.keySet().containsAll(list));
        list = new ArrayList<>(Arrays.asList());
        assertFalse(mhm.keySet().containsAll(list));
        list = new ArrayList<>(Arrays.asList("d", null));
        assertFalse(mhm.keySet().containsAll(list));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKeySetAddAllException() {
        mhm.keySet().addAll(Arrays.asList("a", "b"));
    }

    @Test
    public void testKeySetRetainAllSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertTrue(mhm.keySet().retainAll(Arrays.asList("c", null)));
        assertEquals(1, mhm.size());
        assertTrue(mhm.keySet().contains("c"));
    }

    @Test
    public void testKeySetRetainAllNotSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        assertFalse(mhm.keySet().retainAll(Arrays.asList("a", "b")));
    }

    @Test
    public void testKeySetRemoveAllSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertTrue(mhm.keySet().removeAll(Arrays.asList("a", "b")));
        assertEquals(1, mhm.size());
    }

    @Test
    public void testKeySetRemoveAllNoSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertFalse(mhm.keySet().removeAll(Arrays.asList("d", "e")));
        assertEquals(3, mhm.size());
    }

    @Test
    public void testKeySetClearSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        mhm.keySet().clear();
        assertTrue(mhm.isEmpty());
        assertEquals(0, mhm.size());
    }

    @Test
    public void testValuesSizeZero() {
        assertEquals(0, mhm.values().size());
    }

    @Test
    public void testValuesSizeNonZero() {
        mhm.put("a", 1);
        assertEquals(1, mhm.values().size());
    }

    @Test
    public void testValuesIsEmptyEmpty() {
        assertTrue(mhm.values().isEmpty());
    }

    @Test
    public void testValuesIsEmptyNotEmpty() {
        mhm.put("a", 1);
        assertFalse(mhm.values().isEmpty());
    }

    @Test
    public void testValuesContainsContains() {
        mhm.put("a", 1);
        assertTrue(mhm.values().contains(1));
    }

    @Test
    public void testValuesContainsNotContains() {
        mhm.put("a", 1);
        assertFalse(mhm.values().contains(2));
    }

    @Test
    public void testValuesIteratorHasNextHas() {
        mhm.put("a", 1);
        assertTrue(mhm.values().iterator().hasNext());
    }

    @Test
    public void testValuesIteratorHasNextNotHas() {
        assertFalse(mhm.values().iterator().hasNext());
        mhm.put("a", 1);
        Iterator itr = mhm.values().iterator();
        itr.next();
        assertFalse(itr.hasNext());
    }

    @Test
    public void testValuesIteratorNextSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        Iterator itr = mhm.values().iterator();
        assertEquals(1, itr.next());
        assertEquals(2, itr.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testValuesIteratorNextZeroSizeException() {
        mhm.values().iterator().next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testValuesIteratorNextEndOfListException() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        Iterator itr = mhm.values().iterator();
        itr.next();
        itr.next();
        itr.next();
    }

    @Test
    public void testValuesToArraySuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        assertArrayEquals(new Integer[]{1, 2}, mhm.values().toArray());
    }

    @Test
    public void testValuesToArray2Success() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        Integer[] arr = new Integer[2];
        assertArrayEquals(new Integer[]{1, 2}, mhm.values().toArray(arr));
        arr = new Integer[0];
        assertArrayEquals(new Integer[]{1, 2}, mhm.values().toArray(arr));
        arr = new Integer[]{null, null, null};
        assertArrayEquals(new Integer[]{1, 2, null}, mhm.values().toArray(arr));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValuesAddException() {
        mhm.values().add(1);
    }

    @Test
    public void testValuesRemoveSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        Collection<Integer> values = mhm.values();
        assertTrue(values.remove(3));
        assertTrue(values.remove(2));
        assertEquals(1, values.size());
        assertEquals(1, mhm.size());
    }

    @Test
    public void testValuesRemoveNotSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        assertFalse(mhm.values().remove(5));
        assertFalse(mhm.values().remove(null));
    }

    @Test
    public void testValuesContainsAllContains() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertTrue(mhm.values().containsAll(Arrays.asList(2, 1)));
    }

    @Test
    public void testValuesContainsAllNotContains() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        List<Object> list = null;
        assertFalse(mhm.values().containsAll(list));
        list = new ArrayList<>(Arrays.asList());
        assertFalse(mhm.values().containsAll(list));
        list = new ArrayList<>(Arrays.asList(5, null));
        assertFalse(mhm.values().containsAll(list));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValuesAddAllException() {
        mhm.values().addAll(Arrays.asList(1, 2));
    }

    @Test
    public void testValuesRetainAllSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertTrue(mhm.values().retainAll(Arrays.asList(3, null)));
        assertEquals(1, mhm.size());
        assertTrue(mhm.values().contains(3));
    }

    @Test
    public void testValuesRetainAllNotSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        assertFalse(mhm.values().retainAll(Arrays.asList(1, 2)));
    }

    @Test
    public void testValuesRemoveAllSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertTrue(mhm.values().removeAll(Arrays.asList(1, 2)));
        assertEquals(1, mhm.size());
    }

    @Test
    public void testValuesRemoveAllNoSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        assertFalse(mhm.values().removeAll(Arrays.asList(4, 5)));
        assertEquals(3, mhm.size());
    }

    @Test
    public void testValuesClearSuccess() {
        mhm.put("a", 1);
        mhm.put("b", 2);
        mhm.put("c", 3);
        mhm.values().clear();
        assertTrue(mhm.isEmpty());
        assertEquals(0, mhm.size());
    }
}