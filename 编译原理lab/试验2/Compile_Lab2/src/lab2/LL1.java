package lab2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

public class LL1 {
	String Vn[]=null;
    Vector<String> P=null;
   
    int firstComplete[]=null;//存储已判断过first的数据
    char first[][]=null;//存储最后first结果
    int followComplete[]=null;//存储已判断过follow的数据
    char follow[][]=null;//存储最后follow结果
    char select[][]=null;//存储最后select结果
    int LL=0;//标记是否为LL（1）
    String vt_head[]=null;//储存Vt
    Object paring_table[][]=null;//存储表达式数据paring_table
    char yn_null[]=null;//存储能否推出空
    ArrayList<String> firstList=new ArrayList<>();
    ArrayList<String> followList=new ArrayList<>();
    ArrayList<String> resultList=new ArrayList<>();
    
    public void print_parsing_table(){
    	System.out.println("-------------------------------------------");
    	
    	try {
 		   File file = new File("parsing_table.txt");
 		   // if file doesnt exists, then create it
 		   if (!file.exists()) {
 		    file.createNewFile();
 		   }
		 FileWriter fw = new FileWriter(file.getAbsoluteFile());
		 BufferedWriter bw = new BufferedWriter(fw);
		   
    	String head="";
    	for(int i=0;i<vt_head.length;i++){
    		if(vt_head[i].contains("#")){
    			vt_head[i]="$";
    		}
    		head=head+vt_head[i]+"\t\t";
    	}
    	
    	System.out.println(head);
    	bw.write(head+"\r\n");
    
    	for(int k=0;k<paring_table.length;k++){
    		String line="";
    		for(int j=0;j<paring_table[k].length;j++){
    			if(paring_table[k][j]==null){
    				paring_table[k][j]="";
    			}
    			line=line+paring_table[k][j]+"\t\t";
    		}
    		bw.write((line+"\r\n"));
    		System.out.println(line);
    	}
    	bw.close();
    	System.out.println("Done");
    	} catch (IOException e) {
    		   e.printStackTrace();
     }
    }
    
    
    public ArrayList<String> readContex(String path){				//读取文件的路径
        String encoding="utf-8";		//以utf-8的编码方式读取文件
        String line=null;
        ArrayList<String> list=new ArrayList<>();
        try{
        InputStreamReader read = new InputStreamReader(
                new FileInputStream(new File(path)),encoding);
    	BufferedReader br = new BufferedReader(read);
        while((line=(String)br.readLine())!=null){
        	list.add(line);
        	//System.out.println(line);
       }
        }catch(Exception e){
        	
        }
    	return list;
    }
    
