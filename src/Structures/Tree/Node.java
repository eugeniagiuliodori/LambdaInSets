package Structures.Tree;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

public class Node<V> {

    private V value;

    private int level;

    private boolean isRoot;

    private boolean isLeaf;

    private boolean hasBack;

    private int countSiblings;

    private static final Integer order = Order.getOrder();

    private int grades;

    private List<Node<V>> childs;

    private Node<V> parent;

    Integer code;



    public Node(V value, Integer code){
        this.value=value;
        childs = new LinkedList();
        hasBack = false;
        grades = 0;
        countSiblings = 0;
        this.code = code;
       //desde cualquier nodo, acceso al size del arbol completo
    }

    public Node<V> searchNode(Node<V> nodev, Integer code){
        if(this.getCode().compareTo(code)==0) return this;
        for(Node<V> node : nodev.getChilds()){
            if(node.getCode().compareTo(code)==0) return node;
        }
        for(Node<V> node : nodev.getChilds()){
            for(Node<V> nodeChild : node.getChilds()){
                return searchNode(nodeChild, code);
            }
        }
        return null;
    }

    public List<Node<V>> searchs(Node<V> nodev, List<Node<V>> list, V value){
        if(nodev.getValue().equals(value)) {
            list.add(nodev);
        }
        for(Node<V> node : nodev.getChilds()){
            searchs(node,list,value);
        }
        return list;
    }

    public Node<V> updateNode(Node<V> nodev, V newValue, Integer code){
        if(nodev.getCode().equals(code)) {
            nodev.setValue(newValue);
            return nodev;
        }
        for(Node<V> node : nodev.getChilds()){
            updateNode(node,newValue,code);
        }
        return null;
    }

    public Node<V> addNode(Node<V> nodev, V value){
        Node<V> node = nodev.addChild(value);
        if(node != null) {
            if(node.getChilds().size()==0){
                node.setLeaf(true);
            }
            return node;
        }
        if(!nodev.isRoot()){
            Node<V> parent = nodev.getParent();
            for(Node<V> sibling : parent.getChilds()){
                node = sibling.addChild(value);
                if(node != null) {
                    if(node.getChilds().size()==0){
                        node.setLeaf(true);
                    }
                    return node;
                }
            }
        }
        return addNode(nodev.getChilds().get(0), value);
    }


    public Node<V> removeNode(Node<V> nodev, Integer code){ //elimina subarbol y retorna subarbol resultante
        if(nodev.getCode().compareTo(code)==0 && nodev.isRoot()) { //si remove la raiz
            return null;
        }
        else {
            if (nodev.getCode().compareTo(code) == 0) {
                nodev.getParent().removeChild(code);
            }
            else{
                try {
                    for (Node<V> n : nodev.getChilds()) {
                        removeNode(n, code);
                    }
                }
                catch(ConcurrentModificationException e){}
            }
        }
        return null;
    }

    public Node<V> rmNode(Node<V> nodev, Integer code){ //elimina solo el nodo de codigo 'code'
        if(nodev.getCode().compareTo(code)==0 && nodev.isRoot()) { //si rm la raiz
            if(nodev.getChilds().size()>0){
                for(Node<V> n : nodev.getChilds()){
                    int siblings = n.getCountSiblings();
                    n.setCountSiblings((siblings--));
                }
                Node<V> newRoot = nodev.getChilds().get(0);
                newRoot.setRoot(true);
                newRoot.setChilds(nodev.getChilds());
                newRoot.getChilds().remove(0);
                nodev = newRoot;
                return nodev;
            }
        }
        else{
            if(nodev.isRoot()){
                for(Node<V> n: nodev.getChilds()){
                    if(rmNode(n, code)!=null){
                        break;
                    }
                }
            }
            else {
                Node<V> node = nodev.getParent().rmChild(code);
                if (node != null) {
                    return node;
                } else {
                    for (Node<V> n : nodev.getChilds()) {
                        rmNode(n, code);
                    }
                }
            }
        }
        return null;
    }

