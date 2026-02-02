package net.bounceme.chronos.inteligenciaartificial.util;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.MutableDataSet;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.bounceme.chronos.inteligenciaartificial.exceptions.AssertException;

@UtilityClass
public class JsfUtils {
	
	@SneakyThrows(AssertException.class)
	public void writeMessage(FacesMessage.Severity severity, String title, String detail) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Asserts.assertNotNull(facesContext);
		
		facesContext.addMessage(null, new FacesMessage(severity, title, detail));
	}
	
	@SneakyThrows(AssertException.class)
	public ExternalContext getExternalContext() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Asserts.assertNotNull(facesContext);

		return facesContext.getExternalContext();
	}
	
	public String markdown2Html(String source) {
		MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        
        // Convertir Markdown a HTML
        Document document = parser.parse(source);
        return renderer.render(document);
	}
}