    public void checkContext(String path){/*传入文件路劲*/
    	/*用于检测从文件中读取到的文法*/
             int Vnnum=0,k;
             Vn=new String[100];
             P=new Vector<String>();
  
             ArrayList<String> list=readContex(path);
             for(int i=0;i<list.size();i++)
             {
                 if(list.size()<2){
                    System.out.println("文本输入的文法不符合输入标准");;//判断长度是否符合
                     return;
                     }
                 
                 if(list.get(i).charAt(0)<='Z'&&list.get(i).charAt(0)>='A'&&list.get(i).charAt(1)=='→')
                 {
                     for(k=0;k<Vnnum;k++)
                     {
                         if(Vn[k].equals(list.get(i).substring(0, 1))){
                             break;
                             }
                     }
                     if(Vnnum==0||k>=Vnnum)
                     {
                         Vn[Vnnum]=list.get(i).substring(0, 1);//存入Vn数据
                         Vnnum++;
                     }
                     P.add(list.get(i));
                 }
                 else
                 {
                     System.out.println("文法输入有误，请重新输入");
                     return;
                 }
             }
             yn_null=new char[100];
             first=new char[Vnnum][100];
             int flag=0;
             String firstVn[]=null;
             firstComplete=new int[Vnnum];
             for(int i=0;Vn[i]!=null;i++)   //依次求 FIRST**
             {
                 flag=0;
                 firstVn=new String[20];
                 if((flag=add_First(first[i],Vn[i],firstVn,flag))==-1)return;
                 firstComplete[i]=1;
                
             }
             
             for(int i=0;Vn[i]!=null;i++)
             {
            	 String f="";
            	 f=f+"first("+Vn[i]+")={ ";
                 for(int j=0;first[i][j]!='\0';j++)
                 {
                     if(j==0){
                    	 f=f+first[i][j];
                     }
                     else{
                    	 f=f+" , "+first[i][j];
                     }
                 }
                 f=f+"}";
                 firstList.add(f);
             }
            
             follow=new char[Vnnum][100];
             String followVn[]=null;
             followComplete=new int[Vnnum];
             for(int i=0;Vn[i]!=null;i++)   //求FOLLOW**
             {
                 flag=0;
                 followVn=new String[20];
                 if((flag=add_Follow(follow[i],Vn[i],followVn,flag))==-1)return;
                 followComplete[i]=1;
             }
                      
             for(int i=0;Vn[i]!=null;i++){
            	 String fl="";
            	 fl=fl+"fl("+Vn[i]+")={ ";
                 for(int j=0;follow[i][j]!='\0';j++)
                 {
                	 
                	 
                	 if(j==0){
                	 	fl=fl+follow[i][j];
                	 }
                	 else{
                		 fl=fl+","+follow[i][j];
                	 }
                 }
                 fl=fl+"}";
                 followList.add(fl);
             }
            
            
             select=new char[P.size()][100];
             for(int i=0;i<P.size();i++)          //求SELECT**
             {
                 flag=0;
                 add_Select(select[i],(String)P.elementAt(i),flag);
             }
             
             ArrayList<String> selectList=new ArrayList<>();
             for(int i=0;i<P.size();i++){
            	 String sl="";
                 sl=sl+"select("+(String)P.elementAt(i)+")={ ";
                 for(int j=0;select[i][j]!='\0';j++)
                 {
                     sl=sl+select[i][j]+",";
                 }
                 sl=sl+"}";
                 selectList.add(sl);
             }
            
             for(int i=0;Vn[i]!=null;i++)//判断select交集是否为空
             {
                 int biaozhi=0;
                 char save[]=new char[100];
                 for(int j=0;j<P.size();j++)
                 {
                     String t=(String)P.elementAt(j);
                     if(t.substring(0,1).equals(Vn[i]))
                     {
                         for(k=0;select[j][k]!='\0';k++)
                         {
                             if(checkChar(save,select[j][k]))
                             {
                                 save[biaozhi]=select[j][k];
                                 biaozhi++;
                             }
                             else//当有交集时，不为LL（1）文法
                             {
                            	 System.out.println("您输入的文法不是LL(1)文法。");
                                 return;
                             }
                         }
                     }
                 }
             }
             char Vt[]=new char[100];
             int biaozhi=0;
             for(int i=0;i<P.size();i++)
             {
                 String t=(String)P.elementAt(i);
                 for(int j=2;j<t.length();j++)//提取表达式右侧的终结符存入Vt
                 {
                     if(t.charAt(j)>'Z'||t.charAt(j)<'A')
                     {
                         if(checkChar(Vt,t.charAt(j)))
                         {
                             Vt[biaozhi]=t.charAt(j);
                             biaozhi++;
                         }
                     }
                 }
             }
             if(checkChar(Vt,'#'))
             {
                 Vt[biaozhi]='#';
                 biaozhi++;
             }
             vt_head=new String[biaozhi+1];
             paring_table=new String[Vnnum][biaozhi+1];
             String f="";
             vt_head[0]=f;
             for(int i=0;i<biaozhi;i++)
             {
                 vt_head[i+1]=String.valueOf(Vt[i]);
             }
             for(int i=0;i<Vnnum;i++)
             {
                 paring_table[i][0]=Vn[i];//预测分析表的非终结符部分。
             }
             for(int i=0;i<P.size();i++)
             {
                 String t=(String)P.elementAt(i);
                 int j;
                 for(j=0;j<Vn.length;j++)
                 {
                     if(Vn[j].equals(t.substring(0,1)))
                     {
                         break;
                     }
                 }
                 for(k=0;select[i][k]!='\0';k++)
                 {
                     int y=checkXulie(Vt,select[i][k]);
                     paring_table[j][y+1]=t.substring(1);
                 }
             }
             LL=1;
    }
    
