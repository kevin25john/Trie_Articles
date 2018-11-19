import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.DecimalFormat;

class Trie_Articles{

    public static ArrayList<String> finalWordSet = new ArrayList<>();
    public static Trie t = new Trie(); 
    public static HashMap<String,Integer> hitCount = new HashMap<String, Integer>();
    public static HashMap<String,Double> relevance = new HashMap<String, Double>();
    ArrayList<String> wordArrayCompanies = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        String message ="";
        Trie_Articles a = new Trie_Articles();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Article: ");
        String messageTemp = sc.nextLine();
        String[] words = messageTemp.split("\\W+");
        for(int i=0;i<words.length;i++){
            finalWordSet.add(words[i]);
        }
        
        a.readCompaniesFile();
        a.wordArrayMatch();

        for(int i =0; i<finalWordSet.size();i++){
            if(finalWordSet.get(i).matches("a") || finalWordSet.get(i).matches("A") || finalWordSet.get(i).matches("an") || 
            finalWordSet.get(i).matches("An") || finalWordSet.get(i).matches("the") || finalWordSet.get(i).matches("The") || 
            finalWordSet.get(i).matches("but") || finalWordSet.get(i).matches("But") || finalWordSet.get(i).matches("or") || 
            finalWordSet.get(i).matches("Or") || finalWordSet.get(i).matches("and") || finalWordSet.get(i).matches("And")){
                finalWordSet.remove(finalWordSet.get(i));
                i--;
            }           
        }

