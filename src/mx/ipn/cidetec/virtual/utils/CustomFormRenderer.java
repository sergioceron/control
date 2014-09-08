package mx.ipn.cidetec.virtual.utils;

import com.sun.faces.renderkit.html_basic.FormRenderer;
import com.sun.faces.renderkit.html_basic.TextRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import java.io.IOException;

/**
 * -
 *
 * @author Sergio Ceron F.
 * @version rev: %I%
 * @date 4/08/14 10:38 PM
 */
public class CustomFormRenderer extends FormRenderer {

	private static final String[] attributes = new String[]{
			"data-parsley-validate"
	};

	@Override
	protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue) throws IOException {
		final ResponseWriter originalResponseWriter = context.getResponseWriter();
		context.setResponseWriter(new ResponseWriterWrapper() {

			@Override
			public ResponseWriter getWrapped() {
				return originalResponseWriter;
			}

			@Override
			public void startElement(String name, UIComponent component) throws IOException {
				super.startElement(name, component);
				if ("form".equalsIgnoreCase(name)) {
					for (String attr : attributes) {
						final String value = (String) component.getAttributes().get(attr);
						if (value != null) {
							super.writeAttribute(attr, value, attr);
						}
					}
				}
			}
		});

		super.getEndTextToRender(context, component, currentValue);
		context.setResponseWriter(originalResponseWriter);
	}
}
