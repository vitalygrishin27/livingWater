package entity;

public enum MARKCRITERIA {
    VOCAL,
    REPERTOIRE,
    ARTISTIC,
    INDIVIDUALY;

    public static MARKCRITERIA  getMarkCriteriaByName(String name){
        if(name.equals("VOCAL")) {
            return MARKCRITERIA.VOCAL;
        }
        else if(name.equals("REPERTOIRE")){
            return MARKCRITERIA.REPERTOIRE;
        }else if(name.equals("ARTISTIC")){
            return MARKCRITERIA.ARTISTIC;
        }
        return MARKCRITERIA.INDIVIDUALY;
    }



}