    public void predict(String input){
    	/*输入的input的需要进行的预测分析的input*/
    	
    	  if(LL==1)
          {
    		  String s=input;
              for(int i=0;i<s.length();i++)
              {
                  if(s.charAt(i)=='\0')
                  {
                	  System.out.println("输入的预测传中不要加入空格！");
                      return;
                  }
              }
              char zifu[]=new char[100];
              char fenxi_stack[]=new char[100];//分析栈
              zifu[0]='#';
              fenxi_stack[1]='S';
              fenxi_stack[0]='#';
              int fzifu=1;
              int ffenxi=2;
              for(int i=s.length()-1;i>=0;i--)
              {
                  zifu[fzifu]=s.charAt(i);
                  fzifu++;
              }
              int buzhou=2;
              char n[]=new char[100];
              n[0]='1';
              n[15]='#';
              n[14]='S';
              int u=29;
              for(int i=fzifu-1;i>=0;i--)
              {
                  n[u]=zifu[i];
                  u++;
              }
              while(!(fenxi_stack[ffenxi-1]=='#'&&zifu[fzifu-1]=='#'))//剩余输入串不为#则分析
              {
                  int i,j;
                  char t=zifu[fzifu-1];
                  char k=fenxi_stack[ffenxi-1];
                  if(t==k)//产生式匹配
                  {
                      n[49]=k;
                      n[50]='匹';
                      n[51]='配';
                      resultList.add(String.copyValueOf(n));
                      n=new char[100];
                      fzifu--;
                      ffenxi--;
                      if(buzhou<10)
                      n[0]=(char)('0'+buzhou);//显示步骤数
                      else
                      {
                          n[0]=(char)('0'+buzhou/10);
                          n[1]=(char)('0'+buzhou%10);
                      }
                      u=14;
                      for(int y=ffenxi-1;y>=0;y--)//处理分析栈，出栈
                      {
                          n[u]=fenxi_stack[y];
                          u++;
                      }
                      u=29;
                      for(int y=fzifu-1;y>=0;y--)//处理剩余字符串，消除一个字符
                      {
                          n[u]=zifu[y];
                          u++;
                      }
                      buzhou++;
                      continue;
                  }



                  for(i=0;Vn[i]!=null;i++)//搜寻所用产生式的左部
                  {
                      if(Vn[i].equals(String.valueOf(k)))break;
                  }
                  for(j=0;j<vt_head.length;j++)//判断是否匹配
                  {
                      if(vt_head[j].equals(String.valueOf(t)))break;
                  }
                  if(j>=vt_head.length)//全部产生式都不能符合则报错
                  {
                      resultList.add(String.copyValueOf(n));
                      System.out.println("该串不是该文法的句型！");
                      return;
                  }
                 Object result1;
                 if(i<paring_table.length&&j<paring_table[0].length){
                  result1=paring_table[i][j];
                 }else{
                 	break;
                 }
                  if(result1==null)
                  {
                	  resultList.add(String.copyValueOf(n));
                      System.out.println("该串不是该文法的句型！");
                      return;
                  }
                  else{   
                      n[49]=Vn[i].charAt(0);
                      u=50;
                      String result=(String)result1;
                      for(int y=0;y<result.length();y++)
                      {
                          n[u]=result.charAt(y);
                          u++;
                      }
                      resultList.add(String.copyValueOf(n));
                      n=new char[100];
                     
                      ffenxi--;
                      for(i=result.length()-1;i>0;i--)//将分析栈内非终结符换为右边表达式
                      {
                          if(result.charAt(i)!='#')


                              {
                              fenxi_stack[ffenxi]=result.charAt(i);
                              ffenxi++;
                              }
                          }
                      }
                      if(buzhou<10)//显示“步骤”
                      n[0]=(char)('0'+buzhou);
                      else
                      {
                          n[0]=(char)('0'+buzhou/10);
                          n[1]=(char)('0'+buzhou%10);
                      }
                      u=14;
                      for(int y=ffenxi-1;y>=0;y--)
                      {
                          n[u]=fenxi_stack[y];
                          u++;
                      }
                      u=29;
                      for(int y=fzifu-1;y>=0;y--)
                      {
                          n[u]=zifu[y];
                          u++;
                      }
                      buzhou++;
                  }
                  n=new char[100];
                  n[0]='1';
                  n[14]='#';
                  n[29]='#';    
                  n[49]='分';
                  n[50]='析';
                  n[51]='成';
                  n[52]='功';
                  resultList.add(String.copyValueOf(n));
                  return;
              }
              else
              {
                  return;
              }
    }
    
    
    
