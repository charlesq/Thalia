//package com.charlesq.java.banking;
import java.lang.Object;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/* Member class represents member entity specified in member_profiles.xml.
 *
 */
public class Member extends Object implements Comparable<Member>
{
    final String ssn;/*servers as member id as well */ 
    final String name;
    final Date since; /* membership since */
    String addr; 
    Status status; 
    DBAccess dba;
    CopyOnWriteArrayList<Account> primaryAccounts = new CopyOnWriteArrayList<Account>();
    CopyOnWriteArrayList<Account> coownedAccounts = new CopyOnWriteArrayList<Account>();
    private transient ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public static enum Status{PREAPPROVED, ACTIVE, LOCKED, CLOSED} 
    Member(String ssn, String name, Date since, Status status)
    {
        this.ssn = ssn;
        this.name = name;
        this.since = since;
        this.status = status;
    }
    synchronized void setAddr(String add) throws MemberException
    {
     
        if (add == null)
            throw new MemberException ("address cannot be null");
        lock.writeLock().lock();
        this.addr = add;
        lock.writeLock().unlock();
    }
    synchronized void setStatus(Status status) throws MemberException
    {
        if (this.status == status)
            throw new MemberException("cannot set to the same status");
        lock.writeLock().lock();
        this.status = status;
        lock.writeLock().unlock();
    }
    public Status getStatus()
    {
        Status s;
        lock.readLock().lock();
        s = status;
        lock.readLock().unlock();
        return s;
    }
    public String getAddr()
    {
        String a;
        lock.readLock().lock();
        a = addr;
        lock.readLock().unlock();
        return a;
    }
    /* because ssn serves as member ID, we are not to expose it out of package */
    String getID()
    {
        return ssn;
    }
    public String getName()
    {
        return name;
    }
    public Date getSince()
    {
        return since;
    }
    synchronized void attachPimaryAccount(Account pa) throws MemberException
    {
        if (primaryAccounts.contains(pa) || coownedAccounts.contains(pa))
            throw new MemberException("Account already attached");
        primaryAccounts.add(pa);
    }
    synchronized void dettachPrimaryAccount(Account pa) throws MemberException
    {
        if (!primaryAccounts.contains(pa))
            throw new MemberException("this account is not attached to this member ");
        primaryAccounts.remove(pa);
    }
     synchronized void attachCoownedAccount(Account pa) throws MemberException
     {
        if (primaryAccounts.contains(pa) || coownedAccounts.contains(pa))
            throw new MemberException("Account already attached");
        
        coownedAccounts.add(pa);
    }
    synchronized void dettachCoownedAccount(Account pa) throws MemberException
    {
        if (!coownedAccounts.contains(pa))
            throw new MemberException("this account is not attached to this member ");
        coownedAccounts.remove(pa);
    }
    public Account[] getCoownedAccounts()
    {
        return (Account [] )coownedAccounts.toArray();
    } 
    public Account [] getPrimaryAccounts()
    {
        return (Account[])primaryAccounts.toArray();
    }
    public int compareTo(Member a)
    {
        if (a == null)
            return 1;
        return ssn.compareTo(a.ssn); 
    }
 
}
