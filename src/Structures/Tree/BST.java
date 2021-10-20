package Structures.Tree;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.PdfImage;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

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
            size=1;
        }
        else{
            size++;
            this.getRoot().addNodeInBST(this.getRoot(),code,value);
        }
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public V put(K key, V value){
        add(key,value);
        return value;
    }

    @Override
    public boolean remove(Object key, Object value){
        try {
            remove(key);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }


    @Override
    public V remove(Object key){
        Node<K,V> nres = this.getRoot().rmNodeInBST(this.getRoot(),(K)key);
        this.setRoot(nres);
        size--;
        if(size == 0) this.setRoot(null);
        if (nres != null) return nres.getValue(); else return null;
    }

    @Override
    public void graphic(String path){
        graphicTree(path,new String("BST"));
    }

    @Override
    protected String getCodigoInterno(Node<K,V> node, String etiqueta) {
        size++;
        if(node.getChilds().size()==0){
            if(node.getGrades()!=-1)  etiqueta="nodo"+node.getKey()+" [ label =\""+ "key("+node.getKey()+")-value("+node.getValue().toString()+")"+"\\nlevel("+node.getLevel()+")-grades("+node.getGrades()+")-siblings("+node.getCountSiblings()+")\"];\n";
            if(node.getGrades()==-1)  etiqueta="nodo"+node.getKey()+" [ label =\""+ "key("+node.getKey()+")-value("+node.getValue().toString()+")\"];\n";
        }
        else{
            if(node.getGrades()!=-1)  etiqueta="nodo"+node.getKey()+" [ label =\"<C0>|"+ "key("+node.getKey()+")-value("+node.getValue().toString()+")"+"\\nlevel("+node.getLevel()+")-grades("+node.getGrades()+")-siblings("+node.getCountSiblings()+")|<C1>\"];\n";
            if(node.getGrades()==-1)   etiqueta="nodo"+node.getKey()+" [ label =\"<C0>|"+ "key("+node.getKey()+")-value("+node.getValue().toString()+")|<C1>\"];\n";
        }
        for(Node<K,V> n : node.getChilds()){
            if(((Comparable)n.getValue()).compareTo(node.getValue())<=0){
                etiqueta=etiqueta + getCodigoInterno(n,etiqueta) +
                        "nodo"+n.getParent().getKey()+":C0->nodo"+n.getKey()+"\n";
            }
            else{
                etiqueta=etiqueta + getCodigoInterno(n,etiqueta) +
                        "nodo"+node.getKey()+":C1->nodo"+n.getKey()+"\n";
            }

        }
        return etiqueta;
    }


    @Override
    protected String getCodigoGraphviz() {
        size=0;
        return "digraph grafica{\n" +
                "labelloc=\"t\";"+
                "label =\"RESULTANT BST TREE\n\""+
                "rankdir=TB;\n" +
                "node [shape = record, style=filled, fillcolor=seashell2];\n"+
                getCodigoInterno(this.getRoot(),new String(""))+
                "}\n";
    }



}
