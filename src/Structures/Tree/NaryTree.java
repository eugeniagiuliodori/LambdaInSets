package Structures.Tree;

import Structures.Lambda.SimpleEntry;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.PdfImage;
import jdk.internal.util.xml.impl.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class NaryTree<K extends Comparable<K>,V> implements Map<K,V> {

    private Node<K,V> root;
    private int size;



    public NaryTree(){
        size=0;
    }

    private void registerProperties(Node<K,V> node){
        if(!node.isRoot()) {
            node.setCountSiblings(node.getParent().getChilds().size() - 1);
            node.setGrades(node.getChilds().size());
            node.setLevel(node.getParent().getLevel() + 1);
        }
        else{
            node.setCountSiblings(0);
            node.setGrades(1);
            node.setLevel(1);
        }
        for(Node<K,V> nodev : node.getChilds()){
            registerProperties(nodev);
        }
    }

    public void updateProperties(){
        registerProperties(root);
    }

    private void add(K code,V value){
        if(root == null){
            root = new Node(code, value);
            root.setLeaf(true);
            root.setIsRoot(true);
            root.setParent(null);
            size=1;
        }
        else{
            size++;
            if(size==2) root.setLeaf(false);
            root.addNode(root,code,value);
        }
    }

    private Node<K,V> rm(K code){
        Node<K,V> n = root.rmNode(root,code);
        this.setRoot(n);
        size--;
        return root;
    }

    public void update(K code, V value){
        root.updateNode(root,code, value);
    }

    public Node<K,V> search(K code){
        return root.searchNode(root,code);
    }

    public List<Node<K,V>> searchs(V value){
        return root.searchs(root,new LinkedList<>(),value);
    }


    private String toStringRec(Node<K,V> nodeparent, String s){
        for(Node<K,V> node : nodeparent.getChilds()){
            s = s + ","+node.getValue().toString();
        }
        for(Node<K,V> n : nodeparent.getChilds()) {
            s = toStringRec(n,s);
        }
        return s;
    }

    public String toString(){
        return root.getValue() + toStringRec(root,new String(""));
    }

    protected void setRoot(Node<K,V> root){
        this.root=root;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean containsKey(Object key) {
        return search((K)key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return searchs((V)value).size() > 0;
    }

    @Override
    public V get(Object key) {
        return search((K)key).getValue();
    }

    @Override
    public V put(K key, V value) {
        add(key, value);
        return value;
    }

    @Override
    public V remove(Object key) {
        Node<K,V> node = rm((K)key);
        if(node != null){
            return node.getValue();
        }
        if(size == 0) root = null;
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {}

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        if(this.values().iterator().hasNext()) {
            for (V value : this.values()) {
                List<Node<K, V>> listValue = this.searchs(value);
                for (Node<K, V> node : listValue) {
                    if(!set.contains(node.getKey())){
                        set.add(node.getKey());
                    }
                }
            }
        }
        return set;
    }


    @Override
    public Collection<V> values() {
        return iterate();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K,V>> set = new HashSet<>();
        for(K key : this.keySet()){
            Entry<K,V> entry = new SimpleEntry(key,this.search(key).getValue());
            set.add(entry);
        }
        return set;
    }

    @Override
    public boolean equals(Object o) {
        return this.equals(o);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    private List<V> iterateRec(Node<K,V> nodeparent, List<V> list){
        for(Node<K,V> node : nodeparent.getChilds()){
            list.add(node.getValue());
        }
        for(Node<K,V> n : nodeparent.getChilds()) {
            iterateRec(n,list);
        }
        return list;
    }

    private List<V> iterate(){
        List<V> list = new LinkedList<>();
        list.add(root.getValue());
        return iterateRec(root,list);
    }

    protected String getCodigoGraphviz() {
        return "digraph grafica{\n" +
                "labelloc=\"t\";"+
                "label =\"RESULTANT N-ARY TREE\n\""+
                "rankdir=TB;\n" +
                "node [shape = record, style=filled, fillcolor=seashell2];\n"+
                getCodigoInterno(root,new String(""))+
                "}\n";
    }
    protected String getCodigoInterno(Node<K,V> node, String etiqueta) {
        if (node.getChilds().size() == 0) {
            if(node.getGrades()!=-1)  etiqueta = "nodo" + node.getKey() + " [ label =\"" + "key(" + node.getKey() + ")-value(" + node.getValue().toString() + ")"+"\\nlevel("+node.getLevel()+")-grades("+node.getGrades()+")-siblings("+node.getCountSiblings()+")\"];\n";
            if(node.getGrades()==-1)  etiqueta = "nodo" + node.getKey() + " [ label =\"" + "key(" + node.getKey() + ")-value(" + node.getValue().toString() + ")\"];\n";
        } else {
            if(node.getGrades()!=-1)  etiqueta = "nodo" + node.getKey() + " [ label =\"" + "key(" + node.getKey() + ")-value(" + node.getValue().toString() + ")"+"\\nlevel("+node.getLevel()+")-grades("+node.getGrades()+")-siblings("+node.getCountSiblings()+")\"];\n";
            if(node.getGrades()==-1)  etiqueta = "nodo" + node.getKey() + " [ label =\"" + "key(" + node.getKey() + ")-value(" + node.getValue().toString() + ")\"];\n";
        }
        for(Node<K,V> n : node.getChilds()){
            etiqueta=etiqueta + getCodigoInterno(n,etiqueta) +
                    "nodo"+node.getKey()+"->nodo"+n.getKey()+"\n";
        }
        return etiqueta;
    }

    public void graphic(String path){
        graphicTree(path,new String("N-ARY"));
    }
    protected void graphicTree(String path, String type) {
        FileWriter fichero = null;
        PrintWriter escritor;
        Node<K,V> node = root;
        try {
            if(path.equals(type+"Tree.jpg"))fichero = new FileWriter(type+"GTree.dot");
            if(path.equals(type+"TreeWithDuplicates.jpg"))fichero = new FileWriter(type+"GTreeWithDuplicates.dot");
            escritor = new PrintWriter(fichero);
            while(node.getParent()!=null){
                node=node.getParent();
            }
            escritor.print(getCodigoGraphviz());
        }
        catch (Exception e){
            try{
                if(path.equals(type+"Tree.jpg"))fichero = new FileWriter(type+"GTree.dot");
                if(path.equals(type+"TreeWithDuplicates.jpg"))fichero = new FileWriter(type+"GTreeWithDuplicates.dot");
                escritor = new PrintWriter(fichero);
                while(node.getParent()!=null){
                    node=node.getParent();
                }
                escritor.print(getCodigoGraphviz());
            }
            catch(Exception ex) {
                System.err.println("Error at graphic");
            }
        }
        finally{
            try {
                if (null != fichero)
                    fichero.close();
            }
            catch (Exception e2){
                System.err.println("Error at close file");
            }
        }
        try{
            Runtime rt = Runtime.getRuntime();
            File file = new File(path);
            file.delete();
            Process process=null;
            if(path.equals(type+"Tree.jpg")){
                process = rt.exec( "dot -Tjpg -o "+path+" "+type+"GTree.dot");
            }
            if(path.equals(type+"TreeWithDuplicates.jpg")){
                process = rt.exec( "dot -Tjpg -o "+path+" "+type+"GTreeWithDuplicates.dot");
            }
            while(process.isAlive()){}
            PdfDocument doc = new PdfDocument();
            PdfPageBase page = doc.getPages().add();
            PdfImage image = PdfImage.fromFile(path);
            double widthFitRate = getImgWidth(new File(path))/ page.getActualBounds(true).getWidth();
            double heightFitRate = getImgHeight(new File(path))/ page.getActualBounds(true).getHeight();
            double fitRate = Math.max(widthFitRate, heightFitRate);
            double fitWidth = getImgWidth(new File(path)) / fitRate*0.8f;
            double fitHeight = getImgHeight(new File(path))/ fitRate*0.8f;
            page.getCanvas().drawImage(image, 50, 30, fitWidth, fitHeight);
            doc.saveToFile(path.replace(".jpg",".pdf"));
            doc.close();
            File f = new File(path.replace(".jpg",".pdf"));
            f.setWritable(false);
            String s = f.getPath();
            try {
                Runtime.getRuntime().exec("okular " + f.getPath());
            }
            catch(Exception e){
                Desktop.getDesktop().open(f);
            }
            while(process.isAlive()){}
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error at graphic");
        }
    }

    protected static int getImgHeight(File ImageFile) {

        InputStream is = null;
        BufferedImage src = null;

        int ret = -1;

        try {
            is = new FileInputStream(ImageFile);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getHeight(null);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }



    protected static int getImgWidth(File ImageFile) {

        InputStream is = null;
        BufferedImage src = null;

        int ret = -1;

        try {
            is = new FileInputStream(ImageFile);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getWidth(null);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    protected Node<K,V> getRoot(){return root;}




}
