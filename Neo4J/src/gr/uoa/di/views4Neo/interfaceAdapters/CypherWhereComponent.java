package gr.uoa.di.views4Neo.interfaceAdapters;

public class CypherWhereComponent {
    String varName;
    String whereVar;
    String property;
    String value;

    public CypherWhereComponent(String varName,String whereVar,String property,String value){
        this.varName=varName;
        this.whereVar=whereVar;
        this.property=property;
        this.value="'" + value + "'";
    }

    public CypherWhereComponent(String varName,String whereVar,String property,Number value){
        this.varName=varName;
        this.whereVar=whereVar;
        this.property=property;
        this.value=value.toString();
    }

    @Override
    public String toString(){
        return "any(" + whereVar + " in " + varName + "." + property + " WHERE " + whereVar + " = " + value + ")";
    }

}
