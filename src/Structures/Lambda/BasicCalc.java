package Structures.Lambda;

import Structures.Structure;
import Structures.Tree.BST;
import Structures.Tree.NaryTree;
import Structures.Tree.Order;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class BasicCalc {

    private static final Integer codeArrayListInt= 1;
    private static final Integer codeListInt= 2;
    private static final Integer codeArrayInt= 3;
    private static final Integer codeHashTable= 4;
    private static final Integer codeHashMap= 5;
    private static final Integer codeLinkedHashMap= 6;
    private static final Integer codeLinkedHashSet= 7;
    private static final Integer codeTreeSet= 8;
    private static final Integer codeConcurrentHashMap= 9;
    private static final Integer codeHashSet= 10;
    private static final Integer codeNaryTree = 11;
    private static final Integer codeBST = 12;
    private static final int minCode = codeArrayListInt;
    private static final int maxCode = codeBST;

    private static Integer maxElem = 100;
    private static Structure<Integer, Integer> arrayListInt = new Structure<>(codeArrayListInt,Optional.empty());
    private static Structure<Integer, Integer> listInt = new Structure<>(codeListInt, Optional.empty());
    private static Structure<Integer, Integer> arrayInt = new Structure<>(codeArrayInt, Optional.of(maxElem));
    private static Structure<Integer, Integer> hashtable = new Structure<>(codeHashTable, Optional.empty());
    private static Structure<Integer, Integer> hashmap = new Structure<>(codeHashMap, Optional.empty());
    private static Structure<Integer, Integer> linkedhashmap = new Structure<>(codeLinkedHashMap, Optional.empty());
    private static Structure<Integer, Integer> linkedhashset = new Structure<>(codeLinkedHashSet, Optional.empty());
    private static Structure<Integer, Integer> treeset = new Structure<>(codeTreeSet, Optional.empty());
    private static Structure<Integer, Integer> concurrenthashmap = new Structure<>(codeConcurrentHashMap, Optional.empty());
    private static Structure<Integer, Integer> hashset = new Structure<>(codeHashSet, Optional.empty());
    private static Structure<Integer, Integer> narytree = new Structure(codeNaryTree, Optional.empty());
    private static Structure<Integer, Integer> bst = new Structure(codeBST, Optional.empty());

    private static Stream getStream(Structure structure, boolean isPrimitiveArray, String strPrimitiveArray){
        if(!isPrimitiveArray){
            if(structure.internalStruct() instanceof HashMap ||
               structure.internalStruct() instanceof LinkedHashMap ||
               structure.internalStruct() instanceof ConcurrentHashMap
            ){
                return ((AbstractMap) (structure.internalStruct())).values().stream();
            }
            else {
                if(structure.internalStruct() instanceof Hashtable ||
                        structure.internalStruct() instanceof  NaryTree ||
                        structure.internalStruct() instanceof BST){
                    return ((Map) (structure.internalStruct())).values().stream();
                }
                else {
                    return ((Collection) (structure.internalStruct())).stream();
                }
            }
        }
        else{
            if(strPrimitiveArray.equals("Integer")) {
                return Arrays.stream(((Integer[]) (structure.internalStruct())));
            }
            else{
                return null;
            }
        }
    }

    public static void calculateAndShowInSet(Supplier<Stream<Integer>> stream, int size){
        int total1 = stream.get().reduce((acumulador,num) -> acumulador + num).get();
        int total2 = stream.get().reduce(0,Integer::sum);
        double average = stream.get().mapToInt(e -> (Integer)e).average().getAsDouble();
        System.out.println("Total version 1: "+total1);
        System.out.println("Total version 2: "+total2);
        System.out.println("Promedio: "+average);
        System.out.println("Sumatorias parciales:");
        final AtomicInteger count = new AtomicInteger(0);
        IntStream.range(0, size)
                .map(i -> IntStream.rangeClosed(0, i).reduce(0,Integer::sum))
                .boxed()
                .collect(Collectors.toList()).stream().forEach(e -> {
                                                                        count.incrementAndGet();
                                                                        if((count.get()%30)==0&&count.get()!=0){
                                                                            System.out.print(e + "\n");
                                                                        }
                                                                        else{
                                                                            System.out.print(e + ",");
                                                                        }
                                                                    }
                                                                );
        System.out.print(total1+"\n");
    }


    private static void calculateAndShowInSets(List<Integer> codeEstructList){
        Function<Integer,Object> fgetStruct = n -> {
            if(n.compareTo(codeArrayListInt)==0){
                return (ArrayList<Integer>)arrayListInt.internalStruct();
            }
            if(n.compareTo(codeListInt)==0){
                return (LinkedList<Integer>)listInt.internalStruct();
            }
            if(n.compareTo(codeArrayInt)==0){
                return (Integer[])(arrayInt.internalStruct());
            }
            if(n.compareTo(codeHashTable)==0){
                return (Hashtable<Integer,Integer>)hashtable.internalStruct();
            }
            if(n.compareTo(codeHashMap)==0){
                return (HashMap<Integer,Integer>)hashmap.internalStruct();
            }
            if(n.compareTo(codeLinkedHashMap)==0){
                return (LinkedHashMap<Integer,Integer>)linkedhashmap.internalStruct();
            }
            if(n.compareTo(codeLinkedHashSet)==0){
                return (LinkedHashSet<Integer>)linkedhashset.internalStruct();
            }
            if(n.compareTo(codeTreeSet)==0){
                return (TreeSet<Integer>)treeset.internalStruct();
            }
            if(n.compareTo(codeConcurrentHashMap)==0){
                return (ConcurrentHashMap<Integer,Integer>)concurrenthashmap.internalStruct();
            }
            if(n.compareTo(codeHashSet)==0){
                return (HashSet<Integer>)hashset.internalStruct();
            }
            if(n.compareTo(codeNaryTree)==0){
                return (NaryTree<Integer,Integer>)narytree.internalStruct();
            }
            if(n.compareTo(codeBST)==0){
                return (BST<Integer,Integer>)bst.internalStruct();
            }
            return null;
        };
        Function<Integer,Structure<Integer,Integer>> fgetStructure = n -> {
            if(n.compareTo(codeArrayListInt)==0){
                return arrayListInt;
            }
            if(n.compareTo(codeListInt)==0){
                return listInt;
            }
            if(n.compareTo(codeArrayInt)==0){
                return arrayInt;
            }
            if(n.compareTo(codeHashTable)==0){
                return hashtable;
            }
            if(n.compareTo(codeHashMap)==0){
                return hashmap;
            }
            if(n.compareTo(codeLinkedHashMap)==0){
                return linkedhashmap;
            }
            if(n.compareTo(codeLinkedHashSet)==0){
                return linkedhashset;
            }
            if(n.compareTo(codeTreeSet)==0){
                return treeset;
            }
            if(n.compareTo(codeConcurrentHashMap)==0){
                return concurrenthashmap;
            }
            if(n.compareTo(codeHashSet)==0){
                return hashset;
            }
            if(n.compareTo(codeNaryTree)==0){
                return narytree;
            }
            if(n.compareTo(codeBST)==0){
                return bst;
            }
            return null;
        };
        Function<Integer,String> fgetNameStruct =  n -> {
            if(n.compareTo(codeArrayListInt)==0){
                return "ARRAYLIST";
            }
            if(n.compareTo(codeListInt)==0){
                return "LINKEDLIST";
            }
            if(n.compareTo(codeArrayInt)==0){
                return "ARRAY";
            }
            if(n.compareTo(codeHashTable)==0){
                return "HASHTABLE";
            }
            if(n.compareTo(codeHashMap)==0){
                return "HASHMAP";
            }
            if(n.compareTo(codeLinkedHashMap)==0){
                return "LINKEDHASHMAP";
            }
            if(n.compareTo(codeLinkedHashSet)==0){
                return "LINKEDHASHSET";
            }
            if(n.compareTo(codeTreeSet)==0){
                return "TREESET";
            }
            if(n.compareTo(codeConcurrentHashMap)==0){
                return "CONCURRENTHASHMAP";
            }
            if(n.compareTo(codeHashSet)==0){
                return "HASHSET";
            }
            if(n.compareTo(codeNaryTree)==0){
                return "N-ARYTREE";
            }
            if(n.compareTo(codeBST)==0){
                return "BINARY-SEARCH-TREE";
            }
            return null;
        };

        for(Integer n : codeEstructList){
            System.out.println("\nCALCULOS EN "+fgetNameStruct.apply(n) + ":");
            BiFunction<Structure<Integer,Integer>, Boolean, Stream<Integer>> fgetStream = (struct,b) ->   ((Stream<Integer>)getStream(struct,b,"Integer"));
            Boolean isArray = new Boolean(n.compareTo(codeArrayInt) == 0);
            Supplier<Stream<Integer>> streamSupplier = () -> fgetStream.apply(fgetStructure.apply(n),isArray);
            if(n.compareTo(codeHashMap) == 0|| n.compareTo(codeLinkedHashMap) == 0 || n.compareTo(codeConcurrentHashMap) == 0){
                calculateAndShowInSet(streamSupplier, ((AbstractMap)(fgetStruct.apply(n))).size()-1);
            }
            else {
                if(n.compareTo(codeHashTable) == 0 || n.compareTo(codeNaryTree)==0 || n.compareTo(codeBST)==0){
                    calculateAndShowInSet(streamSupplier, ((Map)(fgetStruct.apply(n))).size()-1);
                }
                else {
                    if(n.compareTo(codeArrayInt) == 0){
                        calculateAndShowInSet(streamSupplier, ((Integer[]) (fgetStruct.apply(n))).length - 1);
                    }
                    else {
                        calculateAndShowInSet(streamSupplier, ((Collection) (fgetStruct.apply(n))).size() - 1);
                    }
                }
            }
        }

    }

    private static void loadStructures(){
        arrayInt.setTypeArray("Integer",maxElem);
        for(Integer i = 0; i.compareTo(maxElem)<0; i++){
            arrayListInt.add(Optional.empty(),i);
            listInt.add(Optional.empty(),i);
            arrayInt.add(Optional.empty(),i);
            hashtable.add(Optional.of(i),i);
            hashmap.add(Optional.of(i),i);
            linkedhashmap.add(Optional.of(i),i);
            linkedhashset.add(Optional.empty(),i);
            treeset.add(Optional.empty(),i);
            concurrenthashmap.add(Optional.of(i),i);
            hashset.add(Optional.empty(),i);
            narytree.add(Optional.of(i),i);
            bst.add(Optional.of(i),i);
        }
    }

    private static List loadCodesStructures(){
        List<Integer> codeStructList = new LinkedList();
        for(Integer i = minCode; i<=maxCode; i++){
            codeStructList.add(Integer.valueOf(i));
        }
        return codeStructList;
    }


    public static void main (String[] args) {
        Order.setOrder(new Integer(2));
        loadStructures();
        List codeStructures = loadCodesStructures();
        calculateAndShowInSets(codeStructures);
    }

}
