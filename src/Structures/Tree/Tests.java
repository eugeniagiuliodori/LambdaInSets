package Structures.Tree;

import java.util.List;

//para que funcione la generacion del grafico del arbol se debe instalar sudo apt install graphviz

public class Tests {

    public static void main(String[] args){
        Order.setOrder(new Integer(2));
        BST<Integer,Integer> tree = new BST();
        Integer k = new Integer(0);
        for(int i=2; i<= 12; i=(i+2)) {
            k++;
            tree.put(Integer.valueOf(k),Integer.valueOf(i));
        }
        System.out.println("NUMBERS IN TREE");
        System.out.println(tree.toString()+"\n");
        List<Node<Integer,Integer>> list = tree.searchs(new Integer(2));
        for(Node<Integer,Integer> node : list){
            tree.update(node.getKey(), new Integer(3));
        }
        System.out.println("NUMBERS IN THE TREE UPDATING VALUE 2 FOR 3");
        System.out.println(tree.toString()+"\n");
        list = tree.searchs(new Integer(3));
        for(Node<Integer,Integer> node : list){
            tree.remove(node.getKey());
        }
        System.out.println("NUMBERS IN THE TREE DELETING THE NUMBER 3");
        System.out.println(tree.toString()+"\n");

        tree.getRoot().graphic("Tree.jpg");

    }
}
