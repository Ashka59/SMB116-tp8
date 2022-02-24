package cnam.smb116.smb116_tp8.CoR;

public abstract class ChainHandler<T1,T2,T3,T4>{
    protected ChainHandler<T1,T2,T3,T4> successor = null;

    public ChainHandler(){ this.successor = null;}
    public ChainHandler(ChainHandler<T1,T2,T3,T4> successor){ this.successor = successor;}
    public void setSuccessor(ChainHandler<T1,T2,T3,T4> successor){this.successor = successor;}
    public ChainHandler<T1,T2,T3,T4> getSuccessor(){return this.successor;}
    public boolean handleRequest(T1 value, T2 number, T3 messenger, T4 filter){
        if ( successor == null )  return false;
        return successor.handleRequest(value, number, messenger, filter);
    }

}