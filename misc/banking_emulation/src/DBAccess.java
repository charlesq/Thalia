//package com.charlesq.java.banking;
import java.lang.Exception;
interface  DBAccess
{
    boolean updateServicesTable(ServicesRecord sr);
    boolean updateMemberProfileRecord(MemberProfileRecord mr);
    boolean updateAccountProfileRecord(AccountProfileRecord ar);
    boolean updateTransactionRecord(TransactionRecord tr);
}
