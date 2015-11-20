package lab2;

public class MAIN {

    public static void main(String args[]){
    	LL1 test=new LL1();
    	test.checkContext("product");
    	test.predict("i+i*i");
    	test.print_first_follow();
    	test.print_parsing_table();
    	test.print_Syntax_Parser();
       	
    }
}
