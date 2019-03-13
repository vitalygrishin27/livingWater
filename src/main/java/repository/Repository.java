package repository;

public abstract class Repository implements IDAO {
    private static boolean single=false;
    private static Repository DAO;
    public static Repository getDAO(String nameOfDBTechnology) {
        //String nameOfDBTechnology используется только при первом обращении.
        //Когда БД уже была создана - поменять технологию
        // например с SQL на MONGO уже нельзя
        //Используемые БД:
       // 1. MONGO по умолчанию
       // 2. SQL

        if (single) {
            return DAO;
        }
        single=true;
        if (nameOfDBTechnology.equals("SQL")){
            return SQLDAO.getDAO();
        }else{
            return MongoDAO.getDAO();
        }


    }



}
