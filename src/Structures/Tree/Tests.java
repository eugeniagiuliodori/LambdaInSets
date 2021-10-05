package Structures.Tree;

import java.util.List;

public class Tests {

    public static void main(String[] args){
        Order.setOrder(new Integer(2));
        NaryTree<Integer> tree = new NaryTree();
        for(int i=2; i<= 12; i=(i+2)) {
            tree.add(Integer.valueOf(i));
        }
        System.out.println("NUMBERS IN TREE");
        System.out.println(tree.toString()+"\n");
        List<Node<Integer>> list = tree.searchs(new Integer(2));
        for(Node<Integer> node : list){
            tree.update(node.getCode(), new Integer(3));
        }
        System.out.println("NUMBERS IN THE TREE UPDATING VALUE 2 FOR 3");
        System.out.println(tree.toString()+"\n");
        list = tree.searchs(new Integer(3));
        for(Node<Integer> node : list){
            tree.rm(node.getCode());
        }
        System.out.println("NUMBERS IN THE TREE DELETING THE NUMBER 3");
        System.out.println(tree.toString()+"\n");
    }
}