        for (int i=0;i<finalWordSet.size();i++){

            t.insert(finalWordSet.get(i));
        }
        a.findHitRate();
        a.relevanceCalculate();
        a.display();
        
    }
    public void readCompaniesFile() throws Exception{

        File f = new File("company.dat");
        Scanner sc = new Scanner(f);
        while(sc.hasNextLine()){
            String tempArr[] = sc.nextLine().split("\t");

            for(int i=0;i<tempArr.length;i++){
                String newTemp ="";
                for(int j=0;j<tempArr[i].length();j++){         
                    if(Character.isLetter(tempArr[i].charAt(j)) || tempArr[i].charAt(j) == ' '){
                        newTemp = newTemp + tempArr[i].charAt(j);
                    }
                      
                }
                wordArrayCompanies.add(newTemp);   
            }
            wordArrayCompanies.add("SEPERATOR");   
        }
    }

    public void wordArrayMatch()
    {
        
        for(int i =0; i<wordArrayCompanies.size();i++)
        {
            
            String temp[] = wordArrayCompanies.get(i).split(" ");
            
            if(temp.length>1){
                
                for(int k=0;k<finalWordSet.size();k++){
                    
                    int in =k;
                    int j=0;
                    int count = 0;
                    int inCounter = in;
                    int tempArraylistCounter=k;
                    
                    while(tempArraylistCounter<finalWordSet.size() && finalWordSet.get(tempArraylistCounter).matches(temp[j])){
                        tempArraylistCounter++;
                        j++;
                        count++;

                        if(j==temp.length){
                            break;
                        }
                    }

                    if(count == temp.length){
                        int x=in;
                        int l =0;
                        String wordTemp = "";
                        while(l<count)
                        {
                            wordTemp = wordTemp + finalWordSet.get(x);
                            if(l!= count-1){                                        
                                wordTemp = wordTemp + " ";
                            }
                            finalWordSet.remove(x);
                            l++;
                        }
                        finalWordSet.add(wordTemp);                            
                    }
                        

                }

            }
        }
        System.out.println();

    }

    public void findHitRate(){
        //Finding hitrate with normalization
        String mainCompany = "";
        int hitCounter = 0;
        for(int i=0;i<wordArrayCompanies.size();i++){
            
            if(i==0){
                mainCompany = wordArrayCompanies.get(i);
            }

            if(wordArrayCompanies.get(i).matches("SEPERATOR") && i!=wordArrayCompanies.size()-1){
                hitCount.put(mainCompany, hitCounter);
                mainCompany = wordArrayCompanies.get(i+1);
                hitCounter = 0;
            }

            for(int j=0;j<finalWordSet.size();j++){
                if(wordArrayCompanies.get(i).matches(finalWordSet.get(j))){
                    hitCounter++;
                }                                
            }
                

            if(wordArrayCompanies.get(i).matches("SEPERATOR") && i == wordArrayCompanies.size()-1){
                hitCount.put(mainCompany, hitCounter);
                hitCounter = 0;
            }

        }


         

    }


    public void relevanceCalculate(){   //useless

        double relevanceCount;
        String companyName = "";
        double hitCountNum = 0;

        Iterator it = hitCount.entrySet().iterator();
      
        while(it.hasNext()){                                        //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
            Map.Entry val = (Map.Entry)it.next();
            companyName = val.getKey().toString();
            String temp = val.getValue().toString();
            hitCountNum = Double.parseDouble(temp);
            DecimalFormat df = new DecimalFormat("####.####");
            double totalWords = Double.parseDouble(Integer.toString(finalWordSet.size()));
            double tempRel = (hitCountNum/totalWords)*100;
            String tempFormat = df.format(tempRel);
            relevanceCount = Double.parseDouble(tempFormat);
            relevance.put(companyName, relevanceCount);
            //it.remove();
        }

        


    }

    public void display(){

        String totalWords  = "Total Words";
        int totalWordsCount = totalWords.length();

        int firstColumnCounter = 0;

        String hitCountt = "Hit Count";

        int hitCountSpaceCount = hitCountt.length()+3;
        int relevanceSpaceCount = 12;


        Iterator it = hitCount.entrySet().iterator();
      
        while(it.hasNext()){                                        //https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
            Map.Entry val = (Map.Entry)it.next();
            String companyName = val.getKey().toString();
            if(companyName.length() > firstColumnCounter){
                firstColumnCounter = companyName.length();
            }
            //it.remove();
        }
        firstColumnCounter = firstColumnCounter+3;
        Iterator itNew = hitCount.entrySet().iterator();

        System.out.format("%"+firstColumnCounter+"s"+"%"+hitCountSpaceCount+"s"+"%"+relevanceSpaceCount+"s","Company","Hit Count","Relevance");
        int spaceCount = firstColumnCounter + hitCountSpaceCount + relevanceSpaceCount;
        System.out.println();
        for(int i=0;i<spaceCount;i++){
            System.out.print("-");
        }
        System.out.println();
       
        int totalHitCount = 0;
        double totalRelevance = 0.0000;
        // }
        while(itNew.hasNext()){
            Map.Entry val = (Map.Entry)itNew.next();
            String companyName = val.getKey().toString();
            int hitCounttt = Integer.parseInt(val.getValue().toString());
            double relevanceCountPercent = relevance.get(companyName);
            String relevanceCountPercentString = Double.toString(relevanceCountPercent);
            relevanceCountPercentString = relevanceCountPercentString + "%";
            totalHitCount = totalHitCount + hitCounttt;
            totalRelevance = totalRelevance + relevanceCountPercent;
            System.out.format("%"+firstColumnCounter+"s"+"%"+hitCountSpaceCount+"d"+"%"+relevanceSpaceCount+"s",companyName,hitCounttt,relevanceCountPercentString);
            System.out.println();
        }

        System.out.println();
        for(int i=0;i<spaceCount;i++){
            System.out.print("-");
        }
        System.out.println();
        DecimalFormat dfd = new DecimalFormat("####.####");
        
        String relevenceTotalPercent = dfd.format(totalRelevance);
        relevenceTotalPercent = relevenceTotalPercent + "%";

        System.out.format("%"+firstColumnCounter+"s"+"%"+hitCountSpaceCount+"d"+"%"+relevanceSpaceCount+"s", "Total",totalHitCount,relevenceTotalPercent);
        
        System.out.println();

        System.out.println();
        int totalWordsSpaceCount =spaceCount/2;
        for(int i=0;i<spaceCount;i++){
            System.out.print("-");
        }
        System.out.println();

        System.out.format("%"+totalWordsSpaceCount+"s"+"%"+(totalWordsSpaceCount/2)+"d","Total Words",finalWordSet.size());
        System.out.println();


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