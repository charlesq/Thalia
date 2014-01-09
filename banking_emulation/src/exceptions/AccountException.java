//package com.charlesq.java.banking;
import java.lang.Exception;
public class AccountException extends Exception
{
    public AccountException(String msg)
    {
       super("Account Exception: " + msg);
       System.out.print("Hell");
    }
}

