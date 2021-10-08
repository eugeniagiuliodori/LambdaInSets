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
            this.size=1;
        }
        else{
            this.size++;
            this.getRoot().addNodeInBST(this.getRoot(),code,value);
        }
    }

    @Override
    public V put(K key, V value){
        add(key,value);
        return value;
    }

    @Override
    public V remove(Object key){
        this.setRoot(this.getRoot().rmNodeInBST(this.getRoot(),(K)key));
        return this.getRoot().getValue();
    }



}
