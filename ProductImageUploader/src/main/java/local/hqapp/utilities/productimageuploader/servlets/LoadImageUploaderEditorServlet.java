//package local.hqapp.utilities.productimageuploader.servlets;
//
//import jakarta.servlet.ServletContext;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * Servlet implementation class LoadImageUploaderEditorServlet
// */
//@WebServlet("/loadEditor")
//public class LoadImageUploaderEditorServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public LoadImageUploaderEditorServlet() {
//        super(); 
//    }
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//		response.setContentType("text/html");
//		
//		String idParam = request.getParameter("id");
//		
//		String html = "";
//		
//		int uploadImageSessionId = 0;
//		
//		if (idParam != null) {
//			
//			try {
//				
//				uploadImageSessionId = Integer.parseInt(idParam); 
//						
//				ServletContext servletContext = getServletContext();
//				
//				String contextPath = servletContext.getRealPath(File.separator);
//				
//				html = String.format(loadEditorHTML(contextPath), uploadImageSessionId);
//				
//			} catch (NumberFormatException e) {
//				
//			}
//			
//		}
//		
//		PrintWriter writer = response.getWriter();
//		writer.print(html);
//		writer.flush();
//		writer.close();
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//		doGet(request, response);
//	}
//
//	private String loadEditorHTML(String contextPath) {
//		
//		StringBuilder sb = new StringBuilder();
//		
//		File f = new File(String.format("%s/editor.html", contextPath));
//		
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(f));
//			
//			String line = null;
//			
//			while ((line = br.readLine()) != null) {
//				sb.append(line);
//			}
//			
//			br.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return sb.toString();
//	}
//}
