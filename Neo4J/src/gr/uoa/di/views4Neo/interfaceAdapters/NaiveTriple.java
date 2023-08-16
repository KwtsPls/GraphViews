package gr.uoa.di.views4Neo.interfaceAdapters;

public class NaiveTriple {
    String subject;
    String predicate;
    String object;

    public NaiveTriple(String s,String p,String o){
        this.subject = s;
        this.predicate = p;
        this.object = o;
    }

    public int isSame(NaiveTriple t){
        if(this.subject.equals(t.getSubject()) && this.predicate.equals(t.getPredicate()) && this.object.equals(t.getObject()))
            return 1;
        else
            return 0;
    }

    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getObject() {
        return object;
    }

    @Override
    public String toString(){
        return "("+subject+","+predicate+","+object+")";
    }
}
