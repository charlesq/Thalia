//package com.charlesq.java.banking;
import java.lang.Object;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.*;
import java.lang.Comparable;
/* Member class represents member entity specified in accounts.xml.
 * Refer to accounts.xml for further info.
 */
public class Account extends Object implements Comparable<Account>
{
    final String acntNum;/*servers as member id as well */ 
    final Date openDate;
    private Member primary;
    private CopyOnWriteArrayList<Member> coowners = new CopyOnWriteArrayList<Member>(); 
    private Status status; 
    private double balance = 0;
    private transient ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public static enum Status{PREAPPROVED, ACTIVE, LOCKED, CLOSED} 
    DBAccess dba; 
    Account(String an, Member pm, Date open, Status s)
    {
        super();
        this.acntNum = an;
        openDate = open; 
        primary = pm;
        status = s;
    }
    synchronized void setStatus(Status s) throws AccountException 
    {
        if (s == this.status)
            throw new AccountException("new status must differ from the current status");
        lock.writeLock().lock();
        this.status = s;
        lock.writeLock().unlock();
    }
    public Status getStatus()
    {
       Status s;
       lock.readLock().lock();
       s = this.status; 
       lock.readLock().unlock();
       return s;
    } 
    synchronized void setBalance(int amt) throws AccountException
    {
       if (amt <0 )
           throw new AccountException("balance can not set to be negative"); 
       if (balance > 0)
           throw new AccountException("balance can not reset if already set"); 
       lock.writeLock().lock();
       balance = amt;
       lock.writeLock().unlock();
    }
    public double getBalance()
    {
        double b;
        lock.readLock().lock();
        b = balance;
        lock.readLock().unlock();
        return b;
    } 
    /* returns new balance */
    synchronized double  makeDeposit(double amt) throws AccountException
    {
        if (amt <= 0)
            throw new AccountException("can not deposit zero or negative ammount");
        lock.writeLock().lock();
        balance += amt; 
        amt = balance;
        lock.writeLock().unlock(); 
        return amt;
    }
    /* returns the remaining balance */
    synchronized double withdraw(double amt) throws AccountException
    {
        if (amt <= 0)
            throw new AccountException("can not withraw negative amount ");
        if (amt > balance)
            throw new AccountException("withdrawal amount may not exceed balance ");
        lock.writeLock().lock();
        balance -= amt;
        amt = balance;
        lock.writeLock().unlock(); 
        return balance;
    }
    public Member getPrimaryOwner()
    {
        return primary;
    }
    public Member[] getCoowners()
    {
        Member [] ret;
        /* see comment for addCoowner()*/
        lock.readLock().lock();
        ret = (Member [])coowners.toArray(); 
        lock.readLock().unlock();
        return ret;
    }
    synchronized void addCoowner(Member m) throws AccountException
    {
        /* addtional lock to protect coowners because CopyOnWriteArrayList.toArray()
           may still introduce race condition if in middle of adding/removing a coowner */
        if (coowners.contains(m)|| primary.compareTo(m) == 0)
            throw new AccountException("the membe to add is already  an owner");
        lock.writeLock().lock(); 
        coowners.add(m);
        lock.writeLock().unlock();
        return;
    }
    public int compareTo(Account a)
    {
        if (a == null)
            return 1;
        return this.acntNum.compareTo(a.acntNum);
    }
}
