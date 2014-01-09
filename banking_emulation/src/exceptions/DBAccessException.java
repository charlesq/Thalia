//package com.charlesq.java.banking;
import java.lang.Exception;
  
public class DBAccessException extends Exception
{
    public DBAccessException (String msg)
    {
        super("DBAccessException " + msg);
    } 
} 
    
