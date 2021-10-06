package Structures.Tree;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

public class Node<K extends Comparable<K>,V>  implements Comparator<K> {

    private static final Integer order = Order.getOrder();

    private V value;
    private int level;
    private boolean isRoot;
    private boolean isLeaf;
    private int countSiblings;
    private int grades;
    private List<Node<K,V>> childs;
    private Node<K,V> parent;
    private K code;



    public Node(K code,V value){
        this.value=value;
        this.code = code;
        this.level = 1;
        this.parent = null;
        this.isRoot = false;
        this.isLeaf = false;
        this.grades = 0;
        this.countSiblings = 0;
        this.childs = new LinkedList();
    }

    public Node<K,V> searchNode(Node<K,V> nodev, K code){
        if(code.compareTo(this.getKey())==0) return this;
        for(Node<K,V> node : nodev.getChilds()){
            if(node.getKey().compareTo(code)==0) return node;
        }
        for(Node<K,V> node : nodev.getChilds()){
            for(Node<K,V> nodeChild : node.getChilds()){
                return searchNode(nodeChild, code);
            }
        }
        return null;
    }

    public List<Node<K,V>> searchs(Node<K,V> nodev, List<Node<K,V>> list, V value){
        if(nodev.getValue().equals(value)) {
            list.add(nodev);
        }
        for(Node<K,V> node : nodev.getChilds()){
            searchs(node,list,value);
        }
        return list;
    }

    public Node<K,V> updateNode(Node<K,V> nodev, K code, V newValue){
        if(nodev.getKey().equals(code)) {
            nodev.setValue(newValue);
            return nodev;
        }
        for(Node<K,V> node : nodev.getChilds()){
            updateNode(node,code, newValue);
        }
        return null;
    }

    public Node<K,V> addNode(Node<K,V> nodev, K key, V value){
        Node<K,V> node = nodev.addChild(key,value);
        if(node != null) {
            return node;
        }
        if(!nodev.isRoot()){
            Node<K,V> parent = nodev.getParent();
            for(Node<K,V> sibling : parent.getChilds()){
                node = sibling.addChild(key,value);
                if(node != null) {
                    if(node.getChilds().size()==0){
                        node.setLeaf(true);
                        node.setRoot(false);
                    }
                    return node;
                }
            }
        }
        return addNode(nodev.getChilds().get(0),key, value);
    }


    public Node<K,V> removeNode(Node<K,V> nodev, K code){ //elimina subarbol y retorna subarbol resultante
        if(nodev.getKey().compareTo(code)==0 && nodev.isRoot()) { //si remove la raiz
            nodev=null;
            return nodev;
        }
        else {
            if (nodev.getKey().compareTo(code) == 0) {
                nodev.getParent().removeChild(code);
            }
            else{
                try {
                    for (Node<K,V> n : nodev.getChilds()) {
                        removeNode(n, code);
                    }
                }
                catch(ConcurrentModificationException e){}
            }
        }
        return null;
    }

    public Node<K,V> rmNode(Node<K,V> nodev, K code){ //elimina solo el nodo de codigo 'code'
        if(nodev.getKey().compareTo(code)==0 && nodev.isRoot()) { //si rm la raiz
            if(nodev.getChilds().size()>0){
                for(Node<K,V> n : nodev.getChilds()){
                    n.setCountSiblings((n.getCountSiblings()-1));
                }
                Node<K,V> newRoot = nodev.getChilds().get(0);
                newRoot.setLevel(1);
                updateLevel(newRoot,false);
                if((nodev.getChilds().size()-1) + newRoot.getChilds().size() <= order){
                    newRoot.setChilds(newRoot.getChilds());
                    for(Node<K,V> node : nodev.getChilds()){
                        if(node.getKey().compareTo(newRoot.getKey())!=0){
                            newRoot.getChilds().add(node);
                        }
                    }

                }
                else{
                    List<Node<K,V>>  listNewRoot = newRoot.getChilds();
                    List<Node<K,V>> listNodev = new LinkedList<>();
                    for(Node<K,V> node : nodev.getChilds()){
                        if(node.getKey().compareTo(newRoot.getKey())!=0) {
                            node.setParent(newRoot);
                            listNodev.add(node);
                        }
                    }
                    newRoot.setChilds(listNodev);
                    reorder(listNewRoot,listNodev);
                }
                newRoot.setGrades(newRoot.getChilds().size());
                newRoot.setLeaf(newRoot.getChilds().size()==0);
                newRoot.setRoot(true);
                newRoot.setParent(null);
                newRoot.setRoot(true);
                nodev = newRoot;
                return nodev;
            }
            else{
                nodev = null;
                return nodev;
            }
        }
        else{
            if(nodev.isRoot()){
                for(Node<K,V> n: nodev.getChilds()){
                    if(rmNode(n, code)!=null){
                        break;
                    }
                }
            }
            else {
                Node<K,V> node = nodev.getParent().rmChild(code);
                if (node != null) {
                    return node;
                } else {
                    for (Node<K,V> n : nodev.getChilds()) {
                        rmNode(n, code);
                    }
                }
            }
        }
        return null;
    }

