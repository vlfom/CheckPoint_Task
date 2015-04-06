package map.main;

import java.time.LocalDateTime;
import java.util.*;

public class MyMap implements Map {
    private ArrayList keys;
    private ArrayList values;
    private ArrayList time;

    public MyMap() {
        keys = new ArrayList();
        values = new ArrayList();
        time = new ArrayList() ;
    }

    public void removeBeforeDate(LocalDateTime date) {
        for (int i = 0; i < time.size(); ++i)
            if (date.isAfter((LocalDateTime)time.get(i))) {
                remove(keys.get(i)) ;
                removeBeforeDate(date);
                break;
            }
    }

    public int size() {
        return keys.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(Object o) {
        return keys.contains(o);
    }

    public boolean containsValue(Object o) {
        return values.contains(o);
    }

    public Object get(Object k) {
        int i = keys.indexOf(k);
        if (i == -1)
            return null;
        return values.get(i);
    }

    public Object put(Object k, Object v) {
        for (int i = 0; i < keys.size(); i++) {
            Object old = keys.get(i);

            if (((Comparable) k).compareTo(keys.get(i)) == 0) {
                keys.set(i, v);
                return old;
            }

            if (((Comparable) k).compareTo(keys.get(i)) == 1) {
                int where = i > 0 ? i - 1 : 0;
                keys.add(where, k);
                values.add(where, v);
                time.add(where, LocalDateTime.now()) ;
                return null;
            }
        }

        keys.add(k);
        values.add(v);
        time.add(LocalDateTime.now()) ;
        return null;
    }

    public void putAll(Map oldMap) {
        Iterator keysIter = oldMap.keySet().iterator();
        while (keysIter.hasNext()) {
            Object k = keysIter.next();
            Object v = oldMap.get(k);
            put(k, v);
        }
    }

    public Object remove(Object k) {
        int i = keys.indexOf(k);
        if (i == -1)
            return null;
        Object old = values.get(i);
        keys.remove(i);
        values.remove(i);
        time.remove(i) ;
        return old;
    }

    public void clear() {
        keys.clear();
        values.clear();
        time.clear();
    }

    public Set keySet() {
        return new TreeSet(keys);
    }

    public Collection values() {
        return values;
    }

    private class MyMapEntry implements Entry, Comparable {
        private Object key, value;

        MyMapEntry(Object k, Object v) {
            key = k;
            value = v;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object nv) {
            throw new UnsupportedOperationException("setValue");
        }

        public int compareTo(Object o2) {
            if (!(o2 instanceof MyMapEntry))
                throw new IllegalArgumentException(
                        "Huh? Not a MapEntry?");
            Object otherKey = ((MyMapEntry) o2).getKey();
            return ((Comparable) key).compareTo((Comparable) otherKey);
        }
    }

    private class MyMapSet extends AbstractSet {
        List list;

        MyMapSet(ArrayList al) {
            list = al;
        }

        public Iterator iterator() {
            return list.iterator();
        }

        public int size() {
            return list.size();
        }
    }

    public Set entrySet() {
        if (keys.size() != values.size())
            throw new IllegalStateException(
                    "InternalError: keys and values are not synchronized");
        ArrayList al = new ArrayList();
        for (int i = 0; i < keys.size(); i++) {
            al.add(new MyMapEntry(keys.get(i), values.get(i)));
        }
        return new MyMapSet(al);
    }
}
