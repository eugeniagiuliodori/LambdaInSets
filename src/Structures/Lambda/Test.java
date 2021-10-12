package Structures.Lambda;

import Structures.Tree.BST;
import Structures.Tree.Node;
import Structures.Tree.Order;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.logging.Filter;

public class Test {




    public static void main(String[] args){
        BST<Integer,Integer> tree = new BST();
        TriConsumer<BST<Integer,Integer>, Integer, Integer> loadTree = (t,order,size) -> {
            Order.setOrder(new Integer(order));
            Integer k = new Integer(0);
            for (int i = 2; i <= size; i = (i + 2)) {
                k++;
                t.put(Integer.valueOf(k), Integer.valueOf(i));
            }
        };
        //Precondition: Uniques values in the tree
        Function<Integer,Integer> duplicateValues = elem -> elem*2;
        BiFunction<BST,Integer,Integer> getKey = (t, value) -> {
            if(t.searchs(value).size()>0) {
               return  ((List<Node<Integer,Integer>>) t.searchs(value)).get(0).getKey();
            }
            else{
                return -1;
            }
        };
        Consumer<BST<Integer,Integer>> duplicateValuesInTree = t -> t.values().forEach(e -> {
            Consumer<Integer> showValue = v ->  System.out.println(v);
            Consumer<BST<Integer,Integer>> showValues = t2 -> t2.values().forEach(v->showValue.accept(v));
            Integer key = getKey.apply(t,e);
            t.remove(key);
            t.put(key, duplicateValues.apply(e));
        });

        Function<BST<Integer,Integer>,BST<Integer,Integer>> fgetDuplicateValues = t -> {duplicateValuesInTree.accept(t);return t;};
        Consumer<Integer> showValue = v ->  System.out.print(v);
        BiPredicate<Integer,Integer> menor = (a,b) -> a < b;
        final AtomicInteger count = new AtomicInteger(1);
        BiConsumer<Boolean,Integer> biconsumer = (b,v) -> {if(b) {showValue.accept(v);System.out.print(","); count.incrementAndGet();}if(!b){ showValue.accept(v);System.out.print("\n\n\n");}};
        BiConsumer<BST<Integer,Integer>, Integer> showValues = (t,i) -> t.values().stream().sorted().forEach(e-> biconsumer.accept(menor.test(count.get(),i),e));

        loadTree.accept(tree,new Integer(2),new Integer(12));
        System.out.println("VALUES IN TREE");
        showValues.accept(tree, tree.size());
        System.out.println("DUPLICATED VALUES OF THE TREE");
        count.set(1);
        showValues.accept(fgetDuplicateValues.apply(tree),new Integer(tree.size()));
        tree.getRoot().graphicBST("Tree.jpg");


        //Callable callable;
        //Supplier<Integer> supplier;
        //BiFunction<Integer,Integer,Integer> bifunction;
        //UnaryOperator<Integer> unaryOperator;
        //BinaryOperator<Integer> binaryoperator;
        //Predicate<Integer> predicate;
        //Funcion predicado: Se trata de expresiones que aceptan uno o dos parámetros
        // y devuelven un valor lógico. Proporciona metodos estaticos como:
        //and(), or(), isEqual(), negate()
        //Filter filter;
        //Map<Integer,Integer> map;





    }


}
