package com.github.houbb.data.struct.core.util.list;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.io.Serializable;
import java.util.*;

/**
 * 数组链表
 * （1）实现基本功能
 * （2）提升性能
 * （3）抽象基础父类
 *
 * 构造器是有缺陷的，因为无法区分到底是 size 还是单个元素信息。
 * @author binbin.hou
 * @since 0.0.1
 * @param <E> elemType
 */
public class ArrayList<E> implements List<E>, Serializable {

    /**
     * 存放列表信息
     * @since 0.0.1
     */
    private transient Object[] array;

    /**
     * 存放数组元素的真实大小
     * @since 0.0.1
     *
     * @serial
     */
    private int size = 0;

    /**
     * 无参构造器
     * 默认大小为 8
     * @since 0.0.1
     */
    public ArrayList() {
        this(8);
    }

    /**
     * 指定大小创建对象
     * @param capacity 容量大小
     * @since 0.0.1
     */
    public ArrayList(final int capacity) {
        array = new Object[capacity];
        this.size = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean add(E e) {
        this.add(size, e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index < 0) {
            return false;
        }

        this.remove(index);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if(CollectionUtil.isEmpty(c)) {
            return false;
        }

        for(Object o : c) {
            if(!this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return this.addAll(size, c);
    }

    /**
     * 在指定位置，添加所有元素列表。
     *
     * @see #add(Object) 添加单个元素
     * @see #add(int, Object) 添加单个元素
     * @see #addAll(Collection) 添加集合
     * @param index 下标
     * @param c 集合
     * @return 是否添加成功
     */
    @Override
    public boolean addAll(final int index, Collection<? extends E> c) {
        // 参数校验
        indexRangeCheck(index);

        if(CollectionUtil.isEmpty(c)) {
            return false;
        }
        // 判断是否扩容
        this.ensureIncrease(this.size+c.size());

        // 保留后续的列表，避免被覆盖
        // 完整列表=index 之前+c+index 之后
        int afterIndexArraySize = this.size-1-index;
        Object[] afterIndexArray = {};
        if(afterIndexArraySize > 0) {
            afterIndexArray = new Object[afterIndexArraySize];
            System.arraycopy(array, index, afterIndexArray, 0, afterIndexArraySize);
        }

        // 开始从 index 设置覆盖
        int setStartIndex = index;
        for(E elem : c) {
            array[setStartIndex] = elem;
            setStartIndex++;
        }

        // 将 after index 的信息进行拷贝
        if(afterIndexArraySize > 0) {
            System.arraycopy(afterIndexArray, 0, array, setStartIndex, afterIndexArraySize);
        }

        this.size += c.size();
        return true;
    }

    /**
     * 移除集合所有信息
     * （1）批量删除，避免多次合并集合。
     * （2）可以对 removeVertex 单个进行优化，统一为单个的特例。
     *
     * 统一为使用 object 进行删除。
     * 这里因为单个 removeVertex(index) 需要返回下标导致二者无法统一。
     *
     * @see #remove(int) 按照下标移除
     * @see #remove(Object) 按照对象移除
     * @param c 集合
     * @return 是否成功移除
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return batchVenn(c, false);
    }

    /**
     * 返回两个集合的公共部分。
     *
     * @param c 集合
     * @return 是否公共部分处理成功。只有当列表发生变化时，才认为成功。
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return batchVenn(c, true);
    }

    /**
     * 韦恩图处理
     * （1）交集
     * （2）差集
     * （3）并集
     *
     * @param c 元素结合
     * @param containsFlag 包含时结果如何进行添加
     * @return 是否修改
     * @since 0.0.1
     */
    private boolean batchVenn(final Collection<?> c,
                              final boolean containsFlag) {
        //1. 快速返回
        // 取交集
        if(containsFlag && CollectionUtil.isEmpty(c)) {
            this.array = Lists.EMPTY_ARRAY;
            this.size = 0;
            return true;
        }
        // 取差集
        if(!containsFlag && CollectionUtil.isEmpty(c)) {
            return false;
        }

        // 用来存放结果信息
        Object[] newArray = new Object[size];
        int newSize = 0;

        // 循环处理
        for(int i = 0; i < size; i++) {
            E elem = this.get(i);
            // 其实 jdk 的实现算是一种比较麻烦的算法，比较简单的方式是循环遍历只有在不包含的列表，才放入。

            //1. 取差集，如果为 false 才放入。
            //2. 取交集，如果为 true 才放入。
            if(c.contains(elem) == containsFlag) {
                // 如果不在移除类表中，则添加到新的类表中。
                // 当然这么做的代价是，需要消耗两倍的内存。
                newArray[newSize++] = elem;
            }
        }

        // 判断是否发生了更新
        // 如果二者不相等，说明发生了变化.
        boolean modified = false;
        if(size != newSize) {
            modified = true;
        }

        // 更新元素信息
        if(modified) {
            this.array = newArray;
            this.size = newSize;
        }
        return modified;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.array = null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) this.array[index];
    }

    @Override
    public E set(int index, E element) {
        E oldValue = this.get(index);
        this.array[index] = element;
        return oldValue;
    }

    @Override
    public void add(int index, E element) {
        this.addAll(index, Collections.singletonList(element));
    }

    @Override
    public E remove(int index) {
        //1. 越界判断

        //2. 获取元素
        E elem = this.get(index);

        //3. 拷贝元素-如果 index 不是最后一个的话
        // 移动的长度
        int movedLength = this.size-1-index;
        if(movedLength > 0) {
            System.arraycopy(array, index+1, array, index, movedLength);
        }
        //4. 移动的位置末尾设置为 null，并减小 size
        this.array[size--] = null;

        return elem;
    }

    @Override
    public int indexOf(Object o) {
        for(int i = 0; i < size; i++) {
            if(ObjectUtil.isEqualsOrNull(o, this.array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for(int i = size-1; i >= 0; i--) {
            if(ObjectUtil.isEqualsOrNull(o, this.array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayListIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ArrayListListIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ArrayListListIterator(index);
    }


    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.array, size);
    }

    /**
     * 将当前 list 的元素返回到入参数组中。
     *
     * @param a the array into which the elements of the list are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @param <T> 泛型
     * @return an array containing the elements of the list
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // 如果 a 的大小不够，则直接新建一个列表
        if(ObjectUtil.isNull(a) || (a.length < this.size)) {
            return (T[]) Arrays.copyOf(this.array, size, a.getClass());
        }

        // 如果 a.length 足够大，那么就将 array 的信息直接赋值到数组 a
        System.arraycopy(this.array, 0, a, 0, size);

        // 这里感觉 jdk 源码有 BUG，应该讲后面的所有元素都设置为 null
        if(a.length > size) {
            for(int i = size; i < a.length; i++) {
                a[i] = null;
            }
        }
        return a;
    }

    /**
     * 截取整个列表
     * 这个列表和 jdk 内置的不同，是属于一份拷贝。
     *
     * @param fromIndex 开始下标
     * @param toIndex 结束下标
     * @return 截取的列表集合
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        final int size = toIndex-fromIndex;
        if(size <= 0) {
            return Collections.emptyList();
        }

        // 做一份拷贝，避免坑
        List<E> newList = new ArrayList<>(size);

        for(int i = fromIndex; i <= toIndex; i++) {
            newList.add(this.get(i));
        }
        return newList;
    }

    /**
     * 确认是否需要扩容
     * （1）如果 {@link #array} 的大小小于 minSize，则进行扩容。
     * （2）直接2倍容量。这个参数可以调整，个人更加倾向于 1.5
     * @param minSize 最小容量
     * @since 0.0.1
     */
    private void ensureIncrease(final int minSize) {
        if(array.length < minSize) {
            // 复制原来的信息到新数组，此处直接使用 copy 更快
            array = Arrays.copyOf(array, minSize*2);
        }
    }

    /**
     * 确认是否需要扩容
     * （1）如果 {@link #array} 的容量为 maxSize 的两倍及其以上，则进行动态缩减。
     * 或者类似 redis，直接预留在这里。避免下次还要扩容
     * @param maxSize 最大大小
     * @since 0.0.1
     */
    private void ensureDecrease(final int maxSize) {

    }

    /**
     * 范围检查
     * @param index 索引范围
     * @since 0.0.1
     */
    private void indexRangeCheck(final int index) {
        if(index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index out of range: " + index + ", it must be in [0, " + size +"]");
        }
    }

    private class ArrayListIterator implements Iterator<E> {

        /**
         * 游标
         */
        protected int cursor;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            E result = ArrayList.this.get(cursor);
            cursor++;
            return result;
        }

        @Override
        public void remove() {
            ArrayList.this.remove(cursor);
        }

    }

    private class ArrayListListIterator extends ArrayListIterator implements ListIterator<E> {

        public ArrayListListIterator(final int index) {
            super();
            cursor = index;
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor-1;
        }

        @Override
        public E previous() {
            return ArrayList.this.get(--cursor);
        }

        @Override
        public void set(E e) {
            int lastReturn = cursor-1;
            ArrayList.this.set(lastReturn, e);
        }

        @Override
        public void add(E e) {
            // 当前 index 添加元素
            ArrayList.this.add(cursor, e);
            cursor++;
        }
    }

}
