//package com.charlesq.java.banking;
public class  AccountProfileRecord
{
    final char [] tableName = (new String("accounts")).toCharArray();
    char [] accountNumber; /* primary key */
    char  primaryOwnerSSN;
    char  [] coownersSSN; 
    char [] since; //account oppening day;
    int  status; /*values 0, 1, 2 correponding to PREAPPROVED, ACTIVE, 
                         LOCKED, CLOSED in Account.Status respectively.*/
}
