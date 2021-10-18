package Structures.Tree;

import Structures.Lambda.SimpleEntry;
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
            root.setIsRoot(true);
            root.setParent(null);
            size=1;
        }
        else{
            size++;
            if(size==2) root.setLeaf(false);
            root.addNode(root,code,value);
        }
    }


    private void removeSubTree(K code){
        root.removeNode(root,code);
    }

    private Node<K,V> rm(K code){
        Node<K,V> n = root.rmNode(root,code);
        this.setRoot(n);
        size--;
        return root;
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


    public Node<K,V> getRoot(){
        return root;
    }

    public void setRoot(Node<K,V> root){
        this.root=root;
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
        if(size == 0) root = null;
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
        Set<K> set = new HashSet<>();
        if(this.values().iterator().hasNext()) {
            for (V value : this.values()) {
                List<Node<K, V>> listValue = this.searchs(value);
                for (Node<K, V> node : listValue) {
                    if(!set.contains(node.getKey())){
                        set.add(node.getKey());
                    }
                }
            }
        }
        return set;
    }


    @Override
    public Collection<V> values() {
        return iterate();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K,V>> set = new HashSet<>();
        for(K key : this.keySet()){
            Entry<K,V> entry = new SimpleEntry(key,this.search(key).getValue());
            set.add(entry);
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        return this.equals(o);
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
