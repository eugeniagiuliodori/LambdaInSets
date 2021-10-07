package Structures.Tree;

import jdk.internal.util.xml.impl.Pair;

import java.util.*;

public class NaryTree<K extends Comparable<K>,V> implements Map<K,V> {

    private Node<K,V> root;
    private int size;



    public NaryTree(){
        size=0;
    }

    private void add(K code,V value){
        if(root == null){
            root = new Node(code, value);
            root.setLeaf(true);
            root.setRoot(true);
            size=1;
        }
        else{
            size++;
            root.addNode(root,code,value);
        }
    }


    private void removeSubTree(K code){
        root.removeNode(root,code);
    }

    private Node<K,V> rm(K code){
        Node<K,V> node = root;
        if(root != null && root.getKey().compareTo(code)==0){
            root = root.rmNode(root,code);
        }
        else{
            node = root.rmNode(root,code);
        }
        //por que no se impactan los cambios en puntero root, si no hago la asignacion?
        size++;
        return node;
    }

    public void update(K code, V value){
        root.updateNode(root,code, value);
    }

    public Node<K,V> search(K code){
        return root.searchNode(root,code);
    }

    public List<Node<K,V>> searchs(V value){
        return root.searchs(root,new LinkedList<>(),value);
    }

    public void graphic(String path){
        root.graphic(path);
    }

    private String toStringRec(Node<K,V> nodeparent, String s){
        for(Node<K,V> node : nodeparent.getChilds()){
            s = s + ","+node.getValue().toString();
        }
        for(Node<K,V> n : nodeparent.getChilds()) {
            s = toStringRec(n,s);
        }
        return s;
    }

    public String toString(){
        return root.getValue() + toStringRec(root,new String(""));
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean containsKey(Object key) {
        return search((K)key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return searchs((V)value).size() > 0;
    }

    @Override
    public V get(Object key) {
        return search((K)key).getValue();
    }

    @Override
    public V put(K key, V value) {
        add(key, value);
        return value;
    }

    @Override
    public V remove(Object key) {
        Node<K,V> node = rm((K)key);
        if(node != null){
            return node.getValue();
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {}

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return iterate();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    private List<V> iterateRec(Node<K,V> nodeparent, List<V> list){
        for(Node<K,V> node : nodeparent.getChilds()){
            list.add(node.getValue());
        }
        for(Node<K,V> n : nodeparent.getChilds()) {
            iterateRec(n,list);
        }
        return list;
    }

    private List<V> iterate(){
        List<V> list = new LinkedList<>();
        list.add(root.getValue());
        return iterateRec(root,list);
    }

}
