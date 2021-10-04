package Structures.Tree;

import java.util.List;

public class Tests {

    public static void main(String[] args){
        Order.setOrder(new Integer(2));//binary tree
        NaryTree<Integer> tree = new NaryTree();
        for(int i=2; i<= 12; i=(i+2)) {
            tree.add(Integer.valueOf(i));
        }
        System.out.println("NUMBERS IN TREE");
        System.out.println(tree.toString()+"\n");
        List<Node<Integer>> list = tree.searchs(new Integer(8));
        for(Node<Integer> node : list){
            tree.update(node.getCode(), new Integer(5));
        }
        System.out.println("NUMBERS IN THE TREE UPDATING VALUE 8 FOR 5");
        System.out.println(tree.toString()+"\n");
        list = tree.searchs(new Integer(5));
        for(Node<Integer> node : list){
            tree.remove(node.getCode());
        }
        System.out.println("NUMBERS IN THE TREE DELETING THE NUMBER 5");
        System.out.println(tree.toString()+"\n");
    }
}
