package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Parser {
    public static int count = 0;
    private static ArrayList<String> rules = new ArrayList<String>();
    private static ArrayList<Integer> lasttermspecialRule = new ArrayList<Integer>();
    private static ArrayList<Integer> firsttermspecialRule = new ArrayList<Integer>();
    private static ArrayList<String> parts = new ArrayList<String>();
    private static ArrayList<String> rightparts = new ArrayList<String>();//this array contains all the right parts
    private static ArrayList<String> terminals = new ArrayList<String>();
    private static ArrayList<Integer> indexes = new ArrayList<Integer>();
    private static ArrayList<String> inputs = new ArrayList<String>();
    private static ArrayList<String>[] parse = new ArrayList[3];
    private static ArrayList<String>[] firstterms;
    public static ArrayList<String>[] lastterms ;
    public static String [][]parseTable;
   // public static String input = "n+*n$";

	
    
    
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% main function
    public static void main(String[] args) throws IOException {
 
		System.out.println("Enter the input for parsing: (put $ at the end)");
		Scanner sc = new Scanner(System.in);
    	String input = sc.nextLine();
    	
       
       rules.add("S ::- $E$;"); //The grammar
       rules.add("E ::- E+T|T;");
       rules.add("T ::- T*F|F;");
       rules.add("F ::- (E)|n;");
       int n = rules.size();
       StringBuilder sb= new StringBuilder();
       for(int i=0; i<n; i++) sb.append(rules.get(i));
       String grammar = sb.toString();
       for(int i=0; i<grammar.length(); i++){
    	   if(lowercase(grammar.charAt(i)) && nonterminal(grammar.charAt(i))) 
    		   terminals.add(Character.toString(grammar.charAt(i)));
       }
       Set<String> set = new HashSet<>(terminals);
       terminals.clear();
       terminals.addAll(set);
      parseTable = new String[terminals.size()+1][terminals.size()+1];
      
      for(int i=0; i<n; i++){
    	  rightparts.addAll(partFinder(rules.get(i)));
      }
      int size = rightparts.size();
      
      for(int i=0; i<size; i++){  	 
    	  String str = rightparts.get(i);
    	  char []strchar = str.toCharArray();
    	  for(int j=0; j<str.length(); j++){
    		  if(uppercase(str.charAt(j))){
    			  strchar[j]='N';
    		  }
    	  }
    	   str = String.copyValueOf(strchar);   	
    	  rightparts.set(i, str);//creating the elements
    	  
      }
      set = new HashSet<>(rightparts);//eliminating repetitive elements
      rightparts.clear();
      rightparts.addAll(set);
      
      for(int i=0; i<terminals.size()+1; i++){ //initialization
   	   for(int j=0; j<terminals.size()+1; j++){
   		   parseTable[i][j]= "e";
   	   }
      }
      
      int k=1;
       for(int i=0; i<terminals.size();i++){
    	   parseTable[k][0] = terminals.get(i);
    	   parseTable[0][k] = terminals.get(i);
    	   k++;
       }
       parseTable[0][0]=" ";
       
     
       
   
       
        lastterms = new ArrayList[n];//keeping lastterms for each variable
        firstterms = new ArrayList[n];//keeping firstterms for each variable
        //############ initializing
        for (int i = 0; i < n; i++) {
            lastterms[i] = new ArrayList<String>();
        }
        for (int i = 0; i < n; i++) {
            firstterms[i] = new ArrayList<String>();
        }
        for (int i = 0; i < 3; i++) {
            parse[i] = new ArrayList<String>();
        }
     
       
        //########################### Ending initialization
        
        count =0; //clear the global variable for lastterms
        for (int i = 0; i < n; i++) { //call the function for each rule
            lasttermFinder(rules.get(i));
        }
        
        for (int i = 0; i < n; i++) //call the function for the number of variables for assurance
       completeLastterm(lasttermspecialRule);
       
        


        count =0;//clear the global variable for firstterms
        for (int i = 0; i < n; i++) { //call the function for each rule
            firsttermFinder(rules.get(i));
        }
        
        for (int i = 0; i < n; i++) //call the function for the number of variables for assurance
       completeFirstterm(firsttermspecialRule);
       
        parseTableGenerator(rules);//filling parse table
        
      

        
       

        for(int i=0; i<terminals.size()+1; i++){//printing parse table
     	   for(int j=0; j<terminals.size()+1; j++){
     		   System.out.print(parseTable[i][j]+"   ");
     	   }
     	   System.out.println();
        }

        parse[0].add("$");
        parse[1].add(input);
        parser(input);
     

        
        //############## printing parse results
        System.out.println("Stack\t\t"+"Input\t\t"+"Action");
        System.out.println("--------------------------------------------------");
      for (int i = 0; i <parse[0].size()-1; i++) { //printing parse   	  
    	  System.out.println(parse[0].get(i)+"\t\t"+ parse[1].get(i)+"\t\t"+ parse[2].get(i));
      	}
      //########################
    }
    
 //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 
    
  /**Below are the lists of functions that were used in the program**/  
    
    
    //This function will find the rule based on its first character 
    public static int ruleFinder(char a){
    	for(int i=0; i<rules.size(); i++){
    		if(rules.get(i).charAt(0)==a)
    			return i;
    	}
    	return 0;
    }
    
    //This function will insert an item in the parse table 
    public static void insertParseTable(String s1, String s2, String str){
    	int row = 0 ,column = 0;
    	for(int i=0; i<terminals.size()+1; i++){
    		if(parseTable[i][0].equals(s1)) row=i;
    		if(parseTable[0][i].equals(s2)) column=i;
    	}
    	parseTable[row][column] = str;
    }
    
    //This function will help eliminating nonterminals
    public static boolean nonterminal(char a){
    	if(a==';' || a==':' ||a=='-' ||a=='|' ||a==' ') return false;
    	return true;
    }

    //This function will recognize a lowercase which is a terminal
    public static boolean lowercase(char a){
        if(a<65 || a>90) return true;
        else return false;
    }
    
    //This function will recognize an uppercase which is a variable
    public static boolean uppercase(char a){
        if(a>=65 && a<=90) return true;
        else return false;
    }
    
    //This function will divide the right side of the given rules
    public static ArrayList<String> partFinder(String rule){
        parts.clear();
        int pos = 0;
        for(int i=0; i<rule.length(); i++){
            if(rule.charAt(i)=='-' || rule.charAt(i)=='|' ||rule.charAt(i)==';'){
                parts.add(rule.substring(pos+1,i).trim());
                pos = i;
            }
        }
        parts.remove(0);//first element is not needed

        return parts;
    }
    
    //This function will find the initial firstterms
    public static void firsttermFinder(String rule){

        partFinder(rule);
        for(int i=0; i<parts.size();i++){
            if(parts.get(i).length()==1){
                if(lowercase(parts.get(i).charAt(0)))
                    firstterms[count].add(parts.get(i));
                else if(uppercase(parts.get(i).charAt(0)))
                    firsttermspecialRule.add(count);//we deal with this later
            }
            else{
                String str = parts.get(i);
                if(lowercase(str.charAt(0))) {
                    firstterms[count].add(Character.toString(str.charAt(0)));
                } else if(uppercase(str.charAt(0))){
                    if(lowercase(str.charAt(1))) {
                        firstterms[count].add(Character.toString(str.charAt(1)));
                    }
                }

            }
        }
        count ++;
    }

    //This function will complete the firstterms
    public static void completeFirstterm(ArrayList specialRule){//needs modification
        for(int i=0; i<specialRule.size();i++){
            int num = (int)specialRule.get(i);
            ArrayList <String>parts = partFinder(rules.get((int)specialRule.get(i)));
            for (int j=0; j<parts.size();j++){
                if(parts.get(j).length()==1 && uppercase(parts.get(j).charAt(0))){
                    char key = parts.get(j).charAt(0);//the letter of the special rule
                    for (int k=0; k<rules.size();k++){//traverse over the rules for match
                        if(rules.get(k).charAt(0)==key){//assumption: no space at the beginning of the rule
                            /*k is the rule which its firstterms must be added
                            * to the current variable (num)*/
                            for(int m=0; m<firstterms[k].size(); m++){
                            	if(!firstterms[num].contains(firstterms[k].get(m)))//if it is not already in the list
                            	firstterms[num].add(firstterms[k].get(m));
                            }
                                
                        }
                    }
                }
            }
        }
    }
    
    //This function will find the initial lastterms
    public static void lasttermFinder(String rule){
    	
        partFinder(rule);
        for(int i=0; i<parts.size();i++){
            if(parts.get(i).length()==1){
                if(lowercase(parts.get(i).charAt(0)))
                    lastterms[count].add(parts.get(i));
                else if(uppercase(parts.get(i).charAt(0)))
                    lasttermspecialRule.add(count);//we deal with this later
            }
            else{
                String str = parts.get(i);
                if(lowercase(str.charAt(str.length()-1))) {
                    lastterms[count].add(Character.toString(str.charAt(str.length()-1)));
                } else if(uppercase(str.charAt(str.length()-1))){
                    if(lowercase(str.charAt(str.length()-2))) {
                        lastterms[count].add(Character.toString(str.charAt(str.length()-2)));
                    }
                }

            }
        }
        count ++;
    }

    //This function will complete the lastterms
    public static void completeLastterm(ArrayList specialRule){//needs modification
        for(int i=0; i<specialRule.size();i++){
            int num = (int)specialRule.get(i);
            ArrayList <String>parts = partFinder(rules.get((int)specialRule.get(i)));
            for (int j=0; j<parts.size();j++){
                if(parts.get(j).length()==1 && uppercase(parts.get(j).charAt(0))){
                    char key = parts.get(j).charAt(0);//the letter of the special rule
                    for (int k=0; k<rules.size();k++){//traverse over the rules for match
                        if(rules.get(k).charAt(0)==key){//assumption: no space at the beginning of the rule
                            /*k is the rule which its lastterms must be added
                            * to the current variable (num)*/
                            for(int m=0; m<lastterms[k].size(); m++){
                            	if(!lastterms[num].contains(lastterms[k].get(m)))//if it is not already in the list
                            	lastterms[num].add(lastterms[k].get(m));
                            }
                                
                        }
                    }
                }
            }
        }
    }
    
    //This function will fill the parse table with appropriate contents
    public static void parseTableGenerator(ArrayList<String> grammar){
    	for(int i=0; i<grammar.size(); i++){
    		ArrayList <String>parts = partFinder(grammar.get(i));
    		for(int j=0; j<parts.size(); j++){
    			String part = parts.get(j);
    			if(part.length()>1){
    				for(int k=0; k<part.length()-1; k++){
    					String substr = part.substring(k, k+2);
    					if(lowercase(substr.charAt(0)) && lowercase(substr.charAt(1))){
    						insertParseTable(Character.toString(substr.charAt(0)), Character.toString(substr.charAt(1)), "=");
    					}else if(lowercase(substr.charAt(0)) && uppercase(substr.charAt(1))){
    						int var = ruleFinder(substr.charAt(1));
    						for(int m=0; m<firstterms[var].size(); m++){
    							insertParseTable(Character.toString(substr.charAt(0)), firstterms[var].get(m),"<");
    						}
    					}
    					else if(uppercase(substr.charAt(0)) && lowercase(substr.charAt(1))){
    						int var = ruleFinder(substr.charAt(0));
    						for(int m=0; m<lastterms[var].size(); m++){
    							insertParseTable(lastterms[var].get(m), Character.toString(substr.charAt(1)), ">");
    						}
    					}
    					if(part.length() >= 3){
    						for(int n=0; n<part.length()-2; n++){
    							String sub = part.substring(n,n+3);
    							if(lowercase(sub.charAt(0)) && uppercase(sub.charAt(1)) && lowercase(sub.charAt(2))){
    								insertParseTable(Character.toString(sub.charAt(0)),Character.toString(sub.charAt(2)), "=");
    							}
    						}
    					}
    				
    				}
    			}
    		}
    	}
    }
    
    //This function will show the proper comment
    public static void SyntaxError(){
    	System.out.println("The parser encountered a problem while parsing the input string.");
    }
    
    //This function will show the acceptance of the string
    public static void success(){
    	System.out.println("String was parsed successfully!");
    }
    
    //This function will get the result from the parse table
    public static String fetch(char a, char b){
    	int row = 0 ,column = 0;
    	String s1 = Character.toString(a);
    	String s2 = Character.toString(b);
    	for(int i=0; i<terminals.size()+1; i++){
    		if(parseTable[i][0].equals(s1)) row=i;
    		if(parseTable[0][i].equals(s2)) column=i;
    	}
    	return parseTable[row][column];
    }
    
    //This function will correct the handle
    public static String correct(String handle){
    	StringBuilder sb = new StringBuilder();
    	sb.append(handle);
    	sb.deleteCharAt(handle.indexOf('<'));
    	String str=sb.toString();
    	return str;
    }
    
    //This function will parse the input string
    public static void parser(String input){
    	StringBuilder sb = new StringBuilder();
    	String handle = null;
    	for(int i=0; i<200; i++){ 
    		if(parse[0].get(i).equals("$N") && parse[1].get(i).equals("$")){
    			parse[0].add("$N");
    			parse[1].add("$");
    			parse[2].add("accept");
    			success();
    			return;
    		}
    		else{
    			String yardstick = null;
    			if(parse[0].get(i).charAt(parse[0].get(i).length()-1)!='N')
    				 yardstick = fetch(parse[0].get(i).charAt(parse[0].get(i).length()-1),parse[1].get(i).charAt(0));//passing arguments
    			if(parse[0].get(i).charAt(parse[0].get(i).length()-1)=='N')
    				yardstick = fetch(parse[0].get(i).charAt(parse[0].get(i).length()-2),parse[1].get(i).charAt(0));
    			if(yardstick.equals("e")){//the symbol table entry is empty 
    				SyntaxError();
    				return;
    			}
    			else if(yardstick.equals("=")){
    				sb = new StringBuilder();
    				sb.append(parse[0].get(i));
    				sb.append(parse[1].get(i).charAt(0));
    				String str = sb.toString();
    				parse[0].add(str);
    				sb = new StringBuilder();
    				str = parse[1].get(i);
    				sb.append(str);
    				sb.deleteCharAt(0);
    				str = sb.toString();
    				parse[1].add(str);
    				parse[2].add("shift");
    			}
    			else if(yardstick.equals("<")){
    				sb = new StringBuilder();
    				sb.append(parse[0].get(i));
    				sb.append("<");
    				sb.append(parse[1].get(i).charAt(0));
    				String str = sb.toString();
    				parse[0].add(str);
    				sb = new StringBuilder();
    				str = parse[1].get(i);
    				sb.append(str);
    				sb.deleteCharAt(0);
    				str = sb.toString();
    				parse[1].add(str);
    				parse[2].add("shift");
    			}
    			else if(yardstick.equals(">")){
    				sb = new StringBuilder();
    				int index =0;
    				String str= parse[0].get(i);
    				for(int j = str.length()-1; j>=0; j--){
    					if(str.charAt(j) == '<'){
    						index=j;
    						break;
    					}
    					
    				}
    				if(str.charAt(index-1)!='N'){
    				handle = str.substring(index,str.length());
    				}
    				else{
    					handle = str.substring(index-1,str.length());
    				}    			
    				String result= correct(handle);
    				if(!rightparts.contains(result)){//if the handle does not match with the right side of any rules
    					parse[0].add(parse[0].get(i));
        				parse[1].add(parse[1].get(i));
        				parse[2].add("syntax error");
    					SyntaxError();
    					return;
    				}
    				if(str.charAt(index-1) == 'N') index--;  				
    				sb.append(str);
    				sb.delete(index, str.length());
    				sb.append("N");
    				str = sb.toString();
    				parse[0].add(str);
    				parse[1].add(parse[1].get(i));
    				parse[2].add("reduce");
    				
    			}
    			
    			
    		}
    	}
    }
    
    




}