    private int add_First(char a[],String b,String firstVn[],int flag){
        if(checkString(firstVn,b.charAt(0))){addString(firstVn,b);}
        else{
            return flag;
        }
        for(int i=0;i<P.size();i++){
           
            String t=(String)P.elementAt(i);
            for(int k=2;k<t.length();k++){
            if(t.substring(0, 1).equals(b)){
                if(t.charAt(k)>'Z'||t.charAt(k)<'A')//表达式右端第i个不是非终结符
                {
                    if(flag==0||checkChar(a,t.charAt(k))){
                       
                        if(t.charAt(k)=='#')//#时直接加入first
                        {
                            if(k+1==t.length())
                            {
                                a[flag]=t.charAt(k);
                                flag++;
                             }
                            int flag1=0;
                            for(int j=0;yn_null[j]!='\0';j++)//所求非终结符进入yn_null**
                            {
                                if(yn_null[j]==b.charAt(0))//判断能否推出空
                                {
                                    flag1=1;break;
                                }
                            }
                            if(flag1==0)
                            {
                                int j;
                                for(j=0;yn_null[j]!='\0';j++)
                                {
                                   
                                }
                                yn_null[j]=b.charAt(0);
                            }
                            continue;
                        }
                        else//终结符直接加入first**
                        {
                            a[flag]=t.charAt(k);
                            flag++;
                            break;
                        }
                    }break;    
                }
                else//非终结符
                {
                    if(!checkString(Vn,t.charAt(k)))
                    {
                        int p=firstComplete(t.charAt(k));//当该非终结符的first已经求出
                        if(p!=-1)
                        {
                            flag=addElementFirst(a,p,flag);//直接加入所求first
                        }
                        else if((flag=add_First(a,String.valueOf(t.charAt(k)),firstVn,flag))==-1)
                            return -1;
                        int flag1=0;
                        for(int j=0;yn_null[j]!='\0';j++)//当非终结符的first有空
                        {
                            if(yn_null[j]==t.charAt(k))
                            {
                                flag1=1;break;
                            }
                        }
                        if(flag1==1)//当非终结符的first能推出空
                        {
                            if(k+1==t.length()&&checkChar(a,'#'))//之后无符号，且所求first无#
                            {
                                a[flag]='#';//first中加入#
                                flag++;
                            }
                            continue;//判断下一个字符
                        }
                        else
                        {
                            break;
                        }
                    }
                    else//错误
                    {
                        return -1;
                    }
                }
            }
            }
        }
        return flag;
    }
    
    private boolean checkChar(char a[],char b)
    {
   
        for(int i=0;a[i]!='\0';i++)
        {
            if(a[i]==b)return false;
           
        }
        return true;
    }
    
    private int firstComplete(char b)
    {
        int i;
        for(i=0;Vn[i]!=null;i++)  
        {
            if(Vn[i].equals(String.valueOf(b)))
            {
                if(firstComplete[i]==1)return i;
                else return -1;
            }
           
        }
        return -1;
    }
    
    private boolean checkString(String a[],char b)
    {
        for(int i=0;a[i]!=null;i++)
        {
            if(a[i].equals(String.valueOf(b)))return false;
           
        }
        return true;
    }
    
    private int addElementFirst(char a[],int p,int flag)  
    {
        for(int i=0;first[p][i]!='\0';i++)
        {
        if(checkChar(a,first[p][i])&&first[p][i]!='#')
            {
                a[flag]=first[p][i];
                flag++;
            }
        }
        return flag;
    }
    
