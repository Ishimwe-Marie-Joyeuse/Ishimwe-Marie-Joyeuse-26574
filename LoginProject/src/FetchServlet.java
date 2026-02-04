import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/fetch")  
public class FetchServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get the search term parameter
        String searchTerm = request.getParameter("searchTerm");
        
        // Check if search term is provided
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            // If no search term, redirect to fetch.html
            response.sendRedirect("fetch.html");
        } else {
            // Encode the search term for URL
            String encodedTerm = searchTerm.replace(" ", "+");
            
            // Create Google search URL
            String googleUrl = "https://www.google.com/search?q=" + encodedTerm;
            
            // Use sendRedirect to redirect to Google
            response.sendRedirect(googleUrl);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If form uses POST, redirect to doGet
        doGet(request, response);
    }
}