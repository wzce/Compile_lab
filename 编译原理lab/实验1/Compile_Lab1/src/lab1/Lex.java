package lab1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * create by wzce13
 * 2015/11/18
 * */

public class Lex {
    private Stack<StringTokenizer> rawInputLines;
    private StringTokenizer tokenedInputLines[];
    private Object processedInput[];
	char temp[];
	//char token[];
	int size=0;
	ArrayList<Token> tagList=new ArrayList<>();
	String keyWord[]={"if","else","for","do","while","return","break","continue","int","double","float","char"};
	
	public Lex(String filepath){
		
		
		File file=new File(filepath);
		try{
			Scanner in = new Scanner(file);
	        rawInputLines = new Stack<StringTokenizer>();
	        while(in.hasNext()){
	        	String nextLine=in.nextLine();
	            rawInputLines.push(new StringTokenizer(nextLine));
	        }
	        tokenedInputLines = new StringTokenizer [rawInputLines.size()];
	        processedInput = new Object[tokenedInputLines.length];
	        for(int i = rawInputLines.size()-1; i >= 0; i--){
	        	StringTokenizer st=(StringTokenizer)rawInputLines.pop();
	            tokenedInputLines[i] = st;
	        }
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("文件打开出错:"+e.toString());
		}
	}
	
	public void analyse(){
		for(int i = 0; i < tokenedInputLines.length; i++){
            Stack tempInputLineValues = new Stack();
            while(tokenedInputLines[i].hasMoreTokens()) {
                convertToChars(
                        new StringReader(tokenedInputLines[i].nextToken()));
            }
            Integer inputLineValues [] = new Integer [tempInputLineValues.size()];
            for(int j = tempInputLineValues.size() - 1; j >= 0; j--){
                inputLineValues[j] = (Integer)tempInputLineValues.pop();
            }
            processedInput[i] = inputLineValues;
        }
	}
	
	private void convertToChars(StringReader sr){
		Stack<Integer> s = new Stack();
        int in = -1;
        do{
            try{
                in = sr.read();
            }
            catch(IOException e){
                break;
            }
            s.push(in);
        } while(in != -1);
        char characters [] = new char [s.size()];
        for(int i = characters.length-1; i >= 0; i--){
            int a = (Integer)s.pop();
            
            if(i < characters.length-1)
            {
                Character b = (char)a;
                characters[i] = b;
                
            }
        }
       // System.out.println("-------单词：-----"+characters[0]);
       // print_char(characters);
      //  System.out.println("*********************");
        dfs(characters,0);
	}
	
	void print_char(char str[]){
		String s=new String(str);
		System.out.println(str);
    	int len=str.length;
    	for(int i=0;i<len;i++){
    		System.out.print(str[i]);
    	}
    	System.out.println("--------");
    }
	
