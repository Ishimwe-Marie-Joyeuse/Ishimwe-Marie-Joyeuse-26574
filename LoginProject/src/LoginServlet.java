import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get parameters from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Set response content type
        response.setContentType("text/html");
        
        // Get PrintWriter object
        PrintWriter out = response.getWriter();
        
        // Generate HTML response
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Login Result</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 50px; background-color: #f4f4f4; }");
        out.println(".container { max-width: 500px; margin: 0 auto; background: white; padding: 30px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
        out.println("h2 { color: #333; text-align: center; }");
        out.println(".result { padding: 20px; margin: 20px 0; border-radius: 4px; }");
        out.println(".weak { background-color: #ffebee; color: #c62828; border: 1px solid #ffcdd2; }");
        out.println(".strong { background-color: #e8f5e9; color: #2e7d32; border: 1px solid #c8e6c9; }");
        out.println("a { display: block; text-align: center; margin-top: 20px; color: #1976d2; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h2>Login Result</h2>");
        
        // Check password length
        if (password.length() < 8) {
            out.println("<div class='result weak'>");
            out.println("<h3>Hello " + username + ", your password is weak.</h3>");
            out.println("<p>Password must be at least 8 characters long. Try a strong one.</p>");
            out.println("</div>");
        } else {
            out.println("<div class='result strong'>");
            out.println("<h3>Welcome " + username + "!</h3>");
            out.println("<p>Your password is strong. Login successful!</p>");
            out.println("</div>");
        }
        
        out.println("<a href='index.html'>Back to Login</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
        
        out.close();
    }
}
