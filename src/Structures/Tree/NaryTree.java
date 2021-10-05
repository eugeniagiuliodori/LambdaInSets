package Structures.Tree;

import java.util.LinkedList;
import java.util.List;

public class NaryTree<V> {

    private Node<V> root;

    private static Integer code;



    public NaryTree(){
        code=1;
    }

    public void add(V value){
        if(root == null){
            root = new Node<>(value,code);
            root.setLeaf(true);
            root.setRoot(true);
            root.setCode(new Integer(1));
        }
        else{
            code++;
            root.addNode(root,value);
        }
    }

    public static Integer getCode(){
        return code;
    }

    public void remove(Integer code){
        root.removeNode(root,code);
    }

    public void rm(Integer code){
        root.rmNode(root,code);
    }

    public void update(Integer code, V value){
        root.updateNode(root,value,code);
    }

    public Node<V> search(Integer code){
        return root.searchNode(root,code);
    }

    public List<Node<V>> searchs(V value){
        return root.searchs(root,new LinkedList<>(),value);
    }

    private String toStringRec(Node<V> nodeparent, String s){
        for(Node<V> node : nodeparent.getChilds()){
            s = s + ","+node.getValue().toString();
        }
        for(Node<V> n : nodeparent.getChilds()) {
            s = toStringRec(n,s);
        }
        return s;
    }

    public String toString(){
        return root.getValue() + toStringRec(root,new String(""));
    }


}
