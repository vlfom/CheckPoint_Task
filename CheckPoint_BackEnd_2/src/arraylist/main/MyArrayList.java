package arraylist.main;

import java.time.LocalDateTime;
import java.util.Arrays;

public class MyArrayList <E> {
    private final static int AVERAGE_ADDITIONAL_CAPACITY = 10;
    private static Object[] DEFAULT_EMPTY_DATA = {};
    private static LocalDateTime[] DEFAULT_EMPTY_DATE = {};
    private int size;
    private Object[] elements = {};
    private LocalDateTime[] elementsDate = {};

    public MyArrayList() {
        this.elements = DEFAULT_EMPTY_DATA;
        this.elementsDate = DEFAULT_EMPTY_DATE;
    }

    public MyArrayList(int size) {
        if (size == 0) {
            this.elements = DEFAULT_EMPTY_DATA;
            this.elementsDate = DEFAULT_EMPTY_DATE;
        } else if (size > 0) {
            this.elements = new Object[size];
            this.elementsDate = new LocalDateTime[size];
        } else
            throw new IllegalArgumentException("Illegal MyList size: " + size);
    }

    private void ensureCapacity(int size) {
        int capacity = elements.length;
        if( capacity == 0 )
            capacity = 10 ;
        while (capacity < size + AVERAGE_ADDITIONAL_CAPACITY)
            capacity *= 2;
        elements = Arrays.copyOf(elements, capacity);
        elementsDate = Arrays.copyOf(elementsDate, capacity);
    }

    public void removeBeforeDate(LocalDateTime date) {
        for (int i = 0; i < size; ++i)
            if (date.isAfter(elementsDate[i])) {
                remove(i);
                removeBeforeDate(date);
                break;
            }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public int indexOf(Object o) {
        if (o != null)
            for (int i = 0; i < size; i++)
                if (o.equals(elements[i]))
                    return i;
        return -1;
    }

    public int lastIndexOf(Object o) {
        if (o != null)
            for (int i = size - 1; i >= 0; i--)
                if (o.equals(elements[i]))
                    return i;
        return -1;
    }

    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    public E get(int index) {
        checkRange(index);
        return (E) elements[index];
    }

    public void set(int index, E e) {
        elements[index] = e;
        elementsDate[index] = LocalDateTime.now();
    }

    public void add(E e) {
        ensureCapacity(size + 1);
        elements[size] = e;
        elementsDate[size++] = LocalDateTime.now();
    }

    public void add(int index, E e) {
        checkRange(index);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1,
                size - index);
        System.arraycopy(elementsDate, index, elementsDate, index + 1,
                size - index);
        elements[index] = e;
        elementsDate[index] = LocalDateTime.now();
        size++;
    }

    public void remove(int index) {
        checkRange(index);
        int movedCount = size - index - 1;
        if (movedCount > 0) {
            System.arraycopy(elements, index + 1, elements, index,
                    movedCount);
            System.arraycopy(elementsDate, index + 1, elementsDate, index,
                    movedCount);
        }
        elements[--size] = null;
        elementsDate[size] = null;
    }

    public boolean remove(Object o) {
        if (o != null) {
            for (int index = 0; index < size; index++)
                if (o.equals(elements[index])) {
                    int numMoved = size - index - 1;
                    if (numMoved > 0) {
                        System.arraycopy(elements, index + 1, elements, index,
                                numMoved);
                        System.arraycopy(elementsDate, index + 1, elementsDate, index,
                                numMoved);
                    }
                    elements[--size] = null;
                    elementsDate[size] = null;
                    return true;
                }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
            elementsDate[i] = null;
        }
        size = 0;
    }

    protected void removeRange(int fromIndex, int toIndex) {
        int numMoved = size - toIndex;
        System.arraycopy(elements, toIndex, elements, fromIndex,
                numMoved);
        System.arraycopy(elementsDate, toIndex, elementsDate, fromIndex,
                numMoved);
        int newSize = size - (toIndex - fromIndex);
        for (int i = newSize; i < size; i++) {
            elements[i] = null;
            elementsDate[i] = null;
        }
        size = newSize;
    }

    private void checkRange(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException("Element with index " + index + " does not exist.");
    }

    private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
        s.defaultWriteObject();
        s.writeInt(size);
        for (int i = 0; i < size; i++)
            s.writeObject(elements[i]);
    }

    private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
        elements = DEFAULT_EMPTY_DATA;
        s.defaultReadObject();
        s.readInt();
        if (size > 0) {
            ensureCapacity(size);
            Object[] a = elements;
            LocalDateTime[] b = elementsDate ;
            for (int i = 0; i < size; i++) {
                a[i] = s.readObject();
                elementsDate[i] = LocalDateTime.now() ;
            }
        }
    }
}
