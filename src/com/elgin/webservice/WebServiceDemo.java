package com.elgin.webservice;


import com.cdyx.base.Base;

public class WebServiceDemo extends Base{  
     
    public String  sayHello(String name){  
        return "hello " + name;  
    }  
      
    public int getAge(){  
        return 26;  
    }  
}