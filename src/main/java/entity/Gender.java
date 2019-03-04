package entity;

public enum Gender {

    M,
    F;

    public static Gender getGenderById(String ch){
        if(ch.equals("M")) {
            return Gender.M;
        }
        else{
            return Gender.F;
        }
    }

}
