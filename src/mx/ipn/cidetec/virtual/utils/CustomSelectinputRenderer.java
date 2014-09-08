package mx.ipn.cidetec.virtual.utils;

import com.sun.faces.renderkit.html_basic.ListboxRenderer;

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
public class CustomSelectinputRenderer extends ListboxRenderer {

	private static final String[] attributes = new String[]{
			"placeholder", "type",
			"min", "max", "pattern",
			"data-parsley-value", "data-parsley-group", "data-parsley-multiple", "data-parsley-validate-if-empty",
			"data-parsley-trim-value", "data-parsley-ui-enabled", "data-parsley-errors-messages-disabled",
			"data-parsley-required", "data-parsley-type", "data-parsley-minlength", "data-parsley-maxlength",
			"data-parsley-length", "data-parsley-min", "data-parsley-max", "data-parsley-range", "data-parsley-pattern",
			"data-parsley-mincheck", "data-parsley-maxcheck", "data-parsley-check", "data-parsley-equalto"
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
				if ("select".equalsIgnoreCase(name)) {
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
