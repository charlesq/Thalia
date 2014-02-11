//package com.charlesq.java.banking;
import java.lang.Exception;
public class MemberException extends Exception
{
    public MemberException(String msg)
    {
       super("Member Exception: " + msg);
    }
}

