package Structures;

import com.sun.beans.TypeResolver;

import javax.print.attribute.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Structure<K, V> {

    Object structure;
    int structureType;
    int freePos;
    String typeArray;


    /*
        HashSet nos permite almacenar objetos en el conjunto, mientras que HashMap nos permite
        almacenar objetos en función de la clave y el valor.
        HashSet se utiliza principalmente para diseñar operaciones y alto rendimiento,
        como la intersección, la unión y la diferencia de dos conjuntos.
        Si contiene muchos elementos, resulta muy costoso recorrerlos mediante un Iterator

        TreeSet realiza las operaciones basicas en tiempo logarítmico con el número de elementos.
        Además, mantiene todos los elementos ordenados en su orden natural o de acuerdo a como
        indique el Comparator que indique el constructor


        Hashtable está sincronizado , mientras HashMap que no lo está.
        Esto HashMap mejora las aplicaciones sin subprocesos,
        ya que los objetos no sincronizados suelen funcionar mejor que los sincronizados.

        Hashtable no permite claves o valores nulos. HashMap permite una clave nula
        y cualquier cantidad de null en valores.

        Una de las subclases de HashMap es LinkedHashMap, por lo que en el caso de que desee un orden de
        iteración predecible (que es el orden de inserción por defecto),
        puede cambiar fácilmente el HashMappor a LinkedHashMap.
        Esto no sería tan fácil si estuviera usando Hashtable.

        Concurrent Hashmap is a class that was introduced in jdk1.5.
        Concurrent hash map applies locks only at bucket level called fragment while
        adding or updating the map. So, a concurrent hash map allows concurrent read and write
        operations to the map.

        HashTable is a thread-safe legacy class introduced in the Jdk1.1.
        It is a base implementation of Map interface. It doesn't allow null keys and values.
        It is synchronized in nature so two different threads can’t access simultaneously.
        Hashtable does not maintain any order.

        HashSet contiene un conjunto de objetos, pero de una manera que le permite determinar fácil
        y rápidamente si un objeto ya está en el conjunto o no. Lo hace administrando internamente una matriz
        y almacenando el objeto utilizando un índice que se calcula a partir del código hash del objeto.
        Este conjunto contiene una colección de elementos únicos, mediante una implementación basada en hash,
        pero desordenados. Es decir, no garantiza el orden de iteración del conjunto,
        lo que significa que la clase no garantiza el orden constante de los elementos a lo largo del tiempo.
        Por otro lado esta clase permite el elemento nulo además de permitir las operaciones de recopilación
        estándar (Agregar, Eliminar, etc).

        LinkedHashSet, es una versión ordenada de HashSet. En cuanto al rendimiento y velocidad,
        la clase más rápida es HashSet aunque tanto HashSet y LinkedHashSet ofrecen un rendimiento
        de tiempo constante.
        El LinkedHashSet es una versión ordenada de HashSet que mantiene una lista doblemente enlazada
        en todos los elementos. Almacena una colección de cosas sin la asociacion clave-valor.
        LinkedHashSet no cambia el valor original. Permite un único valor nulo.

        LinkedHashMap reemplaza el valor con una clave duplicada.
        LinkedHashMap tiene elementos en pares clave-valor, por lo que solo tiene una clave nula
        y varios valores nulos.



    */

    public Structure(int structureType, Optional<Integer> size){
        this.structureType = structureType;
        freePos = 0;
        switch(structureType){
            case 1: {  //1 = ArrayList
                structure = new ArrayList<>();
                break;
            }
            case 2: {  //2 = ListInt
                structure = new LinkedList<>();
                break;
            }
            case 3: {  //3 = ArrayInt
                break;
            }
            case 4: {  //4 = Hashtable
                structure = new Hashtable<K,V>();
                break;
            }
            case 5: {  //5 = HashMap
                structure = new HashMap<K,V>();
                break;
            }
            case 6: {  //6 = LinkedHashMap
                structure = new LinkedHashMap<K,V>();
                break;
            }
            case 7: {  //7 = LinkedHashSet
                structure = new LinkedHashSet<V>();
                break;
            }
            case 8: {  //13 = TreeSet
                structure = new TreeSet<V>();
                break;
            }
            case 9: {  //14 = concurrentHashMap
                structure = new ConcurrentHashMap<K, V>();
                break;
            }
            case 10: {  //15 = HashSet
                structure = new HashSet<V>();
                break;
            }

        }
    }

    public void add(Optional<K> key, V value) {
        switch(structureType){
            case 1: {  //1 = ArrayList
                ((ArrayList<V>)structure).add(value);
                break;
            }
            case 2: {  //2 = ListInt
                ((LinkedList<V>)structure).add(value);
                break;
            }
            case 3: {  //3 = ArrayInt
                switch(typeArray) {
                    case "Integer": {
                        ((Integer[])structure)[freePos]=(Integer)value;
                        freePos++;
                        break;
                    }
                }

                break;
            }
            case 4: {  //4 = Hashtable
                if(key.isPresent()) {
                    ((Hashtable<K, V>) structure).put(key.get(), value);
                }
                break;
            }
            case 5: {  //5 = HashMap
                if(key.isPresent()) {
                    ((HashMap<K, V>) structure).put(key.get(), value);
                }
                break;
            }
            case 6: {  //6 = LinkedHashMap
                if(key.isPresent()) {
                    ((LinkedHashMap<K, V>) structure).put(key.get(), value);
                }
                break;
            }
            case 7: {  //7 = LinkedHashSet
                ((LinkedHashSet<V>) structure).add(value);
                break;
            }
            case 8: {  //8 = TreeSet
                ((TreeSet<V>) structure).add(value);
                break;
            }
            case 9: {  //9 = ConcurrentHashMap
                ((ConcurrentHashMap<K,V>) structure).put(key.get(),value);
                break;
            }
            case 10: {  //10 = HashSet
                ((HashSet<V>) structure).add(value);
                break;
            }

        }
    }

    public void setTypeArray(String typeArray, int size){
        this.typeArray = typeArray;

            if(typeArray.equals("Integer")) {
                structure = new Integer[size];
            }
    }


    public Object internalStruct(){
        return structure;
    }
}
