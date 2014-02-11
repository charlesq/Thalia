//package com.charlesq.java.banking;
public class MemberProfileRecord
{
    final char [] tableName = (new String("members")).toCharArray();
    char [] ssn; /* Primary Key */
    char [] name;
    char [] addr;
    char [] primaryAccounts;/* concatenated account numbers separanted with space */ 
    char [] coownedAccounts; /*concatenated account numbers separated with spadce */ 
    char [] since; /* membership since */
} 
