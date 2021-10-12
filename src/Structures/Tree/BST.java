package Structures.Tree;

public class BST<K extends Comparable<K>,V> extends NaryTree<K,V>{

    private int size;

    public BST(){
        super();
        this.size=0;
    }

    private void add(K code,V value){
        if(this.getRoot() == null){
            this.setRoot(new Node(code, value));
            this.getRoot().setLeaf(true);
            this.getRoot().setIsRoot(true);
            size=1;
        }
        else{
            size++;
            this.getRoot().addNodeInBST(this.getRoot(),code,value);
        }
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public V put(K key, V value){
        add(key,value);
        return value;
    }

    @Override
    public V remove(Object key){
        //this.getRoot().rmNodeInBST(this.getRoot(),(K)key);
        Node<K,V> nres = this.getRoot().rmNodeInBST(this.getRoot(),(K)key);
        this.setRoot(nres);
        size--;
        return nres.getValue();
    }



}
