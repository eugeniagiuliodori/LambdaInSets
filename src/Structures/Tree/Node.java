package Structures.Tree;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

public class Node<V> {

    private static final Integer order = Order.getOrder();

    private V value;
    private int level;
    private boolean isRoot;
    private boolean isLeaf;
    private int countSiblings;
    private int grades;
    private List<Node<V>> childs;
    private Node<V> parent;
    Integer code;



    public Node(V value, Integer code){
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
            return node;
        }
        if(!nodev.isRoot()){
            Node<V> parent = nodev.getParent();
            for(Node<V> sibling : parent.getChilds()){
                node = sibling.addChild(value);
                if(node != null) {
                    if(node.getChilds().size()==0){
                        node.setLeaf(true);
                        node.setRoot(false);
                    }
                    return node;
                }
            }
        }
        return addNode(nodev.getChilds().get(0), value);
    }


    public Node<V> removeNode(Node<V> nodev, Integer code){ //elimina subarbol y retorna subarbol resultante
        if(nodev.getCode().compareTo(code)==0 && nodev.isRoot()) { //si remove la raiz
            nodev=null;
            return nodev;
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
                    n.setCountSiblings((n.getCountSiblings()-1));
                }
                Node<V> newRoot = nodev.getChilds().get(0);
                newRoot.setLevel(1);
                updateLevel(newRoot,false);
                if((nodev.getChilds().size()-1) + newRoot.getChilds().size() <= order){
                    newRoot.setChilds(newRoot.getChilds());
                    for(Node<V> node : nodev.getChilds()){
                        if(node.getCode().compareTo(newRoot.getCode())!=0){
                            newRoot.getChilds().add(node);
                        }
                    }

                }
                else{
                    List<Node<V>>  listNewRoot = newRoot.getChilds();
                    List<Node<V>> listNodev = new LinkedList<>();
                    for(Node<V> node : nodev.getChilds()){
                        if(node.getCode().compareTo(newRoot.getCode())!=0) {
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

    private Node<V> addChild(V value){
        if(value != null && grades < order ) {
            Node<V> child = new Node(value,NaryTree.getCode());
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

    private boolean removeChild(Integer code){ //eliminar subarbol
        boolean found = false;
        if(childs.size()>0) {
            int i = 0;
            while (!found){
                if(((Node<V>)childs.get(i)).getCode().compareTo(code)==0){
                    grades--;
                    found = true;
                    if(childs.size()==1){
                        ((Node<V>) childs.get(i)).getParent().setLeaf(true);
                    }
                    childs.remove(i);
                    this.setCountSiblings(childs.size());
                }
                i++;
            }
        }
        return found;
    }

    private Node<V> rmChild(Integer code){ //eliminar solo el nodo
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
                        if(childs.size()==1 && newChilds.size() == 0){
                            this.setLeaf(true);
                        }
                        else{
                            this.setLeaf(false);
                        }
                        if(parentChilds>1){
                            if(siblings.size() + ((Node<V>)childs.get(i)).getChilds().size() <= order) {
                                Node<V> delNode = (Node<V>) childs.get(i);
                                updateLevel(delNode,false);
                                this.setChilds(siblings);
                                for(Node<V> node :delNode.getChilds()){
                                    this.getChilds().add(node);
                                }
                            }
                            else{
                                reorder(((Node<V>) childs.get(i)).getChilds(), siblings);
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

    private void updateLevel(Node<V> node, boolean increment){
        if(increment){
            for(Node<V> nodev : node.getChilds()){
                nodev.setLevel(nodev.getLevel() + 1);
            }
        }
        if(!increment){
            for(Node<V> nodev : node.getChilds()){
                nodev.setLevel(nodev.getLevel() - 1);
            }
        }

    }

    private boolean reorder(List<Node<V>> nodesToAdd, List<Node<V>> currentChilds){
        int i = 0;
        boolean found = false;
        for(Node<V> node : currentChilds){
            if(node.getChilds().size() + nodesToAdd.size() <= order){
                found = true;
                for(Node<V> n : nodesToAdd){
                    //n.setLevel(node.getChilds().get(0).getLevel());
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
        int j = 0;
        while (j < currentChilds.size() && !found) {
            found = reorder(nodesToAdd, currentChilds.get(j).getChilds());
        }

        return found;
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