	private void dfs(char str[],int location){
		String token=new String();
		int len=str.length;
		int p=location;
		if(p>=len-1){
			return;	//当已经到了最后一个字符的位置后，已经没有字符串，则跳出
		}
		char ch=str[p];
		if((ch>'A'&&ch<'Z')|(ch>'a'&&ch<'z')|ch=='_'){
			/*标识符或者变量名的情况*/
			while((ch>='0'&&ch<='9')|(ch>='a'&&ch<='z')|(ch>='A'&&ch<='Z')|ch=='_'){
				token+=ch;
				ch=str[++p];
			}
			
			boolean tag=false;
			token+='\0';
			for(int k=0;k<keyWord.length;k++){
				if(token.compareTo(keyWord[k])==0){
					if(token.compareTo("int")==0){
						tagList.add(new Token("INT",token));
					}else if(token.compareTo("double")==0){
						tagList.add(new Token("DOUBLE",token));
					}else if(token.compareTo("float")==0){
						tagList.add(new Token("FLOAT",token));
					}else if(token.compareTo("char")==0){
						tagList.add(new Token("CHAR",token));
					}else if(token.compareTo("for")==0){
						tagList.add(new Token("KEY_WORD",token));
					}
					else{
						tagList.add(new Token("KEY_WORD",token));
					}
					tag=true;
					break;
				}
			}
			
			if(!tag){
				tagList.add(new Token("ID",token));
			}
			
			dfs(str,p);
		}
		else if(ch>='0'&&ch<='9'){
			int num=0;
			boolean flag=true;
			while((ch>='0'&&ch<='9')|ch=='.'){
				if(ch!='.'){
				num=num*10+ch-'0';
				ch=str[++p];
				}else{
					flag=false;
					break;
				}
			}
			
			double tem=0;
			double div=10;
			if(!flag){
			ch=str[++p];
			while((ch>='0'&&ch<='9')){
				tem=tem+((double)(ch-'0'))/div;	
				ch=str[++p];	
				div=10.0*div;
			}
			}
			if(flag){
				tagList.add(new Token("INT",num));
			}else{
				tagList.add(new Token("DOUBLE",(num+tem)));
			}
			
//			System.out.println((num+tem));
			dfs(str,p);
		}else{
			token+=ch;
			int pp=p+1;
			switch(ch){
				
				case '>':
					if(str[pp]=='='){
						token+=str[pp];
						p=pp+1;
						tagList.add(new Token("GTE",null));
						dfs(str,p);
					}else{
						dfs(str,pp);
						tagList.add(new Token("GT",null));
					}
					break;
				case '<':
					
					if(str[pp]=='='){
						token+=str[pp];
						p=pp+1;
						tagList.add(new Token("LTE",null));
						dfs(str,p);
					}else{
						tagList.add(new Token("LT",null));
						dfs(str,pp);
					}
					break;
				case '=':
					if(str[pp]=='='){
						token+=str[pp];
						p=pp+1;
						tagList.add(new Token("EQ",""));
						dfs(str,p);
					}else{
						tagList.add(new Token("ASSIGN",""));
						dfs(str,pp);
					}
					break;
				case '&':
					if(str[pp]=='&'){
						token+=str[pp];
						p=pp+1;
						dfs(str,p);
					}else{	
						dfs(str,pp);
					}
					break;
				case '|':
					if(str[pp]=='|'){
						token+=str[pp];
						p=pp+1;
						dfs(str,p);
					}else{	
						dfs(str,pp);
					}
					break;
					
				case '+':
					if(str[pp]=='+'){
						token+=str[pp];
						p=pp+1;
						tagList.add(new Token("ADD_OP",""));
						tagList.add(new Token("ADD_OP",""));
						dfs(str,p);
					}else{	
						tagList.add(new Token("ADD_OP",""));
						dfs(str,pp);
					};
					break;
				case '-':
					if(str[pp]=='-'){
						token+=str[pp];
						p=pp+1;
						tagList.add(new Token("MINES_OP",""));
						tagList.add(new Token("MINES_OP",""));
						dfs(str,p);
					}else{	
						tagList.add(new Token("MINES_OP",""));
						dfs(str,pp);
					};
					break;
				case '!':
					if(str[pp]=='='){
						token+=str[pp];
						p=pp+1;
						tagList.add(new Token("NOTEQ",""));
						dfs(str,p);
					}else{	
						tagList.add(new Token("NOT",""));
						dfs(str,pp);
					};
					break;
				case '*':
					tagList.add(new Token("MULTIPLY_OP",""));
					break;
				case '/':
					tagList.add(new Token("DIVISION_OP",""));
					break;
				case '(':
					tagList.add(new Token("LPAREN",""));
					break;
				case ')':
					tagList.add(new Token("RPAREN",""));
					break;
				case '{':
					tagList.add(new Token("LBRACE",""));
					break;
				case '}':
					tagList.add(new Token("RBRACE",""));
					break;
				case ';':
					tagList.add(new Token("SEMI",""));
					break;
				case ',':
					tagList.add(new Token("COMMA",""));
					break;
					
			}
			dfs(str,pp);
		}
	}
	
	public void output(){
		try {


			   File file = new File("result.txt");

			   // if file doesnt exists, then create it
			   if (!file.exists()) {
			    file.createNewFile();
			   }

			   FileWriter fw = new FileWriter(file.getAbsoluteFile());
			   BufferedWriter bw = new BufferedWriter(fw);
			
		for(int i=0;i<tagList.size();i++){
			if(tagList.get(i).value!=null){
				System.out.println("<"+tagList.get(i).type+","+tagList.get(i).value+">");
				   bw.write("<"+tagList.get(i).type+","+tagList.get(i).value+">"+"\r\n");
			}else{
				System.out.println("<"+tagList.get(i).type+","+">");
				bw.write("<"+tagList.get(i).type+","+">"+"\r\n");
			}
		}
		

			   bw.close();

			   System.out.println("Done");

			  } catch (IOException e) {
			   e.printStackTrace();
			  }
		
	}
	
}

class Token{
	String type;
	Object value;
	public Token(String type,Object value){
		this.type=type;
		this.value=value;
	}
}