    private Node<K,V> addChild(K key, V value){
        if(value != null && grades < order ) {
            Node<K,V> child = new Node(key,value);
            child.setParent(this);
            child.setLevel(this.getLevel() + 1);
            child.setLeaf(true);
            child.setRoot(false);
            childs.add(child);
            countSiblings = childs.size();
            grades++;
            return child;
        }
        return null;
    }

    private boolean removeChild(K code){ //eliminar subarbol
        boolean found = false;
        if(childs.size()>0) {
            int i = 0;
            while (!found){
                if(((Node<K,V>)childs.get(i)).getKey().compareTo(code)==0){
                    grades--;
                    found = true;
                    if(childs.size()==1){
                        ((Node<K,V>) childs.get(i)).getParent().setLeaf(true);
                    }
                    childs.remove(i);
                    this.setCountSiblings(childs.size());
                }
                i++;
            }
        }
        return found;
    }

    private Node<K,V> rmChild(K code){ //eliminar solo el nodo
        if(childs.size()>0) {
            int i = 0;
            boolean found = false;
            while (!found){
                if(((Node<K,V>)childs.get(i)).getKey().compareTo(code)==0){
                    found = true;
                    if(((Node<K,V>)childs.get(i)).getChilds().size()>0){
                        List<Node<K,V>> siblings = new LinkedList();
                        int pos = 0;
                        while(pos<childs.size()){
                            if(((Node<K,V>) childs.get(pos)).getKey().compareTo(code)!=0){
                                siblings.add(childs.get(pos));
                            }
                            pos++;
                        }
                        int parentChilds = this.getChilds().size();
                        List<Node<K,V>> newChilds = childs.get(i).getChilds();
                        if(childs.size()==1 && newChilds.size() == 0){
                            this.setLeaf(true);
                        }
                        else{
                            this.setLeaf(false);
                        }
                        if(parentChilds>1){
                            if(siblings.size() + ((Node<K,V>)childs.get(i)).getChilds().size() <= order) {
                                Node<K,V> delNode = (Node<K,V>) childs.get(i);
                                updateLevel(delNode,false);
                                this.setChilds(siblings);
                                for(Node<K,V> node :delNode.getChilds()){
                                    this.getChilds().add(node);
                                }
                            }
                            else{
                                reorder(((Node<K,V>) childs.get(i)).getChilds(), siblings);
                            }
                        }
                        if(parentChilds == 1){
                            this.setChilds(newChilds);
                        }
                        this.grades=this.childs.size();
                        this.setCountSiblings(this.childs.size());
                        return this;
                    }
                }
                i++;
            }
        }
        return null;
    }

    private void updateLevel(Node<K,V> node, boolean increment){
        if(increment){
            for(Node<K,V> nodev : node.getChilds()){
                nodev.setLevel(nodev.getLevel() + 1);
            }
        }
        if(!increment){
            for(Node<K,V> nodev : node.getChilds()){
                nodev.setLevel(nodev.getLevel() - 1);
            }
        }

    }

    private void reorder(List<Node<K,V>> nodesToAdd, List<Node<K,V>> currentChilds){
        for(Node<K,V> node : currentChilds) {
            while (!nodesToAdd.isEmpty()) {
                Node<K, V> n = nodesToAdd.get(0);
                if (node.getChilds().size() + 1 <= order) {
                    node.getChilds().add(n);
                    if (node.getChilds().get(0).isLeaf()) {
                        n.setLeaf(true);
                    }
                    nodesToAdd.remove(0);
                    node.setGrades(node.getChilds().size());
                } else {
                    reorder(nodesToAdd, node.getChilds());
                }
            }
        }
    }


    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public int getCountSiblings() {
        return countSiblings;
    }

    public void setCountSiblings(int countSiblings) {
        this.countSiblings = countSiblings;
    }

    public int getGrades() {
        return grades;
    }

    public void setGrades(int grades) {
        this.grades = grades;
    }

    public List<Node<K,V>> getChilds() {
        return childs;
    }

    public void setChilds(List<Node<K,V>> childs) {
        this.childs = childs;
    }

    public Node<K,V> getParent() {
        return parent;
    }

    public void setParent(Node<K,V> parent) {
        this.parent = parent;
    }

    public K getKey() {
        return code;
    }

    public void setKey(K code) {
        this.code = code;
    }

    @Override
    public int compare(K o1, K o2) {
        return o1.compareTo(o2);
    }
}