    //把b加入字符串组firstVn[]
    private void addString(String firstVn[],String b)
    {
        int i;
        for(i=0;firstVn[i]!=null;i++)
        {    
        }
        firstVn[i]=b;
    }
    
    
    /****************************/
    private int add_Follow(char a[],String b,String followVn[],int flag)   //计算FOLLOW**（递归）
    {
        if(checkString(followVn,b.charAt(0))){addString(followVn,b);}
        else
        {
            return flag;
        }
        if(b.equals("S"))//当为S时#存入follow
        {
            a[flag]='#';
            flag++;
        }
        for(int i=0;i<P.size();i++)
        {
            String t=(String)P.elementAt(i);
            for(int j=2;j<t.length();j++)
            {
                if(t.charAt(j)==b.charAt(0)&&j+1<t.length())
                {
                    if(t.charAt(j+1)!='\0')
                    {
                        if((t.charAt(j+1)>'Z'||t.charAt(j+1)<'A'))//所求为终结符
                        {
                            if(flag==0||checkChar(a,t.charAt(2)))//自身
                            {
                                a[flag]=t.charAt(j+1);
                                flag++;
                            }
                        }
                        
                        else//所求为非终结符
                        {
                            int k;
                            for(k=0;Vn[k]!=null;k++) 
                            {
                                if(Vn[k].equals(String.valueOf(t.charAt(j+1))))
                                {
                                    break;//找寻下一个非终结符的Vn位置
                                }
                               
                            }
                            flag=addElementFirst(a,k,flag);//把下一个非终结符first加入所求follow集
                            for(k=j+1;k<t.length();k++)
                            {
                                if((t.charAt(j+1)>'Z'||t.charAt(j+1)<'A'))break;
                                else 
                                {
                                    if(isNull(t.charAt(k)))
                                    {}
                                    else
                                    {
                                        break;
                                    }
                                }
                               
                            }
                            if(k>=t.length())//下一个非终结符可推出空，把表达式左边非终结符的follow集加入所求follow集
                            {
                                int p=followComplete(t.charAt(0));
                                if(p!=-1)
                                {
                                    flag=addElementFollow(a,p,flag);
                                }
                                else if((flag=add_Follow(a,String.valueOf(t.charAt(0)),followVn,flag))==-1)
                                    return -1;
                            }
                        }
                    }
                    else//错误
                    {
                        return -1;
                    }
                   
                }
                if(t.charAt(j)==b.charAt(0)&&j+1==t.length())
                {
                    int p=followComplete(t.charAt(0));
                    if(p!=-1)
                    {
                        flag=addElementFollow(a,p,flag);
                    }
                    else if((flag=add_Follow(a,String.valueOf(t.charAt(0)),followVn,flag))==-1)
                        return -1;
                }
            }
        }
           return flag;
    }
    
    private int followComplete(char b)
    {
        for(int i=0;Vn[i]!=null;i++)  
        {
            if(Vn[i].equals(String.valueOf(b)))
            {
                if(followComplete[i]==1)return i;
                else return -1;
            }
        }
        return -1;
    }
    
    private boolean isNull(char a)
    {
        int i;
        for(i=0;Vn[i]!=null;i++)
        {
            if(Vn[i].equals(String.valueOf(a)))
            {
                break;
            }
           
        }
        for(int j=0;first[i][j]!='\0';j++)
        {
            if(first[i][j]=='#')return true;
        }
       
        return false;
    }
    
    private int addElementFollow(char a[],int p,int flag)  
    {
        for(int i=0;follow[p][i]!='\0';i++)
        {
            if(checkChar(a,follow[p][i]))
            {
                a[flag]=follow[p][i];
                flag++;
            }
        }
        return flag;
    }
    
    private int checkXulie(char Vt[],char b)
    {
        int i;
        for(i=0;Vt[i]!='\0';i++)
        {
            if(Vt[i]==b)break;
        }
        return i;
    }
    
