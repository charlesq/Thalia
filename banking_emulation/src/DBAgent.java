//package com.charles.java.banking;
/* DBAgent implements DBAccess and interacts with underlying database 
 * to avoid data race due to concurrent accesses to db, 
 * This is a singleton class.
 */
class DBAgent implements DBAccess
{
    private static DBAgent dba;
    static DBAgent getInstance()
    {
        if (dba == null)
            dba = new DBAgent();
        return dba;
    }
    private DBAgent(){} 
    public synchronized boolean updateServicesTable(ServicesRecord sr)
    {
        return false;
    }
    public synchronized boolean updateMemberProfileRecord(MemberProfileRecord mr)
    {
        return false;
    }
    public synchronized boolean updateAccountProfileRecord(AccountProfileRecord ar) 
    {
        return false;
    }
    public synchronized boolean  updateTransactionRecord(TransactionRecord tr)
    {
        return false;
    }
     
}
