package Structures.Lambda;

import java.util.Map;
import java.util.Map.Entry;

public class SimpleEntry <K, V> implements Map.Entry<K, V>, java.io.Serializable{
    private final K key;
    private V value;


public SimpleEntry(K key, V value){
        this.key = key;
        this.value = value;
        }

public SimpleEntry(Map.Entry<? extends K, ? extends V> entry){
        this.key = entry.getKey();
        this.value = entry.getValue();
        }

//Obtener la clave
    @Override
public K getKey(){
        return key;
        }

// Obtener valor
public V getValue(){
        return value;
        }

// Cambiar el valor del par clave-valor
public V setValue(V value){
        V oldValue = this.value;
        this.value = value;
        return oldValue;
        }

// Compara si dos SimpleExtry son iguales según la clave
public boolean equals(Object o){
        if(o == this){
        return true;
        }
        if(o.getClass() == SimpleEntry.class){
        SimpleEntry se = (SimpleEntry)o;
        return se.getKey().equals(getKey());
        }
        return false;

        }

public int hashCode(){
        return key == null ? 0 : key.hashCode();
        }

public String toString(){
        return key + "=" + value;
        }

}