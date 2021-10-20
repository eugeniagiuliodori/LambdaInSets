package Structures.Tree;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.PdfImage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;


public class Node<K extends Comparable<K>,V>  implements Comparator<K>{

    private static final Integer order = Order.getOrder();

    private V value;
    private int level;
    private boolean isRoot;
    private boolean isLeaf;
    private int countSiblings;
    private int grades;
    private List<Node<K,V>> childs;
    private Node<K,V> leftChild;
    private Node<K,V> rigthChild;
    private Node<K,V> parent;
    private K code;



    public Node(K code,V value){
        this.value=value;
        this.code = code;
        this.level = -1;
        this.parent = null;
        this.isRoot = false;
        this.isLeaf = false;
        this.grades = -1;
        this.countSiblings = -1;
        this.childs = new LinkedList();
    }

    public Node<K,V> searchNode(Node<K,V> nodev, K code){
        Node<K,V> nres=null;
        if(code.compareTo(nodev.getKey())!=0) {
            for (Node<K, V> node : nodev.getChilds()) {
               if(nres != null) break;
                nres = searchNode(node, code);
            }
        }
        else{
            nres=nodev;
        }
        return nres;
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
            node.setLeaf(true);
            return node;
        }
        int index = 0;
        int sum = Integer.MAX_VALUE;
        for(int i=0;i<nodev.getChilds().size();i++){
            int currSize = size(nodev.getChilds().get(i),0);
            if(currSize < sum){
                sum=currSize;
                index=i;
            }
        }
        return addNode(nodev.getChilds().get(index),key, value);
    }

    public Node<K,V> addNodeInBST(Node<K,V> nodev, K key, V value){
        Node<K,V> node = nodev.addChildInBST(key,value);
        if(node != null) {
            return node;
        }
        try {
            Node<K,V> subtree = null;
            Comparable vnodev = (Comparable)nodev.getValue();
            if(nodev.getLeftChild()!=null && (((Comparable)value).compareTo(vnodev)<=0)) {
                subtree = nodev.getLeftChild();
            }
            else {
                if (nodev.getRigthChild() != null && (((Comparable)value).compareTo(vnodev)>0)) {
                    subtree = nodev.getRigthChild();
                }
            }
            return addNodeInBST(subtree,key, value);

        }
        catch(Exception e){
            return null;
        }

    }

    public Node<K,V> rmNode(Node<K,V> nodev, K code){ //elimina solo el nodo de codigo 'code'
            if(nodev.getChilds().size()>0){
                Node<K,V> nodeDel = searchNode(nodev,code);
                Node<K, V> newRoot;
                List<Node<K,V>> listNodev = new LinkedList<>();
                if(nodeDel.isRoot()) {
                    newRoot = nodev.getChilds().get((0));
                    newRoot.setIsRoot(true);
                    newRoot.setParent(null);
                    for (Node<K, V> node : nodev.getChilds()) {
                        if (node.getKey().compareTo(newRoot.getKey()) != 0) {
                            node.setParent(newRoot);
                            listNodev.add(node);
                        }
                    }
                }
                else{
                    newRoot = nodeDel.getParent();
                    newRoot.setIsRoot(nodeDel.getParent().isRoot());
                    for (Node<K, V> node : nodeDel.getChilds()) {
                        node.setParent(newRoot);
                        listNodev.add(node);
                    }
                }

                List<Node<K,V>>  listNewRoot = newRoot.getChilds();
                if(!nodeDel.isRoot()) {
                    for (int i = 0; i < listNewRoot.size(); i++) {
                        Node<K, V> node = listNewRoot.get(i);
                        if (listNewRoot.get(i).getKey().compareTo(code) == 0) {
                            listNewRoot.remove(i);
                        }
                    }
                }
                newRoot.setChilds(listNodev);
                newRoot.setLeaf(listNodev.size()==0);
                reorder(listNewRoot,newRoot);
                if(newRoot.isRoot()) {
                    nodev=newRoot;
                    return nodev;
                }
                else{
                    Node<K, V> nodec = newRoot;
                    while (nodec != null && nodec.getParent() != null) {
                        if (nodec.getParent() != null) {
                            nodec = nodec.getParent();
                        }
                    }
                    return nodec;
                }
            }
            else{
                nodev = null;
                return nodev;
            }
    }



    public Node<K,V> rmNodeInBST(Node<K,V> nodev, K code){ //elimina solo el nodo de codigo 'code'
        if(nodev.getKey().compareTo(code)==0 && nodev.isRoot()) { //si rm la raiz
            if(nodev.getChilds().size()>0){

                Node<K,V> a1, a2, b1, newRoot = null;
                if(nodev.getLeftChild() != null){
                    newRoot = nodev.getLeftChild();
                    newRoot.setIsRoot(true);
                    a1 = newRoot.getLeftChild();
                    a2 = newRoot.getRigthChild();
                    b1 = nodev.getRigthChild();
                }
                else{
                    if(nodev.getRigthChild() != null){
                        newRoot = nodev.getRigthChild();
                        newRoot.setIsRoot(true);
                        newRoot.setLeaf(newRoot.getChilds().size()==0);
                        newRoot.setParent(null);
                        nodev = newRoot;
                        return nodev;
                    }
                    else{
                        nodev = null;
                        return nodev;
                    }
                }
                for(Node<K,V> n : nodev.getChilds()){
                    if(newRoot.getKey().compareTo(n.getKey())!=0) {
                        n.setParent(newRoot);
                    }
                }
                if((nodev.getChilds().size()-1) + newRoot.getChilds().size() <= order){
                    for(Node<K,V> node : nodev.getChilds()){
                        if(node.getKey().compareTo(newRoot.getKey())!=0) {
                            newRoot.getChilds().add(node);
                        }
                    }
                    boolean condition1 = a1== null && a2 == null && b1 == null;
                    boolean condition2 = a1 != null && a2 == null && b1 == null;
                    boolean condition3 = a1 == null && a2 != null && b1 == null;
                    boolean condition4 = a1 == null && a2 == null && b1 != null;
                    boolean condition5 = a1 != null && a2 != null && b1 == null;
                    boolean condition6 = a1 != null && a2 == null && b1 != null;
                    boolean condition7 = a1 == null && a2 != null && b1 != null;
                    int condition = 0;
                    if(condition1) condition=1;
                    if(condition2) condition=2;
                    if(condition3) condition=3;
                    if(condition4) condition=4;
                    if(condition5) condition=5;
                    if(condition6) condition=6;
                    if(condition7) condition=7;
                    switch(condition){
                        case 1:{
                            newRoot.setLeftChild(null);
                            newRoot.setRigthChild(null);
                            newRoot.setLeaf(true);
                            break;
                        }
                        case 2:{
                            break;
                        }
                        case 3:{
                            break;
                        }
                        case 4:{
                            newRoot.setRigthChild(b1);
                            break;
                        }
                        case 5:{
                            break;
                        }
                        case 6:{
                            newRoot.setRigthChild(nodev.getRigthChild());
                            break;
                        }
                        case 7:{
                            if(a2.getRigthChild()==null){
                                a2.setRigthChild(b1);
                            }
                            else{
                                Node<K,V> currNode = a2;
                                while(currNode.getRigthChild() != null){
                                    currNode = currNode.getRigthChild();
                                }
                                currNode.setRigthChild(b1);
                            }
                            break;
                        }
                        default:{ break;}
                    }
                }
                else{
                    Node<K,V> currNode = a2;
                    while(currNode.getRigthChild() != null){
                        currNode = currNode.getRigthChild();
                    }
                    currNode.setRigthChild(b1);
                }
                newRoot.setLeaf(newRoot.getChilds().size()==0);
                newRoot.setIsRoot(true);
                newRoot.setParent(null);
                nodev = newRoot;
                return nodev;
            }
            else{
                nodev = null;
                return nodev;
            }
        }
        else{
            Node<K,V> delNode = nodev.searchNode(nodev,code);
            if(delNode != null){
                delNode.getParent().rmChildInBST(code);
            }
            Node<K, V> nodec = delNode;
            if(delNode == null) nodec = nodev;
            while (nodec != null && nodec.getParent() != null) {
                if (nodec.getParent() != null) {
                    nodec = nodec.getParent();
                }
            }
            return nodec;
        }
    }


    private Node<K,V> addChildInBST(K key, V value){

            Node<K,V> child = new Node(key,value);
            child.setParent(this);
            child.setLeaf(true);
            child.setIsRoot(false);
            try{
                Comparable cv = (Comparable) this.getValue();
                Comparable cchild = (Comparable) child.getValue();
                if(cchild != null && cchild.compareTo(cv)<=0 && this.getLeftChild()!= null) return null;
                if(cchild != null && cchild.compareTo(cv)>0 && this.getRigthChild()!= null) return null;
                if(cchild != null && cchild.compareTo(cv)<=0){
                    leftChild = child;
                }
                else{
                    rigthChild = child;
                }
                this.setLeaf(false);
                childs.add(child);
                return child;
            }
            catch(Exception e){
                return null;
            }

    }


    private Node<K,V> addChild(K key, V value){
        if(value != null && this.getChilds().size() < order ) {
            Node<K,V> child = new Node(key,value);
            child.setParent(this);
            child.setLeaf(true);
            child.setIsRoot(false);
            childs.add(child);
            return child;
        }
        return null;
    }


    private Node<K,V> rmChild(K code){ //eliminar solo el nodo
        if(childs.size()>0) {
            boolean found = false;
            int i=0;
            while (i<childs.size() && !found){
                if(childs.get(i) != null && childs.get(i).getKey().compareTo(code)==0) {
                    found = true;
                    K k = childs.get(i).getKey();
                    V v = childs.get(i).getValue();
                    if (!childs.get(i).isLeaf()) {
                        List<Node<K, V>> list = new LinkedList<>();
                        for(Node<K,V> n : this.getChilds()){
                            if(childs.get(i).getKey().compareTo(n.getKey())!=0) {
                                list.add(n);
                            }
                        }
                        List<Node<K,V>> childsOfNodeDel = childs.get(i).getChilds();
                        this.getChilds().remove(i);
                        this.setChilds(list);
                        reorder(childsOfNodeDel, this);
                        Node<K,V> delNode = new Node(k,v);
                        delNode.setLeaf(false);
                        delNode.setParent(this);
                        return delNode;
                    }
                    else {
                        if (childs.size() == 1) {
                            this.setLeaf(true);
                        }
                        this.getChilds().remove(i);
                        Node<K,V> delNode = new Node(k,v);
                        delNode.setLeaf(true);
                        delNode.setParent(this);
                        return delNode;
                    }
                }
                i++;
            }
        }
        return null;
    }




    private Node<K,V> rmChildInBST(K code){ //eliminar solo el nodo
        if(childs.size()>0) {
            boolean found = false;
            while (!found){
                if(leftChild != null && leftChild.getKey().compareTo(code)==0) {
                    found = true;
                    Node<K, V> node = leftChild.getParent();
                    if (!leftChild.isLeaf()) {
                        List<Node<K, V>> list = new LinkedList<>();
                        for(int i=0;i<node.getChilds().size();i++){
                            Node<K,V> n = node.getChilds().get(i);
                            if(n.getKey().compareTo(code)==0){
                                node.getChilds().remove(i);
                                break;
                            }
                        }
                        for (Node<K, V> n : leftChild.getChilds()) {
                            list.add(n);
                        }
                        K k = leftChild.getKey();
                        V v = leftChild.getValue();
                        node.setLeftChild(null);
                        reorderInBST(list, node);
                        Node<K,V> delNode = new Node(k,v);
                        delNode.setParent(leftChild.getParent());
                        delNode.setLeftChild(null);
                        delNode.setRigthChild(null);
                        return delNode;
                    }
                    else {
                        if (leftChild.getParent().getRigthChild() == null) {
                            leftChild.getParent().setLeaf(true);
                        }
                        for(int i=0;i<node.getChilds().size();i++){
                            Node<K,V> n = node.getChilds().get(i);
                            if(n.getKey().compareTo(code)==0){
                                node.getChilds().remove(i);
                                break;
                            }
                        }

                        K k = leftChild.getKey();
                        V v = leftChild.getValue();
                        Node<K,V> delNode = new Node(k,v);
                        delNode.setParent(leftChild.getParent());
                        node.setLeftChild(null);
                        return delNode;
                    }
                }
                if (rigthChild != null && rigthChild.getKey().compareTo(code) == 0) {
                    found = true;
                    Node<K, V> node = rigthChild.getParent();
                    if (!rigthChild.isLeaf()) {
                        for(int i=0;i<node.getChilds().size();i++){
                            Node<K,V> n = node.getChilds().get(i);
                            if(n.getKey().compareTo(code)==0){
                                node.getChilds().remove(i);
                                break;
                            }
                        }
                        List<Node<K, V>> list = new LinkedList<>();
                        for (Node<K, V> n : rigthChild.getParent().getChilds()) {
                            if(n.getKey().compareTo(rigthChild.getKey())!=0) {
                                list.add(n);
                            }
                        }
                        for (Node<K, V> n : rigthChild.getChilds()) {
                            list.add(n);
                        }
                        K k = rigthChild.getKey();
                        V v = rigthChild.getValue();
                        node.setRigthChild(null);
                        reorderInBST(list, node);
                        Node<K,V> delNode = new Node(k,v);
                        delNode.setParent(rigthChild.getParent());
                        delNode.setLeftChild(null);
                        delNode.setRigthChild(null);
                        return delNode;
                    }
                    else {
                        if (rigthChild.getParent().getLeftChild() == null) {
                            rigthChild.getParent().setLeaf(true);
                        }
                        for(int i=0;i<node.getChilds().size();i++){
                            Node<K,V> n = node.getChilds().get(i);
                            if(n.getKey().compareTo(code)==0){
                                node.getChilds().remove(i);
                                break;
                            }
                        }
                        K k = rigthChild.getKey();
                        V v = rigthChild.getValue();
                        Node<K,V> delNode = new Node(k,v);
                        delNode.setParent(rigthChild.getParent());
                        node.setRigthChild(null);
                        return delNode;
                    }
                }
            }
        }
        return null;
    }




    public int size(Node<K,V> node, int sum){
        sum=sum+1;
        for(Node<K,V> n : node.getChilds()){
            sum = size(n,sum);
        }
        return sum;
    }




    private void reorder(List<Node<K,V>> nodesToAdd, Node<K,V> nr){
        if(!nodesToAdd.isEmpty()) {
            if (nr.getChilds().size() < order) {
                Node<K,V> nmod = nr;
                Node<K, V> n = nodesToAdd.get(0);
                n.setParent(nr);
                n.setLeaf(true);
                nr.getChilds().add(n);
                nr.setLeaf(false);
                nodesToAdd.remove(0);
                while(n.getChilds().size()>0){
                    nodesToAdd.add(n.getChilds().remove(0));
                }
                while(nr.getParent() != null){
                    nr=nr.getParent();
                }
                reorder(nodesToAdd,nr);

            }
            else {
                while(nr.getParent() != null){
                    nr=nr.getParent();
                }
                while(nr.getChilds().size()==order){
                    int index = 0;
                    int sum = Integer.MAX_VALUE;
                    for(int i=0;i<nr.getChilds().size();i++){
                        int currSize = size(nr.getChilds().get(i),0);
                        if(currSize < sum){
                            sum=currSize;
                            index=i;
                        }
                    }
                    nr=nr.getChilds().get(index);
                }
                reorder(nodesToAdd, nr);
            }

        }
    }


    private void reorderInBST(List<Node<K,V>> nodesToAdd, Node<K,V> nodev){
        while (!nodesToAdd.isEmpty()) {
            Node<K, V> n = nodesToAdd.get(0);
            if (((Comparable)nodev.getValue()).compareTo((Comparable)n.getValue())>0 && nodev.getLeftChild()== null) {
                n.setLeaf(n.getChilds().size()==0);
                n.setParent(nodev);
                nodev.getChilds().add(n);
                nodev.setLeftChild(n);
                nodesToAdd.remove(0);
            }
            else {
                if (((Comparable) nodev.getValue()).compareTo((Comparable) n.getValue()) <= 0 && nodev.getRigthChild() == null) {
                    n.setLeaf(n.getChilds().size()==0);
                    n.setParent(nodev);
                    nodev.getChilds().add(n);
                    nodev.setRigthChild(n);
                    nodesToAdd.remove(0);
                } else {
                    if (((Comparable) nodev.getValue()).compareTo((Comparable) n.getValue()) > 0 && nodev.getLeftChild() != null) {
                        reorderInBST(nodesToAdd, nodev.getLeftChild());
                    } else {
                        if (((Comparable) nodev.getValue()).compareTo((Comparable) n.getValue()) <= 0 && nodev.getRigthChild() != null) {
                            reorderInBST(nodesToAdd, nodev.getRigthChild());
                        }
                    }
                }
            }
        }
        nodev.setLeaf(nodev.getChilds().size()==0);
    }

    public Node<K, V> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node<K, V> leftChild) {
        this.leftChild = leftChild;
    }

    public Node<K, V> getRigthChild() {
        return rigthChild;
    }

    public void setRigthChild(Node<K, V> rigthChild) {
        this.rigthChild = rigthChild;
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

    public void setIsRoot(boolean root) {
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
