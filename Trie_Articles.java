import java.util.*;

//import com.sun.org.apache.xml.internal.utils.Trie;

import java.io.*;
import java.lang.*;

class Trie_Articles{

    public static ArrayList<String> finalWordSet = new ArrayList<>();
    public static Trie t = new Trie(); 
    HashMap<String,Integer> hitCount = new HashMap<String, Integer>();

    public static void main(String[] args) throws Exception {
        //Trie t = new Trie(); 
        String message ="";
        Trie_Articles a = new Trie_Articles();
        Scanner sc = new Scanner(System.in);
        String messageTemp = sc.nextLine();
        String[] words = messageTemp.split("\\W+");
        for(int i=0;i<words.length;i++){
            //System.out.print(words[i]+ " ");
            finalWordSet.add(words[i]);
        }
        //System.out.println();

        //System.out.println(finalWordSet);
        for(int i =0; i<finalWordSet.size();i++){
            if(finalWordSet.get(i).matches("a") || finalWordSet.get(i).matches("A") || finalWordSet.get(i).matches("an") || 
            finalWordSet.get(i).matches("An") || finalWordSet.get(i).matches("the") || finalWordSet.get(i).matches("The") || 
            finalWordSet.get(i).matches("but") || finalWordSet.get(i).matches("But") || finalWordSet.get(i).matches("or") || 
            finalWordSet.get(i).matches("Or") || finalWordSet.get(i).matches("and") || finalWordSet.get(i).matches("And")){
                System.out.println(finalWordSet.get(i));
                finalWordSet.remove(finalWordSet.get(i));
                i--;
            }           
        }

        //System.out.println();
        //a, an, the, and, or, but
        System.out.println(finalWordSet);

        // t.insert("the");
         t.insert("Microsoft corporation");
        // t.insert("amAzon");
         System.out.println(t.search("Microsoft corporation"));
        a.readCompaniesFile();
        
    }
    public void readCompaniesFile() throws Exception{

        File f = new File("companies.dat");
        Scanner sc = new Scanner(f);
        ArrayList<String> temp = new ArrayList<>();
        while(sc.hasNextLine()){
            String tempArr[] = sc.nextLine().split("\t");

            for(int i=0;i<tempArr.length;i++){
                String newTemp ="";
                for(int j=0;j<tempArr[i].length();j++){         
                    if(Character.isLetter(tempArr[i].charAt(j)) || tempArr[i].charAt(j) == ' '){
                        newTemp = newTemp + tempArr[i].charAt(j);
                    }
                      
                }
                temp.add(newTemp); 
                //temp.add(tempArr[i]);
            }

        }

        System.out.println(temp);
    }


}

class TNode     //https://www.sanfoundry.com/java-program-implement-trie/
{
    char data;
    boolean endOfWord;
    int count;
    LinkedList<TNode> childNode;

    public TNode(char c){

        childNode = new LinkedList<TNode>();
        endOfWord = false;
        data = c;
        count = 0;
    }

    public TNode subNode(char c){
        if (childNode != null){
            for (TNode eachChild : childNode)
            {
                if (eachChild.data == c)
                {   
                    return eachChild;
                }
            }
        }
        return null;
    }
}

class Trie  //https://www.sanfoundry.com/java-program-implement-trie/
{

    private TNode root;

    public Trie()
    {
        root = new TNode(' ');
    }

    public void insert(String word)
    {
        if (search(word) == true) 
            return;        
        TNode current = root; 
        for (char ch : word.toCharArray() )
        {
            TNode child = current.subNode(ch);
            if (child != null)
            {
                current = child;
            }
            else 
            {
                 current.childNode.add(new TNode(ch));
                 current = current.subNode(ch);
            }
            current.count++;
        }
        current.endOfWord = true;
    }
    public boolean search(String word)
    {
        TNode current = root;  
        for (char ch : word.toCharArray() )
        {
            if (current.subNode(ch) == null)
                return false;
            else
                current = current.subNode(ch);
        }      
        if (current.endOfWord == true) 
            return true;
        return false;
    }
}