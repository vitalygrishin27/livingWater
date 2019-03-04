package entity;


public enum Category {

    TO_21,
    FROM_21_TO_45,
    ENSEMBLE;

    public static Category getCategoryByName(String name){
        if(name.equals("TO_21")){
            return Category.TO_21;
        }
        else if(name.equals("FROM_21_TO_45")){
            return Category.FROM_21_TO_45;
        }
        else {
            return Category.ENSEMBLE;
        }
    }




}
