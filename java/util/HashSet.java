/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.util;

import java.io.InvalidObjectException;

/**
 * 常用方法含义速记：
 *  boolean add(E e) 将指定的元素添加到此集合（如果尚未存在）。
 *
 *  void clear() 从此集合中删除所有元素。
 *
 *  Object clone() 返回此 HashSet实例的浅层副本：元素本身不被克隆。
 *
 *  boolean contains(Object o) 如果此集合包含指定的元素，则返回true。
 *
 *  boolean isEmpty() 如果此集合不包含元素，则返回true。
 *
 *  Iterator<E> iterator() 返回此集合中元素的迭代器。
 *
 *  boolean remove(Object o) 如果存在，则从该集合中删除指定的元素。
 *
 *  int size() 返回此集合中的元素数（其基数）。
 *
 *  Spliterator<E> spliterator() 在此集合中的元素上创建late-binding和故障快速 Spliterator。更详细
 *  解释https://blog.csdn.net/shenshaoming/article/details/100637484
 */

/**
 * 相关问题：
 *  - 详细说明private的writeObject是怎么调用的？https://blog.csdn.net/u014653197/article/details/78114041
 *
 */
public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>, Cloneable, java.io.Serializable
{
    static final long serialVersionUID = -5024744406713321676L;

    private transient HashMap<E,Object> map;

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();

    /**
     * Constructs a new, empty set; the backing HashMap instance has
     * default initial capacity (16) and load factor (0.75).
     *
     * 构造一个新的空集；支持的HashMap实例具有默认的初始容量（16）和负载因子（0.75）。
     */
    public HashSet() {
        map = new HashMap<>();
    }

    /**
     * Constructs a new set containing the elements in the specified
     * collection.  The HashMap is created with default load factor
     * (0.75) and an initial capacity sufficient to contain the elements in
     * the specified collection.
     *
     * 构造一个新集合，其中包含指定集合中的元素。使用默认的加载因子（0.75）和足以容纳指定集合中的元素的初始容量创建HashMap。
     *
     * @param c the collection whose elements are to be placed into this set
     * @throws NullPointerException if the specified collection is null
     */
    public HashSet(Collection<? extends E> c) {
        map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
        addAll(c);
    }

    /**
     * Constructs a new, empty set; the backing HashMap instance has
     * the specified initial capacity and the specified load factor.
     *
     * 构造一个新的空集；支持的HashMap实例具有指定的初始容量和指定的负载系数。
     *
     * @param      initialCapacity   the initial capacity of the hash map
     * @param      loadFactor        the load factor of the hash map
     * @throws     IllegalArgumentException if the initial capacity is less
     *             than zero, or if the load factor is nonpositive
     */
    public HashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    /**
     * Constructs a new, empty set; the backing HashMap instance has
     * the specified initial capacity and default load factor (0.75).
     *
     * 构造一个新的空集；支持的HashMap实例具有指定的初始容量和默认负载因子（0.75）。
     *
     * @param      initialCapacity   the initial capacity of the hash table
     * @throws     IllegalArgumentException if the initial capacity is less
     *             than zero
     */
    public HashSet(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }

    /**
     * Constructs a new, empty linked hash set.  (This package private
     * constructor is only used by LinkedHashSet.) The backing
     * HashMap instance is a LinkedHashMap with the specified initial
     * capacity and the specified load factor.
     *
     * 构造一个新的空链接哈希集。（此程序包私有构造函数仅由LinkedHashSet使用。）
     * 支持的HashMap实例是具有指定的初始容量和指定的加载因子的LinkedHashMap。
     *
     * @param      initialCapacity   the initial capacity of the hash map
     * @param      loadFactor        the load factor of the hash map
     * @param      dummy             ignored (distinguishes this
     *             constructor from other int, float constructor.)
     * @throws     IllegalArgumentException if the initial capacity is less
     *             than zero, or if the load factor is nonpositive
     */
    HashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
    }

    /**
     * Returns an iterator over the elements in this set.  The elements
     * are returned in no particular order.
     *
     * 返回此集合中元素的迭代器。元素以不特定的顺序返回。
     *
     * @return an Iterator over the elements in this set
     * @see ConcurrentModificationException
     */
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    /**
     * Returns the number of elements in this set (its cardinality).
     *
     * 返回此集合中的元素数（其基数）。
     *
     * @return the number of elements in this set (its cardinality)
     */
    public int size() {
        return map.size();
    }

    /**
     * Returns true if this set contains no elements.
     *
     * 如果此集合不包含任何元素，则返回true。
     *
     * @return true if this set contains no elements
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Returns true if this set contains the specified element.
     * More formally, returns true if and only if this set
     * contains an element e such that
     * (o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e)).
     *
     * 如果此集合包含指定的元素，则返回true。更正式地讲，当且仅当此集合包含元素e使得
     * （o == null？e == null：o.equals（e））时才返回true。
     *
     * @param o element whose presence in this set is to be tested
     * @return true if this set contains the specified element
     */
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * More formally, adds the specified element e to this set if
     * this set contains no element e2 such that
     * (e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2)).
     * If this set already contains the element, the call leaves the set
     * unchanged and returns false.
     *
     * 如果指定的元素尚不存在，则将其添加到该集合中。更正式地说，如果此集合不包含元素e2使
     * 得（e == null？e2 == null：e.equals（e2）），则将指定的元素e添加到该集合中。
     * 如果此集合已包含该元素，则调用将使该集合保持不变并返回false。
     *
     * @param e element to be added to this set
     * @return true if this set did not already contain the specified
     * element
     */
    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }

    /**
     * Removes the specified element from this set if it is present.
     * More formally, removes an element e such that
     * (o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e)),
     * if this set contains such an element.  Returns true if
     * this set contained the element (or equivalently, if this set
     * changed as a result of the call).  (This set will not contain the
     * element once the call returns.)
     *
     * 如果存在，则从此集合中删除指定的元素。更正式地讲，如果此集合包含这样的元素，
     * 则删除元素e使其（o == null？e == null：o.equals（e））。
     * 如果此集合包含元素，则返回true（或者等效地，如果此集合由于调用而更改），
     * 则返回true。 （一旦调用返回，此集合将不包含该元素。）
     *
     * @param o object to be removed from this set, if present
     * @return true if the set contained the specified element
     */
    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }

    /**
     * Removes all of the elements from this set.
     * The set will be empty after this call returns.
     *
     * 从该集合中删除所有元素。该调用返回后，该集合将为空。
     */
    public void clear() {
        map.clear();
    }

    /**
     * Returns a shallow copy of this HashSet instance: the elements
     * themselves are not cloned.
     *
     * 返回此HashSet实例的浅表副本：元素本身未克隆。
     *
     * @return a shallow copy of this set
     */
    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            HashSet<E> newSet = (HashSet<E>) super.clone();
            newSet.map = (HashMap<E, Object>) map.clone();
            return newSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    /**
     * Save the state of this HashSet instance to a stream (that is,
     * serialize it).
     *
     * 将此HashSet实例的状态保存到流（即，对其进行序列化）。
     *
     * @serialData The capacity of the backing HashMap instance
     *             (int), and its load factor (float) are emitted, followed by
     *             the size of the set (the number of elements it contains)
     *             (int), followed by all of its elements (each an Object) in
     *             no particular order.
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException {
        // Write out any hidden serialization magic
        s.defaultWriteObject();

        // Write out HashMap capacity and load factor
        s.writeInt(map.capacity());
        s.writeFloat(map.loadFactor());

        // Write out size
        s.writeInt(map.size());

        // Write out all elements in the proper order.
        for (E e : map.keySet())
            s.writeObject(e);
    }

    /**
     * Reconstitute the HashSet instance from a stream (that is,
     * deserialize it).
     *
     * 从流中重构HashSet实例（即，将其反序列化）。
     */
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        // Read in any hidden serialization magic
        s.defaultReadObject();

        // Read capacity and verify non-negative.
        int capacity = s.readInt();
        if (capacity < 0) {
            throw new InvalidObjectException("Illegal capacity: " +
                                             capacity);
        }

        // Read load factor and verify positive and non NaN.
        float loadFactor = s.readFloat();
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new InvalidObjectException("Illegal load factor: " +
                                             loadFactor);
        }

        // Read size and verify non-negative.
        int size = s.readInt();
        if (size < 0) {
            throw new InvalidObjectException("Illegal size: " +
                                             size);
        }

        // Set the capacity according to the size and load factor ensuring that
        // the HashMap is at least 25% full but clamping to maximum capacity.
        capacity = (int) Math.min(size * Math.min(1 / loadFactor, 4.0f),
                HashMap.MAXIMUM_CAPACITY);

        // Create backing HashMap
        map = (((HashSet<?>)this) instanceof LinkedHashSet ?
               new LinkedHashMap<E,Object>(capacity, loadFactor) :
               new HashMap<E,Object>(capacity, loadFactor));

        // Read in all elements in the proper order.
        for (int i=0; i<size; i++) {
            @SuppressWarnings("unchecked")
                E e = (E) s.readObject();
            map.put(e, PRESENT);
        }
    }

    /**
     * Creates a <em><a href="Spliterator.html#binding">late-binding</a></em>
     * and <em>fail-fast</em> {@link Spliterator} over the elements in this
     * set.
     *
     * 在此集合中的元素上创建late-binding和故障快速 Spliterator。更详细
     * 解释https://blog.csdn.net/shenshaoming/article/details/100637484
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#SIZED} and
     * {@link Spliterator#DISTINCT}.  Overriding implementations should document
     * the reporting of additional characteristic values.
     *
     * @return a {@code Spliterator} over the elements in this set
     * @since 1.8
     */
    public Spliterator<E> spliterator() {
        return new HashMap.KeySpliterator<E,Object>(map, 0, -1, 0, 0);
    }
}
