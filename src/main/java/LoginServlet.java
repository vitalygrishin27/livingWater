
import entity.User;
import logical.MongoDAO;
import org.json.HTTP;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("WEBSiTE LOAD!");
        //  resp.sendRedirect("/login");
        // req.getRequestDispatcher("WEB-INF/view/login.html").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Somebody want to log in.");
        JSONObject jsonObject;
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            jsonObject = HTTP.toJSONObject(jb.toString());
            JSONObject userJSon = new JSONObject(jsonObject.getString("Method"));
            JSONObject jsonObjectResponse = new JSONObject();

            if (userJSon.getString("command").equals("logOut")) {
                System.out.println("sID with '" +userJSon.getString("sId")+
                                "' will remove with status - "+ Authentication.removeSId(userJSon.getString("sId")));
                resp.addCookie(new Cookie("LivingWaterSession", "ERROR"));
                return;
            }

        User user=Authentication.getRepository().getUserByName(userJSon.getString("userName"));
      //      User user = new User(userJSon.getString("userName"),
        //            userJSon.getString("password"),
         //           doc.getString("firstName"),
         //           doc.getString("secondName"),
         //           doc.getString("lastName"),
         //           doc.getString("office"));

            user.setRole(Authentication.getRepository().getROLE(user));

            String newSId = Authentication.getSIdAuthenticate(user);

            if(newSId.equals("ERROR")){
                resp.setCharacterEncoding("UTF-8");
                jsonObjectResponse.append("status", "ERROR");
                jsonObjectResponse.append("message", "Ошибка с авторизацией. ("+user.getRole()+")");
                resp.addCookie(new Cookie("LivingWaterSession", "ERROR"));

                resp.getWriter().write(String.valueOf(jsonObjectResponse));
                resp.setContentType("application/json; charset=UTF-8");
               // System.out.println(resp.getCharacterEncoding());
             //   resp.setCharacterEncoding("UTF-8");
             //   System.out.println(resp.getCharacterEncoding());
                resp.flushBuffer();
            }else{
                jsonObjectResponse.append("status", user.getRole());
                jsonObjectResponse.append("message", "You are login in!");
                resp.addCookie(new Cookie("LivingWaterSession", newSId));
                resp.getWriter().write(String.valueOf(jsonObjectResponse));
                resp.setContentType("application/json; charset=UTF-8");
                resp.flushBuffer();
            //    resp.sendRedirect("/" + user.getRole());
            }

        } catch (Exception e) {
            System.out.println("Error with buffered reader.");
        }

    }
}