    public Node<V> addChild(V value){
        int g = grades;
        int o = order;
        if(value != null && grades < order ) {
            Node<V> child = new Node(value,NaryTree.getCode());
            child.setHasBack(true);
            child.setParent(this);
            childs.add(child);
            countSiblings = childs.size();
            grades++;
            child.setLeaf(true);
            return child;
        }
        return null;
    }

    private Node<V> addChild(List<Node<V>> childs, V value){
        int g = grades;
        int o = order;
        if(value != null && grades < order ) {
            Node<V> child = new Node(value,NaryTree.getCode());
            child.setHasBack(true);
            child.setParent(this);
            childs.add(child);
            countSiblings = childs.size();
            grades++;
            child.setLeaf(true);
            return child;
        }
        return null;
    }

    public boolean removeChild(Integer code){ //eliminar subarbol
        boolean found = false;
        if(childs.size()>0) {
            int i = 0;
            while (!found){
                if(((Node<V>)childs.get(i)).getCode().compareTo(code)==0){
                    grades--;
                    found = true;
                    if(childs.size()==1 && ((Node<V>)childs.get(i)).isLeaf()){
                        ((Node<V>) childs.get(i)).getParent().setLeaf(true);
                    }
                    childs.remove(i);
                }
                i++;
            }
        }
        return found;
    }

    public Node<V> rmChild(Integer code){ //eliminar solo el nodo
        if(childs.size()>0) {
            int i = 0;
            boolean found = false;
            while (!found){
                if(((Node<V>)childs.get(i)).getCode().compareTo(code)==0){
                    found = true;
                    if(((Node<V>)childs.get(i)).getChilds().size()>0){
                        List<Node<V>> siblings = new LinkedList();
                        int pos = 0;
                        while(pos<childs.size()){
                            if(((Node<V>) childs.get(pos)).getCode().compareTo(code)!=0){
                                siblings.add(childs.get(pos));
                            }
                            pos++;
                        }
                        int parentChilds = this.getChilds().size();
                        List<Node<V>> newChilds = childs.get(i).getChilds();
                        if(parentChilds>1){
                            if(siblings.size() + ((Node<V>)childs.get(i)).getChilds().size() <= order) {
                                Node<V> delNode = (Node<V>) childs.get(i);
                                this.setChilds(siblings);
                                for(Node<V> node :delNode.getChilds()){
                                    this.getChilds().add(node);
                                }
                                this.grades=this.getChilds().size();
                            }
                            else{
                                reorder(((Node<V>) childs.get(i)).getChilds(), siblings);
                            }
                        }
                        if(parentChilds == 1){
                            this.setChilds(newChilds);
                        }
                        return this;
                    }
                }
                i++;
            }
        }
        return null;
    }

    private void reorder(List<Node<V>> nodesToAdd, List<Node<V>> currentChilds){
        int i = 0;
        boolean found = false;
        for(Node<V> node : currentChilds){
            if(node.getChilds().size() + nodesToAdd.size() <= order){
                found = true;
                for(Node<V> n : nodesToAdd){
                    n.setLevel(node.getChilds().get(0).getLevel());
                    node.getChilds().add(n);
                    if(node.getChilds().get(0).isLeaf()){
                        n.setLeaf(true);
                    }
                }
                node.setGrades(node.getChilds().size() + nodesToAdd.size());
                break;
            }
            i++;
        }
        if(!found) {
            for (Node<V> node : currentChilds) {
                reorder(nodesToAdd, node.getChilds());
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


    public boolean isHasBack() {
        return hasBack;
    }

    public void setHasBack(boolean hasBack) {
        this.hasBack = hasBack;
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

    public List<Node<V>> getChilds() {
        return childs;
    }

    public void setChilds(List<Node<V>> childs) {
        this.childs = childs;
    }

    public Node<V> getParent() {
        return parent;
    }

    public void setParent(Node<V> parent) {
        this.parent = parent;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