   /********************************************/
    private void add_Select(char a[],String b,int flag){        //计算SELECT**

        int i=2;
        int biaozhi=0;
        while(i<b.length())
        {
            if((b.charAt(i)>'Z'||b.charAt(i)<'A')&&b.charAt(i)!='#')//是终结符
            {
                a[flag]=b.charAt(i);//将这个字符加入到Select**，结束Select**的计算
                break;
            }
            else if(b.charAt(i)=='#')//是空
            {
                int j;
                for(j=0;Vn[i]!=null;j++)//将表达式左侧的非终结符的follow加入select
                {
                    if(Vn[j].equals(b.substring(0,1)))
                    {
                        break;
                    }
                }
                for(int k=0;follow[j][k]!='\0';k++)
                {
                    if(checkChar(a,follow[j][k]))
                    {
                        a[flag]=follow[j][k];
                        flag++;
                    }
                }
                break;
            }
            else if(b.charAt(i)>='A'&&b.charAt(i)<='Z'&&i+1<b.length())//是非终结符且有下一个字符
            {
                int j;
                for(j=0;Vn[i]!=null;j++)
                {
                    if(Vn[j].equals(String.valueOf(b.charAt(i))))
                    {
                        break;
                    }
                }
                for(int k=0;first[j][k]!='\0';k++)
                {
                   
                    if(checkChar(a,first[j][k]))//把表达式右侧所有非终结符的first集加入。
                    {
                        if(first[j][k]=='#')//first中存在空
                        {
                            biaozhi=1;
                        }
                        else{
                            a[flag]=first[j][k];
                            flag++;
                        }
                    }
                }
                if(biaozhi==1)//把右侧所有非终结符的first中的#去除
                {
                    i++;biaozhi=0;continue;
                }
                else
                {
                    biaozhi=0;break;
                }
            }
            else if(b.charAt(i)>='A'&&b.charAt(i)<='Z'&&i+1>=b.length())//是非终结符且没有下一个字符
            {
                int j;
                for(j=0;Vn[i]!=null;j++)
                {
                    if(Vn[j].equals(String.valueOf(b.charAt(i))))
                    {
                        break;
                    }
                }
                for(int k=0;first[j][k]!='\0';k++)
                {
                   
                    if(checkChar(a,first[j][k]))
                    {    
                        if(first[j][k]=='#')
                        {
                            biaozhi=1;//表达式右侧能推出空，标记
                        }
                        else
                        {
                            a[flag]=first[j][k];//不能推出空，直接将first集加入select集
                            flag++;
                        }

                    }
                }
                if(biaozhi==1)//表达式右侧能推出空
                {
                    for(j=0;Vn[i]!=null;j++)
                    {
                        if(Vn[j].equals(b.substring(0,1)))



                        {
                            break;
                        }
                    }
                    for(int k=0;follow[j][k]!='\0';k++)
                    {
                        if(checkChar(a,follow[j][k]))
                        {
                            a[flag]=follow[j][k];//将将表达式左侧的非终结符的follow加入select
                            flag++;
                        }
                    }
                    break;
                }
                else
                {
                    biaozhi=0;break;
                }
            }
        }
    }
    
    public void print_first_follow(){
    	
    	try {

 		   File file = new File("first_follow.txt");
 		   // if file doesnt exists, then create it
 		   if (!file.exists()) {
 		    file.createNewFile();
 		   }

 		   FileWriter fw = new FileWriter(file.getAbsoluteFile());
 		   BufferedWriter bw = new BufferedWriter(fw);
    	
    	System.out.println("输出first集合：");
    	bw.write("first集合："+"\r\n");
    	for(int i=0;i<firstList.size();i++){
    		System.out.println(firstList.get(i));
    		bw.write(firstList.get(i)+"\r\n");
    	}
    	
    	System.out.println("输出follow集合：");
    	bw.write("follow集合："+"\r\n");
    	for(int i=0;i<followList.size();i++){
    		System.out.println(followList.get(i));
    		bw.write(followList.get(i)+"\r\n");
    	}
    	
    	bw.close();

    	} catch (IOException e) {
    		   e.printStackTrace();
     }
 
    }
    
    private void change(){
    	
    }
    
    public void print_Syntax_Parser(){
    	try {
 		   File file = new File("syntax_parser.txt");
 		   if (!file.exists()) {
 		    file.createNewFile();
 		   }
 		   FileWriter fw = new FileWriter(file.getAbsoluteFile());
 		   BufferedWriter bw = new BufferedWriter(fw);
    	System.out.println("输出推导过程：");
    	System.out.println("步骤                        分析栈                            剩余输入串                    所用产生式或匹配"+"\n");
    	bw.write("步骤                      分析栈                           剩余输入串                     所用产生式或匹配"+"\r\n");
    	String temp;
    	for(int i=0;i<resultList.size();i++){
    		temp=resultList.get(i);
    		if(temp.contains("i")&(!temp.contains("→"))){
    				temp=temp.replace("#", "$");
    			}
    		System.out.println(temp);
    		bw.write(temp+"\r\n");
    	}
    	bw.close();
    	} catch (IOException e) {
    		   e.printStackTrace();
    	 }
    }

    
}
