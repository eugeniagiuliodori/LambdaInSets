package Structures.Lambda;

import Structures.Tree.BST;
import Structures.Tree.NaryTree;
import Structures.Tree.Node;
import Structures.Tree.Order;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

public class Test {


    public static void main(String[] args){
        NaryTree<Integer,Integer> tree = new BST();
        TriConsumer<NaryTree<Integer,Integer>, Integer, Integer> loadTree = (t, order, size) -> {
            Order.setOrder(new Integer(order));
            Integer k = new Integer(0);
            for (int i = 2; i <= size; i = (i + 2)) {
                k++;
                t.put(Integer.valueOf(k), Integer.valueOf(i));
            }
        };
        //Precondition: Uniques values in the tree
        Function<Integer,Integer> duplicateValues = elem -> elem*2;
        BiFunction<NaryTree,Integer,Integer> getKey = (t, value) -> {
            t.searchs(value);
            if(t.searchs(value).size()>0) {
               return  ((List<Node<Integer,Integer>>) t.searchs(value)).get(0).getKey();
            }
            else{
                return -1;
            }
        };
        Consumer<NaryTree<Integer,Integer>> duplicateValuesInTree = t -> t.entrySet().forEach(entry -> {
            Consumer<Integer> showValue = v ->  System.out.println(v);
            Consumer<NaryTree<Integer,Integer>> showValues = t2 -> t2.values().forEach(v->showValue.accept(v));
            Integer key = entry.getKey();
            t.remove(key);
            t.put(key, duplicateValues.apply(entry.getValue()));

        });
        Function<NaryTree<Integer,Integer>,NaryTree<Integer,Integer>> fgetDuplicateValues = t -> {duplicateValuesInTree.accept(t);return t;};
        Consumer<Integer> showValue = v ->  System.out.print(v);
        BiPredicate<Integer,Integer> menor = (a,b) -> a < b;
        final AtomicInteger count = new AtomicInteger(1);
        BiConsumer<Boolean,Integer> biconsumer = (b,v) -> {if(b) {showValue.accept(v);System.out.print(","); count.incrementAndGet();}if(!b){ showValue.accept(v);System.out.print("\n\n\n");}};
        BiConsumer<NaryTree<Integer,Integer>, Integer> showValues = (t,i) -> t.values().stream().sorted().forEach(e-> biconsumer.accept(menor.test(count.get(),i),e));

        loadTree.accept(tree,new Integer(2),new Integer(100));
        tree.getRoot().graphic("Tree.jpg");
        System.out.println("VALUES IN TREE");
        showValues.accept(tree, tree.size());
        System.out.println("DUPLICATED VALUES OF THE TREE");
        count.set(1);
        showValues.accept(fgetDuplicateValues.apply(tree),new Integer(tree.size()));
        tree.remove(new Integer(2));
        tree.remove(new Integer(25));
        System.out.println("VALUES THEN TO REMOVE TWO NUMBERS");
        count.set(1);
        showValues.accept(tree, tree.size());
        tree.getRoot().graphic("TreeWithDuplicates.jpg");
    }




}
